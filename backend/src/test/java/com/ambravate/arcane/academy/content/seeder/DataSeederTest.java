package com.ambravate.arcane.academy.content.seeder;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DataSeederTest {

    @Test
    void applicationStartsEvenWhenASeedingStepFails() throws Exception {
        JsonContentSeeder     jsonContentSeeder     = mock(JsonContentSeeder.class);
        MarkdownContentSeeder markdownContentSeeder = mock(MarkdownContentSeeder.class);
        DomainSeeder          domainSeeder          = mock(DomainSeeder.class);
        TrackSeeder           trackSeeder           = mock(TrackSeeder.class);
        TestUserSeeder        testUserSeeder        = mock(TestUserSeeder.class);
        AdminSeeder           adminSeeder           = mock(AdminSeeder.class);

        when(jsonContentSeeder.seed()).thenReturn(0);
        when(markdownContentSeeder.seed()).thenReturn(0);
        doThrow(new RuntimeException("database unavailable")).when(domainSeeder).seed();

        DataSeeder dataSeeder = new DataSeeder(
                jsonContentSeeder, markdownContentSeeder, Optional.of(testUserSeeder), domainSeeder, trackSeeder, adminSeeder);

        // A failing seeding step must never propagate out of the ApplicationRunner.
        assertThatCode(() -> dataSeeder.seedData().run(null))
                .doesNotThrowAnyException();

        // A failure in one step must not stop later steps from running.
        verify(testUserSeeder).seed();
    }
}
