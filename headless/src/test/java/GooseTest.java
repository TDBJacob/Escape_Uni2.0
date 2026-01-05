import io.github.team9.escapefromuni.Goose;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Answers.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GooseTest extends BaseTest { //needed as a base to create other tests

    private static Goose goose;

    @BeforeEach
    public void setup() {
        goose = mock(Goose.class, CALLS_REAL_METHODS);

    }

    @Test
    public void testAttackMode() {
        goose.attackMode();

        assertTrue(goose.attackModeActivated);
        assertEquals(0, goose.idleDistance);
    }

    @Test
    public void testDefaultDimensionsWhenNoFrame() {
        goose.currentGooseFrame = null;

        assertEquals(16f, goose.getWidth());
        assertEquals(16f, goose.getHeight());
    }
}
