package duke.resources;

import duke.gfx.Sprite;
import duke.level.Level;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssetManagerTest {
    @Mock(strictness = Mock.Strictness.LENIENT)
    private ResourceLoader resourceLoader;

    @Mock(strictness = Mock.Strictness.LENIENT)
    private SpriteLoader spriteLoader;

    @Mock
    private LevelLoader levelLoader;

    private AssetManager assetManager;

    @BeforeEach
    void create() {
        when(resourceLoader.getSpriteLoader()).thenReturn(spriteLoader);
        when(resourceLoader.getLevelLoader()).thenReturn(levelLoader);

        assetManager = new AssetManager(resourceLoader);
    }

    @Test
    void shouldEnsureResourcesExist() {
        assetManager.load();

        verify(resourceLoader).ensureResourcesExist();
    }

    @Test
    void shouldLoadTiles() {
        when(spriteLoader.readTiles(anyString(), anyBoolean())).thenReturn(List.of(new Sprite(0, 0)));

        assetManager.load();

        verify(spriteLoader, times(25)).readTiles(anyString(), anyBoolean());

        assertThat(assetManager.getTiles()).isNotEmpty();
        assertThat(assetManager.getMan()).isNotEmpty();
        assertThat(assetManager.getFont()).isNotEmpty();
        assertThat(assetManager.getAnim()).isNotEmpty();
        assertThat(assetManager.getObjects()).isNotEmpty();
        assertThat(assetManager.getBorder()).isNotEmpty();
        assertThat(assetManager.getNumbers()).isNotEmpty();
    }

    @Test
    void shouldNotLoadTilesMultipleTimes() {
        when(spriteLoader.readTiles(anyString(), anyBoolean())).thenReturn(List.of(new Sprite(0, 0)));

        assetManager.load();

        reset(spriteLoader);

        assetManager.load();

        verifyNoInteractions(spriteLoader);
    }

    @Test
    void shouldGetAndCacheImage() {
        when(spriteLoader.readImage(any())).thenReturn(new Sprite(0, 0));

        assetManager.getImage("image");
        assetManager.getImage("image");

        verify(spriteLoader, times(1)).readImage("image.DN1");
    }

    @Test
    void shouldGetAndCacheBackdrop() {
        when(spriteLoader.readBackdrop(anyInt())).thenReturn(new Sprite(0, 0));

        assetManager.getBackdrop(0);
        assetManager.getBackdrop(0);

        verify(spriteLoader, times(1)).readBackdrop(0);
    }

    @Test
    void shouldLoadLevel() {
        when(levelLoader.readLevel(anyInt())).thenReturn(new int[11520]);

        Level level = assetManager.getLevel(1);

        assertThat(level).isNotNull();
        verify(levelLoader).readLevel(1);
    }
}
