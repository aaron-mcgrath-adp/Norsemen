package amc.actions;

import amc.GameObject;
import amc.objects.Player;
import amc.statuseffects.StatusEffect;

public class StatusEffectCollisionAction extends PlayerCollisionAction {

  private StatusEffect statusEffect;
  
  public StatusEffectCollisionAction() {
  }
  
  public StatusEffectCollisionAction(StatusEffect effect) {
    this.setStatusEffect(effect);
  }
  
  @Override
  public void handlePlayerCollision(Player player, GameObject me) {
    if(getStatusEffect() != null) {
      if(!player.getStatusEffects().contains(getStatusEffect())) {
        player.getStatusEffects().add(getStatusEffect());
      }
    }
  }

  @Override
  public void handlePlayerExitCollision(Player player, GameObject me) {
    /// do nothing
  }

  public StatusEffect getStatusEffect() {
    return statusEffect;
  }

  public void setStatusEffect(StatusEffect statusEffect) {
    this.statusEffect = statusEffect;
  }

}
