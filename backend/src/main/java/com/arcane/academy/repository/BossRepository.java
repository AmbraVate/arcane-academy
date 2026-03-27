// Copyright (c) 2026 AmbraVate. All rights reserved. See LICENSE for terms.
package com.arcane.academy.repository;

import com.arcane.academy.model.Boss;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BossRepository extends JpaRepository<Boss, String> {
    List<Boss> findAllByOrderByChapterNumberAsc();
}
