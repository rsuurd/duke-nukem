package duke.state;

import duke.GameSystems;
import duke.GameSystemsFixture;
import duke.dialog.Dialog;
import duke.dialog.Hints;
import duke.gameplay.*;
import duke.gameplay.player.Inventory;
import duke.gameplay.player.Player;
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

import java.util.Set;

import static java.awt.event.KeyEvent.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameplayStateTest {
    @Mock
    private GameplayRuntimeFactory runtimeFactory;

    @Mock
    private LevelManager levelManager;

    @Mock
    private Viewport viewport;

    @Mock
    private Collision collision;

    private GameSystems systems;
    private GameplayContext context;

    private GameplayState state;

    @BeforeEach
    void setUp() {
        systems = GameSystemsFixture.create();
        context = spy(GameplayContextFixture.create());

        when(runtimeFactory.createRuntime(systems)).thenReturn(
                new GameplayRuntimeFactory.GameplayRuntime(levelManager, viewport, collision, context)
        );

        state = new GameplayState(runtimeFactory, null);
    }

    @Test
    void shouldStartNewGame() {
        when(context.getPlayer().getHealth()).thenReturn(mock());
        Level level = mock();
        when(level.getDescriptor()).thenReturn(new LevelDescriptor(0, 1, 0));
        when(levelManager.getNextLevel()).thenReturn(level);

        state.start(systems);

        verify(context).switchLevel(level);
        verify(viewport).center(any());
    }

    @Test
    void shouldResumeSavedGame() {
        Inventory inventory = new Inventory();
        inventory.addEquipment(Inventory.Equipment.BOOTS);
        inventory.addEquipment(Inventory.Equipment.GRAPPLING_HOOKS);

        SaveGame saveGame = new SaveGame(0, 0, 0, 0, 4, 5, 3,
                inventory, Set.of(Hints.Hint.ELEVATOR), 200400);

        state = new GameplayState(runtimeFactory, saveGame);

        when(context.getPlayer().getHealth()).thenReturn(mock());
        when(context.getPlayer().getWeapon()).thenReturn(mock());
        when(context.getPlayer().getInventory()).thenReturn(mock());

        Level level = mock();
        when(level.getDescriptor()).thenReturn(new LevelDescriptor(4, 1, 0));
        when(levelManager.warpTo(4)).thenReturn(level);

        state.start(systems);

        verify(levelManager).warpTo(4);
        verify(context).switchLevel(level);
        verify(viewport).center(any());

        verify(context.getPlayer().getHealth()).setCurrent(5);
        verify(context.getPlayer().getWeapon()).setFirepower(3);
        verify(context.getPlayer().getInventory()).addEquipment(Inventory.Equipment.BOOTS);
        verify(context.getPlayer().getInventory()).addEquipment(Inventory.Equipment.GRAPPLING_HOOKS);
        verify(context.getPlayer().getInventory(), never()).addEquipment(Inventory.Equipment.ROBOHAND);
        verify(context.getPlayer().getInventory(), never()).addEquipment(Inventory.Equipment.ACCESS_CARD);
        verify(context.getHints()).setAvailableHints(Set.of(Hints.Hint.ELEVATOR));
        verify(context.getScoreManager()).score(200400);
    }

    @Test
    void shouldUpdateMenuManager() {
        prepareStartedGameplayState();

        state.update(systems);

        verify(systems.getMenuManager()).update(systems);
    }

    @Test
    void shouldOpenHelpMenu() {
        prepareStartedGameplayState();

        when(systems.getKeyHandler().consume(VK_F1)).thenReturn(true);

        state.update(systems);

        verify(systems.getMenuManager()).open(isA(HelpMenu.class), same(systems));
    }

    @Test
    void shouldToggleSound() {
        prepareStartedGameplayState();

        when(systems.getKeyHandler().consume(VK_F1)).thenReturn(false);
        when(systems.getKeyHandler().consume(VK_S)).thenReturn(true);

        state.update(systems);

        verify(context.getSoundManager()).toggle();
        verify(systems.getDialogManager()).open(isA(Dialog.class));
    }

    @Test
    void shouldToggleHints() {
        prepareStartedGameplayState();

        when(systems.getKeyHandler().consume(VK_F1)).thenReturn(false);
        when(systems.getKeyHandler().consume(VK_S)).thenReturn(false);
        when(systems.getKeyHandler().consume(VK_H)).thenReturn(true);

        state.update(systems);

        verify(context.getHints()).toggle();
        verify(systems.getDialogManager()).open(isA(Dialog.class));
    }

    @Test
    void shouldPauseWhenDialogIsOpen() {
        prepareStartedGameplayState();

        when(systems.getDialogManager().hasDialog()).thenReturn(true);

        state.update(systems);

        verify(systems.getDialogManager()).update(systems);

        verifyNoInteractions(context.getPlayer(), context.getActiveManager(), context.getBoltManager(), collision, viewport);
    }

    @Test
    void shouldUpdatePlayer() {
        prepareStartedGameplayState();

        state.update(systems);

        Player player = context.getPlayer();
        verify(player).processInput(systems.getKeyHandler().getInput());
        verify(player).update(context);
        verify(collision).resolve(player, context);
        verify(player).postMovement(context);
    }

    @Test
    void shouldUpdateViewport() {
        prepareStartedGameplayState();

        Player player = context.getPlayer();
        when(context.getViewportManager().getTarget()).thenReturn(player);

        state.update(systems);

        verify(viewport).update(player);
    }

    @Test
    void shouldSnapViewportToCenter() {
        prepareStartedGameplayState();

        Player player = context.getPlayer();
        when(context.getViewportManager().pollSnapToCenter()).thenReturn(true);
        when(context.getViewportManager().getTarget()).thenReturn(player);

        state.update(systems);

        verify(viewport).center(player);
    }

    @Test
    void shouldUpdateBolts() {
        prepareStartedGameplayState();

        state.update(systems);

        verify(context.getBoltManager()).update(context);
    }

    @Test
    void shouldUpdateActives() {
        prepareStartedGameplayState();

        state.update(systems);

        verify(context.getActiveManager()).update(context);
    }

    @Test
    void shouldSwitchLevelOnComplete() {
        prepareStartedGameplayState();

        when(context.getPlayer().getHealth()).thenReturn(mock());
        when(context.getLevel().isCompleted()).thenReturn(true);

        Level nextLevel = mock();
        when(nextLevel.getDescriptor()).thenReturn(new LevelDescriptor(1, 2, 0));
        when(levelManager.getNextLevel()).thenReturn(nextLevel);

        state.update(systems);

        verify(context).switchLevel(nextLevel);
    }

    @Test
    void shouldTransitionToEndWhenLastLevelComplete() {
        prepareStartedGameplayState();

        when(context.getLevel().isCompleted()).thenReturn(true);
        when(levelManager.isLast()).thenReturn(true);

        state.update(systems);

        verify(systems.getStateRequester()).requestState(isA(End.class));
    }

    @Test
    void shouldNotTransitionToEndWhenAlreadyTransitioning() {
        prepareStartedGameplayState();

        when(context.getLevel().isCompleted()).thenReturn(true);
        when(levelManager.isLast()).thenReturn(true);
        when(systems.getStateRequester().isTransitioning()).thenReturn(true);

        state.update(systems);

        verify(systems.getStateRequester(), never()).requestState(isA(End.class));
    }

    private void prepareStartedGameplayState() {
        when(context.getPlayer().getHealth()).thenReturn(mock());
        Level level = mock();
        when(level.getDescriptor()).thenReturn(new LevelDescriptor(0, 1, 0));
        when(levelManager.getNextLevel()).thenReturn(level);

        state.start(systems);

        reset(systems.getMenuManager(), viewport, collision, context, context.getPlayer(), context.getActiveManager(), context.getBoltManager());
    }
}
