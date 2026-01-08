import io.github.team9.escapefromuni.BuildingManager;
import io.github.team9.escapefromuni.GameScreen;
import io.github.team9.escapefromuni.Main;
import io.github.team9.escapefromuni.RonCookeScreen;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class RonCookeScreenTest extends BaseTest{

    @Test
    public void testConstructor() {
        Main game = mock(Main.class);
        BuildingManager buildingManager = mock(BuildingManager.class);
        GameScreen gameScreen = mock(GameScreen.class);
        RonCookeScreen screen = new RonCookeScreen(game, buildingManager, gameScreen);
        assertNotNull(screen);
        assertNotNull(screen.getPlayer());
        assertNotNull(screen.getReceptionist());
        assertEquals(0f, screen.getSpeechTimer(), 0.01);
        assertFalse(screen.getSpeech().isEmpty());
        assertEquals("Locked yourself out huh?", screen.getSpeech().get(0));
        assertFalse(screen.isEPressed());
        assertFalse(screen.isPaused());
    }

    @Test
    public void testPlayerInitialization() {
        Main game = mock(Main.class);
        BuildingManager buildingManager = mock(BuildingManager.class);
        GameScreen gameScreen = mock(GameScreen.class);
        RonCookeScreen screen = new RonCookeScreen(game, buildingManager, gameScreen);
        // Check player properties that don't depend on sprite loading
        assertEquals(2, screen.getPlayer().speed);
        // Note: sprite position and scale may not be testable in headless due to texture loading
    }
}
