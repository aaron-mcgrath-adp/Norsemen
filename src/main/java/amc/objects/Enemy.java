package amc.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import amc.ObjectHandler;

public class Enemy extends GameObjectWithStatusEffects {
  
  private int damage;
  
  private ObjectHandler objectHandler;
  
  public Enemy(int x, int y, ObjectHandler objectHandler) {
    super(x, y);
    this.setObjectHandler(objectHandler);
  }

  public int getDamage() {
    return damage;
  }

  public void setDamage(int damage) {
    this.damage = damage;
  }
  
  public void takeDamage(int damage) {
    this.setHitPoints(this.getHitPoints() - damage);
    if(this.getHitPoints() <= 0)
      getObjectHandler().removeGameObject(this);
  }
  
  @Override
  public void render(Graphics graphics) {
    graphics.setColor(Color.red);
    graphics.fillRect(getX() - 10, getY() - 15, 50, 10);
    
    int healthRemaining = (100 / getHitPoints()) * this.getHealth();
    
    graphics.setColor(Color.green);
    graphics.fillRect(getX() - 10, getY() - 15, healthRemaining / 2, 10);
    
    graphics.drawImage(getIdleImage(), getX(), getY(), null);
  }

  @Override
  public Rectangle getBounds() {
    return new Rectangle(getX(), getY(), getIdleImage().getWidth(), getIdleImage().getHeight());
  }

  public ObjectHandler getObjectHandler() {
    return objectHandler;
  }

  public void setObjectHandler(ObjectHandler objectHandler) {
    this.objectHandler = objectHandler;
  }

}
