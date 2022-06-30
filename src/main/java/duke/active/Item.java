package duke.active;

import duke.Assets;
import duke.GameState;
import duke.Renderer;
import duke.effects.Effect;

public abstract class Item extends Active {
    protected int frame;

    protected int tileIndex;

    protected int points;

    public Item(int x, int y, int tileIndex, int points) {
        super(x, y);

        this.tileIndex = tileIndex;

        this.points = points;
    }

    @Override
    public void update(GameState state) {
        super.update(state);

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
            super(x, y, 97, 100);
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

        @Override
        public void render(Renderer renderer, Assets assets) {
            super.render(renderer, assets);

            frame = (frame + 1) % 3;
        }
    }

    public static class Radio extends Item {
        public Radio(int x, int y) {
            super(x, y, 102, 100);
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

        @Override
        public void render(Renderer renderer, Assets assets) {
            super.render(renderer, assets);

            frame = (frame + 1) % 3;
        }
    }
}
