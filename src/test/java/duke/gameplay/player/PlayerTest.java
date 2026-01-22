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

import static duke.gameplay.Physics.GRAVITY;
import static duke.gameplay.player.Player.JUMP_POWER;
import static duke.gameplay.player.Player.SPEED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    private PlayerAnimator animator;
    @Mock
    private PlayerFeedback feedback;

    private GameplayContext context;

    @BeforeEach
    void createContext() {
        context = GameplayContextFixture.create();
    }

    @Test
    void shouldMove() {
        Player player = create(State.STANDING, Facing.LEFT);

        player.setX(20);
        player.setY(12);

        assertThat(player.getX()).isEqualTo(20);
        assertThat(player.getY()).isEqualTo(12);
    }

    @Test
    void shouldWalkLeftWhenStanding() {
        Player player = create(State.STANDING, Facing.LEFT);

        when(input.left()).thenReturn(true);

        player.processInput(input);

        assertThat(player.getState()).isEqualTo(State.WALKING);
        assertThat(player.getFacing()).isEqualTo(Facing.LEFT);
        assertThat(player.getVelocityX()).isEqualTo(-SPEED);
    }

    @ParameterizedTest
    @EnumSource(value = State.class, names = {"STANDING"}, mode = EnumSource.Mode.EXCLUDE)
    void shouldMoveLeft(State state) {
        Player player = create(state, Facing.LEFT);

        when(input.left()).thenReturn(true);

        player.processInput(input);

        assertThat(player.getState()).isEqualTo(state);
        assertThat(player.getVelocityX()).isEqualTo(-SPEED);
    }

    @Test
    void shouldWalkRight() {
        Player player = create(State.STANDING, Facing.RIGHT);

        when(input.right()).thenReturn(true);

        player.processInput(input);

        assertThat(player.getVelocityX()).isEqualTo(SPEED);
    }

    @ParameterizedTest
    @EnumSource(value = State.class, names = {"STANDING"}, mode = EnumSource.Mode.EXCLUDE)
    void shouldMoveRight(State state) {
        Player player = create(state, Facing.LEFT);

        when(input.left()).thenReturn(true);

        player.processInput(input);

        assertThat(player.getState()).isEqualTo(state);
        assertThat(player.getVelocityX()).isEqualTo(-SPEED);
    }

    @Test
    void shouldStandWhenWalkingStops() {
        Player player = create(State.WALKING, Facing.LEFT);

        player.setVelocityX(-SPEED);

        player.processInput(input);
        player.update(context);

        assertThat(player.getState()).isEqualTo(State.STANDING);
        assertThat(player.getVelocityX()).isEqualTo(0);
    }

    @ParameterizedTest
    @EnumSource(value = State.class, names = {"WALKING"}, mode = EnumSource.Mode.EXCLUDE)
    void shouldStop(State state) {
        Player player = create(state, Facing.RIGHT);
        player.setVelocityX(SPEED);
        player.setVelocityY(JUMP_POWER);

        player.processInput(input);
        player.update(context);

        assertThat(player.getState()).isEqualTo(state);
        assertThat(player.getVelocityX()).isEqualTo(0);
    }

    @ParameterizedTest
    @EnumSource(State.class)
    void shouldIndicateIfGrounded(State state) {
        assertThat(create(state, Facing.LEFT).isGrounded()).isEqualTo(switch (state) {
            case STANDING, WALKING -> true;
            default -> false;
        });
    }

    @ParameterizedTest
    @EnumSource(value = State.class, names = {"STANDING", "WALKING"})
    void shouldJumpWhenStandingOrWalking(State state) {
        Player player = create(state, Facing.RIGHT);

        when(input.jump()).thenReturn(true);

        player.processInput(input);

        assertThat(player.getVelocityY()).isEqualTo(-15);
        assertThat(player.getState()).isEqualTo(State.JUMPING);
    }

    @ParameterizedTest
    @EnumSource(value = State.class, names = {"STANDING", "WALKING"}, mode = EnumSource.Mode.EXCLUDE)
    void shouldNotJumpWhenNotStandingOrWalking(State state) {
        Player player = create(state, Facing.RIGHT);

        when(input.jump()).thenReturn(true);

        player.processInput(input);

        assertThat(player.getVelocityY()).isEqualTo(0);
        assertThat(player.getState()).isEqualTo(state);
    }

    @Test
    void shouldLand() {
        Player player = create(State.FALLING, Facing.LEFT);

        player.setVelocityY(16);

        player.onCollision(Collidable.Direction.DOWN);

        assertThat(player.getVelocityY()).isEqualTo(0);
        assertThat(player.getState()).isEqualTo(State.STANDING);
    }

    @Test
    void shouldBump() {
        Player player = create(State.JUMPING, Facing.LEFT);
        player.setVelocityY(-8);

        player.onCollision(Collidable.Direction.UP);

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
    void shouldHaveGravityWhileJumping() {
        Player player = create(State.JUMPING, Facing.LEFT);

        assertThat(player.getVerticalAcceleration()).isEqualTo(GRAVITY);
    }

    @Test
    void shouldFloatWhileHanging() {
        Player player = create(State.STANDING, Facing.LEFT);
        player.processInput(new KeyHandler.Input(false, false, true, false, false));
        player.setVelocityY(0);

        for (int frame = 1; frame < Player.HANG_TIME; frame++) {
            player.update(context);
            int verticalAcceleration = player.getVerticalAcceleration();
            assertThat(verticalAcceleration).isEqualTo(0);
        }
    }

    @Test
    void shouldAccelerateWhileFalling() {
        Player player = create(State.FALLING, Facing.LEFT);

        assertThat(player.getVerticalAcceleration()).isEqualTo(SPEED);
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

        player.processInput(new KeyHandler.Input(false, false, false, false, true));

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

        verify(feedback).provideFeedback(same(context), same(player), anyBoolean(), anyBoolean(), anyBoolean());
    }

    private Player create(State state, Facing facing) {
        return new Player(state, facing, weapon, health, inventory, animator, feedback);
    }
}
