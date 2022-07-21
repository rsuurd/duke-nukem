package duke;

import duke.active.*;
import duke.active.Item.*;
import duke.active.enemies.*;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import static duke.Gfx.TILE_SIZE;

public class Level {
    public static final int WIDTH = 128;
    public static final int HEIGHT = 90;

    private int number;

    private int[] tiles;
    private int startLocation;
    private BufferedImage backdrop;
    private List<Active> actives;

    public Level(int number, int[] tiles, BufferedImage backdrop) {
        this.number = number;

        this.tiles = tiles;
        this.backdrop = backdrop;

        init();
    }

    private void init() {
        actives = new ArrayList<>();

        for (int i = 0; i < tiles.length; i ++) {
            int tileId = tiles[i];
            int x = (i % Level.WIDTH) * TILE_SIZE;
            int y = (i / Level.WIDTH) * TILE_SIZE;

            switch (tileId) {
                case 0x1800 -> addActive(i, new Bricks(x, y), COPY_NO_TILE);
                case 0x3000 -> addActive(i, new Box(Box.GREY, x, y), COPY_LEFT_TILE);
                case 0x3002, 0x3003, 0x3004, 0x3005, 0x3006, 0x3007, 0x3008, 0x300B, 0x300F, 0x3013, 0x3014, 0x301A, 0x301B, 0x301C, 0x3020, 0x3021, 0x3022, 0x302F, 0x3030, 0x3033, 0x3034, 0x3035, 0x3036, 0x303B, 0x303C, 0x3042, 0x3043 ->
                        System.err.format("Missing tile code: 0x%x\n", tileId);
                case Elevator.TILE_ID -> addActive(i, new Elevator(x, y), COPY_NO_TILE);
                case 0x3009 -> addActive(i, new Flamethrower(x, y, Facing.RIGHT), COPY_RIGHT_TILE);
                case 0x300A -> addActive(i, new Flamethrower(x, y, Facing.LEFT), COPY_LEFT_TILE);
                case 0x300C -> addActive(i, new Ed209(x, y), COPY_LEFT_TILE);
                case 0x300D -> addActive(i, new TankBot(x, y), COPY_LEFT_TILE);
                case 0x300E -> addActive(i, new FlameWheelBot(x, y), COPY_LEFT_TILE);
                case 0x3010 -> addActive(i, new Techbot(x, y), COPY_LEFT_TILE);
                case 0x3011 -> addActive(i, Exit.create(x, y, this), COPY_NO_TILE);
                case 0x3012 -> addActive(i, new Box(Box.GREY, x, y, new Dynamite(x, y)), COPY_LEFT_TILE);
                case 0x3015 -> addActive(i, new Box(Box.RED, x, y, new Soda(x, y)), COPY_LEFT_TILE);
                case 0x3016 -> addActive(i, new WallCrawler(x, y, Facing.RIGHT), COPY_RIGHT_TILE);
                case 0x3017 -> addActive(i, new WallCrawler(x, y, Facing.LEFT), COPY_LEFT_TILE);
                case 0x3018 -> addActive(i, new Box(Box.RED, x, y, new Turkey(x, y)), COPY_LEFT_TILE);
                case 0x3019 -> addActive(i, Bridge.create(i, tiles), COPY_NO_TILE);
                case 0x301D -> addActive(i, new Box(Box.BLUE, x, y, new Football(x, y)), COPY_LEFT_TILE);
                case 0x301E -> addActive(i, new Box(Box.BLUE, x, y, new Joystick(x, y)), COPY_LEFT_TILE);
                case 0x301F -> addActive(i, new Box(Box.BLUE, x, y, new Floppy(x, y)), COPY_LEFT_TILE);
                case 0x3023 -> addActive(i, new Box(Box.BLUE, x, y, new Balloon(x, y - TILE_SIZE)), COPY_LEFT_TILE);
                case 0x3024 -> addActive(i, new Camera(x, y), COPY_BOTTOM_TILE);
                case 0x3029 -> addActive(i, new Box(Box.GREY, x, y, new NuclearMolecule(x, y)), COPY_LEFT_TILE);
                case 0x302A -> addActive(i, new Acme(x, y), COPY_LEFT_TILE);
                case 0x302B -> addActive(i, new Reactor(x, y), COPY_LEFT_TILE);
                case 0x302C -> addActive(i, new Needle(x, y), COPY_TOP_TILE);
                case 0x302D -> addActive(i, new Box(Box.BLUE, x, y, new Flag(x, y)), COPY_LEFT_TILE);
                case 0x302E -> addActive(i, new Box(Box.BLUE, x, y, new Radio(x, y)), COPY_LEFT_TILE);
                case 0x3031 -> addActive(i, new BouncingMine(x, y), COPY_LEFT_TILE);
                case 0x3032 -> {
                    startLocation = i;
                    COPY_LEFT_TILE.accept(i);
                }
                case 0x3037 -> addActive(i, new Box(Box.GREY, x, y, new Letter(x, y, 'D')), COPY_LEFT_TILE);
                case 0x3038 -> addActive(i, new Box(Box.GREY, x, y, new Letter(x, y, 'U')), COPY_LEFT_TILE);
                case 0x3039 -> addActive(i, new Box(Box.GREY, x, y, new Letter(x, y, 'K')), COPY_LEFT_TILE);
                case 0x303A -> addActive(i, new Box(Box.GREY, x, y, new Letter(x, y, 'E')), COPY_LEFT_TILE);
                case 0x3040 -> addActive(i, new Message(x, y), COPY_NO_TILE);
                case 0x3041 -> addActive(i, Tv.create(x, y, this), COPY_NO_TILE);
                case 0x3044 -> addActive(i, new Key(x, y, Key.Type.RED), COPY_LEFT_TILE);
                case 0x3045 -> addActive(i, new Key(x, y, Key.Type.GREEN), COPY_LEFT_TILE);
                case 0x3046 -> addActive(i, new Key(x, y, Key.Type.BLUE), COPY_LEFT_TILE);
                case 0x3047 -> addActive(i, new Key(x, y, Key.Type.MAGENTA), COPY_LEFT_TILE);
                case Door.RED_DOOR_TILE_ID -> addActive(i, new Door(x, y, Key.Type.RED), l -> tiles[l] = Door.RED_DOOR_TILE_ID);
                case Door.GREEN_DOOR_TILE_ID -> addActive(i, new Door(x, y, Key.Type.GREEN), l -> tiles[l] = Door.GREEN_DOOR_TILE_ID);
                case Door.BLUE_DOOR_TILE_ID -> addActive(i, new Door(x, y, Key.Type.BLUE), l -> tiles[l] = Door.BLUE_DOOR_TILE_ID);
                case Door.MAGENTA_DOOR_TILE_ID -> addActive(i, new Door(x, y, Key.Type.MAGENTA), l -> tiles[l] = Door.MAGENTA_DOOR_TILE_ID);
                case Lock.RED_LOCK_TILE_ID -> addActive(i, new Lock(x, y, Key.Type.RED), l -> tiles[l] = Lock.RED_LOCK_TILE_ID);
                case Lock.GREEN_LOCK_TILE_ID -> addActive(i, new Lock(x, y, Key.Type.GREEN), l -> tiles[l] = Lock.GREEN_LOCK_TILE_ID);
                case Lock.BLUE_LOCK_TILE_ID -> addActive(i, new Lock(x, y, Key.Type.BLUE), l -> tiles[l] = Lock.BLUE_LOCK_TILE_ID);
                case Lock.MAGENTA_LOCK_TILE_ID -> addActive(i, new Lock(x, y, Key.Type.MAGENTA), l -> tiles[l] = Lock.MAGENTA_LOCK_TILE_ID);
                case 0x3050 -> addActive(i, new Football(x, y), COPY_LEFT_TILE);
                case 0x3051 -> addActive(i, new Turkey(x, y), COPY_LEFT_TILE);
                case 0x3052 -> addActive(i, new Soda(x, y), COPY_LEFT_TILE);
                case 0x3053 -> addActive(i, new Floppy(x, y), COPY_LEFT_TILE);
                case 0x3054 -> addActive(i, new Joystick(x, y), COPY_LEFT_TILE);
                case 0x3055 -> addActive(i, new Flag(x, y), COPY_LEFT_TILE);
                case 0x3056 -> addActive(i, new Radio(x, y), COPY_LEFT_TILE);
                case 0x3057 -> addActive(i, new Mine(x, y), COPY_LEFT_TILE);
                case 0x3058 -> addActive(i, new Spikes(x, y, true), COPY_TOP_TILE);
                case 0x3059 -> addActive(i, new Spikes(x, y, false), COPY_BOTTOM_TILE);
            }
        }

        actives.sort((o1, o2) -> { // make sure locks are processed before any doors.
            if (o1 instanceof Lock) {
                return -1;
            } else if (o2 instanceof Lock) {
                return 1;
            } else {
                return 0;
            }
        });
    }

    private void addActive(int location, Active active, Consumer<Integer> backgroundTile) {
        actives.add(active); // if active instanceof Lock, prepend

        backgroundTile.accept(location);
    }

    public int getNumber() {
        return number;
    }

    public int getTile(int row, int col) {
        return ((row >= 0) && (row < HEIGHT) && (col >= 0) && (col < WIDTH)) ? tiles[row * WIDTH + col] : 0;
    }

    public BufferedImage getBackdrop() {
        return backdrop;
    }

    public List<Active> getActives() {
        return actives;
    }

    public boolean isSolid(int row, int col) {
        return isSolid(getTile(row, col));
    }

    public static boolean isSolid(int tileId) {
        return ((tileId >= 0x1800) && (tileId <= 0x2FFF)) || (tileId == Elevator.TILE_ID) || ((tileId >= Door.RED_DOOR_TILE_ID) && (tileId <= Door.MAGENTA_DOOR_TILE_ID));
    }

    public int getPlayerStartX() {
        return (startLocation % WIDTH) * TILE_SIZE;
    }

    public int getPlayerStartY() {
        return (startLocation / WIDTH) * TILE_SIZE;
    }

    public void update(GameState state) {
        Iterator<Active> iterator = getActives().iterator();

        while (iterator.hasNext()) {
            Active active = iterator.next();

            active.update(state);

            if (!active.isActive()) {
                iterator.remove();
            }
        }
    }

    public void setTile(int row, int col, int tileId) {
        tiles[row * Level.WIDTH + col] = tileId;
    }

    private Consumer<Integer> COPY_NO_TILE = location -> {};
    private Consumer<Integer> COPY_LEFT_TILE = location -> tiles[location] = tiles[location - 1];
    private Consumer<Integer> COPY_TOP_TILE = location -> tiles[location] = tiles[location - WIDTH];
    private Consumer<Integer> COPY_RIGHT_TILE = location -> tiles[location] = tiles[location + 1];
    private Consumer<Integer> COPY_BOTTOM_TILE = location -> tiles[location] = tiles[location + WIDTH];

    public boolean isIntermission() {
        return (number % 2) == 1;
    }
}
