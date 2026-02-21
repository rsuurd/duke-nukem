package duke.state.storyboard;

import duke.dialog.Dialog;
import duke.gfx.Sprite;
import duke.sfx.Sfx;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class PanelTest {
    @Test
    void shouldCreateEmptyPanel() {
        Panel panel = new Panel(null, null, null);

        assertThat(panel.getImage()).isNull();
        assertThat(panel.getSfx()).isNull();
        assertThat(panel.getDialog()).isNull();
    }

    @Test
    void shouldCreatePanelWithDialog() {
        Sprite image = mock();
        Dialog dialog = mock();

        Panel panel = new Panel(image, dialog);

        assertThat(panel.getImage()).isSameAs(image);
        assertThat(panel.getSfx()).isNull();
        assertThat(panel.getDialog()).isEqualTo(dialog);
    }

    @Test
    void shouldCreatePanelWithSfxAndDialog() {
        Dialog dialog = mock();

        Panel panel = new Panel(Sfx.BOX_EXPLODE, dialog);

        assertThat(panel.getImage()).isNull();
        assertThat(panel.getSfx()).isEqualTo(Sfx.BOX_EXPLODE);
        assertThat(panel.getDialog()).isEqualTo(dialog);
    }
}