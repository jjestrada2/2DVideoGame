package warzone.game.stationaryObjects.powerups;

import warzone.constants.ResourcesConstants;
import warzone.game.GameObject;
import warzone.game.moveableObjects.tanks.Tank;
import warzone.loaders.ResourcesManager;
import warzone.util.Sound;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Speed extends PowerUp {


    public Speed(float x, float y, BufferedImage img) {
        super(x, y, img);
    }

    @Override
    public void empower(Tank tank) {
        tank.changeSpeed(4);
    }

    @Override
    public void playSound() {
        (new Sound(ResourcesManager.getSound(ResourcesConstants.SOUND_PICKUP))).playSound();
    }
}
