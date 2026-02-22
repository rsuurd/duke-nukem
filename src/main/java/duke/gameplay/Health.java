package duke.gameplay;

public class Health {
    private int current;
    private int max;

    public Health(int max) {
        this(max, max);
    }

    Health(int current, int max) {
        this.current = current;
        this.max = max;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = Math.min(current, max);
    }

    public int getMax() {
        return max;
    }

    public boolean isDead() {
        return current <= 0;
    }

    public void takeDamage(int amount) {
        current = Math.max(current - amount, 0);
    }

    public void increaseHealth(int amount) {
        setCurrent(getCurrent() + amount);
    }
}
