package amc.util;

import java.io.File;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FileHelper {

  public static File chooseFile(Stage stage, String filterName, String filter) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setInitialDirectory(new File("./config"));
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(filterName, filter));
    
    return fileChooser.showOpenDialog(stage);
  }
  
}
