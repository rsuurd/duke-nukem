package duke.gameplay.player;

import duke.gameplay.Collidable;
import duke.gameplay.Facing;
import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.level.Flags;
import duke.ui.KeyHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Random;

import static duke.gameplay.GameplayContextFixture.SOLID_TILE_FLAG;
import static duke.gameplay.player.Player.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerTest {
    @Mock
    private KeyHandler.Input input;
    @Mock
    private Random random;
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
    @Mock
    private ClingHandler clingHandler;

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
    void shouldApplyFriction(State state) {
        Player player = create(state, Facing.RIGHT);
        player.setVelocityX(SPEED);

        player.processInput(input);
        player.update(context);

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
    void shouldJump() {
        Player player = create(State.STANDING, Facing.LEFT);
        when(input.jump()).thenReturn(true);

        player.processInput(input);

        assertThat(player.getState()).isEqualTo(State.JUMPING);
        assertThat(player.getVelocityY()).isEqualTo(JUMP_POWER);
    }

    @Test
    void shouldFallWhenDamageTakenMidJump() {
        Player player = create(State.JUMPING, Facing.LEFT);
        when(input.jump()).thenReturn(true);

        player.processInput(input);
        player.getHealth().takeDamage(1);
        player.update(context);

        assertThat(player.getState()).isEqualTo(State.FALLING);
    }

    @Test
    void shouldJumpHigherWithBoots() {
        Player player = create(State.STANDING, Facing.LEFT);
        when(input.jump()).thenReturn(true);
        when(player.getInventory().isEquippedWith(Inventory.Equipment.BOOTS)).thenReturn(true);

        player.processInput(input);

        assertThat(player.getState()).isEqualTo(State.JUMPING);
        assertThat(player.getVelocityY()).isEqualTo(HIGH_JUMP_POWER);
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
        when(input.using()).thenReturn(true);

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

        verifyNoInteractions(input);
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
        verify(input).using();
        verify(input).jump();
    }

    @Test
    void shouldFlip() {
        Player player = create(State.STANDING, Facing.LEFT);
        when(inventory.isEquippedWith(Inventory.Equipment.BOOTS)).thenReturn(true);
        when(input.left()).thenReturn(true);
        when(input.jump()).thenReturn(true);
        when(random.nextBoolean()).thenReturn(true);

        player.processInput(input);

        assertThat(player.isFlipping()).isTrue();
    }

    @Test
    void shouldDelegateToClingHandler() {
        Player player = create(State.STANDING, Facing.LEFT);

        player.update(context);

        verify(clingHandler).update(context);
    }

    @Test
    void shouldCheckIfShouldCling() {
        Player player = create(State.JUMPING, Facing.LEFT);

        player.onCollision(Direction.UP, Flags.CLINGABLE.bit());

        verify(clingHandler).onBump(player, Flags.CLINGABLE.bit());
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

    private Player create(State state, Facing facing) {
        return new Player(state, facing, random, weapon, health, inventory, animator, feedback, clingHandler);
    }
}
