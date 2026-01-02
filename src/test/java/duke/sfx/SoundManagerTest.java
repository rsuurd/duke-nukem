package duke.sfx;

import duke.resources.AssetManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SoundManagerTest {
    @Mock
    private AssetManager assets;

    @Mock
    private SourceDataLine line;

    private SoundManager manager;

    @BeforeEach
    void createManager() {
        manager = new SoundManager(assets, Runnable::run, line);
    }

    @Test
    void shouldOpenLine() throws LineUnavailableException {
        manager.init();

        verify(line).open();
        verify(line).start();
    }

    @Test
    void shouldToggle() {
        manager.toggle();
        assertThat(manager.isEnabled()).isFalse();

        manager.toggle();
        assertThat(manager.isEnabled()).isTrue();
    }

    @Test
    void shouldCloseLine() {
        manager.shutdown();

        verify(line).flush();
        verify(line).close();
    }

    @Test
    void shouldPlaySound() {
        when(assets.getSounds()).thenReturn(List.of(new Sound(1, new byte[]{0, 1, 2})));

        manager.play(0);

        verify(line).write(any(), anyInt(), anyInt());
    }

    @Test
    void shouldNotPlaySoundWhenDisabled() {
        manager.toggle();

        when(assets.getSounds()).thenReturn(List.of(new Sound(1, new byte[]{0, 1, 2})));

        manager.play(0);

        verify(line, never()).write(any(), anyInt(), anyInt());
    }

    @Test
    void shouldNotPlayLowerPrioritySound() {
        manager.setCurrentPriority(2);

        when(assets.getSounds()).thenReturn(List.of(new Sound(1, new byte[]{0, 1, 2})));

        manager.play(0);

        verify(line, never()).write(any(), anyInt(), anyInt());
    }
}
