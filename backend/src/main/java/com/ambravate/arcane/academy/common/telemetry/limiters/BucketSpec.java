// Copyright (c) 2026 AmbraVate. All rights reserved. See LICENSE for terms.
package com.ambravate.arcane.academy.common.telemetry.limiters;

import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.function.Function;

/**
 * Per-route descriptor: capacity, key extractor, bucket cache.
 */
public record BucketSpec(
    String bucketName,
    Map<String, Bucket> cache,
    long capacity,
    long refillTokens,
    long refillPeriodSeconds,
    Function<HttpServletRequest, String> keyFn
) {

  String identityFromRequest(HttpServletRequest req) {
    return keyFn.apply(req);
  }
}
