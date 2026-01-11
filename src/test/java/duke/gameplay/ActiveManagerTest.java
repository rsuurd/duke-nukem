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
    void shouldActivateVisibleActives() {
        TestActive active = spy(new TestActive());
        manager.getActives().add(active);
        when(viewport.isVisible(any())).thenReturn(true);

        manager.update(context);

        verify(active).activate();
        verify(active).update(context);
    }

    @Test
    void shouldUpdateActivatedActives() {
        TestActive active = spy(new TestActive());
        when(active.isActivated()).thenReturn(true);
        manager.getActives().add(active);

        manager.update(context);

        verify(active).update(context);
    }

    @Test
    void shouldNotUpdateInactiveActives() {
        TestActive active = spy(new TestActive());
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
        ActiveWithPhysics active = spy(new ActiveWithPhysics());
        manager.getActives().add(active);
        when(viewport.isVisible(any())).thenReturn(true);

        manager.update(context);

        verify(collision).resolve(active, context.getLevel(), manager.getActives());
    }

    @Test
    void shouldNotResolveDistantCollision() {
        ActiveWithPhysics active = spy(new ActiveWithPhysics());
        manager.getActives().add(active);
        when(viewport.isVisible(any())).thenReturn(false);

        manager.update(context);

        verify(collision, never()).resolve(active, context.getLevel(), manager.getActives());
    }

    @Test
    void shouldRemoveDestroyedActives() {
        TestActive active = spy(new TestActive());
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

    private static class TestActive extends Active implements Updatable {
        private TestActive() {
            super(0, 0, 0, 0);
        }

        @Override
        public void update(GameplayContext context) {
        }
    }

    private static class ActiveWithPhysics extends Active implements Physics {
        private ActiveWithPhysics() {
            super(0, 0, 0, 0);
        }

        @Override
        public int getVerticalAcceleration() {
            return 0;
        }
    }
}