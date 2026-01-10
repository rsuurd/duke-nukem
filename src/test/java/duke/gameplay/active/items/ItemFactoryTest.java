package duke.gameplay.active.items;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ItemFactoryTest {
    @Test
    void shouldCreateFootball() {
        assertThat(ItemFactory.createFootball(16, 32)).isNotNull();
    }

    @Test
    void shouldCreateFloppy() {
        assertThat(ItemFactory.createFloppy(16, 32)).isNotNull();
    }

    @Test
    void shouldCreateJoystick() {
        assertThat(ItemFactory.createJoystick(16, 32)).isNotNull();
    }

    @Test
    void shouldCreateFlag() {
        assertThat(ItemFactory.createFlag(16, 32)).isNotNull();
    }

    @Test
    void shouldCreateRadio() {
        assertThat(ItemFactory.createRadio(16, 32)).isNotNull();
    }

    @Test
    void shouldCreateSoda() {
        Item item = ItemFactory.createSoda(16, 32);

        assertThat(item).isNotNull();
        assertThat(item).isInstanceOf(Soda.class);
    }

    @Test
    void shouldCreateFizzingSoda() {
        Item item = ItemFactory.createFizzingSoda(16, 32);

        assertThat(item).isNotNull();
        assertThat(item).isInstanceOf(FizzingSoda.class);
    }

    @Test
    void shouldCreateTurkeyLeg() {
        Item item = ItemFactory.createTurkeyLeg(16, 32);

        assertThat(item).isNotNull();
        assertThat(item).isInstanceOf(TurkeyLeg.class);
    }

    @Test
    void shouldCreateTurkey() {
        Item item = ItemFactory.createTurkey(16, 32);

        assertThat(item).isNotNull();
    }
}
