package duke.gameplay;

import duke.ui.KeyHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static duke.gameplay.Physics.GRAVITY;
import static duke.gameplay.Player.JUMP_POWER;
import static duke.gameplay.Player.SPEED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerTest {
    @Mock
    private KeyHandler.Input input;

    @Mock
    private GameplayContext context;

    @Test
    void shouldMove() {
        Player player = new Player();

        player.setX(20);
        player.setY(12);

        assertThat(player.getX()).isEqualTo(20);
        assertThat(player.getY()).isEqualTo(12);
    }

    @Test
    void shouldWalkLeftWhenStanding() {
        Player player = new Player(Player.State.STANDING, Facing.LEFT);

        when(input.left()).thenReturn(true);

        player.processInput(input);

        assertThat(player.getState()).isEqualTo(Player.State.WALKING);
        assertThat(player.getFacing()).isEqualTo(Facing.LEFT);
        assertThat(player.getVelocityX()).isEqualTo(-SPEED);
    }

    @ParameterizedTest
    @EnumSource(value = Player.State.class, names = {"STANDING"}, mode = EnumSource.Mode.EXCLUDE)
    void shouldMoveLeft(Player.State state) {
        Player player = new Player(state, Facing.LEFT);

        when(input.left()).thenReturn(true);

        player.processInput(input);

        assertThat(player.getState()).isEqualTo(state);
        assertThat(player.getVelocityX()).isEqualTo(-SPEED);
    }

    @Test
    void shouldWalkRight() {
        Player player = new Player(Player.State.STANDING, Facing.RIGHT);

        when(input.right()).thenReturn(true);

        player.processInput(input);

        assertThat(player.getVelocityX()).isEqualTo(SPEED);
    }

    @ParameterizedTest
    @EnumSource(value = Player.State.class, names = {"STANDING"}, mode = EnumSource.Mode.EXCLUDE)
    void shouldMoveRight(Player.State state) {
        Player player = new Player(state, Facing.LEFT);

        when(input.left()).thenReturn(true);

        player.processInput(input);

        assertThat(player.getState()).isEqualTo(state);
        assertThat(player.getVelocityX()).isEqualTo(-SPEED);
    }

    @Test
    void shouldStandWhenWalkingStops() {
        Player player = new Player(Player.State.WALKING, Facing.LEFT);
        player.setVelocityX(-SPEED);

        player.processInput(input);
        player.update(context);

        assertThat(player.getState()).isEqualTo(Player.State.STANDING);
        assertThat(player.getVelocityX()).isEqualTo(0);
    }

    @ParameterizedTest
    @EnumSource(value = Player.State.class, names = {"WALKING"}, mode = EnumSource.Mode.EXCLUDE)
    void shouldStop(Player.State state) {
        Player player = new Player(state, Facing.RIGHT);
        player.setVelocityX(SPEED);
        player.setVelocityY(JUMP_POWER);

        player.processInput(input);
        player.update(context);

        assertThat(player.getState()).isEqualTo(state);
        assertThat(player.getVelocityX()).isEqualTo(0);
    }

    @ParameterizedTest
    @EnumSource(Player.State.class)
    void shouldIndicateIfGrounded(Player.State state) {
        assertThat(new Player(state, Facing.LEFT).isGrounded()).isEqualTo(switch (state) {
            case STANDING, WALKING -> true;
            default -> false;
        });
    }

    @ParameterizedTest
    @EnumSource(value = Player.State.class, names = {"STANDING", "WALKING"})
    void shouldJumpWhenStandingOrWalking(Player.State state) {
        Player player = new Player(state, Facing.RIGHT);

        when(input.jump()).thenReturn(true);

        player.processInput(input);

        assertThat(player.getVelocityY()).isEqualTo(-15);
        assertThat(player.getState()).isEqualTo(Player.State.JUMPING);
    }

    @ParameterizedTest
    @EnumSource(value = Player.State.class, names = {"STANDING", "WALKING"}, mode = EnumSource.Mode.EXCLUDE)
    void shouldNotJumpWhenNotStandingOrWalking(Player.State state) {
        Player player = new Player(state, Facing.RIGHT);

        when(input.jump()).thenReturn(true);

        player.processInput(input);

        assertThat(player.getVelocityY()).isEqualTo(0);
        assertThat(player.getState()).isEqualTo(state);
    }

    @Test
    void shouldLand() {
        Player player = new Player(Player.State.FALLING, Facing.LEFT);
        player.setVelocityY(16);

        player.onCollision(Collidable.Direction.DOWN);

        assertThat(player.getVelocityY()).isEqualTo(0);
        assertThat(player.getState()).isEqualTo(Player.State.STANDING);
    }

    @Test
    void shouldBump() {
        Player player = new Player(Player.State.JUMPING, Facing.LEFT);
        player.setVelocityY(-8);

        player.onCollision(Collidable.Direction.UP);

        assertThat(player.getVelocityY()).isEqualTo(0);
    }

    @Test
    void shouldFall() {
        Player player = new Player();

        player.fall();

        assertThat(player.getState()).isEqualTo(Player.State.FALLING);
    }

    @Test
    void shouldNotFallWhileJumping() {
        Player player = new Player(Player.State.JUMPING, Facing.LEFT);

        player.fall();

        assertThat(player.getState()).isEqualTo(Player.State.JUMPING);
    }

    @Test
    void shouldSetVelocity() {
        Player player = new Player();

        player.setVelocityX(8);
        player.setVelocityY(-16);

        assertThat(player.getVelocityX()).isEqualTo(8);
        assertThat(player.getVelocityY()).isEqualTo(-16);
    }

    @ParameterizedTest
    @EnumSource(value = Player.State.class, names = {"STANDING", "WALKING"})
    void shouldHaveNoVerticalAcceleration(Player.State state) {
        Player player = new Player(state, Facing.LEFT);

        assertThat(player.getVerticalAcceleration()).isEqualTo(0);
    }

    @Test
    void shouldHaveGravityWhileJumping() {
        Player player = new Player(Player.State.JUMPING, Facing.LEFT);

        assertThat(player.getVerticalAcceleration()).isEqualTo(GRAVITY);
    }

    @Test
    void shouldFloatWhileHanging() {
        Player player = new Player();
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
        Player player = new Player(Player.State.FALLING, Facing.LEFT);

        assertThat(player.getVerticalAcceleration()).isEqualTo(SPEED);
    }
}
