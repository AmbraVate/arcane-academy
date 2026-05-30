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
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("GraphController")
class GraphControllerTest {

    private static final String USER_ID = "user-1";

    @Mock DomainRepository domainRepository;
    @Mock LearningModuleRepository moduleRepository;
    @Mock LessonRepository lessonRepository;
    @Mock UserChunkProgressRepository progressRepository;
    @Mock ModuleGraphService moduleGraphService;
    @Mock SpacingService spacingService;

    @InjectMocks
    GraphController controller;

    // Real ObjectMapper — needed to parse integrationDomainsJson
    final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        // Inject real ObjectMapper via field (InjectMocks won't pick it up automatically
        // because it's not a @Mock — assign directly after construction)
        try {
            var field = GraphController.class.getDeclaredField("objectMapper");
            field.setAccessible(true);
            field.set(controller, objectMapper);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private UserPrincipal principal() {
        return new UserPrincipal(USER_ID, "tester", "test@example.com");
    }

    // ── Fixture helpers ────────────────────────────────────────────────────────

    private Domain domain(String id, String name) {
        return Domain.builder().id(id).name(name).glyph("⚡").active(true).sortOrder(1).build();
    }

    private LearningModule module(String id, String domainId, String title) {
        return LearningModule.builder()
                .id(id).title(title).trackId(domainId)
                .tier(LearnerPath.APPRENTICE).sortOrder(1)
                .build();
    }

    private Lesson lesson(String id, String moduleId, String title) {
        return Lesson.builder().id(id).moduleId(moduleId).title(title).sortOrder(1).build();
    }

    private Lesson lessonWithInteg(String id, String moduleId, String title, String integJson) {
        return Lesson.builder()
                .id(id).moduleId(moduleId).title(title).sortOrder(1)
                .integrationDomainsJson(integJson)
                .build();
    }

    // ── Tests ─────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("node construction")
    class NodeConstruction {

        @Test
        @DisplayName("produces a DOMAIN node for each active domain")
        void domainNodesAreCreated() {
            Domain java = domain("java", "Java");
            when(domainRepository.findByActiveTrue()).thenReturn(List.of(java));
            when(moduleRepository.findAllByOrderBySortOrderAsc()).thenReturn(List.of());
            when(lessonRepository.findByModuleIdIn(anyList())).thenReturn(List.of());
            when(progressRepository.findByUserId(USER_ID)).thenReturn(List.of());
            when(moduleGraphService.getAllModulesWithStatus(USER_ID)).thenReturn(List.of());

            ResponseEntity<GraphResponseDto> resp = controller.getGraph(principal());
            GraphResponseDto body = resp.getBody();

            assertThat(body).isNotNull();
            assertThat(body.getNodes())
                    .extracting(GraphNodeDto::getType)
                    .containsOnly("DOMAIN");
            assertThat(body.getNodes())
                    .extracting(GraphNodeDto::getId)
                    .containsExactly("java");
        }

        @Test
        @DisplayName("produces a MODULE node for each module")
        void moduleNodesAreCreated() {
            Domain java = domain("java", "Java");
            LearningModule mod = module("mod-1", "java", "Module 1");

            when(domainRepository.findByActiveTrue()).thenReturn(List.of(java));
            when(moduleRepository.findAllByOrderBySortOrderAsc()).thenReturn(List.of(mod));
            when(lessonRepository.findByModuleIdIn(List.of("mod-1"))).thenReturn(List.of());
            when(progressRepository.findByUserId(USER_ID)).thenReturn(List.of());
            when(moduleGraphService.getAllModulesWithStatus(USER_ID))
                    .thenReturn(List.of(new ModuleWithStatus(mod, "UNLOCKED")));

            ResponseEntity<GraphResponseDto> resp = controller.getGraph(principal());
            GraphResponseDto body = resp.getBody();

            assertThat(body).isNotNull();
            List<GraphNodeDto> moduleNodes = body.getNodes().stream()
                    .filter(n -> "MODULE".equals(n.getType())).toList();
            assertThat(moduleNodes).hasSize(1);
            assertThat(moduleNodes.getFirst().getId()).isEqualTo("mod-1");
            assertThat(moduleNodes.getFirst().getDomainId()).isEqualTo("java");
            assertThat(moduleNodes.getFirst().getTier()).isEqualTo("APPRENTICE");
        }

        @Test
        @DisplayName("produces a LESSON node for each lesson")
        void lessonNodesAreCreated() {
            Domain java = domain("java", "Java");
            LearningModule mod = module("mod-1", "java", "Module 1");
            Lesson les = lesson("les-1", "mod-1", "Hello World");

            when(domainRepository.findByActiveTrue()).thenReturn(List.of(java));
            when(moduleRepository.findAllByOrderBySortOrderAsc()).thenReturn(List.of(mod));
            when(lessonRepository.findByModuleIdIn(List.of("mod-1"))).thenReturn(List.of(les));
            when(progressRepository.findByUserId(USER_ID)).thenReturn(List.of());
            when(moduleGraphService.getAllModulesWithStatus(USER_ID))
                    .thenReturn(List.of(new ModuleWithStatus(mod, "UNLOCKED")));

            ResponseEntity<GraphResponseDto> resp = controller.getGraph(principal());
            GraphResponseDto body = resp.getBody();

            assertThat(body).isNotNull();
            List<GraphNodeDto> lessonNodes = body.getNodes().stream()
                    .filter(n -> "LESSON".equals(n.getType())).toList();
            assertThat(lessonNodes).hasSize(1);
            assertThat(lessonNodes.getFirst().getId()).isEqualTo("les-1");
            assertThat(lessonNodes.getFirst().getStatus()).isEqualTo("NOT_STARTED");
            assertThat(lessonNodes.getFirst().getMemoryStrength()).isZero();
        }
    }

    @Nested
    @DisplayName("edge construction")
    class EdgeConstruction {

        @Test
        @DisplayName("creates HIERARCHY edge from domain to module")
        void hierarchyEdgeDomainToModule() {
            Domain java = domain("java", "Java");
            LearningModule mod = module("mod-1", "java", "Module 1");

            when(domainRepository.findByActiveTrue()).thenReturn(List.of(java));
            when(moduleRepository.findAllByOrderBySortOrderAsc()).thenReturn(List.of(mod));
            when(lessonRepository.findByModuleIdIn(List.of("mod-1"))).thenReturn(List.of());
            when(progressRepository.findByUserId(USER_ID)).thenReturn(List.of());
            when(moduleGraphService.getAllModulesWithStatus(USER_ID))
                    .thenReturn(List.of(new ModuleWithStatus(mod, "UNLOCKED")));

            GraphResponseDto body = controller.getGraph(principal()).getBody();

            assertThat(body).isNotNull();
            List<GraphEdgeDto> hierarchy = body.getEdges().stream()
                    .filter(e -> "HIERARCHY".equals(e.getEdgeType())).toList();
            assertThat(hierarchy).hasSize(1);
            assertThat(hierarchy.getFirst().getSource()).isEqualTo("java");
            assertThat(hierarchy.getFirst().getTarget()).isEqualTo("mod-1");
        }

        @Test
        @DisplayName("creates HIERARCHY edge from module to lesson")
        void hierarchyEdgeModuleToLesson() {
            Domain java = domain("java", "Java");
            LearningModule mod = module("mod-1", "java", "Module 1");
            Lesson les = lesson("les-1", "mod-1", "Hello World");

            when(domainRepository.findByActiveTrue()).thenReturn(List.of(java));
            when(moduleRepository.findAllByOrderBySortOrderAsc()).thenReturn(List.of(mod));
            when(lessonRepository.findByModuleIdIn(List.of("mod-1"))).thenReturn(List.of(les));
            when(progressRepository.findByUserId(USER_ID)).thenReturn(List.of());
            when(moduleGraphService.getAllModulesWithStatus(USER_ID))
                    .thenReturn(List.of(new ModuleWithStatus(mod, "UNLOCKED")));

            GraphResponseDto body = controller.getGraph(principal()).getBody();

            assertThat(body).isNotNull();
            List<GraphEdgeDto> hierarchy = body.getEdges().stream()
                    .filter(e -> "HIERARCHY".equals(e.getEdgeType())).toList();
            // Domain→module + module→lesson = 2 hierarchy edges
            assertThat(hierarchy).hasSize(2);
            assertThat(hierarchy)
                    .anyMatch(e -> "mod-1".equals(e.getSource()) && "les-1".equals(e.getTarget()));
        }

        @Test
        @DisplayName("creates INTEGRATION edges from lesson to cross-domain targets")
        void integrationEdgesAreCreated() {
            Domain java = domain("java", "Java");
            Domain psych = domain("psychology", "Psychology");
            Domain maths = domain("mathematics", "Mathematics");
            LearningModule mod = module("mod-1", "java", "Module 1");
            // Lesson bridges to psychology AND mathematics
            Lesson les = lessonWithInteg("les-1", "mod-1", "Variables",
                    "[\"psychology\",\"mathematics\"]");

            when(domainRepository.findByActiveTrue()).thenReturn(List.of(java, psych, maths));
            when(moduleRepository.findAllByOrderBySortOrderAsc()).thenReturn(List.of(mod));
            when(lessonRepository.findByModuleIdIn(List.of("mod-1"))).thenReturn(List.of(les));
            when(progressRepository.findByUserId(USER_ID)).thenReturn(List.of());
            when(moduleGraphService.getAllModulesWithStatus(USER_ID))
                    .thenReturn(List.of(new ModuleWithStatus(mod, "UNLOCKED")));

            GraphResponseDto body = controller.getGraph(principal()).getBody();

            assertThat(body).isNotNull();
            List<GraphEdgeDto> integEdges = body.getEdges().stream()
                    .filter(e -> "INTEGRATION".equals(e.getEdgeType())).toList();
            assertThat(integEdges).hasSize(2);
            assertThat(integEdges)
                    .anyMatch(e -> "les-1".equals(e.getSource()) && "psychology".equals(e.getTarget()))
                    .anyMatch(e -> "les-1".equals(e.getSource()) && "mathematics".equals(e.getTarget()));
        }

        @Test
        @DisplayName("does not create integration edge to own domain")
        void noSelfIntegrationEdge() {
            Domain java = domain("java", "Java");
            LearningModule mod = module("mod-1", "java", "Module 1");
            // integrationDomains includes own domain — should be ignored
            Lesson les = lessonWithInteg("les-1", "mod-1", "Variables", "[\"java\"]");

            when(domainRepository.findByActiveTrue()).thenReturn(List.of(java));
            when(moduleRepository.findAllByOrderBySortOrderAsc()).thenReturn(List.of(mod));
            when(lessonRepository.findByModuleIdIn(List.of("mod-1"))).thenReturn(List.of(les));
            when(progressRepository.findByUserId(USER_ID)).thenReturn(List.of());
            when(moduleGraphService.getAllModulesWithStatus(USER_ID))
                    .thenReturn(List.of(new ModuleWithStatus(mod, "UNLOCKED")));

            GraphResponseDto body = controller.getGraph(principal()).getBody();

            assertThat(body).isNotNull();
            assertThat(body.getEdges())
                    .noneMatch(e -> "INTEGRATION".equals(e.getEdgeType()));
        }

        @Test
        @DisplayName("does not create integration edge to unknown domain")
        void noIntegrationEdgeToUnknownDomain() {
            Domain java = domain("java", "Java");
            LearningModule mod = module("mod-1", "java", "Module 1");
            // "unknown-domain" is not in the active domain list
            Lesson les = lessonWithInteg("les-1", "mod-1", "Variables", "[\"unknown-domain\"]");

            when(domainRepository.findByActiveTrue()).thenReturn(List.of(java));
            when(moduleRepository.findAllByOrderBySortOrderAsc()).thenReturn(List.of(mod));
            when(lessonRepository.findByModuleIdIn(List.of("mod-1"))).thenReturn(List.of(les));
            when(progressRepository.findByUserId(USER_ID)).thenReturn(List.of());
            when(moduleGraphService.getAllModulesWithStatus(USER_ID))
                    .thenReturn(List.of(new ModuleWithStatus(mod, "UNLOCKED")));

            GraphResponseDto body = controller.getGraph(principal()).getBody();

            assertThat(body).isNotNull();
            assertThat(body.getEdges())
                    .noneMatch(e -> "INTEGRATION".equals(e.getEdgeType()));
        }

        @Test
        @DisplayName("creates PREREQUISITE edge between modules")
        void prerequisiteEdgesAreCreated() {
            Domain java = domain("java", "Java");
            LearningModule mod1 = module("mod-1", "java", "Module 1");
            LearningModule mod2 = LearningModule.builder()
                    .id("mod-2").title("Module 2").trackId("java")
                    .tier(LearnerPath.APPRENTICE).sortOrder(2)
                    .prerequisites(List.of(mod1)) // mod2 requires mod1
                    .build();

            when(domainRepository.findByActiveTrue()).thenReturn(List.of(java));
            when(moduleRepository.findAllByOrderBySortOrderAsc()).thenReturn(List.of(mod1, mod2));
            when(lessonRepository.findByModuleIdIn(anyList())).thenReturn(List.of());
            when(progressRepository.findByUserId(USER_ID)).thenReturn(List.of());
            when(moduleGraphService.getAllModulesWithStatus(USER_ID))
                    .thenReturn(List.of(
                            new ModuleWithStatus(mod1, "COMPLETE"),
                            new ModuleWithStatus(mod2, "UNLOCKED")));

            GraphResponseDto body = controller.getGraph(principal()).getBody();

            assertThat(body).isNotNull();
            List<GraphEdgeDto> prereqs = body.getEdges().stream()
                    .filter(e -> "PREREQUISITE".equals(e.getEdgeType())).toList();
            assertThat(prereqs).hasSize(1);
            assertThat(prereqs.getFirst().getSource()).isEqualTo("mod-1");
            assertThat(prereqs.getFirst().getTarget()).isEqualTo("mod-2");
        }
    }

    @Nested
    @DisplayName("progress + memory strength")
    class ProgressAndMemoryStrength {

        @Test
        @DisplayName("lesson status reflects user progress")
        void lessonStatusFromProgress() {
            Domain java = domain("java", "Java");
            LearningModule mod = module("mod-1", "java", "Module 1");
            Lesson les = lesson("les-1", "mod-1", "Hello World");

            UserChunkProgress progress = UserChunkProgress.builder()
                    .userId(USER_ID).lessonId("les-1")
                    .status(LessonStatus.COMPLETE)
                    .fsrsStability(5.0)
                    .build();

            when(domainRepository.findByActiveTrue()).thenReturn(List.of(java));
            when(moduleRepository.findAllByOrderBySortOrderAsc()).thenReturn(List.of(mod));
            when(lessonRepository.findByModuleIdIn(List.of("mod-1"))).thenReturn(List.of(les));
            when(progressRepository.findByUserId(USER_ID)).thenReturn(List.of(progress));
            when(moduleGraphService.getAllModulesWithStatus(USER_ID))
                    .thenReturn(List.of(new ModuleWithStatus(mod, "COMPLETE")));
            when(spacingService.computeDecayedStrength(progress)).thenReturn(0.9);

            GraphResponseDto body = controller.getGraph(principal()).getBody();

            assertThat(body).isNotNull();
            GraphNodeDto lessonNode = body.getNodes().stream()
                    .filter(n -> "les-1".equals(n.getId())).findFirst().orElseThrow();
            assertThat(lessonNode.getStatus()).isEqualTo("COMPLETE");
            assertThat(lessonNode.getMemoryStrength()).isEqualTo(0.9);
        }
    }
}
