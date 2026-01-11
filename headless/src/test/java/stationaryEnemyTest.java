import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import io.github.team9.escapefromuni.AudioManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test; //Needed to create test methods
import static org.junit.jupiter.api.Assertions.assertEquals; //Needed for assert equals
import static org.mockito.Mockito.mock;
import io.github.team9.escapefromuni.Main;


public class stationaryEnemyTest extends BaseTest { //needed as a base to create other tests
    private static Main testMain;

    @BeforeAll
    public static void testSetup() {

    }

    @Test //explanation here
    public void testPlayMusic() { //music plays automatically in the constructor
    }

}
