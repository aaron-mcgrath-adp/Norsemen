package amc.levelcreator;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main  extends Application {
  @Override
  public void start(Stage primaryStage) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/ZoneCreator.fxml"));
      loader.setController(new ZoneCreatorController());
      Parent root = loader.load();
      
      Scene scene = primaryStage.getScene();
      if (scene == null) {
          scene = new Scene(root);
          primaryStage.setScene(scene);
      } else {
        primaryStage.getScene().setRoot(root);
      }
      
      scene.getStylesheets().add("/zone-creator.css");
      
      primaryStage.setOnCloseRequest(e -> {
        Platform.exit();
        System.exit(0);
      });

      primaryStage.sizeToScene();
      primaryStage.setTitle("Game Zone Creator");
//      primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/image/adaptris-logo.png")));
      primaryStage.show();
      
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}