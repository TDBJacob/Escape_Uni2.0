package io.github.team9.escapefromuni;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Represents and controls projectiles
 */
public class Projectile {

    public double x;
    public double y;
    public double velX;
    public double velY;

    public int framesAlive;

    public boolean hasHit;

    public Sprite projSprite;

    public float projSize;

    public Main game;

    /**
     * Instantiate a projectile
     * @param game the game instance
     * @param originX the starting x of the projectile
     * @param originY the starting y of the projectile
     * @param velX the projectile's x velocity
     * @param velY the projectile's y velocity
     * @param angle the projectile's angle, used to rotate the sprite
     */
    public Projectile(Main game, int originX, int originY, double velX, double velY, int angle) {
        x = originX;
        y = originY;
        this.velX = velX;
        this.velY = velY;
        this.game = game;

        projSize = 15;
        hasHit = false;
        framesAlive = 0;

        // Create the sprite of the projectile
        projSprite = new Sprite(new Texture("images/BirdProjectile.png"));
        projSprite.setBounds((int)x,(int)y,projSize,projSize);
        projSprite.setRotation(angle+90);
    }

    /**
     * Update the position of the projectile based on its velocity
     * each frame
     * @param dt delta time
     */
    public Sprite update(float dt) {
        framesAlive += 1;

        // *dt so that projectiles move consistently at different framerates
        x += velX*dt;
        y += velY*dt;

        // Move the sprite and draw it
        projSprite.setBounds((int)x,(int)y,projSize,projSize);
        projSprite.draw(game.batch);

        return projSprite;
    }

}
