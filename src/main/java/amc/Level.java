package amc;

import java.awt.image.BufferedImage;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import amc.objects.Player;

public class Level {

  private static final int SPACER = GamePreferences.getInstance().getIntPreference(GamePreferencesEnum.MAP_SPACER_DO_NOT_CHANGE);
  
  private String mapResource;
  
  private String wavResource;
  
  private BufferedImage mapImage;
  
  public Level(String mapResource, String wavResource) {
    this.setMapResource(mapResource);
    this.setWavResource(wavResource);
  }

  public String getMapResource() {
    return mapResource;
  }

  public void setMapResource(String mapResource) {
    this.mapResource = mapResource;
  }
  
  public void loadLevel(ObjectHandler objectHandler) {
    this.setMapImage(BufferedImageLoader.loadImage(getMapResource()));
    
    int width = getMapImage().getWidth();
    int height = getMapImage().getHeight();
    
    for(int x = 0; x < width; x ++) {
      for(int y = 0; y < height; y ++) {
        int pixel = getMapImage().getRGB(x, y);
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;
        
        String code = Integer.toString(red) + Integer.toString(green) + Integer.toString(blue); 
        
        if(!code.equals("000")) { // skip the black squares
          AvailableGameObjects gameObject = AvailableGameObjects.findObject(code);
          if(gameObject != null) {
            GameObject newObject = gameObject.createObjectInstance(0, 0, objectHandler);
            newObject.setX(x * SPACER);
            newObject.setY(y * SPACER);
            
            if(newObject instanceof Player)
              objectHandler.setPlayer(newObject);
            else
              objectHandler.addGameObject(newObject);
          }
        }
      }
    }
    this.startWav();
  }

  private void startWav() {
    try {
      Clip clip = AudioSystem.getClip();
      clip.open(AudioSystem.getAudioInputStream(this.getClass().getClassLoader().getResourceAsStream(getWavResource())));
      clip.start();
    } catch (Exception exc) {
      exc.printStackTrace(System.out);
    }
  }

  public BufferedImage getMapImage() {
    return mapImage;
  }

  public void setMapImage(BufferedImage mapImage) {
    this.mapImage = mapImage;
  }

  public String getWavResource() {
    return wavResource;
  }

  public void setWavResource(String wavResource) {
    this.wavResource = wavResource;
  }
}
