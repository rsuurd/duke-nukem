package duke.state;

import duke.GameSystems;
import duke.dialog.Dialog;
import duke.gameplay.Cheats;
import duke.gfx.Sprite;
import duke.state.storyboard.Panel;
import duke.state.storyboard.Storyboard;
import duke.state.storyboard.StoryboardHandler;

import java.util.List;

public class Prologue implements GameState {
    private StoryboardHandler storyboardHandler;

    public Prologue() {
    }

    Prologue(StoryboardHandler storyboardHandler) {
        this.storyboardHandler = storyboardHandler;
    }

    @Override
    public void start(GameSystems systems) {
        Storyboard storyboard = createStoryboard(systems);
        GameplayState next = new GameplayState(systems, new Cheats(true));

        storyboardHandler = new StoryboardHandler(storyboard, next, StateRequester.Transition.FADE_TO_WHITE);
    }

    @Override
    public void update(GameSystems systems) {
        storyboardHandler.update(systems);
    }

    @Override
    public void render(GameSystems systems) {
        storyboardHandler.render(systems);
    }

    private Storyboard createStoryboard(GameSystems systems) {
        Sprite badGuy = systems.getAssets().getImage("BADGUY");
        Sprite duke = systems.getAssets().getImage("DUKE");

        return new Storyboard(List.of(
                new Panel(badGuy, new Dialog("So you're the pitiful\nhero they sent to stop\nme.  I, Dr. Proton, will\nsoon rule the world!", 16, 144, 3, 13, true, false)),
                new Panel(duke, new Dialog("You're wrong, Proton\nbreath.  I'll be done\nwith you and still have\ntime to watch Oprah!", 112, 144, 3, 13, true, false))
        ));
    }
}
