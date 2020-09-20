package amc.animations;

import java.awt.image.BufferedImage;

import amc.BufferedImageLoader;

public class AnimationRunner {

  private Animation animation;
    
  private int currentDisplayedIndex;
  
  private int currentIterations;
  
  private long lastImageChangeTime;
  
  private long delayBetweenImageFlip;
  
  public AnimationRunner() {
  }
  
  public BufferedImage doAnimationTick() {
    if(getAnimation() == null)
      return null;

    if(isComplete())
      return null;
    
    if(getCurrentDisplayedIndex() == -1) { // no image has been shown yet or we've reached the end, so lets serve up the first one.
      setCurrentDisplayedIndex(0);
      lastImageChangeTime = System.currentTimeMillis();
    } else {
      long currentTime = System.currentTimeMillis();
      boolean timeToFlipImage = (lastImageChangeTime + delayBetweenImageFlip) <= currentTime ? true : false;
      
      if(timeToFlipImage) {
        // is there a next image?
        if((getCurrentDisplayedIndex() + 2) <= getAnimation().getImageResources().size()) {
          setCurrentDisplayedIndex(getCurrentDisplayedIndex() + 1);
        } else {
          setCurrentDisplayedIndex(0);
          currentIterations ++;
        }
        
        lastImageChangeTime = System.currentTimeMillis();
      }
    }
    
    return BufferedImageLoader.loadCachedImage(getAnimation().getImageResources().get(getCurrentDisplayedIndex()));
  }

  public void loadNewAnimation(Animation animation) {
    if(getAnimation() != null) {
      if(getAnimation().isCannotBeInterrupted()) {
        if(!isComplete())
          return;
      }
    }
      
    // only reset the animation if it is a new one.
    if(!animation.equals(getAnimation())) {
      setAnimation(animation);
      setCurrentDisplayedIndex(-1);
      currentIterations = 0;
      lastImageChangeTime = 0;
      delayBetweenImageFlip = getAnimation().getSpeedMs() / getAnimation().getImageResources().size();
    }
  }
  
  public boolean isComplete() {
    if(getAnimation().getLoopCount() > 0) {
      if(getAnimation().getLoopCount() <= currentIterations)
        return true;
    }
    return false;
  }

  public Animation getAnimation() {
    return animation;
  }

  public void setAnimation(Animation animation) {
    this.animation = animation;
  }

  public int getCurrentDisplayedIndex() {
    return currentDisplayedIndex;
  }

  public void setCurrentDisplayedIndex(int currentDisplayedIndex) {
    this.currentDisplayedIndex = currentDisplayedIndex;
  }
  
}
