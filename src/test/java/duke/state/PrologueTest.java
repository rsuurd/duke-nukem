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
class PrologueTest {
    @Mock
    private StoryboardHandler storyboardHandler;

    private Prologue prologue;

    private GameSystems systems;

    @BeforeEach
    void createPrologue() {
        prologue = new Prologue(storyboardHandler);

        systems = GameSystemsFixture.create();
    }

    @Test
    void shouldDelegateToStoryboard() {
        prologue.update(systems);
        verify(storyboardHandler).update(systems);

        prologue.render(systems);
        verify(storyboardHandler).render(systems);
    }
}