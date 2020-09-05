package amc;

import java.io.File;

import amc.util.PropertyFileHandler;

public class Game {
  
  private static final String LEVELS_DIR = "./config/zones/";

  public void start(boolean exitOnClose) {
    GamePreferences.getInstance().loadPreferences();
    
    int height = GamePreferences.getInstance().getIntPreference(GamePreferencesEnum.WINDOW_HEIGHT);
    int width = GamePreferences.getInstance().getIntPreference(GamePreferencesEnum.WINDOW_WIDTH);
    String title = GamePreferences.getInstance().getStringPreference(GamePreferencesEnum.GAME_TITLE);
    
    Window window = new Window(height, width, title, exitOnClose);
    
    Thread gameThread = new Thread(new GameLoop(window.getCanvas()), "GameThread");
    gameThread.start();
  }
  
  public void start(Level selectedLevel, boolean exitOnClose) {
    GameState.getInstance().setCurrentLevel(selectedLevel);
    this.start(exitOnClose);
  }
  
  public static void main(String[] arguments) {
    File file = new File(LEVELS_DIR);
    if(!file.exists())
      file.mkdir();
    
    if(file.listFiles().length > 0) {
      Level loadedBinaryLevel = (Level) PropertyFileHandler.loadBinaryFile(LEVELS_DIR + file.listFiles()[0].getName());
      new Game().start(loadedBinaryLevel, true);
    }
  }

}


