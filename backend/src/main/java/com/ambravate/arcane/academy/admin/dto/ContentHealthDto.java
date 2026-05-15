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

  private String subChunkId;
  private String title;
  private String chunkTitle;
  /** Top-level subject area (e.g. "java", "sql", "tailwind"). */
  private String topicId;
  /** Tier within the topic (e.g. "FOUNDATION", "PRACTITIONER"). */
  private String tier;
  private List<String> issues;
}
