import io.github.team9.escapefromuni.CharacterSelectScreen;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test; //Needed to create test methods
import static org.junit.jupiter.api.Assertions.assertEquals; //Needed for assert equals
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import io.github.team9.escapefromuni.AudioManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test; //Needed to create test methods
import static org.junit.jupiter.api.Assertions.assertEquals; //Needed for assert equals
import com.badlogic.gdx.Gdx;
import static org.mockito.Mockito.mock;
import io.github.team9.escapefromuni.Main;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

public class CharacterSelectScreenTest extends BaseTest { //needed as a base to create other tests
    private static Main testMain;
    private static CharacterSelectScreen testCSelectScreen;
    private static Stage testStage;
    private static Viewport testViewport;

    @BeforeAll
    public static void testSetup() {
        testMain = mock(Main.class);
        testStage = mock(Stage.class);
        testViewport = mock(Viewport.class);
        testCSelectScreen = mock(CharacterSelectScreen.class);
        testCSelectScreen.stage = testStage;
        when(testStage.getViewport()).thenReturn(testViewport);

        doCallRealMethod().when(testCSelectScreen).resize(anyInt(), anyInt());
        doCallRealMethod().when(testCSelectScreen).dispose();
    }

//    @Test //as far as I know it's too closely linked with graphic to test, maybe refactor some code to implement it later.
//    public void testResize() {
//        testCSelectScreen.resize(100, 100);
//        verify(testViewport, times(1)).update(100, 100, true);
//    }

    @Test //marks test code and is needed before every block of test code
    public void testDispose() {
        testCSelectScreen.dispose();
        verify(testStage, times(1)).dispose();
    }
}
