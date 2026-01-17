package duke.gameplay;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BonusTrackerTest {
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
}