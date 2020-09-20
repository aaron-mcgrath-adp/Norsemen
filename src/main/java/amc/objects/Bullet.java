package amc.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import amc.GameObject;
import amc.GamePreferences;
import amc.GamePreferencesEnum;
import amc.ObjectHandler;

public class Bullet extends GameObject {

  private static final long serialVersionUID = -7665747062860123100L;
  private static final int HEIGHT = GamePreferences.getInstance().getIntPreference(GamePreferencesEnum.BULLET_HEIGHT_PIXELS);
  private static final int WIDTH = GamePreferences.getInstance().getIntPreference(GamePreferencesEnum.BULLET_WIDTH_PIXELS);
  private static final int SPEED = GamePreferences.getInstance().getIntPreference(GamePreferencesEnum.BULLET_MOVEMENT_SPEED);
  private static final int DAMAGE = GamePreferences.getInstance().getIntPreference(GamePreferencesEnum.BULLET_DAMAGE);
  
  private ObjectHandler objectHandler;
 
  
  public Bullet(int x, int y, ObjectHandler objectHandler, int mouseX, int mouseY) {
    super(x, y);
    this.setObjectHandler(objectHandler);
    
    this.setVelocityX((mouseX - getX()) / SPEED);
    this.setVelocityY((mouseY - getY()) / SPEED);
  }

  @Override
  public void tick() {
    this.setX(this.getX() + (int) this.getVelocityX());
    this.setY(this.getY() + (int) this.getVelocityY());
    
    GameObject enemyShot = null;
    boolean collisionDetected = false;
    for(int counter = 0; counter < getObjectHandler().getGameObjects().size(); counter ++) {
      GameObject object = getObjectHandler().getGameObjects().get(counter);
      
      if(!(object instanceof Bullet)) {
        try {
        if(object.getBounds().intersects(this.getBounds())) {
          collisionDetected = true;
          if(object instanceof Enemy)
            enemyShot = object;
        }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
      
    if(collisionDetected) {
      getObjectHandler().removeGameObject(this);
    }
    if(enemyShot != null)
      ((Enemy) enemyShot).takeDamage(DAMAGE);
  }

  @Override
  public void render(Graphics graphics) {
    graphics.setColor(Color.pink);
    graphics.fillOval(this.getX(), this.getY(), WIDTH, HEIGHT);
  }

  @Override
  public Rectangle getBounds() {
    return new Rectangle(this.getX(), this.getY(), WIDTH, HEIGHT);
  }

  public ObjectHandler getObjectHandler() {
    return objectHandler;
  }

  public void setObjectHandler(ObjectHandler objectHandler) {
    this.objectHandler = objectHandler;
  }

  @Override
  public void handlePlayerCollision(Player player) {
    // do nothing
  }

  @Override
  public BufferedImage getIdleImage() {
    return null;
  }

}
