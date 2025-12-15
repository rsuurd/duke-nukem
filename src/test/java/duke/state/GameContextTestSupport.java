package duke.state;

import duke.GameContext;
import duke.Renderer;
import duke.gfx.EgaPalette;
import duke.resources.AssetManager;
import duke.ui.KeyHandler;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
abstract class GameContextTestSupport {
    @Mock
    protected AssetManager assets;

    @Mock
    protected Renderer renderer;

    @Mock
    protected EgaPalette palette;

    @Mock
    protected KeyHandler keyHandler;

    @InjectMocks
    protected GameContext context;
}
