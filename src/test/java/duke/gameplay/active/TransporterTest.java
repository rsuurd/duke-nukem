package duke.gameplay.active;

import duke.dialog.Hints;
import duke.gameplay.GameplayContext;
import duke.gameplay.GameplayContextFixture;
import duke.gameplay.effects.BlinkingEffect;
import duke.sfx.Sfx;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

class TransporterTest {
    private Transporter transporter;

    private GameplayContext context;

    @BeforeEach
    void createContext() {
        transporter = new Transporter(0, 0);
        context = GameplayContextFixture.create();
    }

    @Test
    void shouldIndicateInteraction() {
        when(context.getPlayer().intersects(transporter)).thenReturn(true);

        assertThat(transporter.canInteract(context.getPlayer())).isTrue();
    }

    @Test
    void shouldNotBeInteractableWhileTransporting() {
        when(context.getPlayer().intersects(transporter)).thenReturn(true);

        transporter.transport(context);

        assertThat(transporter.canInteract(context.getPlayer())).isFalse();

        while (!transporter.canInteract(context.getPlayer())) {
            transporter.update(context);
        }

        assertThat(transporter.canInteract(context.getPlayer())).isTrue();
    }

    @Test
    void shouldShowHintWhenInteractable() {
        when(context.getPlayer().intersects(transporter)).thenReturn(true);

        transporter.update(context);

        verify(context.getHints()).showHint(Hints.Hint.TRANSPORTER, context);
    }

    @Test
    void shouldTransportPlayer() {
        Transporter destination = new Transporter(100, 100);
        when(context.getActiveManager().getActives()).thenReturn(List.of(transporter, destination));

        transporter.interactRequested(context);

        verify(context.getPlayer()).setX(100);
        verify(context.getPlayer()).setY(100);
        verify(context.getSoundManager()).play(Sfx.TELEPORT);
        verify(context.getActiveManager(), times(2)).spawn(isA(BlinkingEffect.class));
        verify(context.getViewportManager()).snapToCenter();
    }
}