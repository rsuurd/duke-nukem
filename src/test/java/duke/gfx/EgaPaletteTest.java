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
        egaPalette.update();

        assertThat(egaPalette.getColorModel().getRGB(15)).isEqualTo(0xFF000000);
    }

    @Test
    void shouldFadeIn() {
        egaPalette.fadeIn();
        egaPalette.update();

        assertThat(egaPalette.getColorModel().getRGB(1)).isEqualTo(0xFF0000AA);
    }

    @Test
    void shouldIndicatedFadedBlack() {
        egaPalette.fadeOut();

        egaPalette.update();
        assertThat(egaPalette.isFadedBack()).isFalse();
        egaPalette.update();
        assertThat(egaPalette.isFadedBack()).isFalse();
        egaPalette.update();
        assertThat(egaPalette.isFadedBack()).isFalse();
        egaPalette.update();
        assertThat(egaPalette.isFadedBack()).isTrue();
    }

    @Test
    void shouldIndicatedFadedWhite() {
        egaPalette.fadeToWhite();

        egaPalette.update();
        assertThat(egaPalette.isFadedWhite()).isFalse();
        egaPalette.update();
        assertThat(egaPalette.isFadedWhite()).isFalse();
        egaPalette.update();
        assertThat(egaPalette.isFadedWhite()).isFalse();
        egaPalette.update();
        assertThat(egaPalette.isFadedWhite()).isTrue();
    }

    @Test
    void shouldIndicateFadedIn() {
        egaPalette.fadeIn();

        egaPalette.update();
        assertThat(egaPalette.isFadedIn()).isFalse();
        egaPalette.update();
        assertThat(egaPalette.isFadedIn()).isFalse();
        egaPalette.update();
        assertThat(egaPalette.isFadedIn()).isFalse();
        egaPalette.update();
        assertThat(egaPalette.isFadedIn()).isTrue();

        assertThat(egaPalette.isFadedIn()).isTrue();
    }

    @Test
    void shouldIndicateFadedInFromWhite() {
        egaPalette.fadeFromWhite();

        egaPalette.update();
        assertThat(egaPalette.isFadedIn()).isFalse();
        egaPalette.update();
        assertThat(egaPalette.isFadedIn()).isFalse();
        egaPalette.update();
        assertThat(egaPalette.isFadedIn()).isFalse();
        egaPalette.update();
        assertThat(egaPalette.isFadedIn()).isTrue();

        assertThat(egaPalette.isFadedIn()).isTrue();
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
        egaPalette.update();
        assertThat(changed.get()).isTrue();
    }
}
