package amc.objects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import amc.BufferedImageLoader;
import amc.GameObject;
import amc.GamePreferences;
import amc.GamePreferencesEnum;
import amc.ObjectHandler;

public class EntryPortal extends GameObject {
  
  private static final String PORTAL_SPRITE = GamePreferences.getInstance().getStringPreference(GamePreferencesEnum.ENTRY_PORTAL_SPRITE);

  private BufferedImage portalSprite = BufferedImageLoader.loadImage(PORTAL_SPRITE);
  
  private ObjectHandler objectHandler;
  
  public EntryPortal(int x, int y, ObjectHandler objectHandler) {
    super(x, y);
    this.setObjectHandler(objectHandler);
  }

  @Override
  public void render(Graphics graphics) {
    graphics.drawImage(portalSprite, getX(), getY(), null);
  }

  @Override
  public Rectangle getBounds() {
    return new Rectangle(getX(), getY(), portalSprite.getWidth(), portalSprite.getHeight());
  }

  @Override
  public void handlePlayerCollision(Player player) {
    getObjectHandler().getGameObjects().forEach(object -> {
      if(object instanceof ExitPortal) {
        player.setX(object.getX());
        player.setY(object.getY());
      }
    });
  }

  public ObjectHandler getObjectHandler() {
    return objectHandler;
  }

  public void setObjectHandler(ObjectHandler objectHandler) {
    this.objectHandler = objectHandler;
  }

  @Override
  public BufferedImage getIdleImage() {
    return portalSprite;
  }

}
