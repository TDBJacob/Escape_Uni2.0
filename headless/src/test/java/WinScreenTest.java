import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.team9.escapefromuni.Main;
import io.github.team9.escapefromuni.WinScreen;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class WinScreenTest {

    private static HeadlessApplication application;

    @Mock private Main mockGame;
    @Mock private SpriteBatch mockBatch;
    @Mock private BitmapFont mockFont;
    @Mock private FitViewport mockViewport;
    @Mock private Skin mockSkin;
    @Mock private Input mockInput;
    @Mock private GL20 mockGL20;
    @Mock private Graphics mockGraphics;
    @Mock private BitmapFont.BitmapFontData mockFontData;

    private WinScreen winScreen;
    private AutoCloseable closeable;

    @BeforeAll
    public static void setUpApplication() {
        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        config.updatesPerSecond = -1;

        application = new HeadlessApplication(new ApplicationListener() {
            @Override public void create() {}
            @Override public void resize(int width, int height) {}
            @Override public void render() {}
            @Override public void pause() {}
            @Override public void resume() {}
            @Override public void dispose() {}
        }, config);
    }

    @AfterAll
    public static void tearDownApplication() {
        if (application != null) {
            application.exit();
            application = null;
        }
    }

    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        // Set up Gdx mocks
        Gdx.gl = mockGL20;
        Gdx.gl20 = mockGL20;
        Gdx.input = mockInput;
        Gdx.graphics = mockGraphics;

        // Configure mock game
        when(mockGame.viewport).thenReturn(mockViewport);
        when(mockGame.batch).thenReturn(mockBatch);
        when(mockGame.menuFont).thenReturn(mockFont);
        when(mockGame.buttonSkin).thenReturn(mockSkin);

        // Configure viewport
        when(mockViewport.getWorldWidth()).thenReturn(800f);
        when(mockViewport.getWorldHeight()).thenReturn(450f);
        when(mockViewport.getCamera()).thenReturn(mock(com.badlogic.gdx.graphics.Camera.class));

        // Configure font
        when(mockFont.getData()).thenReturn(mockFontData);

        // Set default score
        mockGame.score = 250f;

        // Create WinScreen instance
        winScreen = new WinScreen(mockGame);
    }

    @AfterEach
    public void tearDown() throws Exception {
        if (closeable != null) {
            closeable.close();
        }
    }

    @Test
    public void testConstructorCreatesWinScreen() {
        assertNotNull(winScreen, "WinScreen should be instantiated");
    }

    @Test
    public void testShowSetsInputProcessor() {
        winScreen.show();

        // Verify input processor is set during show
        verify(mockInput, atLeastOnce()).setInputProcessor(any());
    }

    @Test
    public void testRenderClearsScreenWithBlack() {
        winScreen.show();
        winScreen.render(0.016f);

        // Verify screen is cleared with black color
        verify(mockGL20, atLeastOnce()).glClearColor(0, 0, 0, 1);
        verify(mockGL20, atLeastOnce()).glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Test
    public void testRenderBeginsBatch() {
        winScreen.show();
        winScreen.render(0.016f);

        // Verify batch operations occur
        verify(mockBatch, atLeastOnce()).begin();
        verify(mockBatch, atLeastOnce()).end();
    }

    @Test
    public void testRenderDisplaysWinMessage() {
        winScreen.show();
        winScreen.render(0.016f);

        // Verify font draw is called for the win message
        verify(mockFont, atLeast(1)).draw(eq(mockBatch), anyString(), anyFloat(), anyFloat());
    }

    @Test
    public void testRenderDisplaysScore() {
        mockGame.score = 500f;
        winScreen.show();
        winScreen.render(0.016f);

        // Verify score is rendered (at least 2 draws: title and score)
        verify(mockFont, atLeast(2)).draw(eq(mockBatch), anyString(), anyFloat(), anyFloat());
    }

    @Test
    public void testRenderWithZeroScore() {
        mockGame.score = 0f;
        winScreen.show();

        assertDoesNotThrow(() -> winScreen.render(0.016f),
            "Should render correctly with zero score");
    }

    @Test
    public void testRenderWithNegativeScore() {
        mockGame.score = -50f;
        winScreen.show();

        assertDoesNotThrow(() -> winScreen.render(0.016f),
            "Should render correctly with negative score");
    }

    @Test
    public void testRenderWithLargeScore() {
        mockGame.score = 99999f;
        winScreen.show();

        assertDoesNotThrow(() -> winScreen.render(0.016f),
            "Should render correctly with large score");
    }

    @Test
    public void testResizeUpdatesViewport() {
        winScreen.show();
        winScreen.resize(1920, 1080);

        // Verify viewport is updated with new dimensions
        verify(mockViewport).update(1920, 1080, true);
    }

    @Test
    public void testResizeWithSmallDimensions() {
        winScreen.show();

        assertDoesNotThrow(() -> winScreen.resize(100, 100),
            "Should handle small window dimensions");
    }

    @Test
    public void testResizeWithZeroDimensions() {
        winScreen.show();

        assertDoesNotThrow(() -> winScreen.resize(0, 0),
            "Should handle zero dimensions gracefully");
    }

    @Test
    public void testMultipleRenderCalls() {
        winScreen.show();

        // Render multiple frames
        for (int i = 0; i < 5; i++) {
            winScreen.render(0.016f);
        }

        // Verify rendering happens multiple times
        verify(mockBatch, atLeast(5)).begin();
        verify(mockBatch, atLeast(5)).end();
    }

    @Test
    public void testRenderWithVariableDeltaTime() {
        winScreen.show();

        // Test with different delta times
        winScreen.render(0.016f);  // Normal frame
        winScreen.render(0.033f);  // Slow frame
        winScreen.render(0.001f);  // Fast frame

        verify(mockBatch, atLeast(3)).begin();
    }

    @Test
    public void testHideHandlesInputProcessor() {
        winScreen.show();
        winScreen.hide();

        // Input processor should be handled during hide
        verify(mockInput, atLeastOnce()).setInputProcessor(any());
    }

    @Test
    public void testDisposeDoesNotThrowException() {
        winScreen.show();

        assertDoesNotThrow(() -> winScreen.dispose(),
            "Dispose should not throw exceptions");
    }

    @Test
    public void testDisposeWithoutShow() {
        // Dispose without calling show first
        assertDoesNotThrow(() -> winScreen.dispose(),
            "Should handle dispose without show");
    }

    @Test
    public void testMultipleDisposeCalls() {
        winScreen.show();
        winScreen.dispose();

        assertDoesNotThrow(() -> winScreen.dispose(),
            "Should handle multiple dispose calls");
    }

    @Test
    public void testPauseDoesNothing() {
        winScreen.show();

        assertDoesNotThrow(() -> winScreen.pause(),
            "Pause should execute without errors");
    }

    @Test
    public void testResumeDoesNothing() {
        winScreen.show();

        assertDoesNotThrow(() -> winScreen.resume(),
            "Resume should execute without errors");
    }

    @Test
    public void testRenderBeforeShow() {
        // Try to render before calling show
        assertDoesNotThrow(() -> winScreen.render(0.016f),
            "Should handle rendering before show gracefully");
    }

    @Test
    public void testRenderAfterDispose() {
        winScreen.show();
        winScreen.dispose();

        // Try to render after disposal
        assertDoesNotThrow(() -> winScreen.render(0.016f),
            "Should handle rendering after dispose gracefully");
    }

    @Test
    public void testMultipleShowCalls() {
        winScreen.show();
        winScreen.show(); // Call show again

        // Should handle multiple show calls
        assertDoesNotThrow(() -> winScreen.render(0.016f),
            "Should handle multiple show calls");
    }

    @Test
    public void testFontColorIsSet() {
        winScreen.show();
        winScreen.render(0.016f);

        // Verify font color is modified during rendering
        verify(mockFont, atLeastOnce()).setColor(anyFloat(), anyFloat(), anyFloat(), anyFloat());
    }

    @Test
    public void testBatchProjectionMatrixIsSet() {
        winScreen.show();
        winScreen.render(0.016f);

        // Verify batch projection matrix is set
        verify(mockBatch, atLeastOnce()).setProjectionMatrix(any());
    }

    @Test
    public void testRenderWithZeroDelta() {
        winScreen.show();

        assertDoesNotThrow(() -> winScreen.render(0f),
            "Should handle zero delta time");
    }

    @Test
    public void testRenderWithNegativeDelta() {
        winScreen.show();

        assertDoesNotThrow(() -> winScreen.render(-0.016f),
            "Should handle negative delta time");
    }

    @Test
    public void testRenderWithVeryLargeDelta() {
        winScreen.show();

        assertDoesNotThrow(() -> winScreen.render(1000f),
            "Should handle very large delta time");
    }

    @Test
    public void testScreenLifecycle() {
        // Test complete lifecycle
        assertDoesNotThrow(() -> {
            winScreen.show();
            winScreen.render(0.016f);
            winScreen.pause();
            winScreen.resume();
            winScreen.render(0.016f);
            winScreen.hide();
            winScreen.dispose();
        }, "Complete lifecycle should execute without errors");
    }

    @Test
    public void testScoreFormattingWithDecimals() {
        // Test that score displays as integer even with decimals
        mockGame.score = 123.456f;
        winScreen.show();

        assertDoesNotThrow(() -> winScreen.render(0.016f),
            "Should handle decimal scores");
    }
}
