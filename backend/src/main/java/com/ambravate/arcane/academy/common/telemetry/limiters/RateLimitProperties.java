// Copyright (c) 2026 AmbraVate. All rights reserved. See LICENSE for terms.
package com.ambravate.arcane.academy.common.telemetry.limiters;

import com.ambravate.arcane.academy.common.config.BucketConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Strongly-typed view of the {@code ratelimit:} block in application.yml.
 */
@ConfigurationProperties(prefix = "ratelimit")
public class RateLimitProperties {

  private boolean enabled = true;
  private BucketConfig auth = new BucketConfig(10, 10, 60);
  private BucketConfig aiMentor = new BucketConfig(10, 10, 60);
  private BucketConfig codeRunner = new BucketConfig(30, 30, 60);
  private BucketConfig admin = new BucketConfig(30, 30, 60);

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public BucketConfig getAuth() {
    return auth;
  }

  public void setAuth(BucketConfig auth) {
    this.auth = auth;
  }

  public BucketConfig getAiMentor() {
    return aiMentor;
  }

  public void setAiMentor(BucketConfig aiMentor) {
    this.aiMentor = aiMentor;
  }

  public BucketConfig getCodeRunner() {
    return codeRunner;
  }

  public void setCodeRunner(BucketConfig codeRunner) {
    this.codeRunner = codeRunner;
  }

  public BucketConfig getAdmin() {
    return admin;
  }

  public void setAdmin(BucketConfig admin) {
    this.admin = admin;
  }

}
