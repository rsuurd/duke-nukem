package duke;

import duke.gfx.Sprite;

public interface Renderer {
    void clear();

    void draw(Sprite sprite, int x, int y);

    void copy(int sourceX, int sourceY, int destinationX, int destinationY);

    void flip();
}
