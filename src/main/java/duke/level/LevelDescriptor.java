package duke.level;

public record LevelDescriptor(
        int index,
        int number,
        int backdrop,
        int secondaryBackdrop,
        String message
) {
    public LevelDescriptor(int index, int number, int backdrop) {
        this(index, number, backdrop, null);
    }

    public LevelDescriptor(int index, int number, int backdrop, String message) {
        this(index, number, backdrop, backdrop, message);
    }

    public boolean isHallway() {
        return number == 2;
    }
}
