package com.arcane.academy.repository;

import com.arcane.academy.model.Quest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuestRepository extends JpaRepository<Quest, String> {
    List<Quest> findAllByOrderByChapterNumberAscOrderInChapterAsc();
}
