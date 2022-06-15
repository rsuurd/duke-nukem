package duke;

import java.awt.image.BufferedImage;
import java.util.List;

public class Assets {
    private ResourceLoader loader;

    private List<BufferedImage> tileSet;
    private List<BufferedImage> man;
    private List<BufferedImage> anim;
    private List<BufferedImage> object;
    private List<BufferedImage> font;
    private List<BufferedImage> border;
    private List<BufferedImage> numbers;

    public Assets(ResourceLoader loader) {
        this.loader = loader;
    }

    public void init() {
        tileSet = loader.readTiles();
        man = loader.readMan();
        anim = loader.readAnim();
        object = loader.readObject();
        font = loader.readFont();
        border = loader.readBorder();
        numbers = loader.readNumbers();
    }

    public BufferedImage getTileSet(int index) {
        return tileSet.get(index);
    }

    public BufferedImage getMan(int index) {
        return man.get(index);
    }

    public BufferedImage getAnim(int index) {
        return anim.get(index);
    }

    public BufferedImage getObject(int index) {
        return object.get(index);
    }

    public BufferedImage getFont(int index) {
        return font.get(index);
    }

    public BufferedImage getBorder(int index) {
        return border.get(index);
    }

    public BufferedImage getNumber(int index) {
        return numbers.get(index);
    }
}
