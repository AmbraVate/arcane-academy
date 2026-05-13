// Copyright (c) 2026 AmbraVate. All rights reserved. See LICENSE for terms.
package com.ambravate.arcane.academy.practice.domain;

import com.ambravate.arcane.academy.common.domain.SubChunk;
import com.ambravate.arcane.academy.common.domain.UserChunkProgress;

// Inner records for return types
public record SubChunkSession(SubChunk subChunk, UserChunkProgress progress) {

}
