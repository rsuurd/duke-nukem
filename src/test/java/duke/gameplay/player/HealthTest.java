package duke.gameplay.player;

import duke.gameplay.Damaging;
import duke.gameplay.GameplayContextFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static duke.gameplay.player.Health.INVULNERABILITY_FRAMES;
import static duke.gameplay.player.Health.MAX_HEALTH;
import static org.assertj.core.api.Assertions.assertThat;

class HealthTest {
    private Health health;

    @BeforeEach
    void createHealth() {
        health = new Health();
    }

    @Test
    void shouldStartAtMax() {
        assertThat(health.getCurrent()).isEqualTo(MAX_HEALTH);
        assertThat(health.isDamageTaken()).isFalse();
        assertThat(health.isInvulnerable()).isFalse();
    }

    @Test
    void shouldTakeDamage() {
        health.takeDamage(createDamaging(1));

        assertThat(health.isDamageTaken()).isTrue();
        assertThat(health.isInvulnerable()).isTrue();
        assertThat(health.getCurrent()).isEqualTo(MAX_HEALTH - 1);
    }

    @Test
    void shouldNotTakeDamageWhenInvulnerable() {
        Health damaged = new Health(4, false, 4);

        damaged.takeDamage(createDamaging(1));

        assertThat(damaged.getCurrent()).isEqualTo(4);
    }

    @Test
    void shouldNotGoNegative() {
        health.takeDamage(createDamaging(MAX_HEALTH + 4));

        assertThat(health.isDamageTaken()).isTrue();
        assertThat(health.isInvulnerable()).isTrue();
        assertThat(health.getCurrent()).isEqualTo(0);
    }

    @Test
    void shouldIncreaseHealth() {
        Health damaged = new Health(4, false, 4);

        damaged.increaseHealth(3);

        assertThat(damaged.getCurrent()).isEqualTo(7);
    }

    @Test
    void shouldNotExceedMax() {
        Health damaged = new Health(4, false, 4);

        damaged.increaseHealth(MAX_HEALTH);

        assertThat(damaged.getCurrent()).isEqualTo(MAX_HEALTH);
    }

    @Test
    void shouldCountDownInvulnerabilityTimer() {
        Health invulnerable = new Health(9, true, INVULNERABILITY_FRAMES);

        for (int frame = INVULNERABILITY_FRAMES; frame > 0; frame--) {
            assertThat(invulnerable.isInvulnerable()).isTrue();
            assertThat(invulnerable.getInvulnerability()).isEqualTo(frame);

            invulnerable.update(GameplayContextFixture.create());
        }

        assertThat(invulnerable.isInvulnerable()).isFalse();
        assertThat(invulnerable.getInvulnerability()).isEqualTo(0);
    }

    @Test
    void shouldResetDamageTaken() {
        Health invulnerable = new Health(9, true, INVULNERABILITY_FRAMES);

        invulnerable.update(GameplayContextFixture.create());

        assertThat(invulnerable.isDamageTaken()).isFalse();
    }

    private Damaging createDamaging(int damage) {
        return new Damaging() {
            @Override
            public int getDamage() {
                return damage;
            }
        };
    }
}