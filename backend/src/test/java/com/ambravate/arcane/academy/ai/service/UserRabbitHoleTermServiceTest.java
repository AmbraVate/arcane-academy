package com.ambravate.arcane.academy.ai.service;

import com.ambravate.arcane.academy.common.domain.UserRabbitHoleTerm;
import com.ambravate.arcane.academy.content.repository.UserRabbitHoleTermRepository;
import com.ambravate.arcane.academy.practice.dto.RabbitHoleTermDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserRabbitHoleTermService")
class UserRabbitHoleTermServiceTest {

    private static final String USER_ID = "user-42";
    private static final String TERM = "Garbage Collection";
    private static final String DESCRIPTION = "The process by which the JVM reclaims unreachable heap memory.";
    private static final String SUB_CHUNK_ID = "java-fnd-4a";
    private static final String TOPIC_ID = "java";

    @Mock
    private UserRabbitHoleTermRepository repository;

    @InjectMocks
    private UserRabbitHoleTermService service;

    private UserRabbitHoleTerm existingTerm;

    @BeforeEach
    void setUp() {
        existingTerm = UserRabbitHoleTerm.builder()
                .id("rht-1")
                .userId(USER_ID)
                .term(TERM)
                .description(DESCRIPTION)
                .subChunkId(SUB_CHUNK_ID)
                .topicId(TOPIC_ID)
                .savedAt(Instant.now())
                .build();
    }

    // ── getAll() ────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("getAll()")
    class GetAll {

        @Test
        @DisplayName("returns an empty list when the user has no saved terms")
        void emptyWhenNoTerms() {
            when(repository.findByUserIdOrderBySavedAtDesc(USER_ID)).thenReturn(List.of());

            List<RabbitHoleTermDto> result = service.getAll(USER_ID);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("maps every entity to a DTO preserving all fields")
        void mapsEntityToDto() {
            when(repository.findByUserIdOrderBySavedAtDesc(USER_ID)).thenReturn(List.of(existingTerm));

            List<RabbitHoleTermDto> result = service.getAll(USER_ID);

            assertThat(result).hasSize(1);
            RabbitHoleTermDto dto = result.get(0);
            assertThat(dto.getId()).isEqualTo("rht-1");
            assertThat(dto.getTerm()).isEqualTo(TERM);
            assertThat(dto.getDescription()).isEqualTo(DESCRIPTION);
            assertThat(dto.getSubChunkId()).isEqualTo(SUB_CHUNK_ID);
            assertThat(dto.getTopicId()).isEqualTo(TOPIC_ID);
        }

        @Test
        @DisplayName("returns multiple terms in repository order (newest first)")
        void preservesRepositoryOrder() {
            UserRabbitHoleTerm older = UserRabbitHoleTerm.builder()
                    .id("rht-2").userId(USER_ID).term("Heap").build();
            when(repository.findByUserIdOrderBySavedAtDesc(USER_ID))
                    .thenReturn(List.of(existingTerm, older));

            List<RabbitHoleTermDto> result = service.getAll(USER_ID);

            assertThat(result).extracting(RabbitHoleTermDto::getTerm)
                    .containsExactly(TERM, "Heap");
        }
    }

    // ── save() ──────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("save()")
    class Save {

        @Test
        @DisplayName("persists a new term when it does not yet exist for this user")
        void savesNewTerm() {
            when(repository.findByUserIdAndTerm(USER_ID, TERM)).thenReturn(Optional.empty());
            when(repository.save(any())).thenReturn(existingTerm);

            RabbitHoleTermDto dto = service.save(USER_ID, TERM, DESCRIPTION, SUB_CHUNK_ID, TOPIC_ID);

            ArgumentCaptor<UserRabbitHoleTerm> captor = ArgumentCaptor.forClass(UserRabbitHoleTerm.class);
            verify(repository).save(captor.capture());
            UserRabbitHoleTerm saved = captor.getValue();
            assertThat(saved.getUserId()).isEqualTo(USER_ID);
            assertThat(saved.getTerm()).isEqualTo(TERM);
            assertThat(saved.getDescription()).isEqualTo(DESCRIPTION);
            assertThat(dto.getTerm()).isEqualTo(TERM);
        }

        @Test
        @DisplayName("updates description when the term already exists for this user (idempotent)")
        void updatesExistingTerm() {
            when(repository.findByUserIdAndTerm(USER_ID, TERM)).thenReturn(Optional.of(existingTerm));
            when(repository.save(existingTerm)).thenReturn(existingTerm);

            String newDesc = "Updated description";
            service.save(USER_ID, TERM, newDesc, SUB_CHUNK_ID, TOPIC_ID);

            assertThat(existingTerm.getDescription()).isEqualTo(newDesc);
            verify(repository).save(existingTerm);
            verify(repository, times(1)).save(any());
        }

        @Test
        @DisplayName("returned DTO contains the stored term name")
        void returnedDtoHasTerm() {
            when(repository.findByUserIdAndTerm(USER_ID, TERM)).thenReturn(Optional.empty());
            when(repository.save(any())).thenReturn(existingTerm);

            RabbitHoleTermDto dto = service.save(USER_ID, TERM, DESCRIPTION, SUB_CHUNK_ID, TOPIC_ID);

            assertThat(dto.getTerm()).isEqualTo(TERM);
        }

        @Test
        @DisplayName("allows null description (term saved without explanation)")
        void allowsNullDescription() {
            UserRabbitHoleTerm noDesc = UserRabbitHoleTerm.builder()
                    .id("rht-3").userId(USER_ID).term("Stack").build();
            when(repository.findByUserIdAndTerm(USER_ID, "Stack")).thenReturn(Optional.empty());
            when(repository.save(any())).thenReturn(noDesc);

            RabbitHoleTermDto dto = service.save(USER_ID, "Stack", null, null, null);

            assertThat(dto.getDescription()).isNull();
        }
    }

    // ── remove() ────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("remove()")
    class Remove {

        @Test
        @DisplayName("delegates to repository with user ID and term")
        void delegatesToRepository() {
            service.remove(USER_ID, TERM);

            verify(repository).deleteByUserIdAndTerm(USER_ID, TERM);
        }

        @Test
        @DisplayName("does not throw when the term does not exist")
        void noErrorWhenMissing() {
            doNothing().when(repository).deleteByUserIdAndTerm(any(), any());

            service.remove(USER_ID, "NonExistentTerm");

            verify(repository).deleteByUserIdAndTerm(USER_ID, "NonExistentTerm");
        }
    }
}
