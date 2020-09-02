package amc.actions;

import amc.GameObject;
import amc.objects.Player;

public class PlayerTakesDamageAction extends PlayerCollisionAction {
  
  private int damage;

  @Override
  public void handlePlayerCollision(Player player, GameObject me) {
    player.setHealth(player.getHealth() - this.getDamage());
  }

  @Override
  public void handlePlayerExitCollision(Player player, GameObject me) {
    // do nothing
  }

  public int getDamage() {
    return damage;
  }

  public void setDamage(int damage) {
    this.damage = damage;
  }

}
