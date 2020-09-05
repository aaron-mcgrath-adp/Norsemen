package amc.levelcreator;

import java.io.IOException;

import amc.Level;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class LevelPane extends AnchorPane {

  private Level level;
  
  public LevelPane() {
    super();
  }
  
  public LevelPane(Level level) {
    setLevel(level);
  }

  public Level getLevel() {
    return level;
  }

  public void setLevel(Level level) {
    this.level = level;
  }
  
  public LevelPane loadMe() {
    LevelPaneController controller = new LevelPaneController();
    controller.setLevel(getLevel());
    
    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("LevelPane.fxml"));
    loader.setController(controller);

    try {
      AnchorPane internalPane = loader.load();
      this.getChildren().add(internalPane);
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    return this;
  }
  
}
