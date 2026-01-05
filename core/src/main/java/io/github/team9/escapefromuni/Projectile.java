package io.github.team9.escapefromuni;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

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

    public Projectile(Main game, int originX, int originY, double velX, double velY, int angle) {
        x = originX;
        y = originY;
        this.velX = velX;
        this.velY = velY;
        this.game = game;

        projSize = 15;
        hasHit = false;
        framesAlive = 0;

        projSprite = new Sprite(new Texture("images/BirdProjectile.png"));
        projSprite.setBounds((int)x,(int)y,projSize,projSize);
        projSprite.setRotation(angle+90);
    }

    public Sprite update() {
        framesAlive += 1;

        x += velX;
        y += velY;

        projSprite.setBounds((int)x,(int)y,projSize,projSize);

        projSprite.draw(game.batch);

        return projSprite;
    }

}
