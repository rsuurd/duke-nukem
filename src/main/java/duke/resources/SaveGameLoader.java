package duke.resources;

import duke.DukeNukemException;
import duke.dialog.Hints;
import duke.gameplay.SaveGame;
import duke.gameplay.player.Inventory;
import duke.level.Level;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class SaveGameLoader {
    private Path path;

    public SaveGameLoader(Path path) {
        this.path = path;
    }

    public void save(SaveGame saveGame, char slot) {
        try (RandomAccessFile out = new RandomAccessFile(path.resolve("SAVED%c.DN1".formatted(slot)).toFile(), "rw")) {
            write(out, saveGame.cameraX() / Level.HALF_TILE_SIZE);
            write(out, ((saveGame.cameraY() / Level.TILE_SIZE) / Level.WIDTH) * Level.WIDTH);
            write(out, ((saveGame.cameraX() - saveGame.playerX()) + Level.TILE_SIZE) / Level.HALF_TILE_SIZE);
            write(out, (saveGame.cameraY() - saveGame.playerY()) + Level.TILE_SIZE);
            write(out, saveGame.level());
            write(out, saveGame.firepower());
            write(out, saveGame.inventory().isEquippedWith(Inventory.Equipment.BOOTS));
            write(out, saveGame.inventory().isEquippedWith(Inventory.Equipment.GRAPPLING_HOOKS));
            write(out, saveGame.health());
            write(out, saveGame.inventory().isEquippedWith(Inventory.Equipment.ROBOHAND));

            for (Hints.Hint hint : Hints.Hint.values()) {
                write(out, saveGame.hints().contains(hint));
            }

            write(out, 0);
            write(out, 0);
            out.writeBytes(String.valueOf(saveGame.score()));
            out.write('\n');
        } catch (IOException e) {
            throw new DukeNukemException("Failed to save game", e);
        }
    }

    private void write(RandomAccessFile out, int value) throws IOException {
        out.writeShort(Short.reverseBytes((short) value));
    }

    private void write(RandomAccessFile out, boolean value) throws IOException {
        write(out, value ? 1 : 0);
    }

    public SaveGame load(char slot) {
        try (RandomAccessFile in = new RandomAccessFile(path.resolve("SAVED%c.DN1".formatted(slot)).toFile(), "r")) {
            Inventory inventory = new Inventory();

            int cameraX = read(in) * Level.HALF_TILE_SIZE;
            int cameraY = (read(in) / Level.WIDTH) * Level.TILE_SIZE;
            int playerScreenX = read(in) * Level.HALF_TILE_SIZE;
            int playerScreenY = read(in);
            int level = read(in);
            int firepower = read(in);
            readEquipment(in, inventory, Inventory.Equipment.BOOTS);
            readEquipment(in, inventory, Inventory.Equipment.GRAPPLING_HOOKS);
            int health = read(in);
            readEquipment(in, inventory, Inventory.Equipment.ROBOHAND);
            Set<Hints.Hint> hints = readAvailableHints(in);
            String scoreLine = in.readLine();
            int score = Integer.parseInt(scoreLine.trim());

            int playerX = playerScreenX + cameraX - Level.TILE_SIZE;
            int playerY = playerScreenY + cameraY - Level.TILE_SIZE;

            return new SaveGame(cameraX, cameraY, playerX, playerY, level, health, firepower, inventory, hints, score);
        } catch (IOException e) {
            throw new DukeNukemException("Failed to load game", e);
        }
    }

    private int read(RandomAccessFile in) throws IOException {
        return Short.reverseBytes(in.readShort());
    }

    private void readEquipment(RandomAccessFile in, Inventory inventory, Inventory.Equipment equipment) throws IOException {
        if (read(in) == 1) {
            inventory.addEquipment(equipment);
        }
    }

    private Set<Hints.Hint> readAvailableHints(RandomAccessFile in) throws IOException {
        Set<Hints.Hint> hints = new HashSet<>();

        for (Hints.Hint hint : Hints.Hint.values()) {
            if (read(in) == 1) {
                hints.add(hint);
            }
        }

        in.skipBytes(4);

        return hints;
    }

    private static final char AUTO_SAVE_SLOT = 'T';
}
