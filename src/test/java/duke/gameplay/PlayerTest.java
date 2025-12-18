package duke.gameplay;

import duke.ui.KeyHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static duke.gameplay.Player.SPEED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerTest {
    @Mock
    private KeyHandler.Input input;

    @Test
    void shouldMove() {
        Player player = new Player();

        player.moveTo(20, 12);

        assertThat(player.getX()).isEqualTo(20);
        assertThat(player.getY()).isEqualTo(12);
    }

    @Test
    void shouldWalkLeftWhenStanding() {
        Player player = new Player(Player.State.STANDING, Player.Facing.LEFT);

        when(input.left()).thenReturn(true);

        player.processInput(input);

        assertThat(player.getState()).isEqualTo(Player.State.WALKING);
        assertThat(player.getFacing()).isEqualTo(Player.Facing.LEFT);
        assertThat(player.getVelocityX()).isEqualTo(-SPEED);
    }

    @ParameterizedTest
    @EnumSource(value = Player.State.class, names = {"STANDING"}, mode = EnumSource.Mode.EXCLUDE)
    void shouldMoveLeft(Player.State state) {
        Player player = new Player(state, Player.Facing.LEFT);

        when(input.left()).thenReturn(true);

        player.processInput(input);

        assertThat(player.getState()).isEqualTo(state);
        assertThat(player.getVelocityX()).isEqualTo(-SPEED);
    }

    @Test
    void shouldWalkRight() {
        Player player = new Player(Player.State.STANDING, Player.Facing.RIGHT);

        when(input.right()).thenReturn(true);

        player.processInput(input);

        assertThat(player.getVelocityX()).isEqualTo(SPEED);
    }

    @ParameterizedTest
    @EnumSource(value = Player.State.class, names = {"STANDING"}, mode = EnumSource.Mode.EXCLUDE)
    void shouldMoveRight(Player.State state) {
        Player player = new Player(state, Player.Facing.LEFT);

        when(input.left()).thenReturn(true);

        player.processInput(input);

        assertThat(player.getState()).isEqualTo(state);
        assertThat(player.getVelocityX()).isEqualTo(-SPEED);
    }

    @Test
    void shouldStandWhenWalkingStops() {
        Player player = new Player(Player.State.WALKING, Player.Facing.LEFT);
        player.setVelocity(-SPEED, 0);

        player.processInput(input);

        assertThat(player.getState()).isEqualTo(Player.State.STANDING);
        assertThat(player.getVelocityX()).isEqualTo(0);
    }

    @ParameterizedTest
    @EnumSource(value = Player.State.class, names = {"WALKING"}, mode = EnumSource.Mode.EXCLUDE)
    void shouldStop(Player.State state) {
        Player player = new Player(state, Player.Facing.RIGHT);
        player.setVelocity(SPEED, 0);

        player.processInput(input);

        assertThat(player.getState()).isEqualTo(state);
        assertThat(player.getVelocityX()).isEqualTo(0);
    }

    @ParameterizedTest
    @EnumSource(Player.State.class)
    void shouldIndicateIfGrounded(Player.State state) {
        assertThat(new Player(state, Player.Facing.LEFT).isGrounded()).isEqualTo(switch (state) {
            case STANDING, WALKING -> true;
            default -> false;
        });
    }

    @ParameterizedTest
    @EnumSource(value = Player.State.class, names = {"STANDING", "WALKING"})
    void shouldJumpWhenStandingOrWalking(Player.State state) {
        Player player = new Player(state, Player.Facing.RIGHT);

        when(input.jump()).thenReturn(true);

        player.processInput(input);

        assertThat(player.getVelocityY()).isEqualTo(-15);
        assertThat(player.getState()).isEqualTo(Player.State.JUMPING);
    }

    @ParameterizedTest
    @EnumSource(value = Player.State.class, names = {"STANDING", "WALKING"}, mode = EnumSource.Mode.EXCLUDE)
    void shouldNotJumpWhenNotStandingOrWalking(Player.State state) {
        Player player = new Player(state, Player.Facing.RIGHT);

        when(input.jump()).thenReturn(true);

        player.processInput(input);

        assertThat(player.getVelocityY()).isEqualTo(0);
        assertThat(player.getState()).isEqualTo(state);
    }

    @Test
    void shouldLand() {
        Player player = new Player(Player.State.FALLING, Player.Facing.LEFT);
        player.setVelocity(0, 16);

        player.onCollide(Collidable.Direction.DOWN);

        assertThat(player.getVelocityY()).isEqualTo(0);
        assertThat(player.getState()).isEqualTo(Player.State.STANDING);
    }

    @Test
    void shouldBump() {
        Player player = new Player(Player.State.JUMPING, Player.Facing.LEFT);
        player.setVelocity(0, -8);

        player.onCollide(Collidable.Direction.UP);

        assertThat(player.getVelocityY()).isEqualTo(0);
    }

    @Test
    void shouldFall() {
        Player player = new Player();

        player.fall();

        assertThat(player.getState()).isEqualTo(Player.State.FALLING);
    }

    @Test
    void shouldSetVelocity() {
        Player player = new Player();

        player.setVelocity(8, -16);

        assertThat(player.getVelocityX()).isEqualTo(8);
        assertThat(player.getVelocityY()).isEqualTo(-16);
    }
}
