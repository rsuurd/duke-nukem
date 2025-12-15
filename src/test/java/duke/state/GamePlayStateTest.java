package duke.state;

import duke.gameplay.Player;
import duke.gfx.Hud;
import duke.gfx.LevelRenderer;
import duke.gfx.Viewport;
import duke.level.Level;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GamePlayStateTest extends GameContextTestSupport {
    @Mock
    private Level level;
    @Mock
    private Viewport viewport;
    @Mock
    private LevelRenderer levelRenderer;
    @Mock
    private Hud hud;
    @Mock
    private Player player;

    @InjectMocks
    private GamePlayState state;

    @Test
    void shouldMovePlayerToStartPosition() {
        when(level.getPlayerStartX()).thenReturn(16);
        when(level.getPlayerStartY()).thenReturn(16);

        state.start(context);

        verify(player).moveTo(16, 16);
    }

    @Test
    void shouldUpdatePlayer() {
        state.update(context);

        verify(player).processInput(keyHandler);
        verify(player).update();
    }

    @Test
    void shouldUpdateViewport() {
        state.update(context);

        verify(viewport).update(player.getX(), player.getY(), player.isGrounded());
    }

    @Test
    void shouldRender() {
        state.render(context);

        verify(levelRenderer).render(renderer, viewport);
        verify(hud).render(renderer);
    }
}
