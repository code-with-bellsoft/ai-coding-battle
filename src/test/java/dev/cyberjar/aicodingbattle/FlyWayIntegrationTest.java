package dev.cyberjar.aicodingbattle;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class FlyWayIntegrationTest {

    @Autowired
    private Flyway flyway;

    @Test
    void migrationsExecuteSuccessfully() {
        assertThat(flyway).isNotNull();
        int appliedMigrations = flyway.info().applied().length;
        assertThat(appliedMigrations).isGreaterThan(0);
    }

    @Test
    void doctorTableMigrationIsApplied() {
        int appliedMigrations = flyway.info().applied().length;
        assertThat(appliedMigrations).isGreaterThanOrEqualTo(2);

        boolean hasDoctorTableMigration = java.util.Arrays.stream(flyway.info().applied())
                .anyMatch(m -> m.getScript().contains("doctor"));

        assertThat(hasDoctorTableMigration).isTrue();
    }

    @Test
    void flyWayIsHealthy() {
        assertThat(flyway.info().all()).isNotEmpty();
        boolean allMigrationsSucceeded = java.util.Arrays.stream(flyway.info().all())
                .allMatch(m -> m.getState() == MigrationState.SUCCESS);

        assertThat(allMigrationsSucceeded).isTrue();
    }
}
