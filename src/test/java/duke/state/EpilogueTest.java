package duke.state;

import duke.GameSystems;
import duke.GameSystemsFixture;
import duke.state.storyboard.StoryboardHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EpilogueTest {
    @Mock
    private StoryboardHandler storyboardHandler;

    private Epilogue epilogue;

    private GameSystems systems;

    @BeforeEach
    void create() {
        epilogue = new Epilogue(storyboardHandler);

        systems = GameSystemsFixture.create();
    }

    @Test
    void shouldDelegateToStoryboard() {
        epilogue.update(systems);
        verify(storyboardHandler).update(systems);

        epilogue.render(systems);
        verify(storyboardHandler).render(systems);
    }
}