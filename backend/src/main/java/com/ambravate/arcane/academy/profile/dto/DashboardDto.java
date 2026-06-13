package com.ambravate.arcane.academy.profile.dto;

import lombok.*;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(
    setterPrefix = "with",
    builderMethodName = "aDashboardDto"
)
public class DashboardDto {
    private int totalXp;
    private String rank;
    private int streakDays;
    private boolean streakAtRisk;
    private String currentPath;
    private boolean diagnosticCompleted;
    private Instant diagnosticCompletedAt;
    private int reviewsDue;
    private int dailyGoalMinutes;
    private double overallProgress;
    private List<ModuleHealthDto> moduleHealth;
    /** Domain-specific tier display labels, e.g. {"APPRENTICE":"Intern","LEAD":"Tech Lead"}. Null = use defaults. */
    private Map<String, String> tierLabels;
}
