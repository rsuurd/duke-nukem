package duke.active;

public class ActiveFactory {
    public static Active create(int tileId, int x, int y) {
        return switch (tileId) {
            case 0x3024 -> new Camera(x, y);
            case 0x302A -> new Acme(x, y);
            case 0x3000, 0x3006, 0x3008, 0x300F, 0x3012, 0x3020, 0x3029, 0x3033, 0x3037, 0x3038, 0x3039, 0x303A -> new Box(Box.GREY, x, y);
            case 0x3015 -> new Box(Box.RED, x, y, new Item.Soda(x, y));
            case 0x3018 -> new Box(Box.RED, x, y, new Item.Drumstick(x, y));
            case 0x301D -> new Box(Box.BLUE, x, y, new Item.Football(x, y));
            case 0x301E -> new Box(Box.BLUE, x, y, new Item.Joystick(x, y));
            case 0x301F -> new Box(Box.BLUE, x, y, new Item.Floppy(x, y));
            case 0x3023 -> new Box(Box.BLUE, x, y, null); // Balloon
            case 0x302D -> new Box(Box.BLUE, x, y, new Item.Flag(x, y));
            case 0x302E -> new Box(Box.BLUE, x, y, new Item.Radio(x, y));
            case 0x3050 -> new Item.Football(x, y);
            case 0x3051 -> new Item.Drumstick(x, y);
            case 0x3052 -> new Item.Soda(x, y);
            case 0x3053 -> new Item.Floppy(x, y);
            case 0x3054 -> new Item.Joystick(x, y);
            case 0x3055 -> new Item.Flag(x, y);
            case 0x3056 -> new Item.Radio(x, y);
            default -> throw new IllegalArgumentException(String.format("Could not create Active for 0x%x", tileId));
        };
    }
}
