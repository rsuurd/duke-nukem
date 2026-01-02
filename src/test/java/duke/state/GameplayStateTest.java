package duke.state;

import duke.gameplay.*;
import duke.gfx.*;
import duke.level.Level;
import duke.sfx.SoundManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GameplayStateTest extends GameContextTestSupport {
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
    private SpriteRenderer spriteRenderer;
    @Mock
    private ActiveManager activeManager;
    @Mock
    private SoundManager soundManager;
    @InjectMocks
    private GameplayContext context;

    private GameplayState state;

    @BeforeEach
    void setUp() {
        state = new GameplayState(viewport, levelRenderer, hud, font, spriteRenderer, collision, context);
    }

    @Test
    void shouldMovePlayerToStartPosition() {
        when(level.getPlayerStartX()).thenReturn(16);
        when(level.getPlayerStartY()).thenReturn(16);

        state.start(gameContext);

        verify(player).setX(16);
        verify(player).setY(16);
    }

    @Test
    void shouldUpdatePlayer() {
        state.update(gameContext);

        verify(player).processInput(keyHandler.getInput());
        verify(player).update(context);
        verify(collision).resolve(player, level);
        verify(player).postMovement(context);
    }

    @Test
    void shouldUpdateViewport() {
        state.update(gameContext);

        verify(viewport).update(player.getX(), player.getY(), player.isGrounded());
    }

    @Test
    void shouldUpdateActives() {
        state.update(gameContext);

        verify(context.getActiveManager()).update(context);
    }

    @Test
    void shouldRender() {
        state.render(gameContext);

        verify(levelRenderer).render(renderer, viewport);
        verify(activeManager).render(renderer, spriteRenderer, viewport, Layer.BACKGROUND);
        verify(spriteRenderer).render(renderer, player, player.getX(), player.getY());
        verify(activeManager).render(renderer, spriteRenderer, viewport, Layer.FOREGROUND);
        verify(hud).render(renderer, 0, 0);
    }
}
