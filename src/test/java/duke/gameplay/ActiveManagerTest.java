package duke.gameplay;

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

    @BeforeEach
    void createContext() {
        context = GameplayContextFixture.create();
    }

    @Test
    void shouldHandleInteractionWhenRequestedAndAble() {
        when(context.getPlayer().isUsing()).thenReturn(true);
        InteractableActive active = mock();
        when(active.isActivated()).thenReturn(true);
        when(active.canInteract(any())).thenReturn(true);
        manager.getActives().add(active);

        manager.update(context);

        verify(active).interactRequested(context);
    }

    @Test
    void shouldNotHandleInteractionWhenRequestedAndNotAble() {
        when(context.getPlayer().isUsing()).thenReturn(true);
        InteractableActive active = mock();
        when(active.isActivated()).thenReturn(true);
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
    void shouldActivateVisibleActives() {
        TestActive active = mock();
        manager.getActives().add(active);
        when(viewport.isVisible(any())).thenReturn(true);

        manager.update(context);

        verify(active).activate();
    }

    @Test
    void shouldUpdateActivatedActives() {
        TestActive active = mock();
        when(active.isActivated()).thenReturn(true);
        manager.getActives().add(active);

        manager.update(context);

        verify(active).update(context);
    }

    @Test
    void shouldNotUpdateInactiveActives() {
        TestActive active = mock();
        manager.getActives().add(active);

        manager.update(context);

        verify(active, never()).update(context);
    }

    @Test
    void shouldNotUpdateDestroyedActives() {
        TestActive active = mock();
        manager.getActives().add(active);
        when(active.isActivated()).thenReturn(true);
        when(active.isDestroyed()).thenReturn(true);

        manager.update(context);

        verify(active, never()).update(context);
    }

    @Test
    void shouldResolveCollision() {
        ActiveWithPhysics active = mock();
        when(active.isActivated()).thenReturn(true);
        manager.getActives().add(active);

        manager.update(context);

        verify(collision).resolve(active, context);
    }

    @Test
    void shouldNotResolveCollisionForInactiveActives() {
        ActiveWithPhysics active = mock();
        when(active.isActivated()).thenReturn(false);
        manager.getActives().add(active);

        manager.update(context);

        verify(collision, never()).resolve(active, context);
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
}