package duke.gameplay;

import duke.gameplay.effects.Effect;
import duke.level.Level;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;

import static duke.gameplay.Bolt.DESTROYED_BRICKS_TILE_ID;
import static duke.gameplay.Bolt.DESTRUCTIBLE_BRICKS_TILE_ID;
import static duke.level.Level.ACTIVE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class BoltTest {
    @ParameterizedTest
    @EnumSource(Facing.class)
    void shouldCreate(Facing facing) {
        Player player = new Player(Player.State.STANDING, facing);
        player.setX(192);
        player.setY(192);

        Bolt bolt = Bolt.create(player);

        assertThat(bolt.getX()).isEqualTo(192);
        assertThat(bolt.getY()).isEqualTo(205);
        assertThat(bolt.getVelocityX()).isEqualTo(Bolt.SPEED * ((facing == Facing.RIGHT) ? 1 : -1));
    }

    @Test
    void shouldUpdate() {
        Bolt bolt = new Bolt(0, 0, Facing.RIGHT);

        bolt.update(GameplayContextFixture.create());

        assertThat(bolt.getX()).isEqualTo(16);
    }

    @Test
    void shouldDespawnWhenFarAway() {
        GameplayContext context = GameplayContextFixture.create();
        Bolt bolt = new Bolt(0, 0, Facing.RIGHT);
        when(context.getPlayer().getX()).thenReturn(200);

        bolt.update(context);

        assertThat(bolt.isActive()).isFalse();
    }

    @Test
    void shouldCollideWithSolids() {
        for (int tileId = Level.SOLIDS; tileId < ACTIVE; tileId++) {
            GameplayContext context = GameplayContextFixture.create();

            Bolt bolt = new Bolt(0, 0, Facing.RIGHT);
            when(context.getLevel().getTile(anyInt(), anyInt())).thenReturn(tileId);

            bolt.update(context);

            assertThat(bolt.isActive()).isFalse();
            verify(context.getActiveManager()).spawn(any(Effect.class));
        }
    }

    @Test
    void shouldCollideWithDestructibleBricks() {
        GameplayContext context = GameplayContextFixture.create();

        Bolt bolt = new Bolt(0, 0, Facing.RIGHT);
        when(context.getLevel().getTile(anyInt(), anyInt())).thenReturn(DESTRUCTIBLE_BRICKS_TILE_ID);

        bolt.update(context);

        assertThat(bolt.isActive()).isFalse();
        verify(context.getActiveManager()).spawn(any(Effect.class));
        verify(context.getLevel()).setTile(0, 1, DESTROYED_BRICKS_TILE_ID);
        verify(context.getScoreManager()).score(10);
    }

    @Test
    void shouldHitShootable() {
        GameplayContext context = GameplayContextFixture.create();
        TestShootable shootable = mock();
        Bolt bolt = new Bolt(0, 0, Facing.RIGHT);
        when(context.getActiveManager().getActives()).thenReturn(List.of(shootable));
        when(shootable.isActive()).thenReturn(true);
        when(shootable.intersects(bolt)).thenReturn(true);

        bolt.update(context);

        assertThat(bolt.isActive()).isFalse();
        verify(shootable).onShot(context, bolt);
    }

    @Test
    void shouldNotHitInactiveShootable() {
        GameplayContext context = GameplayContextFixture.create();
        TestShootable shootable = mock();
        Bolt bolt = new Bolt(0, 0, Facing.RIGHT);
        when(context.getActiveManager().getActives()).thenReturn(List.of(shootable));
        when(shootable.isActive()).thenReturn(false);

        bolt.update(context);

        assertThat(bolt.isActive()).isTrue();
        verify(shootable, never()).intersects(any());
        verify(shootable, never()).onShot(any(), any());
    }

    @Test
    void shouldNotCheckNonShootables() {
        GameplayContext context = GameplayContextFixture.create();
        Active active = mock();
        Bolt bolt = new Bolt(0, 0, Facing.RIGHT);
        when(context.getActiveManager().getActives()).thenReturn(List.of(active));
        when(active.isActive()).thenReturn(true);

        bolt.update(context);

        assertThat(bolt.isActive()).isTrue();
        verify(active, never()).intersects(any());
    }

    private abstract static class TestShootable extends Active implements Shootable {
        private TestShootable() {
            super(0, 0, 16, 16);
        }
    }
}
