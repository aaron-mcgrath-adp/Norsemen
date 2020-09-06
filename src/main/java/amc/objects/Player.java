package amc.objects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import amc.GameObject;
import amc.ObjectHandler;
import amc.animations.Animation;
import amc.animations.AnimationRunner;

public class Player extends GameObjectWithStatusEffects {
  
  private static final long serialVersionUID = 7578342772510568520L;

  private transient ObjectHandler objectHandler;
  
  private int ammo;
  
  private List<GameObject> inventory;
  
  private List<GameObject> currentCollisions;
  
  private Animation leftAnimation;
  
  private Animation rightAnimation;
  
  private Animation upAnimation;
  
  private Animation downAnimation;
  
  private Animation idleAnimation;
  
  private transient AnimationRunner animationRunner;
  
  
  public Player() {
    this(0, 0, null);
  }
  
  public Player(int x, int y, ObjectHandler objectHandler) {
    super(x, y);
    this.setObjectHandler(objectHandler);
    this.setAmmo(100);
    
    this.setInventory(new ArrayList<>());
    this.setCurrentCollisions(new ArrayList<>());
    this.setAnimationRunner(new AnimationRunner());
    
    this.setIdleAnimation(new Animation("DefaultIdle", 10000, getIdleImageResource()));
    this.setLeftAnimation(new Animation("DefaultLeft", 10000, getIdleImageResource()));
    this.setRightAnimation(new Animation("DefaultRight", 10000, getIdleImageResource()));
    this.setUpAnimation(new Animation("DefaultUp", 10000, getIdleImageResource()));
    this.setDownAnimation(new Animation("DefaultDown", 10000, getIdleImageResource()));
  }

  @Override
  public void render(Graphics graphics) {
    graphics.drawImage(getAnimationRunner().doAnimationTick(), getX(), getY(), null);
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
    
    if((!getObjectHandler().isDown()) && (!getObjectHandler().isUp()) && (!getObjectHandler().isLeft()) && (!getObjectHandler().isRight()))
      getAnimationRunner().loadNewAnimation(getIdleAnimation());
    
    if(getObjectHandler().isDown()) {
      setVelocityY(getSpeed());
      if(!getAnimationRunner().getAnimation().equals(getDownAnimation()))
        getAnimationRunner().loadNewAnimation(getDownAnimation());
    }
    
    if(getObjectHandler().isUp()) {
      setVelocityY(getSpeed() * -1);
      if(!getAnimationRunner().getAnimation().equals(getUpAnimation()))
        getAnimationRunner().loadNewAnimation(getUpAnimation());
    }
    
    if(getObjectHandler().isLeft()) {
      setVelocityX(getSpeed() * -1);
      if(!getAnimationRunner().getAnimation().equals(getLeftAnimation()))
        getAnimationRunner().loadNewAnimation(getLeftAnimation());
    }
    
    if(getObjectHandler().isRight()) {
      setVelocityX(getSpeed());
      if(!getAnimationRunner().getAnimation().equals(getRightAnimation()))
        getAnimationRunner().loadNewAnimation(getRightAnimation());
    }
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
    if(inventory == null)
      inventory = new ArrayList<>();
    return inventory;
  }

  public void setInventory(List<GameObject> inventory) {
    this.inventory = inventory;
  }

  public List<GameObject> getCurrentCollisions() {
    if(currentCollisions == null)
      currentCollisions = new ArrayList<>();
    return currentCollisions;
  }

  public void setCurrentCollisions(List<GameObject> currentCollisions) {
    this.currentCollisions = currentCollisions;
  }

  public Animation getLeftAnimation() {
    if(leftAnimation == null)
      leftAnimation = new Animation("DefaultLeft", 10000, getIdleImageResource());
    return leftAnimation;
  }

  public Animation getRightAnimation() {
    if(rightAnimation == null)
      rightAnimation = new Animation("DefaultRight", 10000, getIdleImageResource());
    return rightAnimation;
  }

  public Animation getUpAnimation() {
    if(upAnimation == null)
      upAnimation = new Animation("DefaultUp", 10000, getIdleImageResource());
    return upAnimation;
  }

  public Animation getDownAnimation() {
    if(downAnimation == null)
      downAnimation = new Animation("DefaulDown", 10000, getIdleImageResource());
    return downAnimation;
  }

  public void setLeftAnimation(Animation leftAnimation) {
    this.leftAnimation = leftAnimation;
  }

  public void setRightAnimation(Animation rightAnimation) {
    this.rightAnimation = rightAnimation;
  }

  public void setUpAnimation(Animation upAnimation) {
    this.upAnimation = upAnimation;
  }

  public void setDownAnimation(Animation downAnimation) {
    this.downAnimation = downAnimation;
  }

  public AnimationRunner getAnimationRunner() {
    if(animationRunner == null) {
      animationRunner = new AnimationRunner();
      animationRunner.loadNewAnimation(getIdleAnimation());
    }
    return animationRunner;
  }

  public void setAnimationRunner(AnimationRunner animationRunner) {
    this.animationRunner = animationRunner;
  }

  public Animation getIdleAnimation() {
    if(idleAnimation == null)
      idleAnimation  = new Animation("DefaulIdle", 10000, getIdleImageResource());
    return idleAnimation;
  }

  public void setIdleAnimation(Animation idleAnimation) {
    this.idleAnimation = idleAnimation;
  }
}
