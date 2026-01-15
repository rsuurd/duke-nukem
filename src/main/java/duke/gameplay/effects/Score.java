package duke.gameplay.effects;

import duke.gfx.AnimationDescriptor;
import duke.gfx.SpriteDescriptor;

import java.util.Map;

import static duke.gfx.SpriteDescriptor.NUMBERS;

public class Score extends Effect {
    public Score(int x, int y, int points) {
        super(x, y, DESCRIPTORS.get(points), TTL);

        setVelocityY(-SPEED);
    }

    private static final int SPEED = 2;
    private static final int TTL = 32;

    private static AnimationDescriptor create(int baseIndex) {
        return new AnimationDescriptor(new SpriteDescriptor(NUMBERS, baseIndex), 2, 2);
    }

    private static final Map<Integer, AnimationDescriptor> DESCRIPTORS = Map.of(
            100, create(0),
            200, create(2),
            500, create(4),
            1000, create(6),
            2000, create(8),
            5000, create(10),
            10000, create(12)
    );

    public static boolean supports(int points) {
        return DESCRIPTORS.containsKey(points);
    }
}
