package duke.state;

import duke.GameSystems;
import duke.dialog.Dialog;
import duke.gfx.Sprite;

import java.util.List;

public class End implements GameState {
    private Sprite background;

    private int y;
    private int timer;

    public End() {
        y = DR_PROTON_Y;
    }

    @Override
    public void start(GameSystems systems) {
        background = systems.getAssets().getImage("END");
    }

    @Override
    public void update(GameSystems systems) {
        if (isFinished()) {
            askForInput(systems);

            if (systems.getKeyHandler().consumeAny()) {
                systems.getStateRequester().requestState(new Epilogue());
            }
        } else {
            flyToTheMoon();
        }
    }

    private void askForInput(GameSystems systems) {
        if (!systems.getDialogManager().hasDialog()) {
            systems.getDialogManager().open(new Dialog("     Press any key.", 64, 0, 2, 13, false, false));
        }
    }

    private boolean isFinished() {
        return timer >= 140;
    }

    private void flyToTheMoon() {
        timer++;

        if (y > 72) {
            y--;
        }
    }

    @Override
    public void render(GameSystems systems) {
        systems.getRenderer().draw(background, 0, 0);
        systems.getDialogManager().render(systems.getRenderer());

        Sprite sprite = getSprite(systems);

        if (sprite != null) {
            systems.getRenderer().draw(sprite, DR_PROTON_X, y);
        }
    }

    private Sprite getSprite(GameSystems systems) {
        List<Sprite> objects = systems.getAssets().getObjects();

        if (timer < FLYING_UP_DURATION) {
            return objects.get(141 + (timer % 2));
        } else if (timer < FLYING_UP_DURATION + ENTERING_ATMOSPHERE_DURATION) {
            int frame = (timer % ENTERING_ATMOSPHERE_DURATION) / 2;

            return objects.get(143 + frame);
        } else {
            return null;
        }
    }

    private static final int DR_PROTON_X = 208;
    private static final int DR_PROTON_Y = 128;

    static final int FLYING_UP_DURATION = 100;
    static final int ENTERING_ATMOSPHERE_DURATION = 4;
}
