package duke.sounds;

public class Sound {
    private int priority;
    private byte[] data;

    public Sound(int priority, byte[] data) {
        this.priority = priority;
        this.data = data;
    }

    public int getPriority() {
        return priority;
    }

    public byte[] getData() {
        return data;
    }
}
