package com.ambravate.arcane.academy.content.controller;

import com.ambravate.arcane.academy.common.dto.CodeSubmitRequest;
import com.ambravate.arcane.academy.common.dto.SubmitResponse;
import com.ambravate.arcane.academy.common.domain.RabbitHoleModule;
import com.ambravate.arcane.academy.common.security.UserPrincipal;
import com.ambravate.arcane.academy.practice.domain.PracticeResult;
import com.ambravate.arcane.academy.content.service.RabbitHoleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rabbit-holes")
@RequiredArgsConstructor
public class RabbitHoleController {

    private final RabbitHoleService rabbitHoleService;
    private final ObjectMapper objectMapper;

    @GetMapping("/{chunkId}")
    public ResponseEntity<List<Map<String, Object>>> getModules(@PathVariable String chunkId) {
        List<RabbitHoleModule> modules = rabbitHoleService.getModulesForChunk(chunkId);
        List<Map<String, Object>> result = modules.stream().map(m -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", m.getId());
            map.put("title", m.getTitle());
            map.put("chunkId", m.getChunkId());
            map.put("sortOrder", m.getSortOrder());
            return map;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/module/{moduleId}")
    public ResponseEntity<Map<String, Object>> getModule(@PathVariable String moduleId) {
        RabbitHoleModule m = rabbitHoleService.getModule(moduleId);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", m.getId());
        result.put("title", m.getTitle());
        result.put("chunkId", m.getChunkId());
        result.put("sortOrder", m.getSortOrder());
        result.put("contentHtml", m.getContentHtml());
        result.put("storyBeats", parseJson(m.getStoryJson()));
        result.put("starterCode", m.getStarterCode());
        result.put("filename", m.getFilename());
        result.put("testCaseLabels", extractTestLabels(m.getTestCasesJson()));
        return ResponseEntity.ok(result);
    }

    @PostMapping("/module/{moduleId}/submit")
    public ResponseEntity<SubmitResponse> submitCode(
            @PathVariable String moduleId,
            @RequestBody CodeSubmitRequest request,
            @AuthenticationPrincipal UserPrincipal user) {
        PracticeResult result = rabbitHoleService.submitCode(user.getId(), moduleId, request.getCode());

        return ResponseEntity.ok(SubmitResponse.builder()
                .allPassed(result.allPassed())
                .testResults(result.testResults().stream().map(tr ->
                        SubmitResponse.TestResult.builder()
                                .label(tr.label()).passed(tr.passed())
                                .actualOutput(tr.actualOutput()).expectedOutput(tr.expectedOutput())
                                .build()
                ).collect(Collectors.toList()))
                .xpEarned(result.xpEarned())
                .mentorFeedback(result.mentorFeedback())
                .errorType(result.errorType())
                .build());
    }

    private Object parseJson(String json) {
        if (json == null) return List.of();
        try { return objectMapper.readValue(json, Object.class); } catch (Exception e) { return List.of(); }
    }

    private Object extractTestLabels(String json) {
        if (json == null) return List.of();
        try {
            List<Map<String, Object>> tests = objectMapper.readValue(json,
                    new com.fasterxml.jackson.core.type.TypeReference<>() {});
            return tests.stream()
                    .map(tc -> Map.of("label", tc.getOrDefault("label", "")))
                    .collect(Collectors.toList());
        } catch (Exception e) { return List.of(); }
    }
}
