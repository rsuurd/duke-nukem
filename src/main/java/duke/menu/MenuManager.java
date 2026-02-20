package duke.menu;

import duke.GameSystems;

import java.util.ArrayDeque;
import java.util.Deque;

public class MenuManager {
    private Deque<Menu> stack;

    public MenuManager() {
        stack = new ArrayDeque<>();
    }

    public void open(Menu menu, GameSystems systems) {
        stack.push(menu);

        menu.open(systems);
    }

    public void closeAll(GameSystems systems) {
        systems.getDialogManager().close();

        stack.clear();
    }

    public Menu current() {
        return stack.peek();
    }

    public void update(GameSystems systems) {
        // maybe global VK_ESCAPE / VK_SPACE / VK_ENTER to close all menus?

        if (isOpen()) {
            current().update(systems);
        }
    }

    public boolean isOpen() {
        return !stack.isEmpty();
    }
}
