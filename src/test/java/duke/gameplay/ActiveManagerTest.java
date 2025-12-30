package duke.gameplay;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActiveManagerTest {
    @Mock
    private GameplayContext context;

    private ActiveManager manager;

    @BeforeEach
    void createManager() {
        manager = new ActiveManager();
    }

    @Test
    void shouldUpdateActives() {
        TestActive active = mock();
        manager.getActives().add(active);

        manager.update(context);

        verify(active).update(context);
    }

    @Test
    void shouldSpawnBolt() {
        Bolt bolt = mock();

        manager.spawn(bolt);

        assertThat(manager.getActives()).containsExactly(bolt);
    }

    @Test
    void shouldRemoveInactives() {
        TestActive active = mock();
        when(active.isActive()).thenReturn(false);
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
}