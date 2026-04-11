package com.ambravate.polymath.academy.service;

import com.ambravate.polymath.academy.model.*;
import com.ambravate.polymath.academy.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {

    private final ChunkRepository chunkRepository;
    private final SubChunkRepository subChunkRepository;
    private final UserChunkProgressRepository progressRepository;
    private final UserRepository userRepository;
    private final UserLearnerProfileRepository profileRepository;
    private final SpacingService spacingService;
    private final StreakService streakService;

    public DashboardData getDashboard(String userId) {
        User user = userRepository.findById(userId).orElseThrow();
        UserLearnerProfile profile = profileRepository.findByUserId(userId)
                .orElse(UserLearnerProfile.builder().userId(userId).build());

        List<ChunkHealth> chunkHealth = getMemoryHealth(userId);
        int reviewsDue = spacingService.getDueReviews(userId).size();
        boolean streakAtRisk = streakService.isStreakAtRisk(userId);

        // Calculate overall progress
        List<UserChunkProgress> allProgress = progressRepository.findByUserId(userId);
        long totalSubChunks = subChunkRepository.count();
        long completedSubChunks = allProgress.stream()
                .filter(p -> p.getStatus() == SubChunkStatus.COMPLETE || p.getStatus() == SubChunkStatus.SKIPPED)
                .count();
        double overallProgress = totalSubChunks > 0 ? (double) completedSubChunks / totalSubChunks : 0.0;

        return new DashboardData(
                user.getTotalXp(), user.getRank(), user.getStreakDays(), streakAtRisk,
                profile.getCurrentPath(), profile.isDiagnosticCompleted(),
                reviewsDue, profile.getDailyGoalMinutes(),
                overallProgress, chunkHealth
        );
    }

    /**
     * Memory health per chunk: average decayed strength of all sub-chunks.
     * GREEN > 0.7, YELLOW 0.4-0.7, RED < 0.4
     */
    public List<ChunkHealth> getMemoryHealth(String userId) {
        List<Chunk> chunks = chunkRepository.findAllByOrderBySortOrderAsc();
        List<UserChunkProgress> allProgress = progressRepository.findByUserId(userId);
        Map<String, UserChunkProgress> progressMap = allProgress.stream()
                .collect(Collectors.toMap(UserChunkProgress::getSubChunkId, p -> p, (a, b) -> a));

        // Build chunk status info
        Set<String> completedChunks = new HashSet<>();
        Map<String, List<SubChunk>> chunkSubs = new HashMap<>();
        for (Chunk c : chunks) {
            List<SubChunk> subs = subChunkRepository.findByChunkIdOrderBySortOrderAsc(c.getId());
            chunkSubs.put(c.getId(), subs);
            if (!subs.isEmpty() && subs.stream().allMatch(sc -> {
                UserChunkProgress p = progressMap.get(sc.getId());
                return p != null && (p.getStatus() == SubChunkStatus.COMPLETE || p.getStatus() == SubChunkStatus.SKIPPED);
            })) {
                completedChunks.add(c.getId());
            }
        }

        List<ChunkHealth> result = new ArrayList<>();
        for (Chunk chunk : chunks) {
            List<SubChunk> subs = chunkSubs.get(chunk.getId());
            double avgStrength = 0.0;
            int count = 0;

            for (SubChunk sc : subs) {
                UserChunkProgress p = progressMap.get(sc.getId());
                if (p != null && (p.getStatus() == SubChunkStatus.COMPLETE || p.getStatus() == SubChunkStatus.SKIPPED)) {
                    avgStrength += spacingService.computeDecayedStrength(p);
                    count++;
                }
            }

            if (count > 0) avgStrength /= count;

            String healthColor;
            if (avgStrength > 0.7) healthColor = "GREEN";
            else if (avgStrength >= 0.4) healthColor = "YELLOW";
            else healthColor = "RED";

            String status;
            if (completedChunks.contains(chunk.getId())) status = "COMPLETE";
            else if (subs.stream().anyMatch(sc -> {
                UserChunkProgress p = progressMap.get(sc.getId());
                return p != null && p.getStatus() == SubChunkStatus.IN_PROGRESS;
            })) status = "IN_PROGRESS";
            else {
                // Check if unlocked
                List<String> prereqs = parsePrereqs(chunk.getPrerequisiteIds());
                status = prereqs.isEmpty() || completedChunks.containsAll(prereqs) ? "UNLOCKED" : "LOCKED";
            }

            result.add(new ChunkHealth(chunk.getId(), chunk.getTitle(), chunk.getGlyph(),
                    status, avgStrength, healthColor,
                    subs.size(), count));
        }

        return result;
    }

    private List<String> parsePrereqs(String json) {
        if (json == null || json.isBlank()) return List.of();
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper().readValue(json,
                    new com.fasterxml.jackson.core.type.TypeReference<List<String>>() {});
        } catch (Exception e) { return List.of(); }
    }

    public record DashboardData(int totalXp, String rank, int streakDays, boolean streakAtRisk,
                                 LearnerPath currentPath, boolean diagnosticCompleted,
                                 int reviewsDue, int dailyGoalMinutes,
                                 double overallProgress, List<ChunkHealth> chunkHealth) {}

    public record ChunkHealth(String chunkId, String title, String glyph,
                               String status, double memoryStrength, String healthColor,
                               int totalSubChunks, int completedSubChunks) {}
}
