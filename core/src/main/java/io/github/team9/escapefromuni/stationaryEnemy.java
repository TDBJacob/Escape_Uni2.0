package io.github.team9.escapefromuni;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;

import io.github.team9.escapefromuni.Main;

/**
 * Stationary enemies - the stationary enemies blocking paths on the map
 */
public class stationaryEnemy {
    Rectangle bounds;
    String type;
    Boolean isDefeated;
    Float enemyX;
    Float enemyY;
    TextureRegion enemyTexture;

    /**
     * Initialises stationary enemies, taking information from the Tiled map
     */
    public stationaryEnemy(Rectangle enemyBounds, String enemyType, Float enemyX, Float enemyY, TextureRegion enemyTexture) {
        this.bounds = enemyBounds;
        this.type = enemyType;
        this.isDefeated = false;
        this.enemyX = enemyX;
        this.enemyY = enemyY;
        this.enemyTexture = enemyTexture;
    }

    public Boolean isDefeated() {
        return this.isDefeated;
    }

    public void defeated() {
        this.isDefeated = true;
    }

    /**
     * responsible for enacting enemy behaviour depending upon enemy type
     */
    public void enemyBehaviour() {
        switch (this.type) {
            case "enemy_dean":
                if ((Player.hasEssay == true) && (Gdx.input.isKeyJustPressed(Input.Keys.J))) {
                    Player.hasEssay = false;
                    this.defeated();
                    AudioManager.playDeanSound();
                }
                if ((Player.coinCount >= 6) && (Gdx.input.isKeyJustPressed(Input.Keys.L))) {
                    Player.coinCount -= 6;
                    this.defeated();
                    AudioManager.playDeanSound();
                }
                break;
        }
    }

    /**
     * Generates the array for stationary enemy objects
     *
     * @param mapLayer the item layer taken from the Tiled map for the level
     * @return allMapEnemies the array of stationary enemy objects to be used
     */
    public static Array<stationaryEnemy> generateLevelStatEnemies(MapLayer mapLayer) {
        Array<stationaryEnemy> allMapEnemies = new Array<>();
        for (MapObject mObject : mapLayer.getObjects()) {
            if (mObject instanceof TiledMapTileMapObject) {
                TiledMapTileMapObject enemyObject = (TiledMapTileMapObject) mObject;
                float enemyX = enemyObject.getX();
                float enemyY = enemyObject.getY();
                float enemyWidth = enemyObject.getTextureRegion().getRegionWidth();
                float enemyHeight = enemyObject.getTextureRegion().getRegionHeight();
                Rectangle enemyBounds = new Rectangle(enemyX, enemyY, (enemyWidth * 0.8f), (enemyHeight * 0.8f));
                String enemyType = enemyObject.getProperties().get("type", String.class);
                TextureRegion enemyTexture = enemyObject.getTextureRegion();
                allMapEnemies.add(new stationaryEnemy(enemyBounds, enemyType, enemyX, enemyY, enemyTexture));
            }
        }
        return allMapEnemies;
    }

    /**
     * Responsible for enacting stationary enemy behaviour
     *
     * @param mapStatEnemies the enemy object array
     */
    public static void statEnemyCollision(Array<stationaryEnemy> mapStatEnemies) {
        for (stationaryEnemy enemy : mapStatEnemies) {
            if (!(enemy.isDefeated) && (Player.getPlayerBounds()).overlaps(enemy.bounds)) {
                enemy.enemyBehaviour();
                Player.setPlayerX(Player.oldX);
                Player.setPlayerY(Player.oldY);
            }
            if (("enemy_dean".equals("enemy_dean")) && (Main.inRange(80, enemy.enemyX, enemy.enemyY, Player.getPlayerX(), Player.getPlayerY()))) {
                enemy.enemyBehaviour();
            }
        }
    }

    /**
     * Used to render stationary enemies
     *
     * @param mapStatEnemies the array of enemies to be rendered
     * @param batch the spritebatch used for rendering objects
     * @param font the font used for enemy text
     */
    public static void render(Array<stationaryEnemy> mapStatEnemies, SpriteBatch batch, BitmapFont font) {
        for (stationaryEnemy statEnemy : mapStatEnemies) {
            if (!(statEnemy.isDefeated)) {
                batch.draw(statEnemy.enemyTexture, statEnemy.enemyX, statEnemy.enemyY);
                if (("enemy_dean".equals(statEnemy.type)) && Main.inRange(80, statEnemy.enemyX, statEnemy.enemyY, Player.getPlayerX(), Player.getPlayerY())) {
                    font.draw(batch, "YOU'RE NOT GOING ANYWHERE!!!", statEnemy.enemyX - 90, statEnemy.enemyY + 70);
                    font.draw(batch, "Press \"J\" to give your essay or \"L\" to give 6 coins", statEnemy.enemyX - 140, statEnemy.enemyY + 50);
                }
            }
        }
    }
}

