package amc;

import java.io.Serializable;
import java.util.List;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import amc.objects.Player;

public class Level implements Serializable {

  private static final long serialVersionUID = 6645756318491192970L;
  
  private String name;
  
  private int width, height;
  
  private String ambientSoundResource;
  
  private String backgroundImageResource;
  
  private boolean repeatBackgroundImage;
  
  private List<GameObject> gameObjects;
  
  public Level() {
  }
  
  public void loadLevel(ObjectHandler objectHandler) {
    getGameObjects().forEach( gameObject -> {
      if(gameObject instanceof Player) {
        ((Player) gameObject).setObjectHandler(objectHandler);
        objectHandler.setPlayer(gameObject);
      }
      else
        objectHandler.addGameObject(gameObject);
    });
  }

  public void startAmbientBackground() {
    try {
      Clip clip = AudioSystem.getClip();
      clip.open(AudioSystem.getAudioInputStream(this.getClass().getClassLoader().getResourceAsStream(getAmbientSoundResource())));
      clip.start();
    } catch (Exception exc) {
      exc.printStackTrace(System.out);
    }
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAmbientSoundResource() {
    return ambientSoundResource;
  }

  public void setAmbientSoundResource(String ambientSoundResource) {
    this.ambientSoundResource = ambientSoundResource;
  }

  public String getBackgroundImageResource() {
    return backgroundImageResource;
  }

  public boolean isRepeatBackgroundImage() {
    return repeatBackgroundImage;
  }

  public void setBackgroundImageResource(String backgroundImageResource) {
    this.backgroundImageResource = backgroundImageResource;
  }

  public void setRepeatBackgroundImage(boolean repeatBackgroundImage) {
    this.repeatBackgroundImage = repeatBackgroundImage;
  }

  public List<GameObject> getGameObjects() {
    return gameObjects;
  }

  public void setGameObjects(List<GameObject> gameObjects) {
    this.gameObjects = gameObjects;
  }
}
