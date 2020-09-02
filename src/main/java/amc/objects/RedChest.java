package amc.objects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import amc.BufferedImageLoader;
import amc.GameObject;
import amc.GamePreferences;
import amc.GamePreferencesEnum;

public class RedChest extends GameObject {
  
  private static final String CHEST_CLOSED_SPRITE = GamePreferences.getInstance().getStringPreference(GamePreferencesEnum.RED_CHEST_CLOSED_SPRITE);
  private static final String CHEST_OPEN_SPRITE = GamePreferences.getInstance().getStringPreference(GamePreferencesEnum.RED_CHEST_OPEN_SPRITE);

  private BufferedImage closedSprite = BufferedImageLoader.loadImage(CHEST_CLOSED_SPRITE);
  private BufferedImage openSprite = BufferedImageLoader.loadImage(CHEST_OPEN_SPRITE);
  
  private boolean isOpened;
  
  private BufferedImage currentSprite;

  public RedChest(int x, int y) {
    super(x, y);
    setOpened(false);
    currentSprite = closedSprite;
  }

  @Override
  public void render(Graphics graphics) {
    graphics.drawImage(currentSprite, getX(), getY(), null);
  }

  @Override
  public Rectangle getBounds() {
    return new Rectangle(getX(), getY(), currentSprite.getWidth(), currentSprite.getHeight());
  }

  @Override
  public void handlePlayerCollision(Player player) {
    if(!isOpened()) {
      
      for(int counter = 0; counter < player.getInventory().size(); counter ++) {
        GameObject object = player.getInventory().get(counter);
        if(object instanceof RedKey) {
          player.setAmmo(player.getAmmo() + 25);
          setOpened(true);
          player.removeInventoryItem(object);
          currentSprite = openSprite;
        }
      }
      if(!isOpened()) {
        // show a key thought bubble?
      }
    }
    
  }

  public boolean isOpened() {
    return isOpened;
  }

  public void setOpened(boolean isOpened) {
    this.isOpened = isOpened;
  }

  @Override
  public BufferedImage getIdleImage() {
    return currentSprite;
  }

}
