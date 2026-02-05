package duke.gameplay.active;

import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.gfx.Animation;
import duke.sfx.Sfx;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ArcTest {
    private Arc arc;

    @Mock
    private Animation animation;

    private GameplayContext context;

    @BeforeEach
    void create() {
        arc = new Arc(0, 0, 32, animation);

        context = GameplayContextFixture.create();
    }

    @Test
    void shouldBeDamaging() {
        assertThat(arc.getDamage()).isGreaterThan(0);
    }

    @Test
    void shouldAnimate() {
        arc.update(context);

        verify(animation).tick();
    }

    @Test
    void shouldPlaySound() {
        arc.update(context);

        verify(context.getSoundManager()).play(Sfx.FORCE_FIELD);
    }
}