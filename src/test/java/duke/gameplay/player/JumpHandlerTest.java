package duke.gameplay.player;

import duke.ui.KeyHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Random;

import static duke.gameplay.Physics.GRAVITY;
import static duke.gameplay.player.JumpHandler.*;
import static duke.gameplay.player.Player.SPEED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JumpHandlerTest {
    @Mock
    private Random random;

    private JumpHandler handler;

    @Mock
    private Player player;

    @Mock
    private PlayerHealth health;

    @Mock
    private Inventory inventory;

    @Mock
    private KeyHandler.Input input;

    @BeforeEach
    void createHandler() {
        handler = new JumpHandler(random, true, 0);
    }

    @Test
    void shouldJumpWhenGrounded() {
        when(input.jump()).thenReturn(true);
        when(player.isGrounded()).thenReturn(true);
        when(player.getInventory()).thenReturn(inventory);

        handler.handleInput(player, input);

        verify(player).setVelocityY(JUMP_POWER);
        verify(player).jump(false);
    }

    @Test
    void shouldJumpHigherWithBoots() {
        when(input.jump()).thenReturn(true);
        when(player.isGrounded()).thenReturn(true);
        when(player.getInventory()).thenReturn(inventory);
        when(inventory.isEquippedWith(Inventory.Equipment.BOOTS)).thenReturn(true);

        handler.handleInput(player, input);

        verify(player).setVelocityY(HIGH_JUMP_POWER);
        verify(player).jump(false);
    }

    @Test
    void shouldRandomlyFlip() {
        when(input.jump()).thenReturn(true);
        when(player.isGrounded()).thenReturn(true);
        when(player.getInventory()).thenReturn(inventory);
        when(inventory.isEquippedWith(Inventory.Equipment.BOOTS)).thenReturn(true);
        when(player.getVelocityX()).thenReturn(SPEED);
        when(random.nextBoolean()).thenReturn(true);

        handler.handleInput(player, input);

        verify(player).setVelocityY(HIGH_JUMP_POWER);
        verify(player).jump(true);
    }

    @Test
    void shouldNotJumpWhenNotGrounded() {
        when(input.jump()).thenReturn(true);
        when(player.isGrounded()).thenReturn(false);

        handler.handleInput(player, input);

        verify(player, never()).setVelocityY(anyInt());
        verify(player, never()).jump(anyBoolean());
    }

    @Test
    void shouldNotJumpWhenNotJumpPressed() {
        when(input.jump()).thenReturn(false);

        handler.handleInput(player, input);

        verify(player, never()).setVelocityY(anyInt());
        verify(player, never()).jump(anyBoolean());
    }


    @Test
    void shouldNotJumpWhenJumpNotReady() {
        when(input.jump()).thenReturn(true);
        when(player.isGrounded()).thenReturn(true);
        when(player.getInventory()).thenReturn(inventory);

        handler.handleInput(player, input);
        reset(player);

        handler.handleInput(player, input);

        verify(player, never()).setVelocityY(anyInt());
        verify(player, never()).jump(anyBoolean());
    }

    @ParameterizedTest
    @EnumSource(value = State.class, names = {"JUMPING"}, mode = EnumSource.Mode.EXCLUDE)
    void shouldNotUpdateIfNotJumping(State state) {
        when(player.getState()).thenReturn(state);

        handler.update(player);

        verifyNoMoreInteractions(player);
    }

    @Test
    void shouldFallWhenDamageTaken() {
        when(player.getState()).thenReturn(State.JUMPING);
        when(player.getHealth()).thenReturn(health);
        when(health.isDamageTaken()).thenReturn(true);

        handler.update(player);

        verify(player).fastFall();
    }

    @Test
    void shouldSlowFallAfterApex() {
        JumpHandler handler = new JumpHandler(random, false, 0);

        when(player.getState()).thenReturn(State.JUMPING);
        when(player.getHealth()).thenReturn(health);

        handler.update(player);

        verify(player).slowFall();
    }

    @Test
    void shouldNotFallWhileJumping() {
        JumpHandler handler = new JumpHandler(random, false, JUMP_TICKS);

        when(player.getState()).thenReturn(State.JUMPING);
        when(player.getHealth()).thenReturn(health);

        for (int i = 0; i < JUMP_TICKS; i++) {
            handler.update(player);
        }

        verify(player, never()).fastFall();
        verify(player, never()).slowFall();
    }

    @Test
    void shouldApplyGravity() {
        JumpHandler handler = new JumpHandler(random, false, JUMP_TICKS);
        when(player.getVelocityY()).thenReturn(JUMP_POWER);
        when(player.getInventory()).thenReturn(inventory);

        assertThat(handler.getVerticalAcceleration(player)).isEqualTo(GRAVITY);
    }

    @Test
    void shouldApexEarlyWithoutBoots() {
        JumpHandler handler = new JumpHandler(random, false, JUMP_TICKS);
        when(player.getVelocityY()).thenReturn(-3);
        when(player.getInventory()).thenReturn(inventory);

        assertThat(handler.getVerticalAcceleration(player)).isEqualTo(3);
    }

    @Test
    void shouldNotApexEarlyWithBoots() {
        JumpHandler handler = new JumpHandler(random, false, JUMP_TICKS);
        when(player.getVelocityY()).thenReturn(-3);
        when(player.getInventory()).thenReturn(inventory);
        when(inventory.isEquippedWith(Inventory.Equipment.BOOTS)).thenReturn(true);

        assertThat(handler.getVerticalAcceleration(player)).isEqualTo(GRAVITY);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void shouldApex(int velocityY) {
        JumpHandler handler = new JumpHandler(random, false, JUMP_TICKS);
        when(player.getVelocityY()).thenReturn(velocityY);
        lenient().when(player.getInventory()).thenReturn(inventory);

        assertThat(handler.getVerticalAcceleration(player)).isEqualTo(-velocityY);
    }
}