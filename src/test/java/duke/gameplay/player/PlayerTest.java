package duke.gameplay.player;

import duke.gameplay.Collidable;
import duke.gameplay.Facing;
import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.gameplay.effects.Effect;
import duke.gameplay.effects.Explosion;
import duke.ui.KeyHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static duke.gameplay.Physics.GRAVITY;
import static duke.gameplay.player.Player.JUMP_POWER;
import static duke.gameplay.player.Player.SPEED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerTest {
    @Mock
    private KeyHandler.Input input;

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
        Player player = new Player(State.STANDING, Facing.LEFT, mock(), mock(), mock(), mock());

        when(input.left()).thenReturn(true);

        player.processInput(input);

        assertThat(player.getState()).isEqualTo(State.WALKING);
        assertThat(player.getFacing()).isEqualTo(Facing.LEFT);
        assertThat(player.getVelocityX()).isEqualTo(-SPEED);
    }

    @ParameterizedTest
    @EnumSource(value = State.class, names = {"STANDING"}, mode = EnumSource.Mode.EXCLUDE)
    void shouldMoveLeft(State state) {
        Player player = new Player(state, Facing.LEFT, mock(), mock(), mock(), mock());

        when(input.left()).thenReturn(true);

        player.processInput(input);

        assertThat(player.getState()).isEqualTo(state);
        assertThat(player.getVelocityX()).isEqualTo(-SPEED);
    }

    @Test
    void shouldWalkRight() {
        Player player = new Player(State.STANDING, Facing.RIGHT, mock(), mock(), mock(), mock());

        when(input.right()).thenReturn(true);

        player.processInput(input);

        assertThat(player.getVelocityX()).isEqualTo(SPEED);
    }

    @ParameterizedTest
    @EnumSource(value = State.class, names = {"STANDING"}, mode = EnumSource.Mode.EXCLUDE)
    void shouldMoveRight(State state) {
        Player player = new Player(state, Facing.LEFT, mock(), mock(), mock(), mock());

        when(input.left()).thenReturn(true);

        player.processInput(input);

        assertThat(player.getState()).isEqualTo(state);
        assertThat(player.getVelocityX()).isEqualTo(-SPEED);
    }

    @Test
    void shouldStandWhenWalkingStops() {
        Player player = new Player(State.WALKING, Facing.LEFT, mock(), mock(), mock(), mock());
        player.setVelocityX(-SPEED);

        player.processInput(input);
        player.update(GameplayContextFixture.create());

        assertThat(player.getState()).isEqualTo(State.STANDING);
        assertThat(player.getVelocityX()).isEqualTo(0);
    }

    @ParameterizedTest
    @EnumSource(value = State.class, names = {"WALKING"}, mode = EnumSource.Mode.EXCLUDE)
    void shouldStop(State state) {
        Player player = new Player(state, Facing.RIGHT, mock(), mock(), mock(), mock());
        player.setVelocityX(SPEED);
        player.setVelocityY(JUMP_POWER);

        player.processInput(input);
        player.update(GameplayContextFixture.create());

        assertThat(player.getState()).isEqualTo(state);
        assertThat(player.getVelocityX()).isEqualTo(0);
    }

    @ParameterizedTest
    @EnumSource(State.class)
    void shouldIndicateIfGrounded(State state) {
        assertThat(new Player(state, Facing.LEFT, mock(), mock(), mock(), mock()).isGrounded()).isEqualTo(switch (state) {
            case STANDING, WALKING -> true;
            default -> false;
        });
    }

    @ParameterizedTest
    @EnumSource(value = State.class, names = {"STANDING", "WALKING"})
    void shouldJumpWhenStandingOrWalking(State state) {
        Player player = new Player(state, Facing.RIGHT, mock(), mock(), mock(), mock());

        when(input.jump()).thenReturn(true);

        player.processInput(input);

        assertThat(player.getVelocityY()).isEqualTo(-15);
        assertThat(player.getState()).isEqualTo(State.JUMPING);
    }

    @ParameterizedTest
    @EnumSource(value = State.class, names = {"STANDING", "WALKING"}, mode = EnumSource.Mode.EXCLUDE)
    void shouldNotJumpWhenNotStandingOrWalking(State state) {
        Player player = new Player(state, Facing.RIGHT, mock(), mock(), mock(), mock());

        when(input.jump()).thenReturn(true);

        player.processInput(input);

        assertThat(player.getVelocityY()).isEqualTo(0);
        assertThat(player.getState()).isEqualTo(state);
    }

    @Test
    void shouldLand() {
        Player player = new Player(State.FALLING, Facing.LEFT, mock(), mock(), mock(), mock());
        player.setVelocityY(16);

        player.onCollision(Collidable.Direction.DOWN);

        assertThat(player.getVelocityY()).isEqualTo(0);
        assertThat(player.getState()).isEqualTo(State.STANDING);
    }

    @Test
    void shouldSpawnDustWhenLanding() {
        GameplayContext context = GameplayContextFixture.create();
        Player player = new Player(State.FALLING, Facing.LEFT, mock(), mock(), mock(), mock());
        player.setVelocityY(16);

        player.onCollision(Collidable.Direction.DOWN);
        player.postMovement(context);

        verify(context.getActiveManager()).spawn(any(Effect.class));
    }

    @Test
    void shouldBump() {
        Player player = new Player(State.JUMPING, Facing.LEFT, mock(), mock(), mock(), mock());
        player.setVelocityY(-8);

        player.onCollision(Collidable.Direction.UP);

        assertThat(player.getVelocityY()).isEqualTo(0);
    }

    @Test
    void shouldFall() {
        Player player = new Player();

        player.fall();

        assertThat(player.getState()).isEqualTo(State.FALLING);
    }

    @Test
    void shouldNotFallWhileJumping() {
        Player player = new Player(State.JUMPING, Facing.LEFT, mock(), mock(), mock(), mock());

        player.fall();

        assertThat(player.getState()).isEqualTo(State.JUMPING);
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
    @EnumSource(value = State.class, names = {"STANDING", "WALKING"})
    void shouldHaveNoVerticalAcceleration(State state) {
        Player player = new Player(state, Facing.LEFT, mock(), mock(), mock(), mock());

        assertThat(player.getVerticalAcceleration()).isEqualTo(0);
    }

    @Test
    void shouldHaveGravityWhileJumping() {
        Player player = new Player(State.JUMPING, Facing.LEFT, mock(), mock(), mock(), mock());

        assertThat(player.getVerticalAcceleration()).isEqualTo(GRAVITY);
    }

    @Test
    void shouldFloatWhileHanging() {
        Player player = new Player();
        player.processInput(new KeyHandler.Input(false, false, true, false, false));
        player.setVelocityY(0);

        for (int frame = 1; frame < Player.HANG_TIME; frame++) {
            player.update(GameplayContextFixture.create());
            int verticalAcceleration = player.getVerticalAcceleration();
            assertThat(verticalAcceleration).isEqualTo(0);
        }
    }

    @Test
    void shouldAccelerateWhileFalling() {
        Player player = new Player(State.FALLING, Facing.LEFT, mock(), mock(), mock(), mock());

        assertThat(player.getVerticalAcceleration()).isEqualTo(SPEED);
    }

    @Test
    void shouldFireWeapon() {
        GameplayContext context = GameplayContextFixture.create();
        Weapon weapon = mock();
        Player player = new Player(State.STANDING, Facing.RIGHT, weapon, mock(), mock(), mock());

        when(input.fire()).thenReturn(true);

        player.processInput(input);
        verify(weapon).setTriggered(true);

        player.postMovement(context);
        verify(weapon).fire(context);
    }

    @Test
    void shouldIndicateUsing() {
        Player player = new Player();

        player.processInput(new KeyHandler.Input(false, false, false, false, true));

        assertThat(player.isUsing()).isTrue();
    }

    @Test
    void shouldTakeDamage() {
        Health health = mock();
        Player player = new Player(State.STANDING, Facing.LEFT, mock(), health, mock(), mock());
        GameplayContext context = GameplayContextFixture.create();
        Explosion explosion = new Explosion(player.getX(), player.getY());
        when(context.getActiveManager().getActives()).thenReturn(List.of(explosion));

        player.postMovement(context);

        verify(health).takeDamage(explosion);
    }

    @Test
    void shouldNotTakeDamageWhileInvulnerable() {
        Health health = mock();
        when(health.isInvulnerable()).thenReturn(true);
        Player player = new Player(State.STANDING, Facing.LEFT, mock(), health, mock(), mock());
        GameplayContext context = GameplayContextFixture.create();

        player.postMovement(context);

        verify(health, never()).takeDamage(any());
    }

    @Test
    void shouldNotTakeDoubleDamage() {
        Health health = mock();
        Player player = new Player(State.STANDING, Facing.LEFT, mock(), health, mock(), mock());
        GameplayContext context = GameplayContextFixture.create();
        Explosion explosion = new Explosion(player.getX(), player.getY());
        when(context.getActiveManager().getActives()).thenReturn(List.of(explosion, explosion));

        player.postMovement(context);

        verify(health, times(1)).takeDamage(explosion);
    }

    @Test
    void shouldAnimate() {
        PlayerAnimator animator = mock();
        Player player = new Player(State.STANDING, Facing.LEFT, mock(), mock(), mock(), animator);

        player.postMovement(GameplayContextFixture.create());

        verify(animator).animate(player);

        player.getSpriteDescriptor();
        verify(animator).getSpriteDescriptor();
    }
}
