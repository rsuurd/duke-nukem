package duke.level;

public record LevelDescriptor(int number, int backdrop, String hint) {
    LevelDescriptor(int number, int backdrop) {
        this(number, backdrop, null);
    }

    public boolean isIntermission() {
        return number == 2;
    }
}