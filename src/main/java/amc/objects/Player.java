package amc.objects;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import amc.GameObject;
import amc.ObjectHandler;

public class Player extends GameObjectWithStatusEffects {
  
  private static final long serialVersionUID = 7578342772510568520L;

  private transient ObjectHandler objectHandler;
  
  private int ammo;
  
  private List<GameObject> inventory;
  
  private List<GameObject> currentCollisions;
  
  public Player() {
    this(0, 0, null);
  }
  
  public Player(int x, int y, ObjectHandler objectHandler) {
    super(x, y);
    this.setObjectHandler(objectHandler);
    this.setAmmo(100);
    
    this.setInventory(new ArrayList<>());
    this.setCurrentCollisions(new ArrayList<>());
  }

  @Override
  public void tick() {
    this.setX(this.getX() + (int) (this.getVelocityX() + getSpeedModifier()));
    this.setY(this.getY() + (int) (this.getVelocityY() + getSpeedModifier()));
    
    if(getX() <= 0)
      setX(0);
    if(getY() <= 0)
      setY(0);
    
    collision();
    
    if((!getObjectHandler().isUp()) && (!getObjectHandler().isDown()))
      setVelocityY(0);
    
    if((!getObjectHandler().isRight()) && (!getObjectHandler().isLeft()))
      setVelocityX(0);
    
    if(getObjectHandler().isDown()) 
      setVelocityY(getSpeed());
    
    if(getObjectHandler().isUp()) 
      setVelocityY(getSpeed() * -1);
    
    if(getObjectHandler().isLeft()) 
      setVelocityX(getSpeed() * -1);
    
    if(getObjectHandler().isRight()) 
      setVelocityX(getSpeed());
  }
  
  public void collision() {
    for(int counter = 0; counter < getObjectHandler().getGameObjects().size(); counter ++) {
      GameObject object = getObjectHandler().getGameObjects().get(counter);
      
      if(object.getBounds().intersects(this.getBounds())) {
        if(!object.isPassable()) {
          setX((int) (getX() + getVelocityX() * -1));
          setY((int) (getY() + getVelocityY() * -1));
        }
        object.handlePlayerCollision(this);
      }
    }
  }

  @Override
  public Rectangle getBounds() {
    return new Rectangle(this.getX(), this.getY(), getIdleImage().getWidth(), getIdleImage().getHeight());
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

  public int getAmmo() {
    return ammo;
  }

  public void setAmmo(int ammo) {
    this.ammo = ammo;
  }
  
  public void addInventoryItem(GameObject object) {
    this.getInventory().add(object);
  }
  
  public void removeInventoryItem(GameObject object) {
    this.getInventory().remove(object);
  }

  public List<GameObject> getInventory() {
    return inventory;
  }

  public void setInventory(List<GameObject> inventory) {
    this.inventory = inventory;
  }

  public List<GameObject> getCurrentCollisions() {
    return currentCollisions;
  }

  public void setCurrentCollisions(List<GameObject> currentCollisions) {
    this.currentCollisions = currentCollisions;
  }
}
