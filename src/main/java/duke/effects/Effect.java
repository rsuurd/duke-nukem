package duke.effects;

import duke.Assets;
import duke.GameState;
import duke.Renderer;

public abstract class Effect {
    protected int x;
    protected int y;

    protected int tileIndex; // TODO protected BufferedImage getTile(Assets assets, int frame) { assets.getObject(tileIndex + frame) }
    protected int timeToLive;
    protected int frame;

    public Effect(int x, int y, int tileIndex, int timeToLive) {
        this.x = x;
        this.y = y;

        this.tileIndex = tileIndex;
        this.timeToLive = timeToLive;

        frame = 0;
    }

    public void render(Renderer renderer, Assets assets) {
        renderer.drawTile(assets.getObject(tileIndex + frame), x, y);

        frame ++;
    }

    public boolean isDone() {
        return frame >= timeToLive;
    }

    public static class Sparks extends Effect {
        public Sparks(int x, int y) {
            super(x, y, 42, 6);
        }

        @Override
        public void render(Renderer renderer, Assets assets) {
            renderer.drawTile(assets.getAnim(tileIndex + frame), x, y);

            frame ++;
        }
    }

    public static class Dustcloud extends Effect {
        public Dustcloud(int x, int y) {
            super(x, y, 19, 4);
        }
    }

    public static class Smoke extends Effect {
        public Smoke(int x, int y) {
            super(x, y, 34, 5);
        }
    }

    public static class Particle extends Effect {
        private int velocityX;
        private int velocityY;

        private Particle(int x, int y, int offset) {
            super(x, y, 1 + offset, 32);

            velocityX = (int) (Math.random() * 8) - 4;
            velocityY = (int) (Math.random() * 8) - 4;
        }

        @Override
        public void render(Renderer renderer, Assets assets) {
            y += velocityY;
            x += velocityX;

            renderer.drawTile(assets.getObject(tileIndex), x, y);

            frame ++;
            velocityY++;
        }

        public static void createParticles(GameState state, int x, int y) {
            for (int offset = 0; offset < 4; offset++) {
                state.addEffect(new Particle(x, y, offset));
            }
        }
    }

    public static class Score extends Effect {
        public Score(int x, int y, int score) {
            super(x, y, -1, 32);

            tileIndex = switch (score) {
                case 100 -> 0;
                case 200 -> 2;
                case 500 -> 4;
                case 1000 -> 6;
                case 2000 -> 8;
                case 5000 -> 10;
                case 10000 -> 12;
                default -> -1;
            };
        }

        @Override
        public void render(Renderer renderer, Assets assets) {
            renderer.drawTile(assets.getNumber(tileIndex + (frame  % 2)), x, y);

            frame ++;

            y -= 2;
        }
    }
}
