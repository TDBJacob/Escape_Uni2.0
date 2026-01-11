package io.github.team9.escapefromuni;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.maps.MapLayer;
import io.github.team9.escapefromuni.Player;
import io.github.team9.escapefromuni.AudioManager;
import io.github.team9.escapefromuni.GameScreen;
import com.badlogic.gdx.maps.MapObject;

/**
 * Map Items - the collectable items on the base map
 */
public class mapItems {
    Rectangle bounds;
    String type;
    Float value;
    Boolean collected;
    Float itemX;
    Float itemY;
    TextureRegion itemTexture;

    /**
     * Initialises items, taking information from the Tiled map
     */
    public mapItems(Rectangle itemBounds, String itemType, Float itemValue, Float itemX, Float itemY, TextureRegion itemTexture) {
        this.bounds = itemBounds;
        this.type = itemType;
        this.value = itemValue;
        this.collected = false;
        this.itemX = itemX;
        this.itemY = itemY;
        this.itemTexture = itemTexture;
    }

    public Boolean isCollected() {
        return this.collected;
    }

    public void collected() {
        this.collected = true;
    }

    /**
     *  responsible for executing item behaviour based upon "type" and "value" fields
     */
    public void itemEffect() {
        switch (this.type) {
            case "item":
                Player.hasEssay = true;
                AudioManager.playEssaySound();
                this.collected();
                break;
            case "coin":
                Player.coinCount += this.value;
                AudioManager.playCoinSound();
                this.collected();
                break;
            case "planet":
                Player.itemSpeedBoost *= this.value;
                AudioManager.playPlanetSound();
                this.collected();
                break;
        }
    }
    /**
     * Generates the array for level items
     *
     * @param mapLayer the item layer taken from the Tiled map for the level
     * @return allMapItems the array of item objects to be used
     */
    public static Array<mapItems> generateLevelItems (MapLayer mapLayer) {
        Array<mapItems> allMapItems = new Array<>();
            for (MapObject mObject : mapLayer.getObjects()) {
                if (mObject instanceof TiledMapTileMapObject) {
                    TiledMapTileMapObject itemObject = (TiledMapTileMapObject) mObject;
                    float itemX = itemObject.getX();
                    float itemY = itemObject.getY();
                    float itemWidth = itemObject.getTextureRegion().getRegionWidth();
                    float itemHeight = itemObject.getTextureRegion().getRegionHeight();
                    Rectangle itemBounds = new Rectangle(itemX, itemY, itemWidth, itemHeight);
                    String itemType = itemObject.getProperties().get("type", String.class);
                    Float itemValue = itemObject.getProperties().get("value", Float.class);
                    TextureRegion itemTexture = itemObject.getTextureRegion();
                    allMapItems.add(new mapItems(itemBounds, itemType, itemValue, itemX, itemY, itemTexture));
                }
            }
        return allMapItems;
    }

    /**
     * Responsible for enacting the effects of items upon collision
     *
     * @param mapItems the item array for item logic upon collision
     */
    public static void itemCollision(Array<mapItems> mapItems) {
        for (mapItems item: mapItems) {
            if (!(item.isCollected()) && (Player.getPlayerBounds()).overlaps(item.bounds)) {
                item.itemEffect();
            }
        }
    }

    /**
     * Used to render map items
     *
     * @param mapItems the item array for item rendering
     * @param batch the spritebatch for rendering different objects
     */
    public static void render(Array<mapItems> mapItems, SpriteBatch batch) {
        for (mapItems item: mapItems) {
            if (!(item.isCollected())) {
                batch.draw(item.itemTexture, item.itemX, item.itemY);
            }
        }
    }
}
