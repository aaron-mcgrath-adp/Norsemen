package amc.objects;

import amc.animations.Animation;

public class LootableGameObject extends GameObjectWithStatusEffects implements LootableItem {

  private static final long serialVersionUID = -2451473995468077867L;
  
  private Animation activatedAnimation;

  public LootableGameObject(int x, int y) {
    super(x, y);
  }

  @Override
  public Animation getActivatedAnimation() {
    return activatedAnimation;
  }

  public void setActivatedAnimation(Animation activatedAnimation) {
    this.activatedAnimation = activatedAnimation;
  }

  
  
}
