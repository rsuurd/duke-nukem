package duke.state;

import duke.GameContext;
import duke.GameContextFixture;
import duke.gameplay.*;
import duke.gameplay.player.Inventory;
import duke.gfx.*;
import duke.level.Level;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameplayStateTest {
    @Mock
    private Viewport viewport;
    @Mock
    private LevelRenderer levelRenderer;
    @Mock
    private Hud hud;
    @Mock
    private Font font;
    @Mock
    private SpriteRenderer spriteRenderer;
    @Mock
    private Collision collision;

    private GameContext gameContext;
    private GameplayContext gameplayContext;

    private GameplayState state;

    private Level level;
    private Player player;

    @BeforeEach
    void setUp() {
        gameContext = GameContextFixture.create();
        gameplayContext = GameplayContextFixture.create();

        state = new GameplayState(viewport, levelRenderer, hud, font, spriteRenderer, collision, gameplayContext);

        player = gameplayContext.getPlayer();
        level = gameplayContext.getLevel();
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

        verify(player).processInput(gameContext.getKeyHandler().getInput());
        verify(player).update(gameplayContext);
        verify(collision).resolve(player, gameplayContext);
        verify(player).postMovement(gameplayContext);
    }

    @Test
    void shouldUpdateViewport() {
        state.update(gameContext);

        verify(viewport).update(player.getX(), player.getY(), player.isGrounded());
    }

    @Test
    void shouldUpdateActives() {
        state.update(gameContext);

        verify(gameplayContext.getActiveManager()).update(gameplayContext);
    }

    @Test
    void shouldRender() {
        Inventory inventory = mock();
        when(player.getHealth()).thenReturn(5);
        when(player.getInventory()).thenReturn(inventory);
        when(gameplayContext.getScoreManager().getScore()).thenReturn(2430);

        state.render(gameContext);

        verify(levelRenderer).render(gameContext.getRenderer(), viewport);
        verify(gameplayContext.getActiveManager()).render(gameContext.getRenderer(), Layer.BACKGROUND);
        verify(spriteRenderer).render(gameContext.getRenderer(), player, player.getX(), player.getY());
        verify(gameplayContext.getActiveManager()).render(gameContext.getRenderer(), Layer.FOREGROUND);
        verify(hud).render(gameContext.getRenderer(), 2430, 5, inventory);
    }
}
