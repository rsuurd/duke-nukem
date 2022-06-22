package duke.active;

import duke.Assets;
import duke.GameState;
import duke.Renderer;
import duke.effects.Effect;

abstract class Item extends Active {
    protected int frame;
    private boolean animated;
    private int tileIndex;

    protected int points;

    public Item(int x, int y, int tileIndex, int points) {
        this(x, y, tileIndex, false, points);
    }

    public Item(int x, int y, int tileIndex, boolean animated, int points) {
        super(x, y);

        this.tileIndex = tileIndex;
        this.animated = animated;

        this.points = points;
    }

    @Override
    public void update(GameState state) {
        super.update(state);

        if (animated) {
            frame = (frame + 1) % 3;
        }

        if (state.getDuke().collidesWith(this)) {
            pickedUp(state);

            active = false;
        }
    }

    protected void pickedUp(GameState state) {
        state.increaseScore(points);
        state.addEffect(new Effect.Score(x, y, points));
    }

    @Override
    public void render(Renderer renderer, Assets assets) {
        renderer.drawTile(assets.getObject(tileIndex + frame), x, y);
    }

    public static class Football extends Item {
        public Football(int x, int y) {
            super(x, y, 58, 100);
        }
    }

    public static class Drumstick extends Item {
        public Drumstick(int x, int y) {
            super(x, y, 44, 100);
        }
    }

    public static class Soda extends Item {
        public Soda(int x, int y) {
            super(x, y, 132, true, 200);
        }

        @Override
        public void render(Renderer renderer, Assets assets) {
            // TODO fix animation
            renderer.drawTile(assets.getAnim(132 + frame), x, y); // 135
        }
    }

    public static class Floppy extends Item {
        public Floppy(int x, int y) {
            super(x, y, 60, 5000);
        }
    }

    public static class Joystick extends Item {
        public Joystick(int x, int y) {
            super(x, y, 59, 2000);
        }
    }

    public static class Flag extends Item {
        public Flag(int x, int y) {
            super(x, y, 97, true, 100);
        }

        @Override
        protected void pickedUp(GameState state) {
            points = switch (frame) {
                case 1 -> 2000;
                case 2 -> 5000;
                default -> 100;
            };

            super.pickedUp(state);
        }
    }

    public static class Radio extends Item {
        public Radio(int x, int y) {
            super(x, y, 102, true, 100);
        }

        @Override
        protected void pickedUp(GameState state) {
            points = switch (frame) {
                case 1 -> 2000;
                case 2 -> 5000;
                default -> 100;
            };

            super.pickedUp(state);
        }
    }
}
