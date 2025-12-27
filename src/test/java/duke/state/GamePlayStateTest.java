package duke.state;

import duke.gameplay.Collision;
import duke.gameplay.Player;
import duke.gfx.*;
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
    private Font font;
    @Mock
    private Player player;
    @Mock
    private Collision collision;
    @Mock
    private AnimationRenderer animationRenderer;

    @InjectMocks
    private GamePlayState state;

    @Test
    void shouldMovePlayerToStartPosition() {
        when(level.getPlayerStartX()).thenReturn(16);
        when(level.getPlayerStartY()).thenReturn(16);

        state.start(context);

        verify(player).setX(16);
        verify(player).setY(16);
    }

    @Test
    void shouldUpdatePlayer() {
        state.update(context);

        verify(player).processInput(keyHandler.getInput());
        verify(player).update();
        verify(collision).resolve(player, level);
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
        verify(hud).render(renderer, 0, 0);
        verify(animationRenderer).render(renderer, player, player.getX(), player.getY());
    }
}
