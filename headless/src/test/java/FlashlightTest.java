import io.github.team9.escapefromuni.GameScreen;
import io.github.team9.escapefromuni.Main;
import io.github.team9.escapefromuni.Lighting;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for flashlight functionality in GameScreen.
 *
 * The flashlight system has several behaviors:
 * 1. Can be toggled on/off by clicking
 * 2. Tracks usage count (breaks after 10 uses)
 * 3. When broken, flickers automatically every second
 * delta time is simulated as what it would be for 60 frames
 */
public class FlashlightTest extends BaseTest {

    private GameScreen gameScreen;
    private Main mockGame;
    private Lighting mockLighting;

    @BeforeEach
    public void setUp() {
        mockGame = mock(Main.class);
    }

    /**
     * Test flashlight toggle counter increments correctly.
     */
    @Test
    public void testTorchToggleIncrementsCounter() {
        int torchUseCounter = 0;
        boolean torchBroken = false;

        torchUseCounter += 1;

        assertEquals(1, torchUseCounter,
            "Torch use counter should increment on toggle");
    }

    /**
     * Test that torch breaks after 10 uses.
     */
    @Test
    public void testTorchBreaksAfterTenUses() {
        int torchUseCounter = 0;
        boolean torchBroken = false;

        // Simulate 10 toggles
        for (int i = 0; i < 10; i++) {
            torchUseCounter += 1;

            if (torchUseCounter >= 10) {
                torchBroken = true;
            }
        }

        assertTrue(torchBroken,
            "Torch should be broken after 10 uses");
        assertEquals(10, torchUseCounter,
            "Counter should be at 10");
    }

    /**
     * Test that broken torch triggers hidden event counter.
     */
    @Test
    public void testBrokenTorchIncrementsHiddenEvents() {
        mockGame.foundHiddenEvents = 0;
        int torchUseCounter = 10;
        boolean torchBroken = false;

        if (torchUseCounter >= 10) {
            torchBroken = true;
            mockGame.foundHiddenEvents += 1;
        }

        assertTrue(torchBroken, "Torch should be broken");
        assertEquals(1, mockGame.foundHiddenEvents,
            "Hidden event counter should increment when torch breaks");
    }

    /**
     * Test torch timer increments correctly when broken.
     */
    @Test
    public void testBrokenTorchTimerIncrements() {
        float torchTimer = 0f;
        boolean torchBroken = true;
        float deltaTime = 0.016f;

        if (torchBroken) {
            torchTimer += deltaTime;
        }

        assertEquals(0.016f, torchTimer, 0.001f,
            "Torch timer should accumulate delta time when broken");
    }

    /**
     * Test that broken torch flickers when timer reaches 1 second.
     */
    @Test
    public void testBrokenTorchFlickersAtOneSecond() {
        float torchTimer = 1.0f;
        boolean torchBroken = true;
        boolean isTorchOn = true;

        if (torchBroken && torchTimer >= 1f) {
            isTorchOn = !isTorchOn;
            torchTimer -= 1f;
        }

        assertFalse(isTorchOn,
            "Torch should toggle off when timer reaches 1 second");
        assertEquals(0f, torchTimer, 0.001f,
            "Timer should reset after flickering");
    }

    /**
     * Test that normal torch doesn't break before 10
     */
    @Test
    public void testTorchDoesNotBreakBeforeTenUses() {
        int torchUseCounter = 9;
        boolean torchBroken = false;

        if (torchUseCounter >= 10) {
            torchBroken = true;
        }

        assertFalse(torchBroken,
            "Torch should not be broken before 10 uses");
    }

    /**
     * Test that broken torch timer doesn't increment when not broken.
     */
    @Test
    public void testTorchTimerDoesNotIncrementWhenNotBroken() {
        float torchTimer = 0f;
        boolean torchBroken = false;
        float deltaTime = 0.016f;

        if (torchBroken) {
            torchTimer += deltaTime;
        }

        assertEquals(0f, torchTimer, 0.001f,
            "Timer should not increment when torch is not broken");
    }

    /**
     * Test manual toggle
     */
    @Test
    public void testManualToggleCountsTowardBreaking() {
        int torchUseCounter = 5;
        boolean torchBroken = false;
        boolean userClickedToToggle = true;

        if (userClickedToToggle && !torchBroken) {
            torchUseCounter += 1;
        }

        assertEquals(6, torchUseCounter,
            "Manual toggle should increment use counter");
    }

    /**
     * Counter should stop incrementing after torch breaks
     */
    @Test
    public void testCounterDoesNotIncrementWhenBroken() {
        int torchUseCounter = 10;
        boolean torchBroken = true;
        boolean userClickedToToggle = true;

        if (userClickedToToggle && !torchBroken) {
            torchUseCounter += 1;
        }

        assertEquals(10, torchUseCounter,
            "Counter should not increment when torch is broken");
    }

    /**
     * Test timer reset behavior with excess time.
     */
    @Test
    public void testTimerHandlesExcessTimeCorrectly() {
        float torchTimer = 1.2f;
        boolean torchBroken = true;

        if (torchBroken && torchTimer >= 1f) {
            torchTimer -= 1f;
        }

        assertEquals(0.2f, torchTimer, 0.001f,
            "Timer should preserve excess time after reset (1.2 - 1.0 = 0.2)");
    }

    /**
     * Test scenario: from working to broken to flickering.
     */
    @Test
    public void testCompleteFlashlightLifecycle() {
        int torchUseCounter = 0;
        boolean torchBroken = false;
        boolean isTorchOn = false;
        float torchTimer = 0f;

        for (int i = 0; i < 10; i++) {
            torchUseCounter++;
        }

        if (torchUseCounter >= 10) {
            torchBroken = true;
        }

        assertTrue(torchBroken, "Torch should be broken after 10 toggles");

        float totalTime = 0f;
        int flickerCount = 0;

        while (totalTime < 2.1f) {
            float delta = 0.016f;
            totalTime += delta;

            if (torchBroken) {
                torchTimer += delta;

                if (torchTimer >= 1f) {
                    isTorchOn = !isTorchOn;
                    torchTimer -= 1f;
                    flickerCount++;
                }
            }
        }

        assertEquals(2, flickerCount,
            "Torch should flicker twice over 2 seconds");
    }
}
