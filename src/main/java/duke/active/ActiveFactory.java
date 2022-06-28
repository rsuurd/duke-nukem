package duke.active;

import duke.Facing;
import duke.active.enemies.*;

import static duke.Gfx.TILE_SIZE;

public class ActiveFactory {
    public static Active create(int tileId, int x, int y) {
        return switch (tileId) {
            case Elevator.TILE_ID -> new Elevator(x, y);
            case 0x300D -> new TankBot(x, y);
            case 0x3010 -> new Techbot(x, y);
            case 0x3024 -> new Camera(x, y);
            case 0x3000, 0x3006, 0x3008, 0x300F, 0x3020, 0x3033 -> new Box(Box.GREY, x, y);
            case 0x300c -> new Ed209(x, y);
            case 0x3009 -> new Flamethrower(x, y, Facing.RIGHT);
            case 0x300A -> new Flamethrower(x, y, Facing.LEFT);
            case 0x3012 -> new Box(Box.GREY, x, y, new Dynamite(x, y));
            case 0x3015 -> new Box(Box.RED, x, y, new Soda(x, y));
            case 0x3016 -> new WallCrawler(x, y, Facing.RIGHT);
            case 0x3017 -> new WallCrawler(x, y, Facing.LEFT);
            case 0x3018 -> new Box(Box.RED, x, y, new Chicken(x, y));
            case 0x301D -> new Box(Box.BLUE, x, y, new Item.Football(x, y));
            case 0x301E -> new Box(Box.BLUE, x, y, new Item.Joystick(x, y));
            case 0x301F -> new Box(Box.BLUE, x, y, new Item.Floppy(x, y));
            case 0x3023 -> new Box(Box.BLUE, x, y, new Balloon(x, y - TILE_SIZE));
            case 0x3029 -> new Box(Box.GREY, x, y, new NuclearMolecule(x, y));
            case 0x302A -> new Acme(x, y);
            case 0x302B -> new Reactor(x, y);
            case 0x302D -> new Box(Box.BLUE, x, y, new Item.Flag(x, y));
            case 0x302E -> new Box(Box.BLUE, x, y, new Item.Radio(x, y));
            case 0x3031 -> new BouncingMine(x, y);
            case 0x3037 -> new Box(Box.GREY, x, y, new Letter(x, y, 'D'));
            case 0x3038 -> new Box(Box.GREY, x, y, new Letter(x, y, 'U'));
            case 0x3039 -> new Box(Box.GREY, x, y, new Letter(x, y, 'K'));
            case 0x303A -> new Box(Box.GREY, x, y, new Letter(x, y, 'E'));
            case Key.RED_KEY_TILE_ID -> new Key(x, y, Key.Type.RED);
            case Key.GREEN_KEY_TILE_ID -> new Key(x, y, Key.Type.GREEN);
            case Key.BLUE_KEY_TILE_ID -> new Key(x, y, Key.Type.BLUE);
            case Key.MAGENTA_KEY_TILE_ID -> new Key(x, y, Key.Type.MAGENTA);
            case Door.RED_DOOR_TILE_ID -> new Door(x, y, Key.Type.RED);
            case Door.GREEN_DOOR_TILE_ID -> new Door(x, y, Key.Type.GREEN);
            case Door.BLUE_DOOR_TILE_ID -> new Door(x, y, Key.Type.BLUE);
            case Door.MAGENTA_DOOR_TILE_ID -> new Door(x, y, Key.Type.MAGENTA);
            case Lock.RED_LOCK_TILE_ID -> new Lock(x, y, Key.Type.RED);
            case Lock.GREEN_LOCK_TILE_ID -> new Lock(x, y, Key.Type.GREEN);
            case Lock.BLUE_LOCK_TILE_ID -> new Lock(x, y, Key.Type.BLUE);
            case Lock.MAGENTA_LOCK_TILE_ID -> new Lock(x, y, Key.Type.MAGENTA);
            case 0x3050 -> new Item.Football(x, y);
            case 0x3051 -> new Chicken(x, y);
            case 0x3052 -> new Soda(x, y);
            case 0x3053 -> new Item.Floppy(x, y);
            case 0x3054 -> new Item.Joystick(x, y);
            case 0x3055 -> new Item.Flag(x, y);
            case 0x3056 -> new Item.Radio(x, y);
            case 0x3057 -> new Mine(x, y);
            case 0x3058 -> new Spikes(x, y, true);
            case 0x3059 -> new Spikes(x, y, false);

            default -> throw new IllegalArgumentException(String.format("Could not create Active for 0x%x", tileId));
        };
    }
}
