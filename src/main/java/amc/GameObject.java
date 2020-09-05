package amc;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import amc.actions.PlayerCollisionAction;
import amc.objects.Player;

public abstract class GameObject implements Serializable, Cloneable {
  
  private static final long serialVersionUID = -5126823914301877987L;

  private String name;

  private int x, y;
  
  private float velocityX, velocityY;
  
  private transient BufferedImage idleImage;
  
  private String idleImageResource;
  
  private List<PlayerCollisionAction> collisionActions;
  
  private boolean passable;
  
  public GameObject(int x, int y) {
    this.setX(x);
    this.setY(y);
    this.setCollisionActions(new ArrayList<>());
  }
  
  public void tick() {
    this.setX(this.getX() + (int) this.getVelocityX());
    this.setY(this.getY() + (int) this.getVelocityY());
  }
  
  public abstract void render(Graphics graphics);
  
  public abstract Rectangle getBounds();
  
  public void handlePlayerCollision(Player player) {
    this.getCollisionActions().forEach( action -> {
      action.handlePlayerCollision(player, this);
    });
  }
  
  public void handlePlayerExitCollision(Player player) {
    this.getCollisionActions().forEach( action -> {
      action.handlePlayerExitCollision(player, this);
    });
  }
  
  public BufferedImage getIdleImage() {
    if(idleImage != null)
      return idleImage;
    this.setIdleImage(BufferedImageLoader.loadImage(getIdleImageResource()));
    return idleImage;
  };
  
  public Object clone() {
    return this;
  }

  public void setIdleImage(BufferedImage idleImage) {
    this.idleImage = idleImage;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public float getVelocityX() {
    return velocityX;
  }

  public float getVelocityY() {
    return velocityY;
  }

  public void setX(int x) {
    this.x = x;
  }

  public void setY(int y) {
    this.y = y;
  }

  public void setVelocityX(float velocityX) {
    this.velocityX = velocityX;
  }

  public void setVelocityY(float velocityY) {
    this.velocityY = velocityY;
  }

  public String getIdleImageResource() {
    return idleImageResource;
  }

  public void setIdleImageResource(String idleImageResource) {
    this.idleImageResource = idleImageResource;
  }
  
  public void addCollisionAction(PlayerCollisionAction action) {
    this.getCollisionActions().add(action);
  }

  public void removeCollisionAction(PlayerCollisionAction action) {
    this.getCollisionActions().remove(action);
  }
  
  public List<PlayerCollisionAction> getCollisionActions() {
    return collisionActions;
  }

  public void setCollisionActions(List<PlayerCollisionAction> collisionActions) {
    this.collisionActions = collisionActions;
  }

  public boolean isPassable() {
    return passable;
  }

  public void setPassable(boolean passable) {
    this.passable = passable;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  
}
