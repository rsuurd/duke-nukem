package duke.level;

public enum Flags {
    SOLID,
    CLINGABLE;

    public int bit() {
        return 1 << ordinal();
    }

    public static boolean isSet(int flags, Flags flag) {
        return (flags & flag.bit()) != 0;
    }
}
