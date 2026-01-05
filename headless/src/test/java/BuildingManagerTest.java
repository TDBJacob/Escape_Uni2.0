import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.team9.escapefromuni.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BuildingManagerTest extends BaseTest { //needed as a base to create other tests

    private static Main testMain;
    private static GameScreen testGameScreen;
    private static Player testPlayer;
    private static AudioManager testAudioManager;
    private static BuildingManager testBuildingManager;
    private static Lighting testLighting;

    @BeforeEach
    public void testSetup() {
        testGameScreen = mock(GameScreen.class);
        testAudioManager = mock(AudioManager.class);
        testLighting = mock(Lighting.class);
        testMain = mock(Main.class);

        testPlayer = mock(Player.class, CALLS_REAL_METHODS);

        testGameScreen.audioManager = testAudioManager;
        testGameScreen.lighting = testLighting;

        testMain.viewport = new FitViewport(800, 450);

        testPlayer.sprite = new Sprite();
        testPlayer.sprite.setBounds(360, 460, 20, 40);
        testBuildingManager = new BuildingManager(testMain, testGameScreen, testPlayer, testAudioManager) {
            @Override
            protected void setLangwithScreen() {
                // nothing here to prevent from making screen
            }
            @Override
            protected void setRonCookeScreen() {
                // prevent real screen creation
            }
        };
    }

    @Test //explanation here
    public void testCheckBuildingTrigger() { //
        testBuildingManager.update(0f);

        // Assert observable behavior
        assertTrue(testBuildingManager.getShowEnterPrompt());
        assertEquals("Ron Cooke", testBuildingManager.getCurrentBuilding());
        assertFalse(testBuildingManager.isInRonCooke());
    }

    @Test //explanation here
    public void testExitBuilding() {
        testBuildingManager.exitBuilding();

        assertFalse(testBuildingManager.isInRonCooke(), "Should not be in Ron Cooke after leaving");
        assertFalse(testBuildingManager.isInLangwith(), "Should not be in Langwith after leaving");

        verify(testMain, times(1)).setScreen(testGameScreen);
    }

    @Test //explanation here
    public void testEnterLangwith() {
        testBuildingManager.enterLangwith();

        assertTrue(testBuildingManager.isInLangwith(), "Should be in Langwith after entering");

        verify(testAudioManager, times(1)).stopFootsteps();
    }

    @Test //explanation here
    public void testEnterRonCooke() {
        testBuildingManager.enterRonCooke();

        assertTrue(testBuildingManager.isInRonCooke(), "Should be inside Ron Cooke after entering");
        assertTrue(testGameScreen.isDark, "Should get dark after entering Ron Cooke");
        assertTrue(testGameScreen.hasTorch, "Should get the torch in Ron Cooke");

        verify(testAudioManager, times(1)).stopFootsteps();
        verify(testLighting, times(1)).isVisible("playerNoTorch", true);

        verify(testLighting, times(1)).isVisible("gooseNoTorch", true);
    }
}
