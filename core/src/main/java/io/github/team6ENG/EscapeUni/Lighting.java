package io.github.team6ENG.EscapeUni;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.GL20;

/**
 *
 * This class renders a dark overlay and adds light sources (circles)
 * to simulate 2D lighting effects.
 *
 */
public class Lighting {
    private HashMap<String, LightSource> lights = new HashMap<String, LightSource>();

    public Lighting() {
    }
    static class LightSource{
        float circleX;
        float circleY;
        Color colour;
        int radius;
        boolean isVisible;
        protected LightSource(float circleX, float circleY, Color colour, int radius){
            this.circleX = circleX;
            this.circleY = circleY;
            this.colour = colour;
            this.radius = radius;
            isVisible = true;
        }
    }

    public void addLightSource(String lightName, float circleX, float circleY, Color colour, int radius){
        lights.put(lightName, new LightSource(circleX, circleY, colour, radius));
    }
    public void removeLightSource(String lightName){
        lights.remove(lightName);
    }
    public void clearLightSources(){
        lights.clear();
    }
    public void updateLightSource(String lightName, float circleX, float circleY){
        lights.get(lightName).circleX = circleX;
        lights.get(lightName).circleY = circleY;

    }
    public void adjustRadius(String lightName, int radius){
        lights.get(lightName).radius = radius;
    }
    public void isVisible(String lightName, boolean isVisible){
        lights.get(lightName).isVisible = isVisible;
    }

    /**
     * Renders the lighting system:
     * 1. Draws a dark overlay
     * 2. Adds transparent circle
     *
     * @param camera The camera
     * @param mapWidth Dimensions of world map for drawing darkness
     * @param mapHeight Dimensions of world map for drawing darkness
     */

    public Texture render(OrthographicCamera camera, int mapWidth, int mapHeight) {

        Pixmap pixmap = new Pixmap(mapWidth,mapHeight, Pixmap.Format.RGBA8888);

        // Fill entire pixmap with semi-transparent black
        pixmap.setColor(0f, 0f, 0f, 0.85f);
        pixmap.fill();

        pixmap.setBlending(Pixmap.Blending.None);

        for(String l : lights.keySet()) {
            if(lights.get(l).isVisible) {
                pixmap.setColor(lights.get(l).colour);
                pixmap.fillCircle((int) (lights.get(l).circleX), mapHeight - (int) (lights.get(l).circleY), lights.get(l).radius);
            }
        }
        Texture texture = new Texture(pixmap);

        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        pixmap.dispose();

        return texture;

    }




}
