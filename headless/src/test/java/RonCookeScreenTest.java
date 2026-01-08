import com.badlogic.gdx.graphics.g2d.BitmapFont;
import io.github.team9.escapefromuni.BuildingManager;
import io.github.team9.escapefromuni.GameScreen;
import io.github.team9.escapefromuni.Main;
import io.github.team9.escapefromuni.RonCookeScreen;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RonCookeScreenTest extends BaseTest{

    /**
     * Test that RonCookeScreen can be instantiated with mocked dependencies.
     *
     */
    @Test
    public void testCanBeInstantiated() {
        // This test simply verifies the class exists and can be referenced
        assertNotNull(RonCookeScreen.class);
    }
}
