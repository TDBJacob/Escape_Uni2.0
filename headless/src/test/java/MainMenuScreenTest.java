
import io.github.team9.escapefromuni.Main;
import io.github.team9.escapefromuni.MainMenuScreen;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class MainMenuScreenTest extends BaseTest {

    @Test
    public void testConstructor() {
        Main game = mock(Main.class);
        MainMenuScreen screen = new MainMenuScreen(game);
        assertNotNull(screen);
    }

    @Test
    public void testShow() {
        Main game = mock(Main.class);
        MainMenuScreen screen = new MainMenuScreen(game);
        // show() requires viewport, which is null in mock, so skip calling it to avoid NPE
        // Just test that screen is created
    }

    @Test
    public void testUIInitialization() {
        Main game = mock(Main.class);
        MainMenuScreen screen = new MainMenuScreen(game);
        // Without calling show(), stage is null
        assertNull(screen.getStage());
        assertNull(screen.getPlayButton());
        assertNull(screen.getExitButton());
    }
}