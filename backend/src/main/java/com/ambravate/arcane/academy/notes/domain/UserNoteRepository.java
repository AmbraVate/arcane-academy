package com.ambravate.arcane.academy.notes.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserNoteRepository extends JpaRepository<UserNote, String> {

    List<UserNote> findByUserIdOrderByUpdatedAtDesc(String userId);

    Optional<UserNote> findByIdAndUserId(String id, String userId);

    boolean existsByUserIdAndLessonId(String userId, String lessonId);

    Optional<UserNote> findByUserIdAndLessonId(String userId, String lessonId);

    long countByUserId(String userId);
}
