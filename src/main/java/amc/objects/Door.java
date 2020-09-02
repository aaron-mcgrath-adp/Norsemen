package amc.objects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import amc.BufferedImageLoader;
import amc.GameObject;
import amc.GamePreferences;
import amc.GamePreferencesEnum;

public class Door extends GameObject {
  
  private static final String DOOR_SPRITE = GamePreferences.getInstance().getStringPreference(GamePreferencesEnum.DOOR_SPRITE);

  private BufferedImage doorSprite = BufferedImageLoader.loadImage(DOOR_SPRITE);

  public Door(int x, int y) {
    super(x, y);
  }

  @Override
  public void render(Graphics graphics) {
    graphics.drawImage(doorSprite, getX(), getY(), null);
  }

  @Override
  public Rectangle getBounds() {
    return new Rectangle(getX(), getY(), doorSprite.getWidth(), doorSprite.getHeight());
  }

  @Override
  public void handlePlayerCollision(Player player) {
    // do nothing
  }

  @Override
  public BufferedImage getIdleImage() {
    return doorSprite;
  }

}
