import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessFiles;
import io.github.team9.escapefromuni.Leaderboard;
import io.github.team9.escapefromuni.LeaderboardEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LeaderboardTest {

    @BeforeEach
    public void setup() {
        Gdx.files = new HeadlessFiles();
    }

    @Test
    public void testConstructorWorks() {
        Leaderboard testLeaderboard = new Leaderboard("leaderboard.json");
        assertNotNull(testLeaderboard);
    }
}
