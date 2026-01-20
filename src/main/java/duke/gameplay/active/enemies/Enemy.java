package duke.gameplay.active.enemies;

import duke.gameplay.*;
import duke.gameplay.active.enemies.behavior.EnemyBehavior;

public abstract class Enemy extends Active implements Updatable, Damaging {
    private Facing facing;
    private EnemyBehavior behavior;

    protected Enemy(int x, int y, int width, int height, Facing facing, EnemyBehavior behavior) {
        super(x, y, width, height);

        this.facing = facing;
        this.behavior = behavior;
    }

    @Override
    public void update(GameplayContext context) {
        behavior.behave(context, this);
    }

    public final void turnAround() {
        setFacing(facing == Facing.LEFT ? Facing.RIGHT : Facing.LEFT);
    }

    public void reverse() {
    }

    public Facing getFacing() {
        return facing;
    }

    protected void setFacing(Facing facing) {
        if (this.facing != facing) {
            onFacingChanged(facing);

            this.facing = facing;
        }
    }

    protected void onFacingChanged(Facing facing) {
    }
}
