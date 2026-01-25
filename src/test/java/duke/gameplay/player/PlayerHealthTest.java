package duke.gameplay.player;

import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import org.junit.jupiter.api.Test;

import static duke.gameplay.player.PlayerHealth.INVULNERABILITY_FRAMES;
import static duke.gameplay.player.PlayerHealth.MAX_HEALTH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

class PlayerHealthTest {
    @Test
    void shouldStartAtMax() {
        PlayerHealth health = new PlayerHealth();

        assertThat(health.getCurrent()).isEqualTo(MAX_HEALTH);
        assertThat(health.isDamageTaken()).isFalse();
        assertThat(health.isInvulnerable()).isFalse();
    }

    @Test
    void shouldTakeDamage() {
        PlayerHealth health = new PlayerHealth();

        health.takeDamage(1);

        assertThat(health.isDamageTaken()).isTrue();
        assertThat(health.isInvulnerable()).isTrue();
        assertThat(health.getCurrent()).isEqualTo(MAX_HEALTH - 1);
    }

    @Test
    void shouldNotTakeDamageWhenInvulnerable() {
        PlayerHealth damaged = new PlayerHealth(4, false, 4);

        damaged.takeDamage(1);

        assertThat(damaged.getCurrent()).isEqualTo(4);
    }

    @Test
    void shouldCountDownInvulnerabilityTimer() {
        PlayerHealth invulnerable = new PlayerHealth(9, true, INVULNERABILITY_FRAMES);

        for (int frame = INVULNERABILITY_FRAMES; frame > 0; frame--) {
            assertThat(invulnerable.isInvulnerable()).isTrue();
            assertThat(invulnerable.getInvulnerability()).isEqualTo(frame);

            invulnerable.update(GameplayContextFixture.create());
        }

        assertThat(invulnerable.isInvulnerable()).isFalse();
        assertThat(invulnerable.getInvulnerability()).isEqualTo(0);
    }

    @Test
    void shouldHandleDamageTaken() {
        PlayerHealth invulnerable = new PlayerHealth(9, true, INVULNERABILITY_FRAMES);
        GameplayContext context = GameplayContextFixture.create();
        invulnerable.update(context);

        assertThat(invulnerable.isDamageTaken()).isFalse();
        verify(context.getBonusTracker()).damageTaken();
    }

    @Test
    void shouldGrantInvulnerability() {
        PlayerHealth health = new PlayerHealth();

        health.grantInvulnerability();

        assertThat(health.isInvulnerable()).isTrue();
        assertThat(health.getInvulnerability()).isEqualTo(INVULNERABILITY_FRAMES);
    }
}