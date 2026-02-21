package duke.state;

import duke.GameSystems;
import duke.dialog.Dialog;
import duke.gfx.Sprite;
import duke.sfx.Sfx;
import duke.state.storyboard.Panel;
import duke.state.storyboard.Storyboard;
import duke.state.storyboard.StoryboardHandler;

import java.util.List;

public class Epilogue implements GameState {
    private StoryboardHandler storyboardHandler;

    public Epilogue() {
    }

    Epilogue(StoryboardHandler storyboardHandler) {
        this.storyboardHandler = storyboardHandler;
    }

    @Override
    public void start(GameSystems systems) {
        Storyboard storyboard = createStoryboard(systems);
        // next should be ordering information, then some GameOver state to input high scores, show highscores, go to title screen

        storyboardHandler = new StoryboardHandler(storyboard, new TitleScreen(), StateRequester.Transition.FADE_TO_BLACK);
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
                new Panel(badGuy, new Dialog("You may have defeated me\non earth, but on the\nMoon I can make plans\nfor my ulimate attack!", 16, 144, 3, 13, true, false)), // sic
                new Panel(duke, new Dialog("Proton you coward!  You\ncan run but you can't\nhide!", 112, 144, 3, 13, true, false)),
                new Panel(badGuy, new Dialog("Once my forces are\ntogether, I'll be back\nto rule earth.", 16, 144, 3, 13, true, false)),
                new Panel(duke, new Dialog("Not if I can stop you\nfirst...\n\n     Here I come!", 112, 144, 3, 13, true, false)),
                new Panel(Sfx.THE_END, new Dialog("""
                             TO BE CONTINUED
                        ------------------------
                        
                        In episode two, Duke
                        rockets to the moon to
                        infiltrate Dr. Proton's
                        massive lunar fortress.
                        
                        Continue your adventure
                        in "Mission: Moonbase",
                        where you'll encounter:
                        New levels, new puzzles,
                        new graphics, and more
                        non-stop action.
                        """, 56, 32, 8, 13, true, false))
        ));
    }
}
