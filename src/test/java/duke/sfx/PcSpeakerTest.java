package duke.sfx;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.util.concurrent.ScheduledExecutorService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PcSpeakerTest {
    @Mock
    private SourceDataLine line;

    @Mock
    private ScheduledExecutorService executor;

    @InjectMocks
    private PcSpeaker speaker;

    @Test
    void shouldPlaySound() {
        Sound sound = new Sound(1, new int[]{});

        speaker.play(sound);

        assertThat(speaker.getCurrent()).isEqualTo(sound);
    }

    @Test
    void shouldNotPlayLowerPrioritySound() {
        Sound important = new Sound(2, new int[]{});
        Sound backgroundNoise = new Sound(1, new int[]{});

        speaker.play(important);
        speaker.play(backgroundNoise);

        assertThat(speaker.getCurrent()).isEqualTo(important);
    }

    @Test
    void shouldSendDataToLine() {
        speaker.play(new Sound(1, new int[]{0}));
        speaker.sendNextAudioSample();

        verify(line).write(any(), anyInt(), anyInt());
    }

    @Test
    void shouldInit() throws LineUnavailableException {
        speaker.init();

        verify(line).open();
        verify(line).start();
        verify(executor).scheduleAtFixedRate(any(), anyLong(), anyLong(), any());
    }

    @Test
    void shouldShutdown() {
        speaker.shutdown();

        verify(executor).shutdown();
        verify(line).flush();
        verify(line).close();
    }
}
