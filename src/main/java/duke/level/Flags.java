package duke.level;

public enum Flags {
    SOLID,
    CLINGABLE;

    public int bit() {
        return 1 << ordinal();
    }

    public boolean isSet(int flags) { return (flags & bit()) != 0; }
}
