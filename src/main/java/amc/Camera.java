package amc;

public class Camera {

  private static final int WINDOW_WIDTH = GamePreferences.getInstance().getIntPreference(GamePreferencesEnum.WINDOW_WIDTH);
  private static final int WINDOW_HEIGHT = GamePreferences.getInstance().getIntPreference(GamePreferencesEnum.WINDOW_HEIGHT);
  
  private float x, y;
  
  private Level currentLevel;
  
  public Camera(float x, float y) {
    this.setX(x);
    this.setY(y);
  }
  
  public void tick(GameObject object) {
    x += ((object.getX() - x) - WINDOW_WIDTH/2) * 0.05f;
    y += ((object.getY() - y) - (WINDOW_HEIGHT - 100)/2) * 0.05f;
    
    if(this.getCurrentLevel() == null) {
      if(x <= 0) x = 0;
      if(x >= WINDOW_WIDTH + 32) x = WINDOW_WIDTH + 32;
      if(y <= 0) y = 0;
      if(y >= WINDOW_HEIGHT + 32) y = WINDOW_HEIGHT + 32;
    } else {
      // can fix the camera moving too far left and down here.
      if(x <= 0) 
        x = 0;
      if(x >= (getCurrentLevel().getWidth())) 
        x = (getCurrentLevel().getWidth());
      if(y <= 0) 
        y = 0;
      if(y >= (getCurrentLevel().getHeight())) 
        x = (getCurrentLevel().getHeight());
    }
  }

  public float getX() {
    return x;
  }

  public float getY() {
    return y;
  }

  public void setX(float x) {
    this.x = x;
  }

  public void setY(float y) {
    this.y = y;
  }

  public Level getCurrentLevel() {
    return currentLevel;
  }

  public void setCurrentLevel(Level currentLevel) {
    this.currentLevel = currentLevel;
  }
  
}
