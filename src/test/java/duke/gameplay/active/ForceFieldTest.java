package duke.gameplay.active;

import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.sfx.Sfx;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

class ForceFieldTest {
    private ForceField forceField;

    private GameplayContext context;

    @BeforeEach
    void createForceField() {
        forceField = new ForceField(0, 0);

        context = GameplayContextFixture.create();
    }

    @Test
    void shouldBeSolid() {
        assertThat(forceField.isSolid()).isTrue();
    }

    @Test
    void shouldPlaySound() {
        forceField.update(context);

        verify(context.getSoundManager()).play(Sfx.FORCE_FIELD);
    }
}