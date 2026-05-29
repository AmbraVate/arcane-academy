package com.ambravate.arcane.academy.capstone.service;

import com.ambravate.arcane.academy.capstone.domain.UserCapstone;
import com.ambravate.arcane.academy.capstone.domain.UserCapstoneRepository;
import com.ambravate.arcane.academy.capstone.dto.AdminCapstoneDto;
import com.ambravate.arcane.academy.capstone.dto.CapstoneDto;
import com.ambravate.arcane.academy.capstone.dto.SaveCapstoneRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserCapstoneService {

    private final UserCapstoneRepository capstoneRepo;

    // â”€â”€ Learner operations â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    @Transactional(readOnly = true)
    public List<CapstoneDto> listForUser(String userId) {
        return capstoneRepo.findByUserIdOrderByCreatedAtDesc(userId)
                .stream().map(CapstoneDto::from).toList();
    }

    @Transactional
    public CapstoneDto save(String userId, SaveCapstoneRequest req) {
        UserCapstone capstone = UserCapstone.builder()
                .userId(userId)
                .moduleId(req.moduleId())
                .title(req.title())
                .description(req.description())
                .codeContent(req.codeContent())
                .githubUrl(req.githubUrl())
                .build();
        return CapstoneDto.from(capstoneRepo.save(capstone));
    }

    @Transactional
    public CapstoneDto update(String userId, String capstoneId, SaveCapstoneRequest req) {
        UserCapstone capstone = capstoneRepo.findByIdAndUserId(capstoneId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Capstone not found"));
        capstone.setTitle(req.title());
        capstone.setDescription(req.description());
        capstone.setCodeContent(req.codeContent());
        capstone.setGithubUrl(req.githubUrl());
        return CapstoneDto.from(capstoneRepo.save(capstone));
    }

    @Transactional
    public void delete(String userId, String capstoneId) {
        UserCapstone capstone = capstoneRepo.findByIdAndUserId(capstoneId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Capstone not found"));
        capstoneRepo.delete(capstone);
    }

    // â”€â”€ Admin operations â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    @Transactional(readOnly = true)
    public Page<AdminCapstoneDto> listAll(Pageable pageable) {
        return capstoneRepo.findAll(pageable).map(AdminCapstoneDto::from);
    }

    @Transactional
    public AdminCapstoneDto addFeedback(String capstoneId, String feedback) {
        UserCapstone capstone = capstoneRepo.findById(capstoneId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Capstone not found"));
        capstone.setAdminFeedback(feedback);
        capstone.setReviewedAt(Instant.now());
        return AdminCapstoneDto.from(capstoneRepo.save(capstone));
    }
}
