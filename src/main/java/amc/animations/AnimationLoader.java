package amc.animations;

import amc.util.PropertyFileHandler;

public class AnimationLoader {
  
  private static final String ANIMATIONS_DIR = "./config/animations/";

  public static Animation loadAnimation(String resourceName) {
    return (Animation) PropertyFileHandler.loadBinaryFile(ANIMATIONS_DIR + resourceName + ".bin");
  }
  
}
