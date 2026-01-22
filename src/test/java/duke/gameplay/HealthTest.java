package duke.gameplay;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HealthTest {
    @Test
    void shouldStartAtMax() {
        assertThat(new Health(3).getCurrent()).isEqualTo(3);
    }

    @Test
    void shouldTakeDamage() {
        Health health = new Health(3);
        health.takeDamage(1);

        assertThat(health.getCurrent()).isEqualTo(2);
    }

    @Test
    void shouldNotGoNegative() {
        Health health = new Health(1);

        health.takeDamage(2);

        assertThat(health.getCurrent()).isEqualTo(0);
    }

    @Test
    void shouldIncreaseHealth() {
        Health damaged = new Health(1, 4);

        damaged.increaseHealth(2);

        assertThat(damaged.getCurrent()).isEqualTo(3);
    }

    @Test
    void shouldNotExceedMax() {
        Health damaged = new Health(1, 4);

        damaged.increaseHealth(10);

        assertThat(damaged.getCurrent()).isEqualTo(4);
    }

    @Test
    void shouldIndicateDeath() {
        Health health = new Health(2);

        assertThat(health.isDead()).isFalse();

        health.takeDamage(2);

        assertThat(health.isDead()).isTrue();
    }
}