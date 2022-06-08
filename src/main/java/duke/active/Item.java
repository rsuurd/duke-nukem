package duke.active;

import duke.Assets;
import duke.GameState;
import duke.Renderer;

import static duke.Gfx.TILE_SIZE;

abstract class Item extends Active {
    private static final int SIZE = TILE_SIZE - 1;

    protected int frame;
    private boolean animated;
    private int tileIndex;

    public Item(int x, int y, int tileIndex) {
        this(x, y, tileIndex, false);
    }

    public Item(int x, int y, int tileIndex, boolean animated) {
        super(x, y);

        this.tileIndex = tileIndex;
        this.animated = animated;
    }

    @Override
    public void update(GameState state) {
        if (!state.getLevel().collides(x, y + 8, SIZE, SIZE)) {
            y += 8;
        }

        if (animated) {
            frame = (frame + 1) % 3;
        }

        if (state.getDuke().collidesWith(x, y, SIZE, SIZE)) {
            pickedUp(state);

            active = false;
        }
    }

    protected void pickedUp(GameState state) {
        state.increaseScore(100);
    }

    @Override
    public void render(Renderer renderer, Assets assets) {
        renderer.drawTile(assets.getObject(tileIndex + frame), x, y);
    }

    public static class Football extends Item {
        public Football(int x, int y) {
            super(x, y, 58);
        }
    }

    public static class Drumstick extends Item {
        public Drumstick(int x, int y) {
            super(x, y, 44);
        }
    }

    public static class Soda extends Item {
        public Soda(int x, int y) {
            super(x, y, 132, true);
        }

        @Override
        public void render(Renderer renderer, Assets assets) {
            // TODO fix animation
            renderer.drawTile(assets.getAnim(132 + frame), x, y); // 135
        }
    }

    public static class Floppy extends Item {
        public Floppy(int x, int y) {
            super(x, y, 60);
        }
    }

    public static class Joystick extends Item {
        public Joystick(int x, int y) {
            super(x, y, 59);
        }
    }

    public static class Flag extends Item {
        public Flag(int x, int y) {
            super(x, y, 97, true);
        }
    }

    public static class Radio extends Item {
        public Radio(int x, int y) {
            super(x, y, 102, true);
        }
    }
}
