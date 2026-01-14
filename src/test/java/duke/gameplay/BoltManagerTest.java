package duke.gameplay;

import duke.Renderer;
import duke.gfx.SpriteRenderer;
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
class BoltManagerTest {
    @Mock
    private Viewport viewport;

    @Mock
    private SpriteRenderer spriteRenderer;

    @InjectMocks
    private BoltManager manager;

    private GameplayContext context;

    @BeforeEach
    void createContext() {
        context = GameplayContextFixture.create();
    }

    @Test
    void shouldSpawnBolt() {
        manager.spawn(mock());

        assertThat(manager.countBolts()).isEqualTo(1);
    }

    @Test
    void shouldUpdateBolts() {
        Bolt bolt = mock();

        manager.spawn(bolt);
        manager.update(context);

        verify(bolt).update(context);
    }

    @Test
    void shouldRemoveDestroyedBolts() {
        Bolt bolt = mock();

        manager.spawn(bolt);
        when(bolt.isDestroyed()).thenReturn(true);

        manager.update(context);

        verify(bolt).update(context);
    }

    @Test
    void shouldRenderBolts() {
        Bolt bolt = mock();
        Renderer renderer = mock();
        when(viewport.isVisible(bolt)).thenReturn(true);

        manager.spawn(bolt);
        manager.render(renderer);

        verify(spriteRenderer).render(same(renderer), same(bolt), anyInt(), anyInt());
        verify(viewport).toScreenX(bolt.getX());
        verify(viewport).toScreenY(bolt.getY());
    }

    @Test
    void shouldNotRenderInvisbleBolts() {
        Bolt bolt = mock();
        Renderer renderer = mock();
        when(viewport.isVisible(bolt)).thenReturn(false);

        manager.spawn(bolt);
        manager.render(renderer);

        verifyNoInteractions(spriteRenderer);
    }
}