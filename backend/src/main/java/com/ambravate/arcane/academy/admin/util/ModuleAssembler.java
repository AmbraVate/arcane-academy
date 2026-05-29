package com.ambravate.arcane.academy.admin.util;

import com.ambravate.arcane.academy.admin.dto.AdminModuleDto;
import com.ambravate.arcane.academy.common.domain.LearningModule;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ModuleAssembler {

  public AdminModuleDto toDto(LearningModule m, long lessonCount) {
    List<String> prereqs = m.getPrerequisites().stream()
        .map(LearningModule::getId)
        .toList();
    return AdminModuleDto.builder()
        .id(m.getId()).title(m.getTitle()).glyph(m.getGlyph())
        .sortOrder(m.getSortOrder()).tier(m.getTier().name())
        .domainId(m.getDomainId()).prerequisiteIds(prereqs)
        .lessonCount(lessonCount)
        .build();
  }

}
