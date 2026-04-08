package com.ambravate.polymath.academy.repository;

import com.ambravate.polymath.academy.model.Boss;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BossRepository extends JpaRepository<Boss, String> {
    List<Boss> findAllByOrderByChapterNumberAsc();
}
