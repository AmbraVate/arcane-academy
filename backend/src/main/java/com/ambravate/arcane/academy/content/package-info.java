@ApplicationModule(type = ApplicationModule.Type.OPEN, displayName = "Content",
        allowedDependencies = {"common", "ai", "practice", "practice::repository", "practice::dto", "practice::domain", "practice::runner", "auth"})
package com.ambravate.arcane.academy.content;

import org.springframework.modulith.ApplicationModule;
