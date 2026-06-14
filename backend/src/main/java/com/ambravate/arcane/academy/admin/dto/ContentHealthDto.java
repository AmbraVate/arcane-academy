// Copyright (c) 2026 AmbraVate. All rights reserved. See LICENSE for terms.
package com.ambravate.arcane.academy.admin.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentHealthDto {

  private String lessonId;
  private String title;
  private String chunkTitle;
  /** Domain id (e.g. "software-engineering", "frontend-engineering", "data-engineering"). */
  private String domainId;
  /** Tier within the topic (e.g. "APPRENTICE", "JUNIOR", "SENIOR", "LEAD"). */
  private String tier;
  private List<String> issues;
}
