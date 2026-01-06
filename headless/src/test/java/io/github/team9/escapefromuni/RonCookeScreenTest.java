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

public class RonCookeScreenTest {

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
        RonCookeScreen screen = new RonCookeScreen(game, buildingManager, gameScreen);
        assertNotNull(screen);
        // Check initial speech
        // Since speech is private, can't directly test, but constructor sets it.
    }

    @Test
    public void testPlayerInitialization() {
        Main game = mock(Main.class);
        BuildingManager buildingManager = mock(BuildingManager.class);
        GameScreen gameScreen = mock(GameScreen.class);
        RonCookeScreen screen = new RonCookeScreen(game, buildingManager, gameScreen);
        // Player is initialized in constructor, but since render is not called, check if player is set.
        // Hard to test without accessing private fields.
        // Perhaps test that no exception is thrown.
    }
}
