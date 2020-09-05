package amc.levelcreator;

import amc.Level;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class LevelPaneController {
  
  @FXML
  private Label levelLabel;
  
  private Level level;
  
  @FXML
  public void initialize() {
    if(getLevel() != null) 
      levelLabel.setText(getLevel().getName());
  }

  public Level getLevel() {
    return level;
  }

  public void setLevel(Level level) {
    this.level = level;
  }

}
