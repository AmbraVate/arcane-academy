package com.ambravate.arcane.academy.admin.controller;

import com.ambravate.arcane.academy.admin.dto.AdminModuleDto;
import com.ambravate.arcane.academy.admin.util.ModuleAssembler;
import com.ambravate.arcane.academy.admin.util.ModuleFactory;
import com.ambravate.arcane.academy.common.domain.LearningModule;
import com.ambravate.arcane.academy.common.domain.LearnerPath;
import com.ambravate.arcane.academy.content.repository.LearningModuleRepository;
import com.ambravate.arcane.academy.content.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/admin/modules")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminModuleController {

    private final LearningModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;
    private final ModuleFactory moduleFactory;
    private final ModuleAssembler moduleAssembler;

    @GetMapping
    public ResponseEntity<List<AdminModuleDto>> list(
            @RequestParam(required = false) String domainId) {
        List<LearningModule> modules = (domainId != null)
                ? moduleRepository.findByDomainIdOrderBySortOrderAsc(domainId)
                : moduleRepository.findAllByOrderBySortOrderAsc();
        return ResponseEntity.ok(modules.stream()
            .map(m -> moduleAssembler.toDto(m, lessonRepository.findByModuleIdOrderBySortOrderAsc(m.getId()).size()))
            .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminModuleDto> get(@PathVariable String id) {
        return moduleRepository.findById(id)
                .map(m -> ResponseEntity.ok(moduleAssembler.toDto(m, lessonRepository.findByModuleIdOrderBySortOrderAsc(m.getId()).size())))
                .orElseThrow(() -> new NoSuchElementException("Module not found: " + id));
    }

    @PostMapping
    public ResponseEntity<AdminModuleDto> create(@RequestBody AdminModuleDto req) {
        if (moduleRepository.existsById(req.getId())) {
            return ResponseEntity.badRequest().build();
        }
        LearningModule module = moduleFactory.buildModule(req);
        return ResponseEntity.ok(moduleAssembler.toDto(moduleRepository.save(module), lessonRepository.findByModuleIdOrderBySortOrderAsc(module.getId()).size()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminModuleDto> update(@PathVariable String id, @RequestBody AdminModuleDto req) {
        LearningModule module = moduleRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Module not found: " + id));
        module.setTitle(req.getTitle());
        module.setGlyph(req.getGlyph());
        module.setSortOrder(req.getSortOrder());
        module.setTier(LearnerPath.valueOf(req.getTier()));
        module.setDomainId(req.getDomainId());
        List<LearningModule> prereqModules = req.getPrerequisiteIds() == null ? List.of() :
                req.getPrerequisiteIds().stream()
                        .map(pId -> moduleRepository.findById(pId)
                                .orElseThrow(() -> new IllegalArgumentException("Prerequisite not found: " + pId)))
                        .toList();
        module.setPrerequisites(prereqModules);
        return ResponseEntity.ok(moduleAssembler.toDto(moduleRepository.save(module), lessonRepository.findByModuleIdOrderBySortOrderAsc(module.getId()).size()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        long lessonCount = lessonRepository.findByModuleIdOrderBySortOrderAsc(id).size();
        if (lessonCount > 0) {
            return ResponseEntity.status(409).build();
        }
        moduleRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
