import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import io.github.team9.escapefromuni.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class BuildingManagerTest extends BaseTest { //needed as a base to create other tests

    private static Main game;
    private static GameScreen gameScreen;
    private static Player player;
    private static AudioManager audioManager;
    private static BuildingManager buildingManagerTest;

    @BeforeAll
    public static void testSetup() {
        gameScreen = mock(GameScreen.class);
        game = mock(Main.class);
        player = mock(Player.class);
        audioManager = new AudioManager(game);
        buildingManagerTest = new BuildingManager(game,gameScreen,player,audioManager);


    }

    @Test //marks test code and is needed before every block of test code
    public void otherExampleTestCode() {
        int otherNum;
        otherNum = 15;
        assertEquals(15, otherNum);
    }

    @Test //marks test code and is needed before every block of test code
    public void playSoundTest() {
        int otherNum;
        otherNum = 15;
        assertEquals(15, otherNum);
    }
}

//If you take the stuff down here, it's a good template

//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test; //Needed to create test methods
//import static org.junit.jupiter.api.Assertions.assertEquals; //Needed for assert equals
//
//public class CharacterSelectScreenTest extends BaseTest { //needed as a base to create other tests
//
//    @BeforeAll (this bit is optional)
//    public static void testSetup() {
//        //insert code here
//    }
//
//    @Test //marks test code and is needed before every block of test code
//    public void exampleTestCode() {
//        //insert code here
//    }
//}
