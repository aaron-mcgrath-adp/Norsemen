package amc.animations;

import java.io.File;
import java.util.ArrayList;

import amc.util.PropertyFileHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class AnimationListController {

  private static final String ANIMATIONS_DIR = "./config/animations/";
  
  @FXML
  private ListView<Animation> animationsListView;
  private ObservableList<Animation> animationsListData;
  
  @FXML
  private Button bSave, bClose, bAdd;
  
  private Animation selectedAnimation;
  
  @FXML
  public void initialize() {
    animationsListData = FXCollections.observableList(new ArrayList<>());
    animationsListView.setItems(animationsListData);
    
    loadAnimations();
    
    bSave.setOnMouseClicked( event -> {
      saveAnimations();
      closeWindow();
    });
    
    bClose.setOnMouseClicked( event -> {
      closeWindow();
    });
    
    bAdd.setOnMouseClicked( event -> {
      try {
        addAnimation();
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
    
    animationsListView.setOnMouseClicked( event -> {
      if(event.getClickCount() == 2) {
        try {
          editAnimation();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  private void editAnimation() throws Exception {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("Animation.fxml"));
    AnimationController animationController = new AnimationController();
    
    animationController.setEditingAnimation(animationsListView.getSelectionModel().getSelectedItem());
    
    fxmlLoader.setController(animationController);
    Parent root = (Parent) fxmlLoader.load();
    Scene scene = new Scene(root);
    scene.getStylesheets().add("/zone-creator.css");
    Stage stage = new Stage();
    stage.setTitle("Animation Editor");
    stage.setScene(scene);
    stage.showAndWait();
    
    if(animationController.getEditingAnimation() != null) {
      animationsListData.set(animationsListView.getSelectionModel().getSelectedIndex(), animationController.getEditingAnimation());
    }
  }

  private void addAnimation() throws Exception {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("Animation.fxml"));
    AnimationController animationController = new AnimationController();
    fxmlLoader.setController(animationController);
    Parent root = (Parent) fxmlLoader.load();
    Scene scene = new Scene(root);
    scene.getStylesheets().add("/zone-creator.css");
    Stage stage = new Stage();
    stage.setTitle("Animation Editor");
    stage.setScene(scene);
    stage.showAndWait();
    
    if(animationController.getEditingAnimation() != null) {
      animationsListData.add(animationController.getEditingAnimation());
    }
  }

  private void closeWindow() {
    Stage stage = (Stage) bClose.getScene().getWindow();
    stage.close();
  }

  private void saveAnimations() {
    if(animationsListView.getSelectionModel().getSelectedIndex() >= 0) {
      setSelectedAnimation(animationsListView.getSelectionModel().getSelectedItem());
    }
    
    animationsListData.forEach( item -> {
      PropertyFileHandler.saveBinaryFile(ANIMATIONS_DIR + item.getName() + ".bin", item);
    });
  }

  private void loadAnimations() {
    animationsListData.clear();
    
    File baseDir = new File(ANIMATIONS_DIR);
    if(!baseDir.exists())
      baseDir.mkdir();
    
    for(File animationFile : baseDir.listFiles()) {
      animationsListData.add((Animation) PropertyFileHandler.loadBinaryFile(ANIMATIONS_DIR + animationFile.getName()));
    }
  }

  public Animation getSelectedAnimation() {
    return selectedAnimation;
  }

  public void setSelectedAnimation(Animation selectedAnimation) {
    this.selectedAnimation = selectedAnimation;
  }
  
}
