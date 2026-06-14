package com.ambravate.arcane.academy.content.service;

import com.ambravate.arcane.academy.ai.service.AiMentorService;
import com.ambravate.arcane.academy.practice.dto.CodeRunResponse;
import com.ambravate.arcane.academy.content.domain.RabbitHoleModule;
import com.ambravate.arcane.academy.content.repository.RabbitHoleModuleRepository;
import com.ambravate.arcane.academy.practice.domain.PracticeResult;
import com.ambravate.arcane.academy.practice.domain.TestResult;
import com.ambravate.arcane.academy.practice.runner.CodeExecutionPort;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitHoleService {

    private final RabbitHoleModuleRepository moduleRepository;
    private final CodeExecutionPort codeRunner;
    private final AiMentorService aiMentorService;
    private final ObjectMapper objectMapper;

    public List<RabbitHoleModule> getModulesForChunk(String chunkId) {
        return moduleRepository.findByModuleIdOrderBySortOrderAsc(chunkId);
    }

    public RabbitHoleModule getModule(String moduleId) {
        return moduleRepository.findById(moduleId)
                .orElseThrow(() -> new NoSuchElementException("Module not found: " + moduleId));
    }

    public PracticeResult submitCode(String userId, String moduleId, String code) {
        RabbitHoleModule module = getModule(moduleId);

        List<Map<String, Object>> testCases = parseTestCases(module.getTestCasesJson());
        if (testCases.isEmpty()) {
            return new PracticeResult(true, List.of(), 25, null, null, List.of());
        }

        // Probe first test
        CodeRunResponse probe = codeRunner.run(code,
                (String) testCases.getFirst().getOrDefault("input", null));

        if (probe.getStatus() == CodeRunResponse.RunStatus.COMPILE_ERROR) {
            String feedback = aiMentorService.explainCompileError(module.getTitle(), "Java", code, probe.getError());
            return new PracticeResult(false, List.of(), 0, feedback, "COMPILE_ERROR", List.of());
        }

        if (probe.getStatus() == CodeRunResponse.RunStatus.RUNTIME_ERROR
                || probe.getStatus() == CodeRunResponse.RunStatus.TIMEOUT) {
            String feedback = aiMentorService.explainRuntimeError(module.getTitle(), "Java", code,
                    probe.getError() != null ? probe.getError() : "Timeout");
            return new PracticeResult(false, List.of(), 0, feedback, "RUNTIME_ERROR", List.of());
        }

        List<TestResult> results = new ArrayList<>();
        boolean allPassed = true;

        for (Map<String, Object> tc : testCases) {
            String label = (String) tc.get("label");
            String input = (String) tc.getOrDefault("input", null);
            String expected = (String) tc.get("expected");
            CodeRunResponse run = codeRunner.run(code, input);
            String actual = run.getOutput() != null ? run.getOutput().trim() : "";
            boolean passed = actual.contains(expected.trim());
            results.add(new TestResult(label, passed, actual, expected));
            if (!passed) allPassed = false;
        }

        int xpEarned = allPassed ? 25 : 0;
        return new PracticeResult(allPassed, results, xpEarned, null,
                allPassed ? null : "TEST_FAILURE", List.of());
    }

    private List<Map<String, Object>> parseTestCases(String json) {
        if (json == null) return List.of();
        try { return objectMapper.readValue(json, new TypeReference<>() {}); } catch (Exception e) { return List.of(); }
    }
}
