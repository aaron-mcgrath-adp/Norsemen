package amc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GamePreferences {
  
  private static final String PREFS_FILE = "game.properties";

  private static GamePreferences INSTANCE;
  
  private Properties properties;
  
  private GamePreferences() {
    properties = new Properties();
  }
  
  public static GamePreferences getInstance() {
    if(INSTANCE == null) {
      INSTANCE = new GamePreferences();
      INSTANCE.loadPreferences();
    }
      
    return INSTANCE;
  }

  public Properties getProperties() {
    return properties;
  }

  public void setProperties(Properties properties) {
    this.properties = properties;
  }
  
  public void loadPreferences() {
    Properties props = new Properties();
    try {
      InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(PREFS_FILE);
      props.load(resourceAsStream);
      this.setProperties(props);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  public String getStringPreference(GamePreferencesEnum name) {
    return this.getProperties().getProperty(name.name());
  }
  
  public int getIntPreference(GamePreferencesEnum name) {
    return Integer.parseInt(getStringPreference(name));
  }
}
