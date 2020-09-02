package amc.objects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import amc.BufferedImageLoader;
import amc.GameObject;
import amc.GamePreferences;
import amc.GamePreferencesEnum;

public class Sword extends GameObject {
  
  private static final String SWORD_SPRITE = GamePreferences.getInstance().getStringPreference(GamePreferencesEnum.SWORD_SPRITE);
  
  private BufferedImage swordSprite = BufferedImageLoader.loadImage(SWORD_SPRITE);

  public Sword(int x, int y) {
    super(x, y);
  }

  @Override
  public void render(Graphics graphics) {
    graphics.drawImage(swordSprite, getX(), getY(), null);
  }

  @Override
  public Rectangle getBounds() {
    return new Rectangle(getX(), getY(), swordSprite.getWidth(), swordSprite.getHeight());
  }

  @Override
  public void handlePlayerCollision(Player player) {
    // do nothing
  }

  @Override
  public BufferedImage getIdleImage() {
    return swordSprite;
  }

}
