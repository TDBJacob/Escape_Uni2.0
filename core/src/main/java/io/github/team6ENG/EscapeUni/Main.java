package io.github.team6ENG.EscapeUni;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Main extends Game{

    public SpriteBatch batch;
    public BitmapFont menuFont;
    public FitViewport viewport;
    public Skin buttonSkin;

    public void create() {
        batch = new SpriteBatch();
        // use libGDX's default font
        menuFont = new BitmapFont(Gdx.files.internal("fonts/menuScreenFont.fnt"));
        viewport = new FitViewport(8, 5);

        //Scale font to our viewport by ratio of viewport height to screen height
        menuFont.setUseIntegerPositions(false);
        menuFont.getData().setScale(viewport.getWorldHeight() / Gdx.graphics.getHeight());
        menuFont.setColor(Color.valueOf("4287f5FF"));

        buttonSkin = new Skin(Gdx.files.internal("skins/uiskin.json"));

        this.setScreen(new MainMenuScreen(this));
    }

    public void render() {
        super.render();
    }

    public void dispose() {
        batch.dispose();
        menuFont.dispose();
    }

}
