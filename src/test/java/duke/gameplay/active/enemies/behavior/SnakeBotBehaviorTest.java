package duke.gameplay.active.enemies.behavior;

import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.gameplay.active.enemies.SnakeBot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static duke.gameplay.active.enemies.behavior.SnakeBotBehavior.PATROL_TIME;
import static duke.gameplay.active.enemies.behavior.SnakeBotBehavior.WAIT_TIME;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SnakeBotBehaviorTest {
    private GameplayContext context;

    @Mock
    private SnakeBot snakeBot;

    @BeforeEach
    void create() {
        context = GameplayContextFixture.create();
    }

    @Test
    void shouldWait() {
        SnakeBotBehavior behavior = new SnakeBotBehavior(1);

        behavior.behave(context, snakeBot);

        verifyNoInteractions(snakeBot);
    }

    @Test
    void shouldSnakeAround() {
        SnakeBotBehavior behavior = new SnakeBotBehavior(0);

        behavior.behave(context, snakeBot);

        verify(snakeBot).setX(anyInt());
        verify(snakeBot).setY(anyInt());
    }

    @Test
    void shouldPauseAfterSnakingAround() {
        SnakeBotBehavior behavior = new SnakeBotBehavior(0, PATROL_TIME);

        for (int i = 0; i < PATROL_TIME; i++) {
            behavior.behave(context, snakeBot);
        }

        reset(snakeBot);

        for (int i = 2; i < WAIT_TIME; i++) {
            behavior.behave(context, snakeBot);
            verifyNoInteractions(snakeBot);
        }
    }
}