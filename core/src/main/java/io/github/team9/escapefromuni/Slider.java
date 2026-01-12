package io.github.team9.escapefromuni;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Slider {

    private Texture texture = new Texture("sprites/Cone.PNG");
    private Sprite sprite = new Sprite(this.texture);
    private Rectangle sliderRectangle;
    private float speed = 10f;
    private int y;
    private float initialX;
    private float x1;
    private float x2;

    public Slider(int y, float x1, float x2) {
        if (0 > y || y > 200) {
            throw new IllegalArgumentException();
        }
        this.y = y;
        if (0 > x1 || x1 > 200) {
            throw new IllegalArgumentException();
        }
        if (0 > x2 || x2 > 200) {
            throw new IllegalArgumentException();
        }
        this.x1 = x1;
        this.initialX = x1;
        this.x2 = x2;
        this.sprite.setX(x1);
        this.sprite.setY(y);
        this.sliderRectangle = new Rectangle(x1, y, this.sprite.getWidth(), this.sprite.getHeight());
    }

    private boolean collides(Player player) {
        Rectangle playerRectangle = new Rectangle(player.sprite.getX(), player.sprite.getY(), player.sprite.getWidth(), player.sprite.getHeight());
        return this.sliderRectangle.overlaps(playerRectangle);
    }

    public void update(float deltaTime, Player player) {
        float updatedX = this.sprite.getX() + (speed * deltaTime);
        if (updatedX < this.x1) {
            updatedX = this.x1;
            this.reverseMovement();
        }
        else if(updatedX + this.sprite.getWidth() + (this.speed * deltaTime) > this.x2) {
            updatedX = this.x2 - this.sprite.getWidth();
            this.reverseMovement();
        }
        this.sprite.setX(updatedX);
        this.disallowMovement(player);
    }

    public void reverseMovement() {
        this.speed = -this.speed;
    }

    public void disallowMovement(Player player) {
        if (collides(player)) {
            player.sprite.setX(player.oldX);
            player.sprite.setY(player.oldY);
        }
    }

    public void draw(SpriteBatch batch) {
        this.sprite.draw(batch);
    }

    // update
    // draw

}
