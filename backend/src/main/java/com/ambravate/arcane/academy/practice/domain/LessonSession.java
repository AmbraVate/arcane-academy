package com.ambravate.arcane.academy.practice.domain;

import com.ambravate.arcane.academy.common.domain.Lesson;
import com.ambravate.arcane.academy.common.domain.UserChunkProgress;

public record LessonSession(Lesson lesson, UserChunkProgress progress) {

}
