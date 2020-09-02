package amc.objects;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import amc.BufferedImageLoader;
import amc.GameObject;
import amc.GamePreferences;
import amc.GamePreferencesEnum;
import amc.ObjectHandler;

public class Player extends GameObjectWithStatusEffects {
  
  private ObjectHandler objectHandler;

  private static final int HEIGHT = GamePreferences.getInstance().getIntPreference(GamePreferencesEnum.PLAYER_HEIGHT_SIZE_PIXELS);
  private static final int WIDTH = GamePreferences.getInstance().getIntPreference(GamePreferencesEnum.PLAYER_WIDTH_SIZE_PIXELS);
  private static final int SPEED = GamePreferences.getInstance().getIntPreference(GamePreferencesEnum.PLAYER_MOVEMENT_SPEED);
  private static final int HIT_POINTS = GamePreferences.getInstance().getIntPreference(GamePreferencesEnum.PLAYER_HIT_POINTS);
  private static final String PLAYER_SPRITE = GamePreferences.getInstance().getStringPreference(GamePreferencesEnum.PLAYER_SPRITE);
  
  private BufferedImage playerSprite = BufferedImageLoader.loadImage(PLAYER_SPRITE);
  
  private int ammo;
  
  private List<GameObject> inventory;
  
  private List<GameObject> currentCollisions;
  
  public Player(int x, int y, ObjectHandler objectHandler) {
    super(x, y);
    this.setObjectHandler(objectHandler);
    this.setHealth(HIT_POINTS);
    this.setHitPoints(HIT_POINTS);
    this.setAmmo(100);
    
    this.setInventory(new ArrayList<>());
    this.setCurrentCollisions(new ArrayList<>());
    this.addInventoryItem(new Sword(0, 0));
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
      setVelocityY(SPEED);
    
    if(getObjectHandler().isUp()) 
      setVelocityY(SPEED * -1);
    
    if(getObjectHandler().isLeft()) 
      setVelocityX(SPEED * -1);
    
    if(getObjectHandler().isRight()) 
      setVelocityX(SPEED);
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

  @Override
  public BufferedImage getIdleImage() {
    return playerSprite;
  }

  public List<GameObject> getCurrentCollisions() {
    return currentCollisions;
  }

  public void setCurrentCollisions(List<GameObject> currentCollisions) {
    this.currentCollisions = currentCollisions;
  }
}
