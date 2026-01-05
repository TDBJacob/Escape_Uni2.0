package io.github.team9.escapefromuni;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Makes a minimap on the side of the screen
 */
public class Minimap {
    public SpriteBatch batch;
    public Texture mapTexture;
    public TextureRegion viewRegion;

    public Texture minimapBorderTexture;
    public Texture playerTexture;

    // How wide should the minimap's view be
    final int VIEW_SIZE = 700;

    /**
     * Loads minimap
     * @param game the game instance
     */
    public Minimap(Main game) {
        batch = game.batch;

        mapTexture = new Texture(Gdx.files.internal("tileMap/map.png"));
        minimapBorderTexture = new Texture(Gdx.files.internal("images/minimapBorder.png"));
        playerTexture = new Texture(Gdx.files.internal("images/minimapPlayer.png"));
        viewRegion = new TextureRegion(mapTexture, VIEW_SIZE, VIEW_SIZE);
    }

    /**
     * Run every frame, generates the minimap and works out where to
     * put the green player icon.
     */
    public void render(int x,int y) {
        int adjX = x - VIEW_SIZE / 2;
        int adjY = mapTexture.getHeight() - y - VIEW_SIZE / 2;

        // The maximum x and y for the minimap
        int maxX = mapTexture.getWidth() - VIEW_SIZE;
        int maxY = mapTexture.getHeight() - VIEW_SIZE;

        // Clamp so that the minimap doesn't go outside the map
        int srcX = MathUtils.clamp(adjX, 0, maxX);
        int srcY = MathUtils.clamp(adjY, 0, maxY);

        viewRegion.setRegion(srcX, srcY, VIEW_SIZE, VIEW_SIZE);
        batch.draw(viewRegion, 640, 265, 120, 120);

        batch.draw(playerTexture, 640+(adjX-srcX)*(120f/VIEW_SIZE), 265+(srcY-adjY)*(120f/VIEW_SIZE), 120, 120);

        batch.draw(minimapBorderTexture, 640, 265, 120, 120);
    }
}
