// Copyright (c) 2026 AmbraVate. All rights reserved. See LICENSE for terms.
package com.ambravate.arcane.academy.common.config;

public class BucketConfig {

  private long capacity;
  private long refillTokens;
  private long refillPeriodSeconds;

  public BucketConfig() {
  }

  public BucketConfig(long capacity, long refillTokens, long refillPeriodSeconds) {
    this.capacity = capacity;
    this.refillTokens = refillTokens;
    this.refillPeriodSeconds = refillPeriodSeconds;
  }

  public long capacity() {
    return capacity;
  }

  public long refillTokens() {
    return refillTokens;
  }

  public long refillPeriodSeconds() {
    return refillPeriodSeconds;
  }

  public void setCapacity(long capacity) {
    this.capacity = capacity;
  }

  public void setRefillTokens(long refillTokens) {
    this.refillTokens = refillTokens;
  }

  public void setRefillPeriodSeconds(long refillPeriodSeconds) {
    this.refillPeriodSeconds = refillPeriodSeconds;
  }
}
