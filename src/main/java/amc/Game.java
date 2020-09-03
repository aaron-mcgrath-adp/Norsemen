package amc;

public class Game {

  public void start() {
    GamePreferences.getInstance().loadPreferences();
    
    int height = GamePreferences.getInstance().getIntPreference(GamePreferencesEnum.WINDOW_HEIGHT);
    int width = GamePreferences.getInstance().getIntPreference(GamePreferencesEnum.WINDOW_WIDTH);
    String title = GamePreferences.getInstance().getStringPreference(GamePreferencesEnum.GAME_TITLE);
    
    Window window = new Window(height, width, title);
    
    Thread gameThread = new Thread(new GameLoop(window.getCanvas()), "GameThread");
    gameThread.start();
  }
  
  public void start(Level selectedLevel) {
    GameState.getInstance().setCurrentLevel(selectedLevel);
    this.start();
  }
  
  public static void main(String[] arguments) {
    new Game().start();
  }

}


