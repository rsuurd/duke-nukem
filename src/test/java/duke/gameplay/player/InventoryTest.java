package duke.gameplay.player;

import duke.gameplay.active.items.Key;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

class InventoryTest {
    @ParameterizedTest
    @EnumSource(Key.Type.class)
    void shouldAddKey(Key.Type key) {
        Inventory inventory = new Inventory();

        assertThat(inventory.hasKey(key)).isFalse();
        assertThat(inventory.useKey(key)).isFalse();

        inventory.addKey(key);

        assertThat(inventory.hasKey(key)).isTrue();
        assertThat(inventory.useKey(key)).isTrue();

        for (Key.Type otherKey : Key.Type.values()) {
            if (otherKey != key) {
                assertThat(inventory.hasKey(otherKey)).isFalse();
                assertThat(inventory.useKey(otherKey)).isFalse();
            }
        }
    }

    @ParameterizedTest
    @EnumSource(Key.Type.class)
    void shouldConsumeKeyOnUse(Key.Type key) {
        Inventory inventory = new Inventory();
        inventory.addKey(key);
        boolean used = inventory.useKey(key);

        assertThat(used).isTrue();
        assertThat(inventory.hasKey(key)).isFalse();
        assertThat(inventory.useKey(key)).isFalse();
    }
}