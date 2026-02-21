package duke.state;

import duke.GameSystems;
import duke.GameSystemsFixture;
import duke.dialog.Dialog;
import duke.gameplay.*;
import duke.gameplay.player.Player;
import duke.gfx.Hud;
import duke.gfx.LevelRenderer;
import duke.gfx.SpriteRenderer;
import duke.gfx.Viewport;
import duke.level.Level;
import duke.level.LevelDescriptor;
import duke.level.LevelManager;
import duke.menu.HelpMenu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.awt.event.KeyEvent.*;
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

    private GameSystems systems;
    private GameplayContext context;

    @Mock
    private Cheats cheats;

    private GameplayState state;

    @BeforeEach
    void setUp() {
        systems = GameSystemsFixture.create();
        context = spy(GameplayContextFixture.create());

        state = new GameplayState(levelManager, levelRenderer, viewport, hud, spriteRenderer, collision, context, cheats);
    }

    @Test
    void shouldSwitchLevelOnStart() {
        Player player = context.getPlayer();

        Level level = mock();
        when(levelManager.getNextLevel()).thenReturn(level);
        when(level.getDescriptor()).thenReturn(new LevelDescriptor(1, 0));
        when(player.getHealth()).thenReturn(mock());
        when(context.getViewportManager().getTarget()).thenReturn(player);

        state.start(systems);

        verify(context).switchLevel(level);
        verify(levelRenderer).setLevel(level);
        verify(viewport).center(player);
        verify(player.getHealth()).grantInvulnerability();
    }

    @Test
    void shouldSwitchLevelOnComplete() {
        Player player = context.getPlayer();

        Level next = mock();
        when(next.getDescriptor()).thenReturn(new LevelDescriptor(2, 0));
        when(context.getLevel().isCompleted()).thenReturn(true);
        when(levelManager.getNextLevel()).thenReturn(next);
        when(player.getHealth()).thenReturn(mock());
        when(context.getViewportManager().getTarget()).thenReturn(player);

        state.update(systems);

        verify(context).switchLevel(next);
        verify(levelRenderer).setLevel(next);
        verify(viewport).center(player);
        verify(player.getHealth()).grantInvulnerability();
    }

    @Test
    void shouldTransitionToEndWhenLastLevelComplete() {
        when(context.getLevel().isCompleted()).thenReturn(true);
        when(levelManager.isLast()).thenReturn(true);

        state.update(systems);

        verify(systems.getStateRequester()).requestState(isA(End.class));
    }

    @Test
    void shouldNotTransitionToEndWhenAlreadyTransitioning() {
        when(context.getLevel().isCompleted()).thenReturn(true);
        when(levelManager.isLast()).thenReturn(true);
        when(systems.getStateRequester().isTransitioning()).thenReturn(true);

        state.update(systems);

        verify(systems.getStateRequester(), never()).requestState(isA(End.class));
    }

    @Test
    void shouldUpdatePlayer() {
        state.update(systems);

        Player player = context.getPlayer();
        verify(player).processInput(systems.getKeyHandler().getInput());
        verify(player).update(context);
        verify(collision).resolve(player, context);
        verify(player).postMovement(context);
    }

    @Test
    void shouldUpdateViewport() {
        Player player = context.getPlayer();
        when(context.getViewportManager().getTarget()).thenReturn(player);

        state.update(systems);

        verify(viewport).update(player);
    }

    @Test
    void shouldSnapViewportToCenter() {
        Player player = context.getPlayer();
        when(context.getViewportManager().pollSnapToCenter()).thenReturn(true);
        when(context.getViewportManager().getTarget()).thenReturn(player);

        state.update(systems);

        verify(viewport).center(player);
    }

    @Test
    void shouldUpdateActives() {
        state.update(systems);

        verify(context.getActiveManager()).update(context);
    }

    @Test
    void shouldRender() {
        when(context.getScoreManager().getScore()).thenReturn(2430);

        state.render(systems);

        Player player = context.getPlayer();
        verify(levelRenderer).render(systems.getRenderer(), viewport);
        verify(context.getActiveManager()).render(systems.getRenderer(), Layer.BACKGROUND);
        verify(spriteRenderer).render(systems.getRenderer(), player, player.getX(), player.getY());
        verify(context.getActiveManager()).render(systems.getRenderer(), Layer.FOREGROUND);
        verify(context.getActiveManager()).render(systems.getRenderer(), Layer.POST_PROCESS);
        verify(hud).render(same(systems.getRenderer()), eq(2430), same(player), anyString());
    }

    @Test
    void shouldPauseWhenDialogIsOpen() {
        when(systems.getDialogManager().hasDialog()).thenReturn(true);

        state.update(systems);

        verify(systems.getDialogManager()).update(systems);

        verifyNoInteractions(context.getPlayer(), context.getActiveManager(), context.getBoltManager(), collision, viewport);
    }

    @Test
    void shouldCheckForCheats() {
        state.update(systems);

        verify(cheats).processInput(systems.getKeyHandler(), context);
    }

    @Test
    void shouldUpdateMenuManager() {
        state.update(systems);

        verify(systems.getMenuManager()).update(systems);
    }

    @Test
    void shouldOpenHelpMenu() {
        when(systems.getKeyHandler().consume(VK_F1)).thenReturn(true);

        state.update(systems);

        verify(systems.getMenuManager()).open(isA(HelpMenu.class), same(systems));
    }

    @Test
    void shouldToggleSound() {
        when(systems.getKeyHandler().consume(VK_F1)).thenReturn(false);
        when(systems.getKeyHandler().consume(VK_S)).thenReturn(true);

        state.update(systems);

        verify(context.getSoundManager()).toggle();
        verify(systems.getDialogManager()).open(isA(Dialog.class));
    }

    @Test
    void shouldToggleHints() {
        when(systems.getKeyHandler().consume(VK_F1)).thenReturn(false);
        when(systems.getKeyHandler().consume(VK_S)).thenReturn(false);
        when(systems.getKeyHandler().consume(VK_H)).thenReturn(true);

        state.update(systems);

        verify(context.getHints()).toggle();
        verify(systems.getDialogManager()).open(isA(Dialog.class));
    }
}
