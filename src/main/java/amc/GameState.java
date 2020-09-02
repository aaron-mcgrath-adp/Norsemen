package amc;

public class GameState {

  private static GameState INSTANCE;
  
  private volatile boolean isRunning = false;
  
  private volatile boolean isPaused = false;
  
  private Level currentLevel;
  
  private GameState() {
  }
  
  public static GameState getInstance() {
    if(INSTANCE == null)
      INSTANCE = new GameState();
    
    return INSTANCE;
  }

  public boolean isRunning() {
    return isRunning;
  }

  public boolean isPaused() {
    return isPaused;
  }

  public void setRunning(boolean isRunning) {
    this.isRunning = isRunning;
  }

  public void setPaused(boolean isPaused) {
    this.isPaused = isPaused;
  }

  public Level getCurrentLevel() {
    return currentLevel;
  }

  public void setCurrentLevel(Level currentLevel) {
    this.currentLevel = currentLevel;
  }
}
