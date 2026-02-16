package duke.gameplay.player;

import duke.gameplay.Collidable;
import duke.gameplay.Facing;
import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.ui.KeyHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static duke.gameplay.GameplayContextFixture.SOLID_TILE_FLAG;
import static duke.gameplay.player.Player.GRAVITY;
import static duke.gameplay.player.Player.SPEED;
import static duke.gameplay.player.PlayerHealth.INVULNERABILITY_FRAMES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerTest {
    @Mock
    private KeyHandler.Input input;
    @Mock
    private Weapon weapon;
    @Mock
    private PlayerHealth health;
    @Mock
    private Inventory inventory;

    @Mock
    private MovementHandler movementHandler;
    @Mock
    private GameplayContext gameplayContext;
    @Mock
    private JumpHandler jumpHandler;
    @Mock
    private FallHandler fallHandler;
    @Mock
    private GrapplingHooks grapplingHooks;
    @Mock
    private PullUpHandler pullUpHandler;

    @Mock
    private PlayerAnimator animator;
    @Mock
    private PlayerFeedback feedback;

    private GameplayContext context;

    @BeforeEach
    void createContext() {
        context = GameplayContextFixture.create();
    }

    @Test
    void shouldProcessInput() {
        Player player = create(State.STANDING, Facing.LEFT);

        player.processInput(input);

        verify(movementHandler).handleInput(player, input);
        verify(weapon).setTriggered(input.fire());
        verify(jumpHandler).handleInput(player, input);
    }

    @Test
    void shouldMove() {
        Player player = create(State.STANDING, Facing.LEFT);

        player.setX(20);
        player.setY(12);

        assertThat(player.getX()).isEqualTo(20);
        assertThat(player.getY()).isEqualTo(12);
    }

    @ParameterizedTest
    @EnumSource(State.class)
    void shouldIndicateIfGrounded(State state) {
        assertThat(create(state, Facing.LEFT).isGrounded()).isEqualTo(switch (state) {
            case STANDING, WALKING -> true;
            default -> false;
        });
    }

    @Test
    void shouldWalk() {
        Player player = create(State.STANDING, Facing.LEFT);

        player.walk();

        assertThat(player.getState()).isEqualTo(State.WALKING);
    }

    @ParameterizedTest
    @EnumSource(value = State.class, names = {"STANDING"}, mode = EnumSource.Mode.EXCLUDE)
    void shouldNotWalkIfNotStanding(State state) {
        Player player = create(state, Facing.LEFT);

        player.walk();

        assertThat(player.getState()).isEqualTo(state);
    }

    @Test
    void shouldStand() {
        Player player = create(State.WALKING, Facing.LEFT);

        player.stand();

        assertThat(player.getState()).isEqualTo(State.STANDING);
    }

    @Test
    void shouldJump() {
        Player player = create(State.STANDING, Facing.LEFT);

        player.jump(false);

        assertThat(player.getState()).isEqualTo(State.JUMPING);
        assertThat(player.isFlipping()).isFalse();
    }

    @Test
    void shouldSlowFall() {
        Player player = create(State.JUMPING, Facing.LEFT);

        player.slowFall();

        assertThat(player.getState()).isEqualTo(State.FALLING);
        verify(fallHandler).slowFall();
    }

    @Test
    void shouldFastFall() {
        Player player = create(State.JUMPING, Facing.LEFT);

        player.fastFall();

        assertThat(player.getState()).isEqualTo(State.FALLING);
        verify(fallHandler).fall();
    }

    @Test
    void shouldLand() {
        Player player = create(State.FALLING, Facing.LEFT);

        player.setVelocityY(16);

        player.onCollision(Collidable.Direction.DOWN, SOLID_TILE_FLAG);

        assertThat(player.getVelocityY()).isEqualTo(0);
        assertThat(player.getState()).isEqualTo(State.STANDING);
    }

    @Test
    void shouldBump() {
        Player player = create(State.JUMPING, Facing.LEFT);
        player.setVelocityY(-8);

        player.onCollision(Collidable.Direction.UP, SOLID_TILE_FLAG);

        assertThat(player.getVelocityY()).isEqualTo(0);
    }

    @Test
    void shouldFall() {
        Player player = create(State.WALKING, Facing.LEFT);

        player.fall();

        assertThat(player.getState()).isEqualTo(State.FALLING);
    }

    @Test
    void shouldNotFallWhileJumping() {
        Player player = create(State.JUMPING, Facing.LEFT);

        player.fall();

        assertThat(player.getState()).isEqualTo(State.JUMPING);
    }

    @Test
    void shouldSetVelocity() {
        Player player = create(State.STANDING, Facing.LEFT);

        player.setVelocityX(8);
        player.setVelocityY(-16);

        assertThat(player.getVelocityX()).isEqualTo(8);
        assertThat(player.getVelocityY()).isEqualTo(-16);
    }

    @ParameterizedTest
    @EnumSource(value = State.class, names = {"STANDING", "WALKING"})
    void shouldHaveNoVerticalAcceleration(State state) {
        Player player = create(state, Facing.LEFT);

        assertThat(player.getVerticalAcceleration()).isEqualTo(0);
    }

    @Test
    void shouldAccelerateWhileFalling() {
        when(fallHandler.getVerticalAcceleration()).thenReturn(SPEED);

        Player player = create(State.FALLING, Facing.LEFT);

        assertThat(player.getVerticalAcceleration()).isEqualTo(SPEED);

        verify(fallHandler).getVerticalAcceleration();
    }

    @Test
    void shouldDetermineTerminalVelocity() {
        Player player = create(State.FALLING, Facing.LEFT);

        player.getTerminalVelocity();

        verify(fallHandler).getTerminalVelocity();
    }

    @Test
    void shouldAccelerateWhileJumping() {
        when(jumpHandler.getVerticalAcceleration(any())).thenReturn(GRAVITY);

        Player player = create(State.JUMPING, Facing.LEFT);

        assertThat(player.getVerticalAcceleration()).isEqualTo(GRAVITY);

        verify(jumpHandler).getVerticalAcceleration(player);
    }

    @Test
    void shouldFireWeapon() {
        Player player = create(State.STANDING, Facing.RIGHT);

        when(input.fire()).thenReturn(true);

        player.processInput(input);
        verify(weapon).setTriggered(true);

        player.postMovement(context);
        verify(weapon).fire(context);
    }

    @Test
    void shouldIndicateUsing() {
        Player player = create(State.STANDING, Facing.RIGHT);
        when(input.up()).thenReturn(true);

        player.processInput(input);

        assertThat(player.isUsing()).isTrue();
    }

    @Test
    void shouldAnimate() {
        Player player = create(State.STANDING, Facing.LEFT);

        player.postMovement(context);

        verify(animator).animate(player);

        player.getSpriteDescriptor();
        verify(animator).getSpriteDescriptor();
    }

    @Test
    void shouldProvideFeedback() {
        Player player = create(State.STANDING, Facing.LEFT);

        player.postMovement(context);

        verify(feedback).provideFeedback(same(context), same(player), anyBoolean(), anyBoolean(), anyBoolean(), anyBoolean());
    }

    @Test
    void shouldDisableControls() {
        Player player = create(State.STANDING, Facing.LEFT);
        player.setVelocityX(4);
        player.setVelocityY(4);

        player.disableControls();
        player.processInput(input);

        verifyNoInteractions(input, jumpHandler);
        verify(weapon).setTriggered(false);
        assertThat(player.getVelocityX()).isEqualTo(0);
        assertThat(player.getVelocityY()).isEqualTo(0);
    }

    @Test
    void shouldEnableControls() {
        Player player = create(State.STANDING, Facing.LEFT);
        player.disableControls();

        player.enableControls();
        player.processInput(input);

        verify(input, atLeastOnce()).left();
        verify(input, atLeastOnce()).right();
        verify(input).fire();
        verify(movementHandler).handleInput(player, input);
        verify(jumpHandler).handleInput(player, input);
    }

    @Test
    void shouldUseGrapplingHooks() {
        Player player = create(State.JUMPING, Facing.LEFT);

        player.postMovement(context);

        verify(grapplingHooks).update(context, input);
    }

    @Test
    void shouldUpdateStateWhenClinging() {
        Player player = create(State.JUMPING, Facing.LEFT);

        player.cling();

        assertThat(player.getVelocityY()).isEqualTo(0);
        assertThat(player.getState()).isEqualTo(State.CLINGING);
        assertThat(player.isFlipping()).isFalse();
    }

    @Test
    void shouldFallWhenClingReleased() {
        Player player = create(State.CLINGING, Facing.LEFT);

        player.releaseCling();

        assertThat(player.getState()).isEqualTo(State.FALLING);
    }

    @Test
    void shouldPullUp() {
        Player player = create(State.CLINGING, Facing.LEFT);

        player.pullUp();

        assertThat(player.getState()).isEqualTo(State.PULLING_UP);
        assertThat(player.isControllable()).isFalse();
        assertThat(player.isCollisionEnabled()).isFalse();
        verify(pullUpHandler).beginPullUp();
    }

    @Test
    void shouldDelegateToPullUpHandler() {
        Player player = create(State.PULLING_UP, Facing.LEFT);

        player.update(context);

        verify(pullUpHandler).update(player);
    }

    @Test
    void shouldCompletePullUp() {
        Player player = create(State.PULLING_UP, Facing.LEFT);

        player.pullUpComplete();

        assertThat(player.isControllable()).isTrue();
        assertThat(player.isCollisionEnabled()).isTrue();
        assertThat(player.getState()).isEqualTo(State.STANDING);
    }

    @Test
    void shouldNotBlinkWhenNotInvulnerable() {
        Player player = create(State.STANDING, Facing.LEFT);
        when(health.isInvulnerable()).thenReturn(false);

        assertThat(player.isVisible()).isTrue();
    }

    @Test
    void shouldBlinkWhenInvulnerable() {
        Player player = create(State.STANDING, Facing.LEFT);
        when(health.isInvulnerable()).thenReturn(true);

        for (int i = 0; i < INVULNERABILITY_FRAMES; i++) {
            when(health.getInvulnerability()).thenReturn(i);

            assertThat(player.isVisible()).isEqualTo(i % 2 == 1);
        }
    }

    private Player create(State state, Facing facing) {
        return new Player(input, state, facing, weapon, health, inventory, movementHandler, jumpHandler, fallHandler, grapplingHooks, pullUpHandler, animator, feedback);
    }
}
