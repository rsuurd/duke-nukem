package duke.resources;

import duke.gfx.Sprite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssetManagerTest {
    @Mock
    private ResourceLoader resourceLoader;

    @Mock
    private SpriteLoader spriteLoader;

    private AssetManager assetManager;

    @BeforeEach
    void create() {
        when(resourceLoader.getSpriteLoader()).thenReturn(spriteLoader);

        assetManager = new AssetManager(resourceLoader);
    }

    @Test
    void shouldEnsureResourcesExist() {
        assetManager.load();

        verify(resourceLoader).ensureResourcesExist();
    }

    @Test
    void shouldLoadTiles() {
        assetManager.load();

        verify(spriteLoader, times(4)).readTiles(startsWith("BACK"), eq(true));
        verify(spriteLoader, times(4)).readTiles(startsWith("SOLID"), eq(true));

        assertThat(assetManager.getTiles()).isNotNull();
    }

    @Test
    void shouldNotLoadTilesMultipleTimes() {
        when(spriteLoader.readTiles(anyString(), anyBoolean())).thenReturn(List.of(new Sprite(0 ,0)));

        assetManager.load();

        reset(spriteLoader);

        assetManager.load();

        verifyNoInteractions(spriteLoader);
    }

    @Test
    void shouldProvideImage() {
        assetManager.getImage("image");

        verify(spriteLoader).readImage("image.DN1");
    }

    @Test
    void shouldCacheImage() {
        when(spriteLoader.readImage(any())).thenReturn(new Sprite(0 ,0));

        assetManager.getImage("image");
        assetManager.getImage("image");

        verify(spriteLoader, times(1)).readImage("image.DN1");
    }
}
