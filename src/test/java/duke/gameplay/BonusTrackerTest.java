package duke.gameplay;

import duke.gameplay.active.Acme;
import duke.gameplay.active.SecurityCamera;
import duke.level.Level;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BonusTrackerTest {
    private BonusTracker bonusTracker;

    @BeforeEach
    void createBonusTracker() {
        bonusTracker = new BonusTracker();
    }

    @Test
    void shouldTrackHealthLost() {
        assertThat(bonusTracker.isEarned(BonusTracker.Type.HEALTH)).isTrue();

        bonusTracker.damageTaken();

        assertThat(bonusTracker.isEarned(BonusTracker.Type.HEALTH)).isFalse();
    }

    @Test
    void shouldTrackCharactersInOrder() {
        BonusTracker bonusTracker = new BonusTracker();

        assertThat(bonusTracker.trackDUKE('D')).isFalse();
        assertThat(bonusTracker.trackDUKE('U')).isFalse();
        assertThat(bonusTracker.trackDUKE('K')).isFalse();
        assertThat(bonusTracker.trackDUKE('E')).isTrue();
    }

    @Test
    void shouldNotAwardBonusOutOfOrder() {
        BonusTracker bonusTracker = new BonusTracker();

        assertThat(bonusTracker.trackDUKE('E')).isFalse();
        assertThat(bonusTracker.trackDUKE('K')).isFalse();
        assertThat(bonusTracker.trackDUKE('U')).isFalse();
        assertThat(bonusTracker.trackDUKE('D')).isFalse();
    }

    @ParameterizedTest
    @MethodSource("destructionTypes")
    void shouldTrackDestruction(BonusTracker.Type type, Class<? extends Active> clazz) {
        Level level = mock();
        when(level.getActives()).thenReturn(List.of(mock(clazz)));

        bonusTracker.reset(level);

        assertThat(bonusTracker.isEarned(type)).isFalse();

        bonusTracker.trackDestroyed(type);

        assertThat(bonusTracker.isEarned(type)).isTrue();
    }

    @ParameterizedTest
    @MethodSource("destructionTypes")
    void shouldNotGrantDestructionBonusIfAbsent(BonusTracker.Type type) {
        Level level = mock();
        when(level.getActives()).thenReturn(List.of());

        bonusTracker.reset(level);

        assertThat(bonusTracker.isEarned(type)).isFalse();
    }

    static Stream<Arguments> destructionTypes() {
        return Stream.of(
                Arguments.of(BonusTracker.Type.CAMERAS, SecurityCamera.class),
                Arguments.of(BonusTracker.Type.ACME, Acme.class)
        );
    }
}