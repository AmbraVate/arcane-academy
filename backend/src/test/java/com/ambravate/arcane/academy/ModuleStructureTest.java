package com.ambravate.arcane.academy;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

class ModuleStructureTest {

    @Test
    void verifyModuleStructure() {
        ApplicationModules.of(ArcaneAcademyApplication.class).verify();
    }
}
