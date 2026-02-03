package duke.gameplay.active.enemies;

import duke.gameplay.*;
import duke.gameplay.active.enemies.behavior.EnemyBehavior;

public abstract class Enemy extends Active implements Updatable, Damaging, Shootable {
    private Facing facing;
    private EnemyBehavior behavior;
    protected Health health;

    protected Enemy(int x, int y, int width, int height, Facing facing, EnemyBehavior behavior) {
        this(x, y, width, height, facing, behavior, new Health(1));
    }

    protected Enemy(int x, int y, int width, int height, Facing facing, EnemyBehavior behavior, Health health) {
        super(x, y, width, height);

        this.facing = facing;
        this.behavior = behavior;
        this.health = health;
    }

    @Override
    public void update(GameplayContext context) {
        behavior.behave(context, this);
    }

    public boolean isAbleToMove() {
        return true;
    }

    public final void turnAround() {
        setFacing(facing == Facing.LEFT ? Facing.RIGHT : Facing.LEFT);
    }

    public void reverse() {
    }

    public Facing getFacing() {
        return facing;
    }

    public void setFacing(Facing facing) {
        if (this.facing != facing) {
            onFacingChanged(facing);

            this.facing = facing;
        }
    }

    protected void onFacingChanged(Facing facing) {
    }

    public void shoot() {
    }

    public void jump() {
    }

    public boolean isGrounded() {
        return true;
    }

    @Override
    public void onShot(GameplayContext context, Bolt bolt) {
        health.takeDamage(1);

        if (health.isDead()) {
            destroy();

            onDestroyed(context);
        }
    }

    public int getHealth() {
        return health.getCurrent();
    }

    protected abstract void onDestroyed(GameplayContext context);
}
