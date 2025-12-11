package duke.gfx;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;

class EgaPaletteTest {
    private EgaPalette egaPalette = new EgaPalette();

    @Test
    void shouldBlackout() {
        egaPalette.blackout();

        for (int i = 0; i < 16; i++) {
            assertThat(egaPalette.getColorModel().getRGB(0)).isEqualTo(0xFF000000);
        }
    }

    @Test
    void shouldReset() {
        egaPalette.blackout();
        egaPalette.reset();

        assertThat(egaPalette.getColorModel().getRGB(15)).isEqualTo(0xFFFFFFFF);
    }

    @Test
    void shouldFadeOut() {
        egaPalette.fadeOut();

        assertThat(egaPalette.getColorModel().getRGB(15)).isEqualTo(0xFFAAAAAA);
    }

    @Test
    void shouldFadeIn() {
        egaPalette.blackout();
        egaPalette.fadeIn();

        assertThat(egaPalette.getColorModel().getRGB(15)).isEqualTo(0xFFAAAAAA);
    }

    @Test
    void shouldInformListenersOnBlackout() {
        testListener(egaPalette::blackout);
    }

    @Test
    void shouldInformListenersOnReset() {
        testListener(egaPalette::reset);
    }

    @Test
    void shouldInformListenersOnFadeIn() {
        egaPalette.blackout();
        testListener(egaPalette::fadeIn);
    }

    @Test
    void shouldInformListenersOnFadeOut() {
        testListener(egaPalette::fadeOut);
    }

    private void testListener(Runnable operation) {
        AtomicBoolean changed = new AtomicBoolean(false);
        egaPalette.addListener(() -> changed.set(true));
        operation.run();

        assertThat(changed.get()).isTrue();
    }
}
