package duke.gameplay.active.enemies;

import duke.gameplay.BonusTracker;
import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.gameplay.active.enemies.behavior.EnemyBehavior;
import duke.gameplay.effects.Effect;
import duke.sfx.Sfx;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SnakeBotTest {
    private SnakeBot snakeBot;

    private GameplayContext context;

    @Mock
    private EnemyBehavior behavior;

    @BeforeEach
    void create() {
        snakeBot = new SnakeBot(0, 0, behavior);

        context = GameplayContextFixture.create();
    }

    @Test
    void shouldBehave() {
        snakeBot.update(context);

        verify(behavior).behave(context, snakeBot);
    }

    @Test
    void shouldBeDestroyed() {
        snakeBot.onDestroyed(context);

        verify(context.getActiveManager()).spawn(isA(Effect.class));
        verify(context.getActiveManager()).spawn(anyList());
        verify(context.getScoreManager()).score(1000);
        verify(context.getSoundManager()).play(Sfx.BOX_EXPLODE);
        verify(context.getBonusTracker()).trackDestroyed(BonusTracker.Type.SNAKE);
    }
}