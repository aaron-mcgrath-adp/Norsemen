package amc.statuseffects;

import amc.objects.GameObjectWithStatusEffects;

public class HealthStatusEffect extends StatusEffect {

  private int amount;
  
  @Override
  public void apply(GameObjectWithStatusEffects object) {
    object.setHealth(object.getHealth() + getAmount());
    // over heal?
    if(object.getHealth() > object.getHitPoints())
      object.setHealth(object.getHitPoints());
  }

  @Override
  public void unApply(GameObjectWithStatusEffects object) {
    // do nothing
  }

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }

}
