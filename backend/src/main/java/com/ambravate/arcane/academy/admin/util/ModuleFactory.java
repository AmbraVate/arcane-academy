package com.ambravate.arcane.academy.admin.util;

import com.ambravate.arcane.academy.admin.dto.AdminModuleDto;
import com.ambravate.arcane.academy.common.domain.LearningModule;
import com.ambravate.arcane.academy.common.domain.LearnerPath;
import com.ambravate.arcane.academy.content.repository.LearningModuleRepository;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ModuleFactory {

  private final LearningModuleRepository moduleRepository;

  public ModuleFactory(LearningModuleRepository moduleRepository) {
    this.moduleRepository = moduleRepository;
  }

  public LearningModule buildModule(AdminModuleDto req) {
    List<LearningModule> prereqs = req.getPrerequisiteIds() == null ? List.of() :
        req.getPrerequisiteIds().stream()
            .map(pId -> moduleRepository.findById(pId)
                .orElseThrow(() -> new IllegalArgumentException("Prerequisite module not found: " + pId)))
            .toList();
    return LearningModule.builder()
        .id(req.getId()).title(req.getTitle()).glyph(req.getGlyph())
        .sortOrder(req.getSortOrder())
        .tier(req.getTier() != null ? LearnerPath.valueOf(req.getTier()) : LearnerPath.APPRENTICE)
        .trackId(req.getDomainId() != null ? req.getDomainId() : "java")
        .prerequisites(prereqs)
        .build();
  }

}
