package amc.levelcreator;

import java.io.File;

import amc.actions.StatusEffectCollisionAction;
import amc.objects.GameObjectWithStatusEffects;
import amc.statuseffects.StatusEffect;
import amc.statuseffects.StatusEffectController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class NewItemController {

  @FXML
  private ImageView idleImage;
  
  @FXML
  private TextField name;
  
  @FXML
  private CheckBox cbPassable;
  
  @FXML
  private CheckBox cbDialogue;
  
  @FXML
  private CheckBox cbCollisionAnimation;
  
  @FXML
  private CheckBox cbIdleAnimation;
  
  @FXML
  private CheckBox cbStatusEffects;
  
  @FXML
  private Button bChooseDialogue, bCollisionAnimation, bIdleAnimation, bStatusEffect;
  
  @FXML
  private TextArea statusEffectsText;

  @FXML
  private TextField dialogueText, collisionAnimationText, idlenimation;
  
  @FXML
  private Button bClose, bSave;
  
  private Stage stage;
  
  private Item newItem;
  
  @FXML
  public void initialize() {
    cbDialogue.setOnMouseClicked( event -> {
      bChooseDialogue.setDisable(!cbDialogue.isSelected());
    });
    cbCollisionAnimation.setOnMouseClicked( event -> {
      bCollisionAnimation.setDisable(!cbCollisionAnimation.isSelected());
    });
    cbIdleAnimation.setOnMouseClicked( event -> {
      bIdleAnimation.setDisable(!cbIdleAnimation.isSelected());
    });
    cbStatusEffects.setOnMouseClicked( event -> {
      bStatusEffect.setDisable(!cbStatusEffects.isSelected());
    });
    
    bChooseDialogue.setOnMouseClicked( event -> {
      File dialogueFile = this.chooseFile("Dialogue Files", "*.dialog");
      if(dialogueFile != null) 
        dialogueText.setText(dialogueFile.getName());
      else
        dialogueText.setText("");
    });
    
    bCollisionAnimation.setOnMouseClicked( event -> {
     
    });
    
    bIdleAnimation.setOnMouseClicked( event -> {
      
    });
    
    bStatusEffect.setOnMouseClicked( event -> {
      try {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("StatusEffect.fxml"));
        StatusEffectController statusEffectController = new StatusEffectController();
        fxmlLoader.setController(statusEffectController);
        Parent root = (Parent)fxmlLoader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/zone-creator.css");
        Stage stage = new Stage();
        stage.setTitle("Status Effects");
        stage.setScene(scene);
        stage.showAndWait();
        
        StatusEffect selectedEffect = statusEffectController.getSelectedEffect();
        if(selectedEffect != null) {
          statusEffectsText.appendText(selectedEffect.getName() + "\n");
          newItem.getGameObject().addCollisionAction(new StatusEffectCollisionAction(selectedEffect));
        }
        
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    });
    
    idleImage.setOnMouseClicked( event -> {
      try {
        File imageFile = this.chooseFile("PNG Files", "*.png");
        if(imageFile != null) {
          idleImage.setImage(new Image(getClass().getClassLoader().getResourceAsStream(imageFile.getName())));
          newItem.setImageResource(imageFile.getName());
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    });
    
    bSave.setOnMouseClicked(event -> {
      newItem.setName(name.getText());
      
      GameObjectWithStatusEffects gameObject = newItem.getGameObject();
      gameObject.setPassable(cbPassable.isSelected());
      
      closeWindow();
    });
    
    bClose.setOnMouseClicked(event -> {
      newItem = null;
      closeWindow();
    });
    
    newItem = new Item(null, null);
  }
  
  public Item getCreatedItem() {
    return newItem;
  }
  
  public void closeWindow() {
    Stage stage = (Stage) bClose.getScene().getWindow();
    stage.close();
  }
  
  private File chooseFile(String filterName, String filter) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setInitialDirectory(new File("./config"));
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(filterName, filter));
    
    return fileChooser.showOpenDialog(stage);
  }
}
