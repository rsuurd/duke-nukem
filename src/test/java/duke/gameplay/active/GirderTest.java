package duke.gameplay.active;

import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.level.Level;
import duke.sfx.Sfx;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static duke.gameplay.active.Girder.GIRDER_BLOCK_TILE_ID;
import static duke.level.Level.SOLIDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class GirderTest {
    private Girder girder;

    private GameplayContext context;

    @BeforeEach
    void create() {
        girder = new Girder(0, 0);

        context = GameplayContextFixture.create();
    }

    @Test
    void shouldStartExtension() {
        girder.extend(context);

        verify(context.getSoundManager()).play(Sfx.BRIDGE_EXTEND);
    }

    @Test
    void shouldNotDoubleStartExtension() {
        girder.extend(context);
        girder.extend(context);

        verify(context.getSoundManager(), times(1)).play(Sfx.BRIDGE_EXTEND);
    }

    @Test
    void shouldPlaceGirderTilesWhileExtending() {
        girder.extend(context);

        when(context.getLevel().getTile(anyInt(), anyInt())).thenReturn(0x0);

        girder.update(context);

        verify(context.getLevel()).setTile(0, 1, GIRDER_BLOCK_TILE_ID);
        assertThat(girder.getWidth()).isEqualTo(2 * Level.TILE_SIZE);
    }

    @Test
    void shouldIncludeExistingGirderTilesWhileExtending() {
        girder.extend(context);

        when(context.getLevel().getTile(anyInt(), anyInt())).thenReturn(GIRDER_BLOCK_TILE_ID);

        girder.update(context);

        verify(context.getLevel()).setTile(0, 1, GIRDER_BLOCK_TILE_ID);
        assertThat(girder.getWidth()).isEqualTo(2 * Level.TILE_SIZE);
    }

    @Test
    void shouldStopExtendingWhenSolidTileIsFound() {
        girder.extend(context);

        when(context.getLevel().getTile(anyInt(), anyInt())).thenReturn(SOLIDS);

        girder.update(context);

        verify(context.getLevel(), never()).setTile(anyInt(), anyInt(), anyInt());
        assertThat(girder.getWidth()).isEqualTo(Level.TILE_SIZE);
    }
}