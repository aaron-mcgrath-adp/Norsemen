package amc.objects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import amc.GameObject;
import amc.actions.PlayerCollisionAction;
import amc.statuseffects.StatusEffect;

public class GameObjectWithStatusEffects extends GameObject {

  private static final long serialVersionUID = 4768744296216593904L;

  private List<StatusEffect> statusEffects;
  
  private int hitPoints;
  
  private int health;
  
  private int speedModifier;
  
  public GameObjectWithStatusEffects(int x, int y) {
    super(x, y);
    setStatusEffects(new ArrayList<>());
  }
  
  public void tick() {
    this.getStatusEffects().forEach( effect -> {
      effect.applyEffect(this);
    });
    
    super.tick();
  }
  
  public Object clone() {
    GameObjectWithStatusEffects cloned = new GameObjectWithStatusEffects(getX(), getY());
    cloned.setName(this.getName());
    cloned.setVelocityX(this.getVelocityX());
    cloned.setVelocityY(this.getVelocityY());
    cloned.setIdleImageResource(this.getIdleImageResource());
    cloned.setPassable(this.isPassable());
    List<PlayerCollisionAction> actions = new ArrayList<>();
    actions.addAll(this.getCollisionActions());
    cloned.setCollisionActions(actions);
    List<StatusEffect> effects = new ArrayList<>();
    getStatusEffects().forEach( statusEffect -> {
      effects.add((StatusEffect) statusEffect.clone());
    });
    cloned.setStatusEffects(effects);
    cloned.setHitPoints(this.getHitPoints());
    cloned.setHealth(this.getHealth());
    cloned.setSpeedModifier(this.getSpeedModifier());
    
    return cloned;
  }
  
  @Override
  public Rectangle getBounds() {
    return new Rectangle(getX(), getY(), getIdleImage().getWidth(), getIdleImage().getHeight());
  }
  
  @Override
  public void render(Graphics graphics) {
    graphics.drawImage(getIdleImage(), getX(), getY(), null);
  }

  public void addStatusEffect(StatusEffect effect) {
    this.getStatusEffects().add(effect);
  }
  
  public void removeStatusEffect(StatusEffect effect) {
    this.getStatusEffects().remove(effect);
  }
  
  public List<StatusEffect> getStatusEffects() {
    return statusEffects;
  }

  public void setStatusEffects(List<StatusEffect> statusEffects) {
    this.statusEffects = statusEffects;
  }

  // Could be a player or Monster or NPC
  public void applyStatusEffects(GameObjectWithStatusEffects object) {
    this.getStatusEffects().forEach( statusEffect -> {
      statusEffect.applyEffect(object);
    });
  }

  public int getHitPoints() {
    return hitPoints;
  }

  public int getHealth() {
    return health;
  }

  public void setHitPoints(int hitPoints) {
    this.hitPoints = hitPoints;
  }

  public void setHealth(int health) {
    this.health = health;
  }

  public int getSpeedModifier() {
    return speedModifier;
  }

  public void setSpeedModifier(int speedModifier) {
    this.speedModifier = speedModifier;
  }
  
}
