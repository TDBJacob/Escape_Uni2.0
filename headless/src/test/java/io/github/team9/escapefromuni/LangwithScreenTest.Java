package io.github.team9.escapefromuni;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class LangwithScreenTest {

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
        Main game = mock(Main.class);
        BuildingManager buildingManager = mock(BuildingManager.class);
        GameScreen gameScreen = mock(GameScreen.class);
        LangwithScreen screen = new LangwithScreen(game, buildingManager, gameScreen);
        assertNotNull(screen);
    }

    @Test
    public void testPlayerInitialization() {
        Main game = mock(Main.class);
        BuildingManager buildingManager = mock(BuildingManager.class);
        GameScreen gameScreen = mock(GameScreen.class);
        LangwithScreen screen = new LangwithScreen(game, buildingManager, gameScreen);
        // Similar to RonCooke, hard to test private fields.
    }
}
