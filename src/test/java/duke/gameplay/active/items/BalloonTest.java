package duke.gameplay.active.items;

import duke.gameplay.Collidable;
import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.gameplay.effects.PoppedBalloon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

class BalloonTest {
    private GameplayContext context;

    @BeforeEach
    void createContext() {
        context = GameplayContextFixture.create();
    }

    @Test
    void shouldFloatUp() {
        Balloon balloon = new Balloon(0, 0);

        assertThat(balloon.getVelocityY()).isEqualTo(-1);
    }

    @Test
    void shouldPopOnCollision() {
        Balloon balloon = new Balloon(0, 0);

        balloon.onCollision(Collidable.Direction.UP);
        balloon.update(context);

        assertThat(balloon.isDestroyed()).isTrue();
        verify(context.getActiveManager()).spawn(isA(PoppedBalloon.class));
    }

    @Test
    void shouldPopOnShot() {
        Balloon balloon = new Balloon(0, 0);

        balloon.onShot(context, mock());
        balloon.update(context);

        assertThat(balloon.isDestroyed()).isTrue();
        verify(context.getActiveManager()).spawn(isA(PoppedBalloon.class));
    }

    @Test
    void shouldNotBePickedUpWhenPopped() {
        BonusItemBehavior behavior = mock();
        Balloon balloon = new Balloon(0, 0, behavior);
        when(context.getPlayer().intersects(balloon)).thenReturn(true);

        balloon.onCollision(Collidable.Direction.UP);
        balloon.update(context);

        verifyNoInteractions(behavior);
    }
}