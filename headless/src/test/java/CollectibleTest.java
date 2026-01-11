import io.github.team9.escapefromuni.AudioManager;
import io.github.team9.escapefromuni.Collectible;
import io.github.team9.escapefromuni.Main;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test; //Needed to create test methods
import io.github.team9.escapefromuni.Player;
import static org.junit.jupiter.api.Assertions.assertEquals; //Needed for assert equals
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class CollectibleTest extends BaseTest { //needed as a base to create other tests
    private static Player testPlayer;
    private static Collectible testCollectible;
    private static Main testMain;
    private static AudioManager testAudioManager;
    private static String testOriginScreen;

    @BeforeAll //(this bit is optional)
    public static void testSetup() {
        testPlayer = mock(Player.class);
        testMain = mock(Main.class);
        testAudioManager = mock(AudioManager.class);
        testOriginScreen = "testOriginScreen";
        testCollectible = new Collectible(testMain, "test", 10f, 10f, 0.03f, true, testOriginScreen, testAudioManager); //the real collectible is instantiated here
        when(testPlayer.getPlayerX()).thenReturn(10f);
        when(testPlayer.getPlayerX()).thenReturn(10f);
    }

    @Test
    public void checkInRangeTest() {
        assertTrue(testCollectible.checkInRange(testPlayer.getPlayerX(), testPlayer.getPlayerY()));
    }

    @Test //marks test code and is needed before every block of test code
    public void collectTest() {
        testCollectible.Collect();
        assertTrue(testCollectible.getPlayerHas());
    }

//    @Test //audio tests cause issues due to headless so unimplemented
//    public void playSoundTest() {
//        //empty currently
//    }

}
