@ApplicationModule(displayName = "Admin",
        allowedDependencies = {"common", "auth", "content", "gamification::api", "practice", "practice::repository"})
package com.ambravate.arcane.academy.admin;

import org.springframework.modulith.ApplicationModule;
