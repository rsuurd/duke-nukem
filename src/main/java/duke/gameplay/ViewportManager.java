package duke.gameplay;

public class ViewportManager {
    private Movable target;
    private boolean snapToCenter;

    public ViewportManager(Movable target, boolean snapToCenter) {
        this.target = target;
        this.snapToCenter = snapToCenter;
    }

    public Movable getTarget() {
        return target;
    }

    public void setTarget(Movable target) {
        this.target = target;
    }

    public void snapToCenter() {
        this.snapToCenter = true;
    }

    public boolean pollSnapToCenter() {
        boolean value = snapToCenter;

        snapToCenter = false;

        return value;
    }


}
