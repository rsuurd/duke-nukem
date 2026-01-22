package duke.gameplay;

import duke.gameplay.active.Wakeable;
import duke.gameplay.active.enemies.EnemyFire;
import duke.gameplay.player.PlayerHealth;
import duke.gfx.Viewport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActiveManagerTest {
    @Mock
    private Viewport viewport;

    @Mock
    private Collision collision;

    @InjectMocks
    private ActiveManager manager;

    private GameplayContext context;

    @Mock
    private PlayerHealth health;

    @BeforeEach
    void createContext() {
        context = GameplayContextFixture.create();

        lenient().when(context.getPlayer().getHealth()).thenReturn(health);
    }

    @Test
    void shouldHandleInteractionWhenRequestedAndAble() {
        when(context.getPlayer().isUsing()).thenReturn(true);
        InteractableActive active = mock();
        when(active.canInteract(any())).thenReturn(true);
        manager.getActives().add(active);

        manager.update(context);

        verify(active).interactRequested(context);
    }

    @Test
    void shouldNotHandleInteractionWhenRequestedAndNotAble() {
        when(context.getPlayer().isUsing()).thenReturn(true);
        InteractableActive active = mock();
        when(active.canInteract(any())).thenReturn(false);
        manager.getActives().add(active);

        manager.update(context);

        verify(active, never()).interactRequested(context);
    }

    @Test
    void shouldNotHandleInteractionWhenNotRequested() {
        when(context.getPlayer().isUsing()).thenReturn(false);
        InteractableActive active = mock();
        manager.getActives().add(active);

        manager.update(context);

        verify(active, never()).canInteract(context.getPlayer());
        verify(active, never()).interactRequested(context);
    }

    @Test
    void shouldWakeUpSleepingActives() {
        SleepingActive active = mock();
        manager.getActives().add(active);
        when(viewport.isVisible(active)).thenReturn(true);

        manager.update(context);

        verify(active).wakeUp();
    }

    @Test
    void shouldUpdateAwakeActives() {
        SleepingActive active = mock();
        manager.getActives().add(active);
        when(active.isAwake()).thenReturn(true);

        manager.update(context);

        verify(active).update(context);
    }

    @Test
    void shouldUpdateVisibleActives() {
        TestActive active = mock();
        manager.getActives().add(active);
        when(viewport.isVisible(active)).thenReturn(true);

        manager.update(context);

        verify(active).update(context);
    }

    @Test
    void shouldNotUpdateInvisibleActives() {
        TestActive active = mock();
        manager.getActives().add(active);

        manager.update(context);

        verify(active, never()).update(context);
    }

    @Test
    void shouldNotUpdateDestroyedActives() {
        TestActive active = mock();
        manager.getActives().add(active);
        when(active.isDestroyed()).thenReturn(true);

        manager.update(context);

        verify(active, never()).update(context);
    }

    @Test
    void shouldNotUpdateInvisibleSleepingActives() {
        SleepingActive active = mock();
        manager.getActives().add(active);

        manager.update(context);

        verify(active, never()).update(context);
    }

    @Test
    void shouldResolveCollision() {
        ActiveWithPhysics active = mock();
        manager.getActives().add(active);
        when(viewport.isVisible(active)).thenReturn(true);

        manager.update(context);

        verify(collision).resolve(active, context);
    }

    @Test
    void shouldRemoveDestroyedActives() {
        TestActive active = mock();
        when(active.isDestroyed()).thenReturn(true);
        manager.getActives().add(active);

        manager.update(context);

        assertThat(manager.getActives()).isEmpty();
    }

    @Test
    void shouldAddSpawnsToActivesAfterUpdate() {
        TestActive active = mock();

        manager.spawn(active);

        assertThat(manager.getSpawns()).containsExactly(active);

        manager.update(context);

        assertThat(manager.getActives()).containsExactly(active);
        assertThat(manager.getSpawns()).isEmpty();
    }

    @Test
    void shouldNotDamagePlayerWhileInvulnerable() {
        DamagingActive active = mock();
        when(health.isInvulnerable()).thenReturn(true);
        manager.getActives().add(active);

        manager.update(context);

        verify(health, never()).takeDamage(anyInt());
    }

    @Test
    void shouldNotDamagePlayerWithZeroDamage() {
        DamagingActive active = mock();
        when(active.getDamage()).thenReturn(0);
        manager.getActives().add(active);

        manager.update(context);

        verify(health, never()).takeDamage(anyInt());
    }

    @Test
    void destroyedActiveShouldNotDamagePlayer() {
        DamagingActive active = mock();
        when(active.isDestroyed()).thenReturn(true);
        manager.getActives().add(active);

        manager.update(context);

        verify(health, never()).takeDamage(anyInt());
    }

    @Test
    void shouldDamagePlayer() {
        DamagingActive active = mock();
        when(active.getDamage()).thenReturn(1);
        when(context.getPlayer().intersects(active)).thenReturn(true);
        manager.getActives().add(active);

        manager.update(context);

        verify(health).takeDamage(1);
    }

    @Test
    void shouldRemoveEnemyFireOnHit() {
        EnemyFire enemyFire = mock();
        when(enemyFire.getDamage()).thenReturn(1);
        when(context.getPlayer().intersects(enemyFire)).thenReturn(true);
        manager.getActives().add(enemyFire);

        manager.update(context);

        verify(health).takeDamage(1);
        verify(enemyFire).destroy();
    }

    private static abstract class InteractableActive extends Active implements Interactable {
        private InteractableActive() {
            super(0, 0, 0, 0);
        }
    }

    private static abstract class TestActive extends Active implements Updatable {
        private TestActive() {
            super(0, 0, 0, 0);
        }
    }

    private static abstract class ActiveWithPhysics extends Active implements Physics {
        private ActiveWithPhysics() {
            super(0, 0, 0, 0);
        }
    }

    private static abstract class SleepingActive extends TestActive implements Wakeable {
        private SleepingActive() {
        }
    }

    private static abstract class DamagingActive extends Active implements Damaging {
        private DamagingActive() {
            super(0, 0, 1, 1);
        }
    }
}
