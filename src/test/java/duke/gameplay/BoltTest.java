package duke.gameplay;

import duke.gameplay.effects.Effect;
import duke.gameplay.player.Player;
import duke.level.Level;
import duke.sfx.Sfx;
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
        Player player = mock();
        when(player.getFacing()).thenReturn(facing);
        when(player.getX()).thenReturn(192);
        when(player.getY()).thenReturn(192);

        Bolt bolt = Bolt.create(player);

        assertThat(bolt.isActivated()).isTrue();
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
    void shouldCollideWithSolidTiles() {
        for (int tileId = Level.SOLIDS; tileId < ACTIVE; tileId++) {
            GameplayContext context = GameplayContextFixture.create();

            Bolt bolt = new Bolt(0, 0, Facing.RIGHT);
            when(context.getLevel().getTile(anyInt(), anyInt())).thenReturn(tileId);

            bolt.update(context);

            assertThat(bolt.isDestroyed()).isTrue();
            verify(context.getActiveManager()).spawn(any(Effect.class));
        }
    }

    @Test
    void shouldCollideWithDestructibleBricks() {
        GameplayContext context = GameplayContextFixture.create();

        Bolt bolt = new Bolt(0, 0, Facing.RIGHT);
        when(context.getLevel().getTile(anyInt(), anyInt())).thenReturn(DESTRUCTIBLE_BRICKS_TILE_ID);

        bolt.update(context);

        assertThat(bolt.isDestroyed()).isTrue();
        verify(context.getActiveManager()).spawn(any(Effect.class));
        verify(context.getLevel()).setTile(0, 1, DESTROYED_BRICKS_TILE_ID);
        verify(context.getScoreManager()).score(10);
        verify(context.getSoundManager()).play(Sfx.HIT_A_BREAKER);
    }

    @Test
    void shouldHitShootable() {
        GameplayContext context = GameplayContextFixture.create();
        TestShootable shootable = mock();
        Bolt bolt = new Bolt(0, 0, Facing.RIGHT);
        when(context.getActiveManager().getActives()).thenReturn(List.of(shootable));
        when(shootable.isActivated()).thenReturn(true);
        when(shootable.intersects(bolt)).thenReturn(true);

        bolt.update(context);

        assertThat(bolt.isDestroyed()).isTrue();
        verify(shootable).onShot(context, bolt);
    }

    @Test
    void shouldNotHitInactiveShootable() {
        GameplayContext context = GameplayContextFixture.create();
        TestShootable shootable = mock();
        Bolt bolt = new Bolt(0, 0, Facing.RIGHT);
        when(context.getActiveManager().getActives()).thenReturn(List.of(shootable));
        when(shootable.isActivated()).thenReturn(false);

        bolt.update(context);

        assertThat(bolt.isDestroyed()).isFalse();
        verify(shootable, never()).intersects(any(Active.class));
        verify(shootable, never()).onShot(any(), any());
    }

    @Test
    void shouldNotHitDestroyedShootable() {
        GameplayContext context = GameplayContextFixture.create();
        TestShootable shootable = mock();
        Bolt bolt = new Bolt(0, 0, Facing.RIGHT);
        when(context.getActiveManager().getActives()).thenReturn(List.of(shootable));
        when(shootable.isActivated()).thenReturn(true);
        when(shootable.isDestroyed()).thenReturn(true);

        bolt.update(context);

        assertThat(bolt.isDestroyed()).isFalse();
        verify(shootable, never()).intersects(any(Active.class));
        verify(shootable, never()).onShot(any(), any());
    }

    @Test
    void shouldHitSolidActives() {
        GameplayContext context = GameplayContextFixture.create();
        TestSolid solid = mock();
        when(solid.isSolid()).thenReturn(true);
        Bolt bolt = new Bolt(0, 0, Facing.RIGHT);
        when(context.getActiveManager().getActives()).thenReturn(List.of(solid));
        when(solid.isActivated()).thenReturn(true);
        when(solid.intersects(bolt)).thenReturn(true);

        bolt.update(context);

        assertThat(bolt.isDestroyed()).isTrue();
        verify(context.getActiveManager()).spawn(isA(Effect.class));
    }

    @Test
    void shouldNotHitNonSolidActives() {
        GameplayContext context = GameplayContextFixture.create();
        TestSolid solid = mock();
        when(solid.isSolid()).thenReturn(false);
        Bolt bolt = new Bolt(0, 0, Facing.RIGHT);
        when(context.getActiveManager().getActives()).thenReturn(List.of(solid));
        when(solid.isActivated()).thenReturn(true);
        when(solid.intersects(bolt)).thenReturn(true);

        bolt.update(context);

        assertThat(bolt.isDestroyed()).isFalse();
    }

    private abstract static class TestShootable extends Active implements Shootable {
        private TestShootable() {
            super(0, 0, 16, 16);
        }
    }

    private abstract static class TestSolid extends Active implements Solid {
        private TestSolid() {
            super(0, 0, 16, 16);
        }
    }
}
