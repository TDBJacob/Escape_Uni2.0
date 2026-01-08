import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import io.github.team9.escapefromuni.Main;
import io.github.team9.escapefromuni.PositiveEventGuide;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class GuideTest extends BaseTest{

    @Test
    public void testConstructor() {
        Main game = mock(Main.class);
        Vector2 roncooke = new Vector2(100, 100);
        Vector2 langwith = new Vector2(200, 200);
        float radius = 10f;
        BitmapFont font = mock(BitmapFont.class);
        TiledMapTileLayer collisionLayer = new TiledMapTileLayer(10, 10, 8, 8);
        int mapWallsId = 1;
        PositiveEventGuide guide = new PositiveEventGuide(game, roncooke, langwith, radius, font, collisionLayer, mapWallsId);
        assertFalse(guide.isActive());
        assertFalse(guide.isCompleted());
    }

    @Test
    public void testStartStop() {
        Main game = mock(Main.class);
        Vector2 roncooke = new Vector2(100, 100);
        Vector2 langwith = new Vector2(200, 200);
        float radius = 10f;
        BitmapFont font = mock(BitmapFont.class);
        TiledMapTileLayer collisionLayer = new TiledMapTileLayer(10, 10, 8, 8);
        int mapWallsId = 1;
        PositiveEventGuide guide = new PositiveEventGuide(game, roncooke, langwith, radius, font, collisionLayer, mapWallsId);
        guide.start();
        assertTrue(guide.isActive());
        guide.stop();
        assertFalse(guide.isActive());
    }

    @Test
    public void testUpdateCompletion() {
        Main game = mock(Main.class);
        Vector2 roncooke = new Vector2(100, 100);
        Vector2 langwith = new Vector2(200, 200);
        float radius = 10f;
        BitmapFont font = mock(BitmapFont.class);
        TiledMapTileLayer collisionLayer = new TiledMapTileLayer(10, 10, 8, 8);
        int mapWallsId = 1;
        PositiveEventGuide guide = new PositiveEventGuide(game, roncooke, langwith, radius, font, collisionLayer, mapWallsId);
        guide.start();
        guide.update(95, 95); // Within radius
        assertTrue(guide.isActive()); // Still active, stage 1
        guide.update(195, 195); // Within radius
        assertTrue(guide.isCompleted());
        assertFalse(guide.isActive());
        // Try to start again after completion
        guide.start();
        assertFalse(guide.isActive());
    }
}
