package com.ambravate.arcane.academy.profile.service;

import com.ambravate.arcane.academy.common.domain.LearningModule;
import com.ambravate.arcane.academy.common.domain.Lesson;
import com.ambravate.arcane.academy.common.domain.LessonStatus;
import com.ambravate.arcane.academy.common.domain.Domain;
import com.ambravate.arcane.academy.common.domain.User;
import com.ambravate.arcane.academy.common.domain.UserChunkProgress;
import com.ambravate.arcane.academy.common.dto.BadgeDto;
import com.ambravate.arcane.academy.content.repository.LearningModuleRepository;
import com.ambravate.arcane.academy.content.repository.LessonRepository;
import com.ambravate.arcane.academy.content.repository.DomainRepository;
import com.ambravate.arcane.academy.gamification.api.GamificationFacade;
import com.ambravate.arcane.academy.practice.repository.UserChunkProgressRepository;
import com.ambravate.arcane.academy.auth.repository.UserRepository;
import com.ambravate.arcane.academy.profile.domain.EarnedBadge;
import com.ambravate.arcane.academy.profile.domain.PublicProfile;
import com.ambravate.arcane.academy.profile.domain.TopicEntry;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Assembles the read-only data shown at <code>/u/{username}</code>.
 *
 * <p>Returns {@link Optional#empty()} when:
 * <ul>
 *   <li>No user has the username, OR</li>
 *   <li>The user has not opted into public profiles ({@code publicProfileEnabled=false})</li>
 * </ul>
 *
 * <p>The DTO deliberately does NOT include email, role, providerId, completedAt instants
 * for individual sub-chunks, or anything that could be used for fingerprinting.
 * It surfaces: username, member-since (date only, not time), rank, streak,
 * per-topic XP + chunk completion, and earned badges.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PublicProfileService {

    private final UserRepository userRepository;
    private final UserChunkProgressRepository progressRepository;
    private final GamificationFacade gamificationFacade;
    private final LearningModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;
    private final DomainRepository domainRepository;

    public Optional<PublicProfile> findByUsername(String username) {
        return userRepository.findByUsername(username)
            .filter(User::isPublicProfileEnabled)
            .map(this::assemble);
    }

    private PublicProfile assemble(User user) {
        Map<String, String> chunkTopic = moduleRepository.findAll()
            .stream()
            .collect(Collectors.toMap(LearningModule::getId, LearningModule::getTrackId, (a, b) -> a));

        Map<String, Lesson> subChunkById = lessonRepository.findAll()
            .stream()
            .collect(Collectors.toMap(Lesson::getId, sc -> sc, (a, b) -> a));

        Map<String, int[]> agg = new HashMap<>();
        for (UserChunkProgress p : progressRepository.findByUserId(user.getId())) {
            if (p.getStatus() != LessonStatus.COMPLETE) continue;
            Lesson sc = subChunkById.get(p.getLessonId());
            if (sc == null) continue;
            String topic = chunkTopic.get(sc.getModuleId());
            if (topic == null) continue;
            int[] arr = agg.computeIfAbsent(topic, k -> new int[]{0, 0});
            arr[0] += sc.getXpReward();
            arr[1] += 1;
        }

        Map<String, Domain> topicsById = domainRepository.findAll().stream()
            .collect(Collectors.toMap(Domain::getId, t -> t, (a, b) -> a));

        List<TopicEntry> topics = agg.entrySet().stream()
            .map(e -> {
                Domain t = topicsById.get(e.getKey());
                return new TopicEntry(
                    e.getKey(),
                    t != null ? t.getName() : e.getKey(),
                    t != null ? t.getGlyph() : "âœ¦",
                    t != null ? t.getAccentColor() : null,
                    e.getValue()[0],
                    e.getValue()[1]
                );
            })
            .sorted(Comparator.comparingInt(TopicEntry::xpEarned).reversed())
            .toList();


        List<EarnedBadge> badges = gamificationFacade.getEarnedBadges(user.getId())
            .stream()
            .map(b -> new EarnedBadge(b.getId(), b.getDisplayName(), b.getGlyph(), b.getCategory(), b.getEarnedAt()))
            .sorted(Comparator.comparing(EarnedBadge::earnedAt).reversed())
            .toList();

        return new PublicProfile(
            user.getUsername(),
            user.getCreatedAt(),
            user.getRank(),
            user.getTotalXp(),
            user.getStreakDays(),
            topics,
            badges
        );
    }

}
