package amc.objects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import amc.BufferedImageLoader;
import amc.GameObject;
import amc.GamePreferences;
import amc.GamePreferencesEnum;
import amc.ObjectHandler;

public class RedKey extends GameObject {
  
  private ObjectHandler objectHandler;

private static final String RED_KEY_SPRITE = GamePreferences.getInstance().getStringPreference(GamePreferencesEnum.RED_KEY_SPRITE);
  
  private BufferedImage redKeySprite = BufferedImageLoader.loadImage(RED_KEY_SPRITE);

  public RedKey(int x, int y, ObjectHandler objectHandler) {
    super(x, y);
    this.setObjectHandler(objectHandler);
  }

  @Override
  public void render(Graphics graphics) {
    graphics.drawImage(redKeySprite, getX(), getY(), null);
  }

  @Override
  public Rectangle getBounds() {
    return new Rectangle(getX(), getY(), redKeySprite.getWidth(), redKeySprite.getHeight());
  }

  @Override
  public void handlePlayerCollision(Player player) {
    player.getInventory().add(this);
    getObjectHandler().removeGameObject(this);
  }

  public ObjectHandler getObjectHandler() {
    return objectHandler;
  }

  public void setObjectHandler(ObjectHandler objectHandler) {
    this.objectHandler = objectHandler;
  }

  @Override
  public BufferedImage getIdleImage() {
    return redKeySprite;
  }

}
