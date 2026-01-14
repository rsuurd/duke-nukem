package duke.gameplay.player;

import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.sfx.Sfx;
import org.junit.jupiter.api.Test;

import static duke.gameplay.player.Weapon.MAX_FIREPOWER;
import static duke.gameplay.player.Weapon.STARTING_FIREPOWER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class WeaponTest {
    @Test
    void shouldStartWithDefaultFirepower() {
        Weapon weapon = new Weapon();

        assertThat(weapon.getFirepower()).isEqualTo(STARTING_FIREPOWER);
        assertThat(weapon.isTriggered()).isFalse();
        assertThat(weapon.isReady()).isTrue();
    }

    @Test
    void shouldTrigger() {
        Weapon weapon = new Weapon();

        assertThat(weapon.isTriggered()).isFalse();

        weapon.setTriggered(true);

        assertThat(weapon.isTriggered()).isTrue();
    }

    @Test
    void shouldFireWhenTriggeredAndReady() {
        GameplayContext context = GameplayContextFixture.create();
        Weapon weapon = new Weapon(STARTING_FIREPOWER, true, true);

        weapon.fire(context);

        assertThat(weapon.isReady()).isFalse();
        verify(context.getBoltManager()).spawn(any());
        verify(context.getSoundManager()).play(Sfx.PLAYER_GUN);
    }

    @Test
    void shouldNotFireWhenNotReady() {
        GameplayContext context = GameplayContextFixture.create();
        Weapon weapon = new Weapon(STARTING_FIREPOWER, true, false);

        weapon.fire(context);

        verifyNoInteractions(context.getBoltManager());
    }

    @Test
    void shouldNotFireWhenFirepowerLimitReached() {
        GameplayContext context = GameplayContextFixture.create();
        when(context.getBoltManager().countBolts()).thenReturn(1);
        Weapon weapon = new Weapon(STARTING_FIREPOWER, true, true);

        weapon.fire(context);

        assertThat(weapon.isTriggered()).isTrue();
        assertThat(weapon.isReady()).isFalse();
        verify(context.getBoltManager(), never()).spawn(any());
    }

    @Test
    void shouldReadyOnTriggerRelease() {
        GameplayContext context = GameplayContextFixture.create();
        Weapon weapon = new Weapon(STARTING_FIREPOWER, false, false);

        weapon.fire(context);

        assertThat(weapon.isReady()).isTrue();
    }

    @Test
    void shouldIncreaseFirepower() {
        Weapon weapon = new Weapon();

        for (int firepower = STARTING_FIREPOWER; firepower <= MAX_FIREPOWER; firepower++) {
            assertThat(weapon.getFirepower()).isEqualTo(firepower);
            weapon.increaseFirepower();
        }
    }

    @Test
    void shouldNotExceedMaxFirepower() {
        Weapon weapon = new Weapon(MAX_FIREPOWER, false, true);

        weapon.increaseFirepower();

        assertThat(weapon.getFirepower()).isEqualTo(MAX_FIREPOWER);
    }
}
