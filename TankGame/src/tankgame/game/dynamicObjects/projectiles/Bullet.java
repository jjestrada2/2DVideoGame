package tankgame.game.dynamicObjects.projectiles;

import tankgame.game.dynamicObjects.tanks.Tank;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Bullet extends Projectile {

    public Bullet(float x, float y, float vx, float vy, float angle, float R, BufferedImage img, Tank ownership) {
        super(x, y, vx, vy, angle, R, img, ownership);
    }

    @Override
    public void playSound() {
        this.ownership.getBulletCollideSound().playSound();
    }

    @Override
    public void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);
        super.drawHitbox(g);
    }
}
