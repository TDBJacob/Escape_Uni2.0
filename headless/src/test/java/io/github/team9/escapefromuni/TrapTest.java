package io.github.team9.escapefromuni;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class TrapTest {

    @BeforeAll
    public static void init() {
        if (Gdx.gl == null) {
            HeadlessApplicationConfiguration hAConfig = new HeadlessApplicationConfiguration();
            new HeadlessApplication(new ApplicationAdapter() {}, hAConfig);
            Gdx.gl = mock(GL20.class);
            Gdx.gl20 = Gdx.gl;
        }
    }

    @Test
    public void testConstructor() {
        // Mock objects
        Main game = mock(Main.class);
        Drawable drawable = mock(Drawable.class);
        Image img = new Image(drawable);
        img.setSize(16, 16); // Set size for activation radius
        AudioManager audioManager = mock(AudioManager.class);
        Trap trap = new Trap(game, img, 100, 200, true, "GameScreen", audioManager);
        assertEquals(100, trap.x, 0.01);
        assertEquals(200, trap.y, 0.01);
        assertTrue(trap.isVisible);
        assertEquals("GameScreen", trap.originScreen);
        assertFalse(trap.isActive());
    }

    @Test
    public void testCheckInRange() {
        Main game = mock(Main.class);
        Drawable drawable = mock(Drawable.class);
        Image img = new Image(drawable);
        img.setSize(16, 16);
        AudioManager audioManager = mock(AudioManager.class);
        Trap trap = new Trap(game, img, 100, 100, true, "GameScreen", audioManager);
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
        AudioManager audioManager = mock(AudioManager.class);
        Trap trap = new Trap(game, img, 100, 100, true, "GameScreen", audioManager);
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
        AudioManager audioManager = mock(AudioManager.class);
        Trap trap = new Trap(game, img, 100, 100, true, "GameScreen", audioManager);
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
        AudioManager audioManager = mock(AudioManager.class);
        Trap trap = new Trap(game, img, 100, 100, true, "GameScreen", audioManager);
        trap.activateTrap();
        trap.update(5f);
        assertEquals(5f, trap.getTrapDuration(), 0.01);
        trap.update(6f);
        assertFalse(trap.isActive()); // Should deactivate after 10f
    }

    @Test
    public void testGetSlowMultiplier() {
        Main game = mock(Main.class);
        Drawable drawable = mock(Drawable.class);
        Image img = new Image(drawable);
        img.setSize(16, 16);
        AudioManager audioManager = mock(AudioManager.class);
        Trap trap = new Trap(game, img, 100, 100, true, "GameScreen", audioManager);
        assertEquals(0f, trap.getSlowMultiplier(), 0.01);
    }
}
