package duke.gameplay;

public class BonusTracker {
    private StringBuilder characters;

    public BonusTracker() {
        this.characters = new StringBuilder();
    }

    public boolean trackDUKE(char c) {
        characters.append(c);

        return characters.toString().equals("DUKE");
    }
}
