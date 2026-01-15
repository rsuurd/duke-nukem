package duke.sfx;

import duke.resources.AssetManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SoundManagerTest {
    @Mock
    private AssetManager assets;

    @Mock
    private PcSpeaker speaker;

    private SoundManager manager;

    @BeforeEach
    void createManager() {
        manager = new SoundManager(assets, speaker);
    }

    @Test
    void shouldToggle() {
        manager.toggle();
        assertThat(manager.isEnabled()).isFalse();

        manager.toggle();
        assertThat(manager.isEnabled()).isTrue();
    }

    @Test
    void shouldPlaySound() {
        Sound sound = mock();
        when(assets.getSounds()).thenReturn(List.of(sound));

        manager.play(Sfx.PLAYER_DEATH);

        verify(speaker).play(sound);
    }

    @Test
    void shouldNotPlaySoundWhenDisabled() {
        Sound sound = mock();
        manager.toggle();

        when(assets.getSounds()).thenReturn(List.of(sound));

        manager.play(Sfx.PLAYER_DEATH);

        verifyNoInteractions(speaker);
    }
}
