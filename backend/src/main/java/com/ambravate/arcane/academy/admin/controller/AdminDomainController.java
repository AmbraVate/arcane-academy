package com.ambravate.arcane.academy.admin.controller;

import com.ambravate.arcane.academy.common.domain.Domain;
import com.ambravate.arcane.academy.content.repository.DomainRepository;
import com.ambravate.arcane.academy.content.repository.LearningModuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/admin/domains")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminDomainController {

    private final DomainRepository domainRepository;
    private final LearningModuleRepository moduleRepository;

    @GetMapping
    public ResponseEntity<List<Domain>> list() {
        return ResponseEntity.ok(domainRepository.findAllByOrderBySortOrderAsc());
    }

    @PostMapping
    public ResponseEntity<Domain> create(@RequestBody Domain req) {
        if (domainRepository.existsById(req.getId())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(domainRepository.save(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Domain> update(@PathVariable String id, @RequestBody Domain req) {
        Domain existing = domainRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Domain not found: " + id));
        existing.setName(req.getName());
        existing.setGlyph(req.getGlyph());
        existing.setTagline(req.getTagline());
        existing.setAccentColor(req.getAccentColor());
        existing.setSortOrder(req.getSortOrder());
        existing.setActive(req.isActive());
        existing.setGuildName(req.getGuildName());
        existing.setSchoolId(req.getSchoolId());
        existing.setTierLabels(req.getTierLabels());
        return ResponseEntity.ok(domainRepository.save(existing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        long moduleCount = moduleRepository.findByTrackIdOrderBySortOrderAsc(id).size();
        if (moduleCount > 0) {
            return ResponseEntity.status(409).build();
        }
        domainRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
