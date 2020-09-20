package amc.animations;

import java.awt.Image;
import java.io.File;
import java.util.ArrayList;

import amc.BufferedImageLoader;
import amc.util.FileHelper;
import amc.util.PropertyFileHandler;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class AnimationController {
  
  private static final String ANIMATIONS_DIR = "./config/animations/";
  
  private static final int ANIMATION_THREAD_SLEEP = 10;
  
  @FXML
  private Button bSave, bAdd, bDelete, bClose;
  
  @FXML
  private TextField speed, name, iterations;
  
  @FXML
  private Label play, pause;
  
  @FXML
  private ImageView imageView;
  
  @FXML
  private CheckBox cbNoInterruption;
  
  @FXML
  private ListView<AnimationImage> imageListView;
  private ObservableList<AnimationImage> imageListData;
  
  private Animation editingAnimation;
  
  private volatile boolean animationPlaying;
  
  @FXML
  public void initialize() {
    imageListData = FXCollections.observableList(new ArrayList<AnimationImage>());
    imageListView.setItems(imageListData);
    
    bClose.setOnMouseClicked( event -> {
      stopAnimation();
      setEditingAnimation(null);
      closeWindow();
    });
    
    bDelete.setOnMouseClicked( event -> {
      stopAnimation();
      deleteImage();
    });
    
    bSave.setOnMouseClicked( event -> {
      stopAnimation();
      saveEditingAnimation();
      closeWindow();
    });
    
    bAdd.setOnMouseClicked( event -> {
      stopAnimation();
      addImage();
    });
    
    play.setOnMouseClicked( event -> {
      playAnimation();
    });
    
    pause.setOnMouseClicked( event -> {
      stopAnimation();
    });
    
    imageListView.setOnMouseClicked( event -> {
      int selectedIndex = imageListView.getSelectionModel().getSelectedIndex();
      if(selectedIndex >= 0) {
        imageView.setImage(BufferedImageLoader.toFxImage(BufferedImageLoader.loadImage(imageListData.get(selectedIndex).getResource())));
      }
    });
    
    loadEditingAnimation();
  }
  
  private void loadEditingAnimation() {
    if(getEditingAnimation() != null) {
      name.setText(getEditingAnimation().getName());
      speed.setText(Integer.toString(getEditingAnimation().getSpeedMs()));
      cbNoInterruption.setSelected(getEditingAnimation().isCannotBeInterrupted());
      iterations.setText(Integer.toString(getEditingAnimation().getLoopCount()));
      
      imageListData.clear();
      getEditingAnimation().getImageResources().forEach( imageResource -> {
        imageListData.add(new AnimationImage(imageResource, BufferedImageLoader.loadImage(imageResource)));
      });
    }
  }
  
  private void addImage() {
    File chosenFile = FileHelper.chooseFile((Stage) bAdd.getScene().getWindow(), "PNG Files", "*.png");
    if(chosenFile != null) {
      AnimationImage animationImage = new AnimationImage(chosenFile.getName(), BufferedImageLoader.loadImageNoModify(chosenFile.getName()));
      imageListData.add(animationImage);
    }
  }
  
  private void deleteImage() {
    int selectedIndex = imageListView.getSelectionModel().getSelectedIndex();
    if(selectedIndex >= 0) {
      imageListData.remove(selectedIndex);
    }
  }
  
  private void saveEditingAnimation() {
    if(getEditingAnimation() == null)
      setEditingAnimation(new Animation());
    
    getEditingAnimation().setName(name.getText());
    getEditingAnimation().setSpeedMs(Integer.parseInt(speed.getText()));
    getEditingAnimation().setCannotBeInterrupted(cbNoInterruption.isSelected());
    getEditingAnimation().setLoopCount(Integer.parseInt(iterations.getText()));
    
    getEditingAnimation().getImageResources().clear();
    imageListData.forEach( animationImage -> {
      getEditingAnimation().getImageResources().add(animationImage.getResource());
    });
    
    File baseDir = new File(ANIMATIONS_DIR);
    if(!baseDir.exists())
      baseDir.mkdir();
    
    PropertyFileHandler.saveBinaryFile(ANIMATIONS_DIR + name.getText() + ".bin", getEditingAnimation());
  }
  
  private void playAnimation() {
    saveEditingAnimation();
    
    animationPlaying = true;
    
    AnimationRunner runner = new AnimationRunner();
    runner.loadNewAnimation(getEditingAnimation());
    
    new Thread(() -> {
      while(animationPlaying) {
        Platform.runLater(() -> {
          imageView.setImage(BufferedImageLoader.toFxImage(runner.doAnimationTick()));
          
          imageListView.getSelectionModel().select(runner.getCurrentDisplayedIndex());
        });
        
        if(runner.isComplete())
          animationPlaying = false;
        
        try {
          Thread.sleep(ANIMATION_THREAD_SLEEP);
        } catch (InterruptedException e) {
          animationPlaying = false;
        }
      }
    }).start();
  }
  
  private void stopAnimation() {
    animationPlaying = false;
  }
  
  private void closeWindow() {
    Stage stage = (Stage) bClose.getScene().getWindow();
    stage.close();
  }
  
  class AnimationImage {
    
    private String resource;
    
    private Image image;
    
    AnimationImage(String resource, Image image) {
      setResource(resource);
      setImage(image);
    }

    public String toString() {
      return getResource();
    }
    
    public String getResource() {
      return resource;
    }

    public Image getImage() {
      return image;
    }

    public void setResource(String resource) {
      this.resource = resource;
    }

    public void setImage(Image image) {
      this.image = image;
    }
  }


  public Animation getEditingAnimation() {
    return editingAnimation;
  }


  public void setEditingAnimation(Animation editingAnimation) {
    this.editingAnimation = editingAnimation;
  }
}
