package com.ambravate.polymath.academy.service;

import com.ambravate.polymath.academy.model.*;
import com.ambravate.polymath.academy.repository.ChunkRepository;
import com.ambravate.polymath.academy.repository.SubChunkRepository;
import com.ambravate.polymath.academy.repository.UserChunkProgressRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChunkGraphService {

    private final ChunkRepository chunkRepository;
    private final SubChunkRepository subChunkRepository;
    private final UserChunkProgressRepository progressRepository;
    private final ObjectMapper objectMapper;

    /**
     * Get all chunks with their status for a given user.
     * Status: LOCKED, UNLOCKED, IN_PROGRESS, COMPLETE
     */
    public List<ChunkWithStatus> getAllChunksWithStatus(String userId) {
        List<Chunk> allChunks = chunkRepository.findAllByOrderBySortOrderAsc();
        List<UserChunkProgress> allProgress = progressRepository.findByUserId(userId);
        Map<String, UserChunkProgress> progressBySubChunk = allProgress.stream()
                .collect(Collectors.toMap(UserChunkProgress::getSubChunkId, p -> p, (a, b) -> a));

        // Build a set of completed chunk IDs
        Set<String> completedChunkIds = new HashSet<>();
        for (Chunk chunk : allChunks) {
            List<SubChunk> subs = subChunkRepository.findByChunkIdOrderBySortOrderAsc(chunk.getId());
            if (!subs.isEmpty() && subs.stream().allMatch(sc -> {
                UserChunkProgress p = progressBySubChunk.get(sc.getId());
                return p != null && (p.getStatus() == SubChunkStatus.COMPLETE || p.getStatus() == SubChunkStatus.SKIPPED);
            })) {
                completedChunkIds.add(chunk.getId());
            }
        }

        List<ChunkWithStatus> result = new ArrayList<>();
        for (Chunk chunk : allChunks) {
            String status;
            if (completedChunkIds.contains(chunk.getId())) {
                status = "COMPLETE";
            } else if (!isChunkUnlocked(chunk, completedChunkIds)) {
                status = "LOCKED";
            } else {
                // Check if any sub-chunk is in progress
                List<SubChunk> subs = subChunkRepository.findByChunkIdOrderBySortOrderAsc(chunk.getId());
                boolean anyStarted = subs.stream().anyMatch(sc -> {
                    UserChunkProgress p = progressBySubChunk.get(sc.getId());
                    return p != null && p.getStatus() == SubChunkStatus.IN_PROGRESS;
                });
                status = anyStarted ? "IN_PROGRESS" : "UNLOCKED";
            }
            result.add(new ChunkWithStatus(chunk, status));
        }
        return result;
    }

    public boolean isChunkUnlocked(String userId, String chunkId) {
        Chunk chunk = chunkRepository.findById(chunkId).orElse(null);
        if (chunk == null) return false;

        Set<String> completedChunkIds = getCompletedChunkIds(userId);
        return isChunkUnlocked(chunk, completedChunkIds);
    }

    private boolean isChunkUnlocked(Chunk chunk, Set<String> completedChunkIds) {
        List<String> prereqs = parsePrerequisites(chunk.getPrerequisiteIds());
        if (prereqs.isEmpty()) return true;
        return completedChunkIds.containsAll(prereqs);
    }

    public SubChunk getNextSubChunk(String userId, String chunkId) {
        List<SubChunk> subs = subChunkRepository.findByChunkIdOrderBySortOrderAsc(chunkId);
        for (SubChunk sc : subs) {
            Optional<UserChunkProgress> progress = progressRepository.findByUserIdAndSubChunkId(userId, sc.getId());
            if (progress.isEmpty() || progress.get().getStatus() == SubChunkStatus.NOT_STARTED
                    || progress.get().getStatus() == SubChunkStatus.IN_PROGRESS) {
                return sc;
            }
        }
        return null; // All complete
    }

    public Set<String> getCompletedChunkIds(String userId) {
        List<Chunk> allChunks = chunkRepository.findAllByOrderBySortOrderAsc();
        List<UserChunkProgress> allProgress = progressRepository.findByUserId(userId);
        Map<String, UserChunkProgress> progressBySubChunk = allProgress.stream()
                .collect(Collectors.toMap(UserChunkProgress::getSubChunkId, p -> p, (a, b) -> a));

        Set<String> completed = new HashSet<>();
        for (Chunk chunk : allChunks) {
            List<SubChunk> subs = subChunkRepository.findByChunkIdOrderBySortOrderAsc(chunk.getId());
            if (!subs.isEmpty() && subs.stream().allMatch(sc -> {
                UserChunkProgress p = progressBySubChunk.get(sc.getId());
                return p != null && (p.getStatus() == SubChunkStatus.COMPLETE || p.getStatus() == SubChunkStatus.SKIPPED);
            })) {
                completed.add(chunk.getId());
            }
        }
        return completed;
    }

    private List<String> parsePrerequisites(String json) {
        if (json == null || json.isBlank()) return List.of();
        try {
            return objectMapper.readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            log.warn("Failed to parse prerequisiteIds: {}", json);
            return List.of();
        }
    }

    public record ChunkWithStatus(Chunk chunk, String status) {}
}
