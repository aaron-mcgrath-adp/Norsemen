package amc.animations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Animation implements Serializable {

  private static final long serialVersionUID = -5690862255489955042L;

  private String name;
  
  private int speedMs;
  
  private boolean cannotBeInterrupted;
  
  private int loopCount;
  
  private List<String> imageResources;
  
  public Animation() {
    setImageResources(new ArrayList<>());
  }

  public Animation(String name, int speed, List<String> resources) {
    this();
    setName(name);
    setSpeedMs(speed);
    setImageResources(resources);
  }
  
  public Animation(String name, int speed, String singleResources) {
    this();
    setName(name);
    setSpeedMs(speed);
    getImageResources().add(singleResources);
  }
  
  public String getName() {
    return name;
  }

  public int getSpeedMs() {
    return speedMs;
  }

  public List<String> getImageResources() {
    return imageResources;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setSpeedMs(int speedMs) {
    this.speedMs = speedMs;
  }

  public void setImageResources(List<String> imageResources) {
    this.imageResources = imageResources;
  }
  
  public String toString() {
    return getName();
  }

  public boolean isCannotBeInterrupted() {
    return cannotBeInterrupted;
  }

  public void setCannotBeInterrupted(boolean cannotBeInterrupted) {
    this.cannotBeInterrupted = cannotBeInterrupted;
  }

  public int getLoopCount() {
    return loopCount;
  }

  public void setLoopCount(int loopCount) {
    this.loopCount = loopCount;
  }
}
