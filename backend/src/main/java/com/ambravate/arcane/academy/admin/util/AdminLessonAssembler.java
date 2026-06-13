package com.ambravate.arcane.academy.admin.util;

import com.ambravate.arcane.academy.admin.dto.AdminLessonDto;
import com.ambravate.arcane.academy.common.domain.Lesson;
import com.ambravate.arcane.academy.common.domain.LessonPracticeType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import org.springframework.stereotype.Component;

@Component
public class AdminLessonAssembler {

  private final ObjectMapper objectMapper;

  public AdminLessonAssembler(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public AdminLessonDto toDto(Lesson l, long qCount) {
    return AdminLessonDto.builder()
        .id(l.getId()).moduleId(l.getModuleId()).title(l.getTitle())
        .sortOrder(l.getSortOrder()).xpReward(l.getXpReward())
        .practiceType(l.getPracticeType() != null ? l.getPracticeType().name() : "JAVA")
        .filename(l.getFilename())
        .hookHtml(l.getHookHtml()).explanationHtml(l.getExplanationHtml())
        .storyBeats(parseJsonList(l.getStoryJson()))
        .guidedPracticeHtml(l.getGuidedPracticeHtml())
        .guidedPracticeStarterCode(l.getGuidedPracticeStarterCode())
        .guidedPracticeTests(parseJsonList(l.getGuidedPracticeTestsJson()))
        .guidedPracticeModelAnswer(l.getGuidedPracticeModelAnswer())
        .soloPracticeHtml(l.getSoloPracticeHtml())
        .modelAnswer(l.getModelAnswer())
        .feynmanPrompt(l.getFeynmanPrompt())
        .teachBackExpectedOutput(l.getTeachBackExpectedOutput())
        .rabbitHoleTerms(parseJsonList(l.getRabbitHoleTermsJson()))
        .questionCount(qCount)
        .learningObjectives(parseStringList(l.getLearningObjectivesJson()))
        .challenge(parseJsonMap(l.getChallengeHtml(), l.getChallengeStarterCode(), l.getChallengeTestsJson()))
        .miniProject(l.getMiniProjectHtml())
        .commonMistakes(parseStringList(l.getCommonMistakesJson()))
        .assessmentCriteria(parseStringList(l.getAssessmentCriteriaJson()))
        .downloadables(parseJsonList(l.getDownloadablesJson()))
        .build();
  }

  public Lesson fromDto(AdminLessonDto dto) {
    LessonPracticeType pt = LessonPracticeType.JAVA;
    if (dto.getPracticeType() != null) {
      try { pt = LessonPracticeType.valueOf(dto.getPracticeType()); } catch (Exception ignored) {}
    }
    return Lesson.builder()
        .id(dto.getId()).moduleId(dto.getModuleId()).title(dto.getTitle())
        .sortOrder(dto.getSortOrder())
        .xpReward(dto.getXpReward() > 0 ? dto.getXpReward() : 50)
        .practiceType(pt).filename(dto.getFilename())
        .hookHtml(dto.getHookHtml()).explanationHtml(dto.getExplanationHtml())
        .storyJson(toJson(dto.getStoryBeats()))
        .guidedPracticeHtml(dto.getGuidedPracticeHtml())
        .guidedPracticeStarterCode(dto.getGuidedPracticeStarterCode())
        .guidedPracticeTestsJson(toJson(dto.getGuidedPracticeTests()))
        .guidedPracticeModelAnswer(dto.getGuidedPracticeModelAnswer())
        .soloPracticeHtml(dto.getSoloPracticeHtml())
        .modelAnswer(dto.getModelAnswer())
        .feynmanPrompt(dto.getFeynmanPrompt())
        .teachBackExpectedOutput(dto.getTeachBackExpectedOutput())
        .rabbitHoleTermsJson(toJson(dto.getRabbitHoleTerms()))
        .build();
  }

  private List<Map<String, Object>> parseJsonList(String json) {
    if (json == null || json.isBlank()) return List.of();
    try { return objectMapper.readValue(json, new TypeReference<>() {}); } catch (Exception e) { return List.of(); }
  }

  private List<String> parseStringList(String json) {
    if (json == null || json.isBlank()) return Collections.emptyList();
    try { return objectMapper.readValue(json, new TypeReference<>() {}); } catch (Exception e) { return Collections.emptyList(); }
  }

  private Map<String, Object> parseJsonMap(String challengeHtml, String starterCode, String testsJson) {
    if (challengeHtml == null || challengeHtml.isBlank()) return null;
    List<Map<String, Object>> tests = List.of();
    if (testsJson != null && !testsJson.isBlank()) {
      try { tests = objectMapper.readValue(testsJson, new TypeReference<>() {}); } catch (Exception ignored) {}
    }
    return Map.of("html", challengeHtml, "starterCode", starterCode != null ? starterCode : "", "tests", tests);
  }

  private String toJson(List<?> list) {
    if (list == null || list.isEmpty()) return null;
    try { return objectMapper.writeValueAsString(list); } catch (Exception e) { return null; }
  }

}
