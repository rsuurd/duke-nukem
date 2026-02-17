package duke.state;

import duke.GameContext;
import duke.GameContextFixture;
import duke.gameplay.*;
import duke.gameplay.player.Player;
import duke.gfx.Hud;
import duke.gfx.LevelRenderer;
import duke.gfx.SpriteRenderer;
import duke.gfx.Viewport;
import duke.level.Level;
import duke.level.LevelDescriptor;
import duke.level.LevelManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameplayStateTest {
    @Mock
    private LevelManager levelManager;
    @Mock
    private LevelRenderer levelRenderer;
    @Mock
    private Viewport viewport;
    @Mock
    private Hud hud;
    @Mock
    private SpriteRenderer spriteRenderer;
    @Mock
    private Collision collision;

    private GameContext gameContext;
    private GameplayContext gameplayContext;

    @Mock
    private Cheats cheats;

    private GameplayState state;

    @BeforeEach
    void setUp() {
        gameContext = GameContextFixture.create();
        gameplayContext = spy(GameplayContextFixture.create());

        state = new GameplayState(levelManager, levelRenderer, viewport, hud, spriteRenderer, collision, gameplayContext, cheats);
    }

    @Test
    void shouldSwitchLevelOnStart() {
        Player player = gameplayContext.getPlayer();

        Level level = mock();
        when(levelManager.getNextLevel()).thenReturn(level);
        when(level.getDescriptor()).thenReturn(new LevelDescriptor(1, 0));
        when(player.getHealth()).thenReturn(mock());
        when(gameplayContext.getViewportManager().getTarget()).thenReturn(player);

        state.start(gameContext);

        verify(gameplayContext).switchLevel(level);
        verify(levelRenderer).setLevel(level);
        verify(viewport).center(player);
        verify(player.getHealth()).grantInvulnerability();
    }

    @Test
    void shouldSwitchLevelOnComplete() {
        Player player = gameplayContext.getPlayer();

        Level next = mock();
        when(next.getDescriptor()).thenReturn(new LevelDescriptor(2, 0));
        when(gameplayContext.getLevel().isCompleted()).thenReturn(true);
        when(levelManager.getNextLevel()).thenReturn(next);
        when(player.getHealth()).thenReturn(mock());
        when(gameplayContext.getViewportManager().getTarget()).thenReturn(player);

        state.update(gameContext);

        verify(gameplayContext).switchLevel(next);
        verify(levelRenderer).setLevel(next);
        verify(viewport).center(player);
        verify(player.getHealth()).grantInvulnerability();
    }

    @Test
    void shouldUpdatePlayer() {
        state.update(gameContext);

        Player player = gameplayContext.getPlayer();
        verify(player).processInput(gameContext.getKeyHandler().getInput());
        verify(player).update(gameplayContext);
        verify(collision).resolve(player, gameplayContext);
        verify(player).postMovement(gameplayContext);
    }

    @Test
    void shouldUpdateViewport() {
        Player player = gameplayContext.getPlayer();
        when(gameplayContext.getViewportManager().getTarget()).thenReturn(player);

        state.update(gameContext);

        verify(viewport).update(player);
    }

    @Test
    void shouldSnapViewportToCenter() {
        Player player = gameplayContext.getPlayer();
        when(gameplayContext.getViewportManager().pollSnapToCenter()).thenReturn(true);
        when(gameplayContext.getViewportManager().getTarget()).thenReturn(player);

        state.update(gameContext);

        verify(viewport).center(player);
    }

    @Test
    void shouldUpdateActives() {
        state.update(gameContext);

        verify(gameplayContext.getActiveManager()).update(gameplayContext);
    }

    @Test
    void shouldRender() {
        when(gameplayContext.getScoreManager().getScore()).thenReturn(2430);

        state.render(gameContext);

        Player player = gameplayContext.getPlayer();
        verify(levelRenderer).render(gameContext.getRenderer(), viewport);
        verify(gameplayContext.getActiveManager()).render(gameContext.getRenderer(), Layer.BACKGROUND);
        verify(spriteRenderer).render(gameContext.getRenderer(), player, player.getX(), player.getY());
        verify(gameplayContext.getActiveManager()).render(gameContext.getRenderer(), Layer.FOREGROUND);
        verify(gameplayContext.getActiveManager()).render(gameContext.getRenderer(), Layer.POST_PROCESS);
        verify(hud).render(same(gameContext.getRenderer()), eq(2430), same(player), anyString());
    }

    @Test
    void shouldPauseWhenDialogIsOpen() {
        when(gameContext.getDialogManager().hasDialog()).thenReturn(true);

        state.update(gameContext);

        verify(gameContext.getDialogManager()).update(gameContext);

        verifyNoInteractions(gameplayContext.getPlayer(), gameplayContext.getActiveManager(), gameplayContext.getBoltManager(), collision, viewport);
    }

    @Test
    void shouldCheckForCheats() {
        state.update(gameContext);

        verify(cheats).processInput(gameContext.getKeyHandler(), gameplayContext);
    }
}
