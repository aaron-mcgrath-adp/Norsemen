package amc.actions;

import amc.GameObject;
import amc.objects.Player;

public abstract class PlayerCollisionAction {
  
  public abstract void handlePlayerCollision(Player player, GameObject me);
  
  public abstract void handlePlayerExitCollision(Player player, GameObject me);

}
