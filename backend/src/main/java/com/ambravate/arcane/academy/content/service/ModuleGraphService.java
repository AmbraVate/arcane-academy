package com.ambravate.arcane.academy.content.service;

import com.ambravate.arcane.academy.common.domain.LearningModule;
import com.ambravate.arcane.academy.common.domain.Lesson;
import com.ambravate.arcane.academy.common.domain.LessonStatus;
import com.ambravate.arcane.academy.common.domain.UserChunkProgress;
import com.ambravate.arcane.academy.content.repository.LearningModuleRepository;
import com.ambravate.arcane.academy.content.repository.LessonRepository;
import com.ambravate.arcane.academy.practice.repository.UserChunkProgressRepository;
import com.ambravate.arcane.academy.content.domain.ModuleWithStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ModuleGraphService {

    private final LearningModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;
    private final UserChunkProgressRepository progressRepository;

    public List<ModuleWithStatus> getAllModulesWithStatus(String userId) {
        List<LearningModule> allModules = moduleRepository.findAllByOrderBySortOrderAsc();
        List<UserChunkProgress> allProgress = progressRepository.findByUserId(userId);
        Map<String, UserChunkProgress> progressByLesson = allProgress.stream()
                .collect(Collectors.toMap(UserChunkProgress::getLessonId, p -> p, (a, b) -> a));

        Set<String> completedModuleIds = new HashSet<>();
        for (LearningModule module : allModules) {
            List<Lesson> lessons = lessonRepository.findByModuleIdOrderBySortOrderAsc(module.getId());
            if (!lessons.isEmpty() && lessons.stream().allMatch(l -> {
                UserChunkProgress p = progressByLesson.get(l.getId());
                return p != null && (p.getStatus() == LessonStatus.COMPLETE || p.getStatus() == LessonStatus.SKIPPED);
            })) {
                completedModuleIds.add(module.getId());
            }
        }

        List<ModuleWithStatus> result = new ArrayList<>();
        for (LearningModule module : allModules) {
            String status;
            if (completedModuleIds.contains(module.getId())) {
                status = "COMPLETE";
            } else if (!isModuleUnlocked(module, completedModuleIds)) {
                status = "LOCKED";
            } else {
                List<Lesson> lessons = lessonRepository.findByModuleIdOrderBySortOrderAsc(module.getId());
                boolean anyStarted = lessons.stream().anyMatch(l -> {
                    UserChunkProgress p = progressByLesson.get(l.getId());
                    return p != null && p.getStatus() == LessonStatus.IN_PROGRESS;
                });
                status = anyStarted ? "IN_PROGRESS" : "UNLOCKED";
            }
            result.add(new ModuleWithStatus(module, status));
        }
        return result;
    }

    public boolean isModuleUnlocked(String userId, String moduleId) {
        LearningModule module = moduleRepository.findById(moduleId).orElse(null);
        if (module == null) return false;
        return isModuleUnlocked(module, getCompletedModuleIds(userId));
    }

    private boolean isModuleUnlocked(LearningModule module, Set<String> completedModuleIds) {
        List<String> prereqs = module.getPrerequisites().stream().map(LearningModule::getId).toList();
        if (prereqs.isEmpty()) return true;
        return completedModuleIds.containsAll(prereqs);
    }

    public Lesson getNextLesson(String userId, String moduleId) {
        List<Lesson> lessons = lessonRepository.findByModuleIdOrderBySortOrderAsc(moduleId);
        for (Lesson l : lessons) {
            Optional<UserChunkProgress> progress = progressRepository.findByUserIdAndLessonId(userId, l.getId());
            if (progress.isEmpty() || progress.get().getStatus() == LessonStatus.NOT_STARTED
                    || progress.get().getStatus() == LessonStatus.IN_PROGRESS) {
                return l;
            }
        }
        return null;
    }

    public Set<String> getCompletedModuleIds(String userId) {
        List<LearningModule> allModules = moduleRepository.findAllByOrderBySortOrderAsc();
        List<UserChunkProgress> allProgress = progressRepository.findByUserId(userId);
        Map<String, UserChunkProgress> progressByLesson = allProgress.stream()
                .collect(Collectors.toMap(UserChunkProgress::getLessonId, p -> p, (a, b) -> a));

        Set<String> completed = new HashSet<>();
        for (LearningModule module : allModules) {
            List<Lesson> lessons = lessonRepository.findByModuleIdOrderBySortOrderAsc(module.getId());
            if (!lessons.isEmpty() && lessons.stream().allMatch(l -> {
                UserChunkProgress p = progressByLesson.get(l.getId());
                return p != null && (p.getStatus() == LessonStatus.COMPLETE || p.getStatus() == LessonStatus.SKIPPED);
            })) {
                completed.add(module.getId());
            }
        }
        return completed;
    }

}
