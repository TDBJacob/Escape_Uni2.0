import io.github.team9.escapefromuni.Achievement;
import io.github.team9.escapefromuni.Goose;
import io.github.team9.escapefromuni.Main;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Answers.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;

public class AchievementTest extends BaseTest { //needed as a base to create other tests

    private static Main testMain;
    private static Achievement achievement;

    @BeforeEach
    public void setup() {
        testMain = mock(Main.class);

        achievement = mock(Achievement.class, CALLS_REAL_METHODS);
    }

    @Test
    public void testUnlock() {
        achievement.unlock(testMain);

        assertTrue(achievement.unlocked);
    }

}
