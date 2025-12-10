import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import org.junit.jupiter.api.BeforeAll; //Used to allow the setup of code that runs before all the tests
import static org.mockito.Mockito.mock; //Used to create mock objects
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;

public class BaseTest {

    @BeforeAll // Runs before any of the tests
    public static void init() {
        HeadlessApplicationConfiguration hAConfig = new HeadlessApplicationConfiguration();
        new HeadlessApplication(new ApplicationAdapter() {}, hAConfig);

        Gdx.gl = mock(GL20.class); //Creates mock graphic to prevent crashes
        Gdx.gl20 = Gdx.gl;
    }
}
