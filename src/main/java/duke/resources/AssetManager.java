package duke.resources;

import duke.gfx.Sprite;
import duke.level.Level;
import duke.level.LevelBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssetManager {
    private ResourceLoader resourceLoader;

    private Map<Category, List<Sprite>> tiles;
    private Map<String, Sprite> images;

    public AssetManager(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;

        tiles = new HashMap<>();
        images = new HashMap<>();
    }

    public void load() {
        resourceLoader.ensureResourcesExist();

        loadTiles();
    }

    private void loadTiles() {
        for (Category category : Category.values()) {
            tiles.computeIfAbsent(category, this::loadTiles);
        }
    }

    private List<Sprite> loadTiles(Category category) {
        return category.names.stream().flatMap(name ->
                loadTiles(String.format("%s.DN1", name), category.opaque).stream().limit(category.tilesPerFile)
        ).toList();
    }

    private List<Sprite> loadTiles(String name, boolean opaque) {
        return resourceLoader.getSpriteLoader().readTiles(name, opaque);
    }

    public List<Sprite> getTiles() {
        return tiles.get(Category.TILES);
    }

    public List<Sprite> getMan() {
        return tiles.get(Category.MAN);
    }

    public List<Sprite> getFont() {
        return tiles.get(Category.FONT);
    }

    public List<Sprite> getAnim() {
        return tiles.get(Category.ANIM);
    }

    public List<Sprite> getObjects() {
        return tiles.get(Category.OBJECTS);
    }

    public List<Sprite> getBorder() {
        return tiles.get(Category.BORDER);
    }

    public List<Sprite> getNumbers() {
        return tiles.get(Category.NUMBERS);
    }

    public Sprite getImage(String name) {
        return images.computeIfAbsent(name, n ->
                resourceLoader.getSpriteLoader().readImage(String.format("%s.DN1", n))
        );
    }

    public Sprite getBackdrop(int number) {
        return images.computeIfAbsent(String.format("DRPOP%d", number), n ->
                resourceLoader.getSpriteLoader().readBackdrop(number)
        );
    }

    public Level getLevel(int number) {
        int[] data = resourceLoader.getLevelLoader().readLevel(number);

        return new LevelBuilder(number, data).build();
    }

    private enum Category {
        TILES(true, 48, "BACK0", "BACK1", "BACK2", "BACK3", "SOLID0", "SOLID1", "SOLID2", "SOLID3"),
        MAN("MAN0", "MAN1", "MAN2", "MAN3"),
        FONT("FONT1", "FONT2"),
        ANIM("ANIM0", "ANIM1", "ANIM2", "ANIM3", "ANIM4", "ANIM5"),
        OBJECTS("OBJECT0", "OBJECT1", "OBJECT2"),
        BORDER(true, 50, "BORDER"),
        NUMBERS("NUMBERS");

        private List<String> names;
        private boolean opaque;
        private int tilesPerFile;

        Category(String... names) {
            this(false, 50, names);
        }

        Category(boolean opaque, int tilesPerFile, String... names) {
            this.names = Arrays.asList(names);
            this.opaque = opaque;
            this.tilesPerFile = tilesPerFile;
        }
    }
}
