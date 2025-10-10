package io.github.team6ENG.EscapeUni;

import java.util.List;
import java.util.ArrayList;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.GL20;

/**
 * SimpleLighting system for LibGDX.
 * 
 * This class renders a dark overlay and adds light sources (soft circles)
 * to simulate simple 2D lighting effects.
 * 
 * It uses two blend modes:
 *  - Normal alpha blending for the dark overlay
 *  - Additive blending for light sources
 */
public class SimpleLighting {
    private final SpriteBatch batch;
    private final Texture lightTexture;     // Texture representing a soft radial light
    private final Texture darknessTexture;  // Semi-transparent dark overlay
    private final List<LightSource> lights; // Active light sources

    public SimpleLighting() {
        this.batch = new SpriteBatch();
        this.lights = new ArrayList<>();
        this.lightTexture = createLightTexture();
        this.darknessTexture = createDarknessTexture();
    }

    /**
     * Creates a circular gradient texture for light rendering.
     * The center is bright and fades smoothly to transparent edges.
     */
    private Texture createLightTexture() {
        int size = 256;
        Pixmap pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);

        int center = size / 2;
        float maxRadius = center;

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                float distance = (float) Math.sqrt(
                    Math.pow(x - center, 2) + Math.pow(y - center, 2)
                );

                if (distance <= maxRadius) {
                    // Light intensity decreases with distance
                    float intensity = 1.0f - (distance / maxRadius);
                    intensity = (float) Math.pow(intensity, 2.5f);

                    pixmap.setColor(1, 1, 1, intensity);
                } else {
                    pixmap.setColor(0, 0, 0, 0);
                }
                pixmap.drawPixel(x, y);
            }
        }

        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    /** Updates the position of the first (main) light — e.g., the player's torch. */
    public void setLightPosition(float x, float y) {
        if (!lights.isEmpty()) {
            LightSource mainLight = lights.get(0);
            mainLight.x = x;
            mainLight.y = y;
        }
    }


    /**
     * Creates a full-screen semi-transparent dark texture
     * used to simulate nighttime or low-light environments.
     */
    private Texture createDarknessTexture() {
        
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);

        // Alpha controls the darkness intensity (0 = transparent, 1 = fully black)
        pixmap.setColor(0.1f, 0.05f, 0, 0.94f);
        pixmap.drawPixel(0, 0);

        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    /**
     * Adds a new light source to the lighting system.
     * @param x      X position in world coordinates
     * @param y      Y position in world coordinates
     * @param radius Radius of the light
     * @param color  Color (including intensity via alpha)
     */
    public void addLight(float x, float y, float radius, Color color) {
        lights.add(new LightSource(x, y, radius, color));
    }

    /**
     * Renders the lighting system:
     * 1. Draws a dark overlay
     * 2. Adds light sources using additive blending
     * 
     * @param camera The camera for correct projection
     */
    public void render(OrthographicCamera camera) {
        if (lights.isEmpty()) return;

        System.out.println("=== LIGHTING DEBUG ===");
        System.out.println("Camera center: " + camera.position.x + ", " + camera.position.y);
        System.out.println("Camera viewport: " + camera.viewportWidth + "x" + camera.viewportHeight);
    
        for (LightSource light : lights) {
            System.out.println("Light position: " + light.x + ", " + light.y + " (radius: " + light.radius + ")");
        }

        batch.setProjectionMatrix(camera.combined);

        // Step 1️: Draw semi-transparent darkness layer
        batch.begin();
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.setColor(1, 1, 1, 1);

        float darknessWidth = camera.viewportWidth * 3f;
        float darknessHeight = camera.viewportHeight * 3f;

        batch.draw(
            darknessTexture,
            camera.position.x - darknessWidth / 2,
            camera.position.y - darknessHeight / 2,
            darknessWidth,
            darknessHeight
        );
        batch.end();

        // Step 2️: Draw light sources (additive blending)
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        for (LightSource light : lights) {
            batch.setColor(light.color);
            batch.draw(
                lightTexture,
                light.x - light.radius,
                light.y - light.radius,
                light.radius * 2,
                light.radius * 2
            );
        }
        batch.end();

        // Step 3️: Reset blending to default (optional)
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        System.out.println("=== LIGHTING COMPLETE ===");
    }

    /** Returns all active lights. */
    public List<LightSource> getLights() {
        return lights;
    }

    /** Removes all lights from the scene. */
    public void clearLights() {
        lights.clear();
    }

    /** Frees GPU resources. */
    public void dispose() {
        lightTexture.dispose();
        darknessTexture.dispose();
        batch.dispose();
    }

    /** Represents an individual light in the scene. */
    public static class LightSource {
        public float x, y, radius;
        public Color color;

        public LightSource(float x, float y, float radius, Color color) {
            this.x = x;
            this.y = y;
            this.radius = radius;
            this.color = color;
        }
    }
}
