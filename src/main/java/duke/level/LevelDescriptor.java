package duke.level;

public record LevelDescriptor(int number, int backdrop, String message) {
    LevelDescriptor(int number, int backdrop) {
        this(number, backdrop, null);
    }

    public boolean isHallway() {
        return number == 2;
    }
}
