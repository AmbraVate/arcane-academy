@ApplicationModule(type = ApplicationModule.Type.OPEN, displayName = "AI",
        allowedDependencies = {"common", "content", "practice", "practice::repository", "practice::dto", "auth", "gamification::api"})
package com.ambravate.arcane.academy.ai;

import org.springframework.modulith.ApplicationModule;
