package amc.statuseffects;

import java.util.ArrayList;
import java.util.List;

import amc.util.PropertyFileHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.Stage;

public class StatusEffectController {
  
  private static final String STATUS_EFFECTS_FILE = "./config/status-effects.bin";
  
  @FXML
  private TextField effectName;
  
  @FXML
  private TextField initialDelay;
  
  @FXML
  private TextField maxTime;
  
  @FXML
  private TextField numberOfTimes;
  
  @FXML
  private TextField delayBetween;
  
  @FXML
  private TextField affectHealth;
  
  @FXML
  private TextField affectSpeed;
  
  @FXML
  private Button bClose, bSave;
  
  @FXML
  private Button bNew, bSaveNew, bDelete;
  
  @FXML
  private ListView<StatusEffect> effectsList;
  private ObservableList<StatusEffect> effectsListData;
  
  @FXML
  private ListView<StatusEffect> counterList;
  private ObservableList<StatusEffect> counterListData;
  
  @FXML
  private CheckBox cbPermanent;
    
  public static DataFormat dataFormat =  new DataFormat("mycell");
  
  private static StatusEffect draggedItem = null;
  
  private StatusEffect inProgressCreating;
  
  private int selectedItemIndex;
  
  private boolean creatingNewItem;
  
  private StatusEffect onSaveSelectedEffect;
  
  @FXML
  public void initialize() {
    creatingNewItem = false;
    
    effectsListData = FXCollections.observableList(new ArrayList<StatusEffect>());
    counterListData = FXCollections.observableList(new ArrayList<StatusEffect>());
    
    effectsList.setItems(effectsListData);
    counterList.setItems(counterListData);
    
    effectsList.setCellFactory(lv -> {
      ListCell<StatusEffect> cell = new ListCell<StatusEffect>() {
        @Override
        public void updateItem(StatusEffect item, boolean empty) {
          super.updateItem(item, empty);
          if (item != null)
            setText(item.getName());
        }
      };

      cell.setOnDragDetected(event -> {
        Dragboard db = effectsList.startDragAndDrop(TransferMode.ANY);
        draggedItem = (StatusEffect) effectsListData.get(effectsList.getSelectionModel().getSelectedIndex()).clone();
        draggedItem.setCounterEffects(new ArrayList<>());
        /* Put a string on a dragboard */
        ClipboardContent content = new ClipboardContent();
        if (draggedItem != null)
          content.put(dataFormat, draggedItem.toString());
        else
          content.put(dataFormat, "XData");
        db.setContent(content);
        event.consume();

      });
      return cell;
    });
    
    counterList.setOnDragOver(event -> {
      event.acceptTransferModes(TransferMode.ANY);
      
      effectsList.getSelectionModel().select(selectedItemIndex);
    });
    
    counterList.setOnDragDropped(event -> {
      if (event.getTransferMode() == TransferMode.MOVE) {
        Object o = event.getDragboard().getContent(dataFormat);
        if (o != null) {
          counterListData.add(draggedItem);
          if(inProgressCreating != null) {
            inProgressCreating.getCounterEffects().add(draggedItem);
          } else {
            StatusEffect selectedItem = effectsList.getSelectionModel().getSelectedItem();
            selectedItem.getCounterEffects().add(draggedItem);
          }
        }
      }
    });
    
    counterList.setOnDragExited(event -> {
      effectsList.getSelectionModel().select(selectedItemIndex);
    });
    
    bNew.setOnMouseClicked( event -> {
      creatingNewItem = true;
      inProgressCreating = new StatusEffect();
      effectName.setEditable(true);
      effectName.setText("");
      initialDelay.setEditable(true);
      initialDelay.setText("");
      maxTime.setEditable(true);
      maxTime.setText("");
      numberOfTimes.setEditable(true);
      numberOfTimes.setText("");
      delayBetween.setEditable(true);
      delayBetween.setText("");
      affectHealth.setEditable(true);
      affectHealth.setText("");
      affectSpeed.setEditable(true);
      affectSpeed.setText("");
      cbPermanent.setSelected(false);
      counterListData.clear();
    });
    
    effectsList.setOnMouseClicked( event -> {
      selectedItemIndex = effectsList.getSelectionModel().getSelectedIndex();
      showSelectedItem();
      creatingNewItem = false;
    });
    
    bSaveNew.setOnMouseClicked( event -> {
      if(inProgressCreating == null)
        inProgressCreating = effectsList.getSelectionModel().getSelectedItem();
        
      inProgressCreating.setName(effectName.getText());
      if(!numberOfTimes.getText().equals(""))
        inProgressCreating.setAppliesNumberOfTimes(Integer.parseInt(numberOfTimes.getText()));
      if(!affectHealth.getText().equals(""))
        inProgressCreating.setHealthModifier(Integer.parseInt(affectHealth.getText()));
      if(!initialDelay.getText().equals(""))
        inProgressCreating.setInitialDelayOfEffect(Long.parseLong(initialDelay.getText()));
      if(!maxTime.getText().equals(""))
        inProgressCreating.setMaxTimeOfEffect(Integer.parseInt(maxTime.getText()));
      if(!delayBetween.getText().equals(""))
        inProgressCreating.setMsBetweenApplication(Integer.parseInt(delayBetween.getText()));
      inProgressCreating.setPermanent(cbPermanent.isSelected());
      if(!affectSpeed.getText().equals(""))
        inProgressCreating.setSpeedModifier(Integer.parseInt(affectSpeed.getText()));
      
      inProgressCreating.getCounterEffects().clear();
      counterListData.forEach( item -> {
        inProgressCreating.getCounterEffects().add(item);
      });
      
      if(creatingNewItem)
        effectsListData.add(inProgressCreating);
      
      inProgressCreating = null;
    });
    
    bDelete.setOnMouseClicked( event -> {
      StatusEffect selectedItem = effectsList.getSelectionModel().getSelectedItem();
      if(selectedItem != null) {
        effectsListData.remove(selectedItem);
      }
    });
    
    bSave.setOnMouseClicked( event -> {
      onSaveSelectedEffect = effectsList.getSelectionModel().getSelectedItem();
      this.saveEffects();
      closeWindow();
    });
    
    bClose.setOnMouseClicked(event -> {
      closeWindow();
    });
    
    this.loadEffects();
  }
  
  public void closeWindow() {
    Stage stage = (Stage) bClose.getScene().getWindow();
    stage.close();
  }
  
  public StatusEffect getSelectedEffect() {
    return onSaveSelectedEffect;
  }

  protected void showSelectedItem() {
    StatusEffect selectedItem = effectsList.getSelectionModel().getSelectedItem();
    if(selectedItem != null) {
      effectName.setText(selectedItem.getName());
      initialDelay.setText(Long.toString(selectedItem.getInitialDelayOfEffect()));
      maxTime.setText(Long.toString(selectedItem.getMaxTimeOfEffect()));
      numberOfTimes.setText(Integer.toString(selectedItem.getAppliesNumberOfTimes()));
      delayBetween.setText(Long.toString(selectedItem.getMsBetweenApplication()));
      affectHealth.setText(Integer.toString(selectedItem.getHealthModifier()));
      affectSpeed.setText(Integer.toString(selectedItem.getSpeedModifier()));
      cbPermanent.setSelected(selectedItem.isPermanent());
      
      counterListData.clear();
      counterListData.addAll(selectedItem.getCounterEffects());
    }
  }
  
  private void loadEffects() {
    List<StatusEffect> effects = PropertyFileHandler.loadPropertiesFile(STATUS_EFFECTS_FILE, StatusEffect.class);
      
    effectsListData.addAll(effects);
    if(effectsListData.size() > 0) {
      selectedItemIndex = 0;
      effectsList.getSelectionModel().select(0);
      showSelectedItem();
    }
  }
  
  private void saveEffects() {
    ArrayList<StatusEffect> statusList = new ArrayList<>(effectsListData);
    PropertyFileHandler.savePropertyFile(STATUS_EFFECTS_FILE, statusList);
  }

}
