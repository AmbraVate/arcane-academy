package com.ambravate.polymath.academy.controller.admin;

import com.ambravate.polymath.academy.dto.admin.AdminUserDto;
import com.ambravate.polymath.academy.model.SubChunkStatus;
import com.ambravate.polymath.academy.model.User;
import com.ambravate.polymath.academy.repository.*;
import com.ambravate.polymath.academy.service.AdminStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserRepository userRepository;
    private final UserChunkProgressRepository progressRepository;
    private final AdminStatsService statsService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false)    String search) {

        PageRequest pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<User> users = (search == null || search.isBlank())
                ? userRepository.findAll(pageable)
                : userRepository.findBySearchTerm(search, pageable);

        return ResponseEntity.ok(Map.of(
                "content", users.getContent().stream().map(u -> {
                    long completed = progressRepository.countByUserIdAndStatus(u.getId(), SubChunkStatus.COMPLETE);
                    return statsService.toUserDto(u, completed);
                }).toList(),
                "totalElements", users.getTotalElements(),
                "totalPages", users.getTotalPages(),
                "page", page
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminUserDto> get(@PathVariable String id) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + id));
        long completed = progressRepository.countByUserIdAndStatus(id, SubChunkStatus.COMPLETE);
        return ResponseEntity.ok(statsService.toUserDto(u, completed));
    }

    @DeleteMapping("/{id}/progress")
    public ResponseEntity<Void> resetProgress(@PathVariable String id) {
        if (!userRepository.existsById(id)) {
            throw new NoSuchElementException("User not found: " + id);
        }
        var progress = progressRepository.findByUserId(id);
        progressRepository.deleteAll(progress);
        return ResponseEntity.noContent().build();
    }
}
