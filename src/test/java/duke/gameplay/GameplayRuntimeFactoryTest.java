package duke.gameplay;

import duke.GameSystems;
import duke.GameSystemsFixture;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GameplayRuntimeFactoryTest {
    @Test
    void shouldCreateGameplayRuntime() {
        GameplayRuntimeFactory factory = new GameplayRuntimeFactory();

        GameSystems systems = GameSystemsFixture.create();

        GameplayRuntimeFactory.GameplayRuntime runtime = factory.createRuntime(systems);

        assertThat(runtime).isNotNull();
        assertThat(runtime.levelManager()).isNotNull();
        assertThat(runtime.viewport()).isNotNull();
        assertThat(runtime.collision()).isNotNull();

        GameplayContext context = runtime.context();
        assertThat(context.getPlayer()).isNotNull();
        assertThat(context.getLevel()).isNull();
        assertThat(context.getBoltManager()).isNotNull();
        assertThat(context.getActiveManager()).isNotNull();
        assertThat(context.getSoundManager()).isNotNull();
        assertThat(context.getScoreManager()).isNotNull();
        assertThat(context.getBonusTracker()).isNotNull();
        assertThat(context.getDialogManager()).isNotNull();
        assertThat(context.getHints()).isNotNull();
        assertThat(context.getViewportManager()).isNotNull();
    }
}