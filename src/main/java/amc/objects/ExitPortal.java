package amc.objects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import amc.BufferedImageLoader;
import amc.GameObject;
import amc.GamePreferences;
import amc.GamePreferencesEnum;

public class ExitPortal extends GameObject {

  private static final String PORTAL_SPRITE = GamePreferences.getInstance().getStringPreference(GamePreferencesEnum.EXIT_PORTAL_SPRITE);

  private BufferedImage portalSprite = BufferedImageLoader.loadImage(PORTAL_SPRITE);
  
  public ExitPortal(int x, int y) {
    super(x, y);
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
    // do nothing
  }

  @Override
  public BufferedImage getIdleImage() {
    return portalSprite;
  }

}
