package duke.state;

import duke.gameplay.*;
import duke.gfx.*;
import duke.level.Level;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    private GameplayContext context;

    private GameplayState state;

    @BeforeEach
    void setUp() {
        context = new GameplayContext(player, level);

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
    }

    @Test
    void shouldUpdateViewport() {
        state.update(gameContext);

        verify(viewport).update(player.getX(), player.getY(), player.isGrounded());
    }

    @Test
    void shouldRender() {
        state.render(gameContext);

        verify(levelRenderer).render(renderer, viewport);
        verify(hud).render(renderer, 0, 0);
        verify(spriteRenderer).render(renderer, player, player.getX(), player.getY());
    }

    @Test
    void shouldUpdateVisibleActives() {
        TestActive active = spy();
        context.spawn(active);
        context.flushSpawns();

        when(viewport.isVisible(any())).thenReturn(true);

        state.update(gameContext);

        verify(active).update(context);
    }

    @Test
    void shouldNotUpdateInvisibleActives() {
        TestActive active = spy();
        context.spawn(active);
        context.flushSpawns();

        when(viewport.isVisible(any())).thenReturn(false);

        state.update(gameContext);

        verifyNoInteractions(active);
    }

    @Test
    void shouldShoot() {
        when(player.isFiring()).thenReturn(true);

        state.update(gameContext);

        assertThat(context.getActives()).hasExactlyElementsOfTypes(Bolt.class);
    }

    @Test
    void shouldAddSpawnsToActives() {
        TestActive active = spy();
        context.spawn(active);

        state.update(gameContext);

        assertThat(context.getActives()).contains(active);
    }

    private static class TestActive extends Active implements Updatable {
        private TestActive() {
            super(0, 0, 16, 16);
        }

        @Override
        public void update(GameplayContext context) {
        }
    }
}
