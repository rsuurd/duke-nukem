package duke.resources;

import duke.gfx.Sprite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssetManager {
    private ResourceLoader resourceLoader;

    private List<Sprite> tiles;
    private Map<String, Sprite> images;

    public AssetManager(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;

        tiles = new ArrayList<>();
        images = new HashMap<>();
    }

    public void load() {
        resourceLoader.ensureResourcesExist();

        loadTiles();
    }

    private void loadTiles() {
        if (!tiles.isEmpty()) return;

        loadTileSet("BACK");
        loadTileSet("SOLID");
    }

    private void loadTileSet(String name) {
        for (int i = 0; i < 4; i++) {
            tiles.addAll(loadTiles(String.format("%s%d.DN1", name, i)).stream().limit(48).toList());
        }
    }

    private List<Sprite> loadTiles(String name) {
        return resourceLoader.getSpriteLoader().readTiles(name, true);
    }

    public List<Sprite> getTiles() {
        return tiles;
    }

    public Sprite getImage(String name) {
        return images.computeIfAbsent(name, this::loadImage);
    }

    private Sprite loadImage(String name) {
        return resourceLoader.getSpriteLoader().readImage(String.format("%s.DN1", name));
    }
}
