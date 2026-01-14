package duke.gfx;

import duke.Renderer;
import duke.gameplay.Player;
import duke.gameplay.player.Weapon;
import duke.resources.AssetManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static duke.level.Level.TILE_SIZE;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HudTest {
    @Mock
    private List<Sprite> borders;

    @Mock
    private List<Sprite> objects;

    @Mock
    private AssetManager assets;

    @Mock
    private Font font;

    @InjectMocks
    private Hud hud;

    @Mock
    private Renderer renderer;

    @BeforeEach
    void create() {
        when(assets.getBorder()).thenReturn(borders);
        when(assets.getObjects()).thenReturn(objects);
    }

    @Test
    void shouldDrawScore() {
        Player player = mock();
        when(player.getWeapon()).thenReturn(mock());
        when(player.getHealth()).thenReturn(mock());
        when(player.getInventory()).thenReturn(mock());

        hud.render(renderer, 0, player);

        verify(font).drawText(renderer, "00000000", 240, 24);
    }

    @Test
    void shouldDrawFirepower() {
        Player player = mock();
        Weapon weapon = mock();
        when(player.getWeapon()).thenReturn(weapon);
        when(player.getHealth()).thenReturn(mock());
        when(player.getInventory()).thenReturn(mock());
        when(weapon.getFirepower()).thenReturn(4);

        hud.render(renderer, 0, player);

        for (int i = 0; i < 4; i++) {
            verify(assets.getObjects()).get(6 + i);
            verify(renderer).draw(any(), eq(240 + (i * TILE_SIZE)), eq(106));
        }
    }
}