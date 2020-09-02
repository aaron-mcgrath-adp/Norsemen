package amc.objects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import amc.BufferedImageLoader;
import amc.GameObject;
import amc.GamePreferences;
import amc.GamePreferencesEnum;

public class WallBlock extends GameObject {
  
  private static final int SIZE = GamePreferences.getInstance().getIntPreference(GamePreferencesEnum.WALLBLOCK_SIZE_PIXELS);
  private static final String WALL_SPRITE = GamePreferences.getInstance().getStringPreference(GamePreferencesEnum.WALLBLOCK_SPRITE);
  
  private BufferedImage wallSprite = BufferedImageLoader.loadImage(WALL_SPRITE);
  
  public WallBlock(int x, int y) {
    super(x, y);
  }

  @Override
  public void tick() {
    this.setX(this.getX() + (int) this.getVelocityX());
    this.setY(this.getY() + (int) this.getVelocityY());
  }

  @Override
  public void render(Graphics graphics) {
    graphics.drawImage(wallSprite, getX(), getY(), null);
  }

  @Override
  public Rectangle getBounds() {
    return new Rectangle(this.getX(), this.getY(), SIZE, SIZE);
  }

  @Override
  public void handlePlayerCollision(Player player) {
    // do nothing
  }

  @Override
  public BufferedImage getIdleImage() {
    return wallSprite;
  }

}
