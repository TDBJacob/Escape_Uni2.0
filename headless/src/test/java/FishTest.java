import io.github.team9.escapefromuni.Fish;
import io.github.team9.escapefromuni.Main;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the Fish class.
 *
 *if the player stays too long in the water a fish bites you and the player loses score
 */
public class FishTest extends BaseTest {

    private Fish fish;
    private Main mockGame;


    @BeforeEach
    public void setUp() {
        mockGame = mock(Main.class);

        fish = new Fish(mockGame);

        mockGame.score = 100f;
        mockGame.foundHiddenEvents = 0;
    }

    /**
     * Test that the Fish constructor properly initializes the object.
     */
    @Test
    public void testFishConstructor() {
        assertNotNull(fish, "Fish should be instantiated");
    }

    /**
     * Test that onPlayerInWaterTooLong reduces score by 10.
     */
    @Test
    public void testOnPlayerInWaterTooLongReducesScore() {
        mockGame.score = 100f;

        fish.onPlayerInWaterTooLong();

        assertEquals(90f, mockGame.score, 0.01f,
            "Score should be reduced by 10 when fish bites player");
    }

    /**
     * Test that score cannot go below zero.
     */
    @Test
    public void testScoreDoesNotGoNegative() {
        mockGame.score = 5f;

        fish.onPlayerInWaterTooLong();

        assertEquals(0f, mockGame.score, 0.01f,
            "Score should not go below zero");
    }

    /**
     * Test that score stays at zero if already at zero.
     */
    @Test
    public void testScoreStaysAtZeroWhenAlreadyZero() {
        mockGame.score = 0f;

        fish.onPlayerInWaterTooLong();

        assertEquals(0f, mockGame.score, 0.01f,
            "Score should remain at zero");
    }

    /**
     * Test that hidden event counter increments on first bite.
     */
    @Test
    public void testHiddenEventCounterIncrementsOnFirstBite() {
        mockGame.foundHiddenEvents = 0;

        fish.onPlayerInWaterTooLong();

        assertEquals(1, mockGame.foundHiddenEvents,
            "Hidden event counter should increment on first fish bite");
    }

    /**
     * Test that fishText flag is set to true on first bite.
     */
    @Test
    public void testFishTextFlagIsSetOnFirstBite() {
        fish.onPlayerInWaterTooLong();

        assertTrue(Fish.fishText,
            "fishText flag should be true after fish bites player");
    }
}
