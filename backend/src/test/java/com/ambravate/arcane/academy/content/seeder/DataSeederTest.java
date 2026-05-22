package com.ambravate.arcane.academy.content.seeder;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DataSeederTest {

    @Test
    void applicationStartsEvenWhenASeedingStepFails() throws Exception {
        JsonContentSeeder jsonContentSeeder = mock(JsonContentSeeder.class);
        TopicSeeder topicSeeder = mock(TopicSeeder.class);
        TestUserSeeder testUserSeeder = mock(TestUserSeeder.class);

        when(jsonContentSeeder.seed()).thenReturn(0);
        doThrow(new RuntimeException("database unavailable")).when(topicSeeder).seed();

        DataSeeder dataSeeder = new DataSeeder(jsonContentSeeder, testUserSeeder, topicSeeder);

        // A failing seeding step must never propagate out of the ApplicationRunner.
        assertThatCode(() -> dataSeeder.seedData().run(null))
                .doesNotThrowAnyException();

        // A failure in one step must not stop later steps from running.
        verify(testUserSeeder).seed();
    }
}
