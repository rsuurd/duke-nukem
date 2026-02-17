package duke.level;

public record LevelDescriptor(int number, int backdrop, int secondaryBackdrop, String message) {
    public LevelDescriptor(int number, int backdrop) {
        this(number, backdrop, null);
    }

    public LevelDescriptor(int number, int backdrop, String message) {
        this(number, backdrop, backdrop, message);
    }

    public boolean isHallway() {
        return number == 2;
    }
}
