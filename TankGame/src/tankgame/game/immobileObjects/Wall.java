package tankgame.game.immobileObjects;

import tankgame.game.Collidable;
import tankgame.game.immobileObjects.ImmobileObject;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Wall extends ImmobileObject implements Collidable {
    protected boolean isDestroyed = false;

    public Wall(float x, float y, BufferedImage img) {
        super(x, y, img);
    }


    @Override
    public void drawImage(Graphics g) {
        g.drawImage(super.img, (int) x, (int) y, null);
        super.drawHitbox(g);
    }

    @Override
    public void update() {}

    @Override
    public void handleCollision(Collidable obj) {}

    @Override
    public boolean isCollidable() {
        return true;
    }

    public abstract void setDestroyed(boolean destroyed);

    public boolean getIsDestroyed() {
        return this.isDestroyed;
    }

}
