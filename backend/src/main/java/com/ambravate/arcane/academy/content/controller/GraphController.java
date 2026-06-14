package com.ambravate.arcane.academy.content.controller;

import com.ambravate.arcane.academy.ai.service.SpacingService;
import com.ambravate.arcane.academy.common.domain.*;
import com.ambravate.arcane.academy.common.security.UserPrincipal;
import com.ambravate.arcane.academy.content.domain.ModuleWithStatus;
import com.ambravate.arcane.academy.content.dto.GraphEdgeDto;
import com.ambravate.arcane.academy.content.dto.GraphNodeDto;
import com.ambravate.arcane.academy.content.dto.GraphResponseDto;
import com.ambravate.arcane.academy.content.repository.DomainRepository;
import com.ambravate.arcane.academy.content.repository.LearningModuleRepository;
import com.ambravate.arcane.academy.content.repository.LessonRepository;
import com.ambravate.arcane.academy.content.service.ModuleGraphService;
import com.ambravate.arcane.academy.practice.repository.UserChunkProgressRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Exposes the curriculum knowledge graph for the React Flow knowledge-map UI (Phase 7).
 *
 * <p>{@code GET /api/graph} returns all DOMAIN, MODULE, and LESSON nodes plus three
 * edge types:
 * <ul>
 *   <li>{@code HIERARCHY} — structural containment: Domain→Module and Module→Lesson</li>
 *   <li>{@code PREREQUISITE} — a module must be completed before another</li>
 *   <li>{@code INTEGRATION} — a lesson bridges to a cross-domain concept (from
 *       {@code integrationDomainsJson})</li>
 * </ul>
 *
 * <p>Each node carries the authenticated user's progress status and FSRS
 * memory-strength so the frontend can colour nodes by health.
 */
@RestController
@RequestMapping("/api/graph")
@RequiredArgsConstructor
@Slf4j
public class GraphController {

    private final DomainRepository domainRepository;
    private final LearningModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;
    private final UserChunkProgressRepository progressRepository;
    private final ModuleGraphService moduleGraphService;
    private final SpacingService spacingService;
    private final ObjectMapper objectMapper;

    private static final TypeReference<List<String>> STRING_LIST = new TypeReference<>() {};

    @GetMapping
    public ResponseEntity<GraphResponseDto> getGraph(
            @AuthenticationPrincipal UserPrincipal user) {

        String userId = user.getId();

        // ── Data loading ──────────────────────────────────────────────────────
        List<Domain> domains = domainRepository.findByActiveTrue();
        List<LearningModule> modules = moduleRepository.findAllByOrderBySortOrderAsc();

        // Load all lessons in one query via module ids
        List<String> moduleIds = modules.stream().map(LearningModule::getId).toList();
        List<Lesson> lessons = lessonRepository.findByModuleIdIn(moduleIds);

        Map<String, UserChunkProgress> progressByLesson = progressRepository.findByUserId(userId)
                .stream()
                .collect(Collectors.toMap(UserChunkProgress::getLessonId, p -> p, (a, b) -> a));

        // Module status + completion for lock logic
        List<ModuleWithStatus> moduleStatuses = moduleGraphService.getAllModulesWithStatus(userId);
        Map<String, String> moduleStatusMap = moduleStatuses.stream()
                .collect(Collectors.toMap(mws -> mws.module().getId(), ModuleWithStatus::status));

        // Pre-build module → lessons index for memory-strength aggregation
        Map<String, List<Lesson>> lessonsByModule = lessons.stream()
                .collect(Collectors.groupingBy(Lesson::getModuleId));

        // Module lookup map
        Map<String, LearningModule> moduleById = modules.stream()
                .collect(Collectors.toMap(LearningModule::getId, m -> m));

        // Domain id set — used to validate integration edge targets
        Set<String> domainIds = domains.stream()
                .map(Domain::getId)
                .collect(Collectors.toSet());

        List<GraphNodeDto> nodes = new ArrayList<>();
        List<GraphEdgeDto> edges = new ArrayList<>();

        // ── Domain nodes ──────────────────────────────────────────────────────
        for (Domain domain : domains) {
            nodes.add(GraphNodeDto.builder()
                    .id(domain.getId())
                    .type("DOMAIN")
                    .label(domain.getName())
                    .glyph(domain.getGlyph())
                    .domainId(domain.getId())
                    .build());
        }

        // ── Module nodes + hierarchy + prerequisite edges ─────────────────────
        for (LearningModule module : modules) {
            String domainId = module.getTrackId();

            // Aggregate memory strength across all lessons in this module
            List<Lesson> moduleLessons = lessonsByModule.getOrDefault(module.getId(), List.of());
            double avgStrength = moduleLessons.stream()
                    .map(l -> progressByLesson.get(l.getId()))
                    .filter(Objects::nonNull)
                    .mapToDouble(spacingService::computeDecayedStrength)
                    .average()
                    .orElse(0.0);

            String status = moduleStatusMap.getOrDefault(module.getId(), "UNLOCKED");

            nodes.add(GraphNodeDto.builder()
                    .id(module.getId())
                    .type("MODULE")
                    .label(module.getTitle())
                    .glyph(module.getGlyph())
                    .domainId(domainId)
                    .tier(module.getTier() != null ? module.getTier().name() : null)
                    .status(status)
                    .memoryStrength(avgStrength)
                    .build());

            // Domain → Module hierarchy edge (only if domain is in our active set)
            if (domainIds.contains(domainId)) {
                edges.add(GraphEdgeDto.builder()
                        .id("e-d-" + domainId + "-m-" + module.getId())
                        .source(domainId)
                        .target(module.getId())
                        .edgeType("HIERARCHY")
                        .build());
            }

            // Prerequisite edges: prereqModule → this module (must-complete-before)
            for (LearningModule prereq : module.getPrerequisites()) {
                edges.add(GraphEdgeDto.builder()
                        .id("e-pre-" + prereq.getId() + "-" + module.getId())
                        .source(prereq.getId())
                        .target(module.getId())
                        .edgeType("PREREQUISITE")
                        .build());
            }
        }

        // ── Lesson nodes + hierarchy + integration edges ───────────────────────
        for (Lesson lesson : lessons) {
            UserChunkProgress progress = progressByLesson.get(lesson.getId());
            double strength = progress != null ? spacingService.computeDecayedStrength(progress) : 0.0;
            String status = progress != null ? progress.getStatus().name() : "NOT_STARTED";

            LearningModule parentModule = moduleById.get(lesson.getModuleId());
            String domainId = parentModule != null ? parentModule.getTrackId() : null;
            String tier = parentModule != null && parentModule.getTier() != null
                    ? parentModule.getTier().name() : null;

            nodes.add(GraphNodeDto.builder()
                    .id(lesson.getId())
                    .type("LESSON")
                    .label(lesson.getTitle())
                    .domainId(domainId)
                    .tier(tier)
                    .status(status)
                    .memoryStrength(strength)
                    .build());

            // Module → Lesson hierarchy edge
            edges.add(GraphEdgeDto.builder()
                    .id("e-m-" + lesson.getModuleId() + "-l-" + lesson.getId())
                    .source(lesson.getModuleId())
                    .target(lesson.getId())
                    .edgeType("HIERARCHY")
                    .build());

            // Integration edges: Lesson → Domain (cross-domain connections)
            if (lesson.getIntegrationDomainsJson() != null) {
                try {
                    List<String> integDomains = objectMapper.readValue(
                            lesson.getIntegrationDomainsJson(), STRING_LIST);
                    for (String integDomainId : integDomains) {
                        // Only create the edge if the target domain exists and differs from own domain
                        if (domainIds.contains(integDomainId)
                                && !integDomainId.equals(domainId)) {
                            edges.add(GraphEdgeDto.builder()
                                    .id("e-integ-" + lesson.getId() + "-" + integDomainId)
                                    .source(lesson.getId())
                                    .target(integDomainId)
                                    .edgeType("INTEGRATION")
                                    .build());
                        }
                    }
                } catch (Exception e) {
                    log.debug("Failed to parse integrationDomainsJson for lesson {}: {}",
                            lesson.getId(), e.getMessage());
                }
            }
        }

        return ResponseEntity.ok(GraphResponseDto.builder()
                .nodes(nodes)
                .edges(edges)
                .build());
    }
}
