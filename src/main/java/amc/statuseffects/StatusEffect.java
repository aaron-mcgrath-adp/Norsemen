package amc.statuseffects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import amc.objects.GameObjectWithStatusEffects;

public class StatusEffect implements Serializable, Cloneable {
  
  private static final long serialVersionUID = -6271674496233108347L;

  private String name;
  
  private String description;
  
  private List<StatusEffect> counterEffects;
  
  private transient long timeEffectGranted;
  
  private long initialDelayOfEffect;
  
  private transient long timeLastApplied;
  
  private int appliesNumberOfTimes;
  
  private int msBetweenApplication;
  
  private transient int timesEffectApplied;
  
  private int maxTimeOfEffect;
  
  private boolean permanent;
  
  private int healthModifier;
  
  private int speedModifier;
  
  public StatusEffect() {
    this.setCounterEffects(new ArrayList<>());
  }

  public void applyEffect(GameObjectWithStatusEffects object) {
    if(getTimeEffectGranted() == 0)
      this.setTimeEffectGranted(System.currentTimeMillis());
    
    if(this.effectShoultEnd()) {
      if(!isPermanent())
        this.unApply(object);
      this.remove(object);
      return;
    }
    
    if((getInitialDelayOfEffect() + getTimeEffectGranted()) > System.currentTimeMillis()) // should not apply yet.
      return;
    
    if(getTimeLastApplied() == 0) { // never been applied, so apply now.
      this.setTimeLastApplied(System.currentTimeMillis());
      this.setTimesEffectApplied(this.getTimesEffectApplied() + 1);
      apply(object);
      return;
    } else { // has been applied before, are we due to apply again?
      if((getTimeLastApplied() + getMsBetweenApplication()) > System.currentTimeMillis()) {
        this.setTimeLastApplied(System.currentTimeMillis());
        this.setTimesEffectApplied(this.getTimesEffectApplied() + 1);
        apply(object);
        return;
      }
    }
  }
  
  public void apply(GameObjectWithStatusEffects object) {
    object.setHealth(object.getHealth() + getHealthModifier());
    if(object.getHealth() == object.getHitPoints())
      object.setHealth(object.getHitPoints());
    
    object.setSpeedModifier(object.getSpeedModifier() + getSpeedModifier());
  }
  
  public void unApply(GameObjectWithStatusEffects object) {
    object.setSpeedModifier(object.getSpeedModifier() - getSpeedModifier());
  }
  
  public void remove(GameObjectWithStatusEffects object) {
    object.getStatusEffects().remove(this);
  }

  public boolean effectShoultEnd() {
    
    if((getTimesEffectApplied() >= getAppliesNumberOfTimes()) && (getAppliesNumberOfTimes() > 0))
      return true;
    if((getMaxTimeOfEffect() > 0) && (getTimeEffectGranted() + getMaxTimeOfEffect() < System.currentTimeMillis()))
      return true;

    return false;
  }
  
  public Object clone() {
    StatusEffect statusEffect = new StatusEffect();
    statusEffect.setAppliesNumberOfTimes(this.getAppliesNumberOfTimes());
    statusEffect.setCounterEffects(this.getCounterEffects());
    statusEffect.setDescription(this.getDescription());
    statusEffect.setHealthModifier(this.getHealthModifier());
    statusEffect.setInitialDelayOfEffect(this.getInitialDelayOfEffect());
    statusEffect.setMaxTimeOfEffect(this.getMaxTimeOfEffect());
    statusEffect.setMsBetweenApplication(this.getMsBetweenApplication());
    statusEffect.setName(this.getName());
    statusEffect.setPermanent(this.isPermanent());
    statusEffect.setSpeedModifier(this.getSpeedModifier());
    
    return statusEffect;
  }
  
  public boolean equals(Object object) {
    if(object instanceof StatusEffect) {
      StatusEffect other = (StatusEffect) object;
       return (other.toString().equals(this.toString()));
    } else
      return false;
  }
  
  
  public String toString() {
    return getName();
  }
  
  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public List<StatusEffect> getCounterEffects() {
    return counterEffects;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setCounterEffects(List<StatusEffect> counterEffects) {
    this.counterEffects = counterEffects;
  }

  public long getTimeLastApplied() {
    return timeLastApplied;
  }

  public int getAppliesNumberOfTimes() {
    return appliesNumberOfTimes;
  }

  public int getMsBetweenApplication() {
    return msBetweenApplication;
  }

  public void setTimeLastApplied(long timeLastApplied) {
    this.timeLastApplied = timeLastApplied;
  }

  public void setAppliesNumberOfTimes(int appliesNumberOfTimes) {
    this.appliesNumberOfTimes = appliesNumberOfTimes;
  }

  public void setMsBetweenApplication(int msBetweenApplication) {
    this.msBetweenApplication = msBetweenApplication;
  }

  public long getTimeEffectGranted() {
    return timeEffectGranted;
  }

  public long getInitialDelayOfEffect() {
    return initialDelayOfEffect;
  }

  public void setTimeEffectGranted(long timeEffectGranted) {
    this.timeEffectGranted = timeEffectGranted;
  }

  public void setInitialDelayOfEffect(long initialDelayOfEffect) {
    this.initialDelayOfEffect = initialDelayOfEffect;
  }

  public int getTimesEffectApplied() {
    return timesEffectApplied;
  }

  public void setTimesEffectApplied(int timesEffectApplied) {
    this.timesEffectApplied = timesEffectApplied;
  }

  public int getMaxTimeOfEffect() {
    return maxTimeOfEffect;
  }

  public void setMaxTimeOfEffect(int maxTimeOfEffect) {
    this.maxTimeOfEffect = maxTimeOfEffect;
  }

  public boolean isPermanent() {
    return permanent;
  }

  public void setPermanent(boolean permanent) {
    this.permanent = permanent;
  }

  public int getHealthModifier() {
    return healthModifier;
  }

  public int getSpeedModifier() {
    return speedModifier;
  }

  public void setHealthModifier(int healthModifier) {
    this.healthModifier = healthModifier;
  }

  public void setSpeedModifier(int speedModifier) {
    this.speedModifier = speedModifier;
  }
  
}
