package io.github.team6ENG.EscapeUni;

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
 * SimpleLighting system for LibGDX.
 *
 * This class renders a dark overlay and adds light sources (soft circles)
 * to simulate simple 2D lighting effects.
 *
 * It uses two blend modes:
 *  - Normal alpha blending for the dark overlay
 *  - Additive blending for light sources
 */
public class Lighting {
    private static final boolean DEBUG = false;
    private final SpriteBatch batch;
    private final Texture lightTexture;     // Texture representing a soft radial light
    private final Texture darknessTexture;  // Semi-transparent dark overlay

    private final List<LightSource> lights; // Active light sources
    private final List<Integer> lightsToRemove;

    public Lighting() {
        this.batch = new SpriteBatch();
        this.lights = new ArrayList<LightSource>();
        this.lightTexture = createLightTexture();
        this.darknessTexture = createDarknessTexture();
        this.lightsToRemove = new ArrayList<Integer>();
    }

    public void safeRemoveLight(int index) {
        synchronized (this) {
            if (index >= 0 && index < lights.size()) {
                lights.remove(index);
                if (DEBUG) System.out.println("removeLight -> total lights: " + lights.size());
            }
        }
    }

    public void updateLights() {
        synchronized (lights) {
            if (!lightsToRemove.isEmpty()) {
                lightsToRemove.sort(Collections.reverseOrder());
                for (int index : lightsToRemove) {
                    if (index >= 0 && index < lights.size()) {
                        lights.remove(index);
                    }
                }

                lightsToRemove.clear();
            }
        }
    }

    /**
     * Creates a circular gradient texture for light rendering.
     * The center is bright and fades smoothly to transparent edges.
     */
    private Texture createLightTexture() {
        int size = 256;
        Pixmap pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        try {
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
            return new Texture(pixmap);
        } finally {
            pixmap.dispose();

        }
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
        pixmap.setColor(0f, 0f, 0, 0.90f);
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
        synchronized (lights) {
            lights.add(new LightSource(x, y, radius, color));
            if (DEBUG) System.out.println(" addLight -> total lights: " + lights.size());
        }
    }

    /**
     * Renders the lighting system:
     * 1. Draws a dark overlay
     * 2. Adds light sources using additive blending
     *
     * @param camera The camera for correct projection
     */
    private SpriteBatch lightingBatch = new SpriteBatch();

    public void render(OrthographicCamera camera) {
        if (lights.isEmpty()) return;

        try {
            lightingBatch.setProjectionMatrix(camera.combined);

            // Step 1️: Draw semi-transparent darkness layer
            lightingBatch.begin();
            lightingBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            lightingBatch.setColor(1, 1, 1, 1);

            float darknessWidth = camera.viewportWidth * 3f;
            float darknessHeight = camera.viewportHeight * 3f;

            lightingBatch.draw(
                darknessTexture,
                camera.position.x - darknessWidth / 2,
                camera.position.y - darknessHeight / 2,
                darknessWidth,
                darknessHeight
            );
            lightingBatch.end();

            lightingBatch.begin();
            lightingBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
            for (LightSource light : lights) {
                lightingBatch.setColor(light.color);
                lightingBatch.draw(
                    lightTexture,
                    light.x - light.radius,
                    light.y - light.radius,
                    light.radius * 2,
                    light.radius * 2
                );
            }
            lightingBatch.end();

            Gdx.gl.glFlush();

        } catch (Exception e) {
            Gdx.app.error("Lighting", "Lighting render failed", e);

        }


        // Step 3️: Reset blending to default (optional)
        lightingBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        if (DEBUG) System.out.println("=== LIGHTING COMPLETE ===");
    }

    /** Returns all active lights. */
    public List<LightSource> getLights() {
        synchronized (lights) {
            return Collections.unmodifiableList(lights);
        }
    }

    /** Removes all lights from the scene. */
    public void clearLights() {
        synchronized (lights) {
            lights.clear();
            lightsToRemove.clear();
        }

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
