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

    public List<BufferedImage> getFont() {
        return font;
    }

    public List<BufferedImage> getBorder() {
        return border;
    }
}
