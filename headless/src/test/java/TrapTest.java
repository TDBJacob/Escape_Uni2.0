import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import io.github.team9.escapefromuni.Main;
import io.github.team9.escapefromuni.Trap;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;



/**
 * Tests the Trap class
 */
public class TrapTest extends BaseTest{ // Base Test creates headless backend and creates mock graphics

    @Test
    public void testConstructor() {

        // Mock all the attributes

        Main game = mock(Main.class); // Mocks Main class, responsible for rendering and game logic

        Drawable drawable = mock(Drawable.class); // Mocks Drawable class to avoid testure or rendering

        Image img = new Image(drawable); // Set image size so trap activation is well defined

        img.setSize(16, 16); // Set size for activation radius

        // Create a Trap object, at coordinates (100,200)
        Trap trap = new Trap(game, img, 100, 200, true, "GameScreen"); 

        assertEquals(100, trap.getX(), 0.01); // Checks if trap is at x coordinate 100
        assertEquals(200, trap.getY(), 0.01); // Checks if trap is at y coordinate
        assertTrue(trap.getIsVisible()); // Check if trap is visible
        assertEquals("GameScreen", trap.getOriginScreen()); // Check 
        assertFalse(trap.isActive());
        assertEquals(8f, trap.getActivationRadius(), 0.01); // Check activation radius calculation
    }

    @Test
    public void testCheckInRange() {
        Main game = mock(Main.class);
        Drawable drawable = mock(Drawable.class);
        Image img = new Image(drawable);
        img.setSize(16, 16);
        Trap trap = new Trap(game, img, 100, 100, true, "GameScreen");
        trap.setActivationRadius(10f);
        assertTrue(trap.checkInRange(105, 100)); // Within radius
        assertFalse(trap.checkInRange(120, 100)); // Outside
    }

    @Test
    public void testActivateDeactivate() {
        Main game = mock(Main.class);
        Drawable drawable = mock(Drawable.class);
        Image img = new Image(drawable);
        img.setSize(16, 16);
        Trap trap = new Trap(game, img, 100, 100, true, "GameScreen");
        assertFalse(trap.isActive());
        trap.activateTrap();
        assertTrue(trap.isActive());
        assertEquals(0f, trap.getTrapDuration(), 0.01);
        trap.deactivateTrap();
        assertFalse(trap.isActive());
    }

    @Test
    public void testCheckEscapeInput() {
        Main game = mock(Main.class);
        Drawable drawable = mock(Drawable.class);
        Image img = new Image(drawable);
        img.setSize(16, 16);
        Trap trap = new Trap(game, img, 100, 100, true, "GameScreen");
        trap.setEscapeKey("F");
        assertFalse(trap.checkEscapeInput("F")); // Not active
        trap.activateTrap();
        assertTrue(trap.checkEscapeInput("F"));
        assertFalse(trap.checkEscapeInput("G"));
        assertFalse(trap.checkEscapeInput(null));
    }

    @Test
    public void testUpdate() {
        Main game = mock(Main.class);
        Drawable drawable = mock(Drawable.class);
        Image img = new Image(drawable);
        img.setSize(16, 16);
        Trap trap = new Trap(game, img, 100, 100, true, "GameScreen");
        trap.activateTrap();
        trap.update(5f);
        assertEquals(5f, trap.getTrapDuration(), 0.01);
        trap.update(25f);
        assertFalse(trap.isActive()); // Check if trap active after 30f
        trap.deactivateTrap();
        assertFalse(trap.isActive());
        assertEquals(0f, trap.getTrapDuration(), 0.01);
    }

    @Test
    public void testEscapeRestoresSpeed() {
        Main game = mock(Main.class);
        Drawable drawable = mock(Drawable.class);
        Image img = new Image(drawable);
        img.setSize(16, 16);
        Trap trap = new Trap(game, img, 100, 100, true, "GameScreen");
        trap.activateTrap();
        assertEquals(0f, trap.getSlowMultiplier(), 0.01); // check if speed is 0 when stepping on trap
        // Speed reduced when active
        // Simulate escape key press
        if (trap.checkEscapeInput("F")) {
            trap.deactivateTrap();
        }
        assertEquals(1f, trap.getSlowMultiplier(), 0.01); // Speed restored after escape
    }
}
