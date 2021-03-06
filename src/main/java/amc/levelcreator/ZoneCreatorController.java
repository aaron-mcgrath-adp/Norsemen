package amc.levelcreator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import amc.Game;
import amc.GameObject;
import amc.GamePreferences;
import amc.GamePreferencesEnum;
import amc.Level;
import amc.animations.Animation;
import amc.animations.AnimationListController;
import amc.objects.Player;
import amc.util.FileHelper;
import amc.util.PropertyFileHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ZoneCreatorController {
  
  private static final String ZONES_DIR = "./config/zones/";
  private static final String ITEMS_FILE = "./config/items.bin";
  private static final String MONSTER_FILE = "./config/monsters.bin";
  private static final String PLAYER_FILE = "./config/player.bin";
  
  private static final int SPACER = GamePreferences.getInstance().getIntPreference(GamePreferencesEnum.MAP_SPACER_DO_NOT_CHANGE);
  
  @FXML
  private Button bNew, bSave, bClose;
  
  @FXML
  private Button bNewItem, bNewMonster, bDeleteItem;
  
  @FXML
  private GridPane zonePane;
  
  @FXML
  private TextField zoneName, zoneX, zoneY;
  
  @FXML
  private TextField zoneBackground, ambientSoundResource;
  
  @FXML
  private ListView<Item> itemListView;
  private ObservableList<Item> itemListData;
  
  @FXML
  private ListView<Monster> monsterListView;
  private ObservableList<Monster> monsterListData;
  
  @FXML
  private TabPane objectsTabPane;
  
  @FXML
  private Tab itemsTab, monstersTab, lootablesTab;
  
  @FXML
  private CheckBox cbRepeatBackground;
  
  private Level editingLevel;
  
  @FXML
  private ImageView playerIdleImage;
  
  @FXML
  private TextField playerName, playerHitpoints, playerSpeed;
  
  @FXML
  private Button bPlayerSave;
  
  @FXML
  private Button bLiveTest;
  
  @FXML
  private Button bLeftAnimation, bRightAnimation, bUpAnimation, bDownAnimation;
  
  private Player editingPlayer;
  
  private Map<String, ImageView> onScreenDisplayItemsMap;
  private Map<ImageView, String> onScreenDisplayItemsReverseMap;
  private Map<ImageView, Item> imageViewToItemsMap;
  
  public static DataFormat dataFormat =  new DataFormat("myplayer");
  
  @FXML
  public void initialize() {
    onScreenDisplayItemsMap = new HashMap<>();
    onScreenDisplayItemsReverseMap = new HashMap<>();
    imageViewToItemsMap = new HashMap<>();
    
    itemListData = FXCollections.observableList(new ArrayList<Item>());
    monsterListData = FXCollections.observableList(new ArrayList<Monster>());
    
    itemListView.setItems(itemListData);
    monsterListView.setItems(monsterListData);
    
    zonePane.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
    resizeZone();
    redrawGrid(Integer.parseInt(zoneX.getText()), Integer.parseInt(zoneY.getText()));
    
    loadLists();
    
    bLeftAnimation.setOnMouseClicked(event -> {
      editingPlayer.setLeftAnimation(addPlayerAnimation());
      updatePlayerAnimationButtons();
    });
    
    bRightAnimation.setOnMouseClicked(event -> {
      editingPlayer.setRightAnimation(addPlayerAnimation());
      updatePlayerAnimationButtons();
    });
    
    bUpAnimation.setOnMouseClicked(event -> {
      editingPlayer.setUpAnimation(addPlayerAnimation());
      updatePlayerAnimationButtons();
    });
    
    bDownAnimation.setOnMouseClicked(event -> {
      editingPlayer.setDownAnimation(addPlayerAnimation());
      updatePlayerAnimationButtons();
    });
    
    bLiveTest.setOnMouseClicked( event -> {
      doSaveZone();
      new Game().start(getEditingLevel(), false);
    });
    
    playerIdleImage.setOnMouseClicked( event -> {
      try {
        File imageFile = FileHelper.chooseFile((Stage) playerIdleImage.getScene().getWindow(), "PNG Files", "*.png");
        if(imageFile != null) {
          playerIdleImage.setImage(new Image(getClass().getClassLoader().getResourceAsStream(imageFile.getName())));
          editingPlayer.setIdleImageResource(imageFile.getName());
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    });
    
    playerIdleImage.setOnDragDetected( event -> {
      Dragboard db = playerIdleImage.startDragAndDrop(TransferMode.ANY);
      /* Put a string on a dragboard */
      ClipboardContent content = new ClipboardContent();
      if (editingPlayer != null)
        content.put(dataFormat, editingPlayer.toString());
      else
        content.put(dataFormat, "XData");
      db.setContent(content);
      event.consume();
    });
    
    zonePane.setOnDragOver( event -> {
      event.acceptTransferModes(TransferMode.ANY);
    });
    
    zonePane.setOnDragDropped( event -> {
      double mouseX = event.getX();
      double mouseY = event.getY();

      int gridX = (int) (mouseX / SPACER);
      int gridY = (int) (mouseY / SPACER);
      
      handleZonePaneEdit(new Item(editingPlayer), gridX, gridY, mouseX, mouseY);
    });
    
    bPlayerSave.setOnMouseClicked( event -> {
      editingPlayer.setName(playerName.getText());
      editingPlayer.setHitPoints(Integer.parseInt(playerHitpoints.getText()));
      editingPlayer.setSpeed(Integer.parseInt(playerSpeed.getText()));
      
      PropertyFileHandler.saveBinaryFile(PLAYER_FILE, editingPlayer);
    });
    
    bNewItem.setOnMouseClicked( event -> {
      newItemWindow();
    });
    
    bNewMonster.setOnMouseClicked( event -> {
      newMonsterWindow();
    });
    
    bDeleteItem.setOnMouseClicked( event -> {
      deleteSelectedItem();
    });
    
    zoneBackground.textProperty().addListener( (observable, oldValue, newValue) -> {
     loadBackground();
    });
    
    cbRepeatBackground.armedProperty().addListener( (observable, oldValue, newValue) -> {
      loadBackground();
    });
    
    bSave.setOnMouseClicked( event -> {
      doSaveZone();
      closeWindow();
    });
    
    bClose.setOnMouseClicked( event -> {
      closeWindow();
    });
    
    loadLevel();
    loadBackground();
    loadPlayer();
    updatePlayerAnimationButtons();
  }

  private void updatePlayerAnimationButtons() {
    if(editingPlayer.getLeftAnimation() != null) 
      bLeftAnimation.setText(editingPlayer.getLeftAnimation().getName());
    if(editingPlayer.getRightAnimation() != null) 
      bRightAnimation.setText(editingPlayer.getRightAnimation().getName());
    if(editingPlayer.getUpAnimation() != null) 
      bUpAnimation.setText(editingPlayer.getUpAnimation().getName());
    if(editingPlayer.getDownAnimation() != null) 
      bDownAnimation.setText(editingPlayer.getDownAnimation().getName());
  }

  private Animation addPlayerAnimation() {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("AnimationList.fxml"));
    AnimationListController animationController = new AnimationListController();    
    fxmlLoader.setController(animationController);
    Parent root = null;
    try {
      root = (Parent) fxmlLoader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
    Scene scene = new Scene(root);
    scene.getStylesheets().add("/zone-creator.css");
    Stage stage = new Stage();
    stage.setTitle("Animation List");
    stage.setScene(scene);
    stage.showAndWait();
    
    return animationController.getSelectedAnimation();
  }

  private void loadPlayer() {
    File file = new File(PLAYER_FILE);
    if(file.exists()) {
      editingPlayer = (Player) PropertyFileHandler.loadBinaryFile(PLAYER_FILE);
      playerIdleImage.setImage(new Image(getClass().getClassLoader().getResourceAsStream(editingPlayer.getIdleImageResource())));
      playerName.setText(editingPlayer.getName());
      playerHitpoints.setText(Integer.toString(editingPlayer.getHitPoints()));
      playerSpeed.setText(Integer.toString(editingPlayer.getSpeed()));
    } else {
      editingPlayer = new Player();
    }
  }

  private void loadLevel() {
    if(getEditingLevel() != null) {
      zoneName.setText(getEditingLevel().getName());
      zoneX.setText(Integer.toString(getEditingLevel().getWidth()));
      zoneY.setText(Integer.toString(getEditingLevel().getHeight()));
      zoneBackground.setText(getEditingLevel().getBackgroundImageResource());
      cbRepeatBackground.setSelected(getEditingLevel().isRepeatBackgroundImage());
      ambientSoundResource.setText(getEditingLevel().getAmbientSoundResource());
      
      getEditingLevel().getGameObjects().forEach( gameObject -> {
        int gridX = gameObject.getX() / SPACER;
        int gridY = gameObject.getY() / SPACER;
        handleZonePaneEdit(new Item(gameObject), gridX, gridY, gameObject.getX(), gameObject.getY());
      });
    }
  }

  private void doSaveZone() {
    if(getEditingLevel() == null)
      setEditingLevel(new Level());
    if(!zoneName.getText().isEmpty()) {
      getEditingLevel().setName(zoneName.getText());
      getEditingLevel().setWidth(Integer.parseInt(zoneX.getText()));
      getEditingLevel().setHeight(Integer.parseInt(zoneY.getText()));
      getEditingLevel().setBackgroundImageResource(zoneBackground.getText());
      getEditingLevel().setRepeatBackgroundImage(cbRepeatBackground.isSelected());
      getEditingLevel().setAmbientSoundResource(ambientSoundResource.getText());
      
      List<GameObject> objects = new ArrayList<>();
      imageViewToItemsMap.forEach( (image, item) -> {
        objects.add(imageViewToItemsMap.get(image).getGameObject());
      });
      getEditingLevel().setGameObjects(objects);
      
      String levelResource = ZONES_DIR + getEditingLevel().getName();
      PropertyFileHandler.saveBinaryFile(levelResource + ".bin", getEditingLevel());
    } else
      System.out.println("Zone name is not set, cannot save.");
  }

  public void closeWindow() {
    Stage stage = (Stage) bClose.getScene().getWindow();
    stage.close();
  }
  
  private void loadBackground() {
    try {
      if(zoneBackground.getText() != null) {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(zoneBackground.getText());
        if(stream != null) {
          zonePane.setBackground(new Background(new BackgroundImage(
                      new Image(stream), 
                      cbRepeatBackground.isSelected() ? BackgroundRepeat.REPEAT : BackgroundRepeat.NO_REPEAT, 
                      cbRepeatBackground.isSelected() ? BackgroundRepeat.REPEAT : BackgroundRepeat.NO_REPEAT, 
                      BackgroundPosition.DEFAULT, 
                      null)));
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      // do nothing
    }
    
  }

  private void deleteSelectedItem() {    
    if(itemListView.getSelectionModel().getSelectedIndex() >= 0)
      itemListView.getItems().remove(itemListView.getSelectionModel().getSelectedIndex());
    saveItemList();
  }

  private void saveItemList() {
    ArrayList<Item> itemList = new ArrayList<Item>(itemListData);
    PropertyFileHandler.savePropertyFile(ITEMS_FILE, itemList);
  }

  private void newMonsterWindow() {
    // TODO Auto-generated method stub
    
  }

  private void newItemWindow() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("NewItem.fxml"));
      NewItemController newItemController = new NewItemController();
      fxmlLoader.setController(newItemController);
      Parent root = (Parent)fxmlLoader.load();
      Scene scene = new Scene(root);
      scene.getStylesheets().add("/zone-creator.css");
      Stage stage = new Stage();
      stage.setTitle("New Item");
      stage.setScene(scene);
      stage.showAndWait();
      if(newItemController.getCreatedItem() != null) {
        itemListData.add(newItemController.getCreatedItem());
        saveItemList();
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void loadLists() {
    List<Item> loadPropertiesFile = PropertyFileHandler.loadPropertiesFile(ITEMS_FILE, Item.class);
    if(loadPropertiesFile != null) {
      loadPropertiesFile.forEach( item -> {
        itemListData.add(item.loadToLabel());
      });
    }
    
    List<Monster> loadPropertiesFile2 = PropertyFileHandler.loadPropertiesFile(MONSTER_FILE, Monster.class);
    if(loadPropertiesFile2 != null) {
      loadPropertiesFile2.forEach( item -> {
        monsterListData.add(item.loadToLabel());
      });
    }

    zonePane.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
      if(event.getButton().equals(MouseButton.PRIMARY)) {
        double mouseX = event.getX();
        double mouseY = event.getY();
  
        int gridX = (int) (mouseX / SPACER);
        int gridY = (int) (mouseY / SPACER);
        if(itemsTab.isSelected()) {
          Item selectedItem = (Item) itemListView.getSelectionModel().getSelectedItem().clone();
          if(selectedItem != null) {
            handleZonePaneEdit(selectedItem, gridX, gridY, gridX * SPACER, gridY * SPACER);
          }
        }
      }
    });
  }

  private void handleZonePaneEdit(Item item, int gridX, int gridY, double xCoord, double yCoord) {
    String key = gridX + ":::" + gridY;
    
    if(onScreenDisplayItemsMap.containsKey(key)) {
      System.out.println("Item already at this location, skipping.");
    } else {
      ImageView imageView = new ImageView(new Image(this.getClass().getClassLoader().getResourceAsStream(item.getImageResource())));
      imageView.setOnMouseClicked( event -> {
        if(event.getButton().equals(MouseButton.SECONDARY)) 
          handleDeleteItem(imageView);
        else if(event.getButton().equals(MouseButton.MIDDLE))
          handleEditItem(imageView);
      });
      
      item.getGameObject().setX((int) xCoord);
      item.getGameObject().setY((int) yCoord);
      
      zonePane.add(imageView, gridX, gridY);
      onScreenDisplayItemsMap.put(key, imageView);
      imageViewToItemsMap.put(imageView, item);
      onScreenDisplayItemsReverseMap.put(imageView, key);
    }
  }

  private void handleDeleteItem(ImageView imageView) {
    imageViewToItemsMap.remove(imageView);
    String key = onScreenDisplayItemsReverseMap.get(imageView);
    onScreenDisplayItemsReverseMap.remove(imageView);
    onScreenDisplayItemsMap.remove(key);
    
    zonePane.getChildren().remove(imageView);
    System.out.println("Delete Item x");
  }

  private void handleEditItem(ImageView imageView) {
    System.out.println("Edit item");
  }

  // Resizes the zone pane whenever the values change
  private void resizeZone() {
    zoneX.textProperty().addListener((observable, oldValue, newValue) -> {
      try {
        int x = Integer.parseInt(zoneX.getText());
        zonePane.setPrefWidth(x);
        redrawGrid(zonePane.getPrefWidth(), zonePane.getPrefHeight());
      } catch (Throwable ex) {}
    });
    zoneY.textProperty().addListener((observable, oldValue, newValue) -> {
      try {
        int y = Integer.parseInt(zoneY.getText());
        zonePane.setPrefHeight(y);
        redrawGrid(zonePane.getPrefWidth(), zonePane.getPrefHeight());
      } catch (Throwable ex) {}
    });
  }

  private void redrawGrid(double prefWidth, double prefHeight) {
    int numberXGrids = (int) (prefWidth / SPACER);
    int numberYGrids = (int) (prefHeight / SPACER);
    
    for (int i = 0; i < numberXGrids; i++) {
      ColumnConstraints colConst = new ColumnConstraints();
//      colConst.setPercentWidth(100.0 / numberXGrids);
      colConst.setPrefWidth(SPACER);
      colConst.setMinWidth(SPACER);
      colConst.setMaxWidth(SPACER);
      try {
        zonePane.getColumnConstraints().get(i);
        zonePane.getColumnConstraints().set(i, colConst);
      } catch (IndexOutOfBoundsException ex) {
        zonePane.getColumnConstraints().add(i, colConst);
      }
    }
    
    for (int i = 0; i < numberYGrids; i++) {
      RowConstraints rowConst = new RowConstraints();
//      rowConst.setPercentHeight(100.0 / numberYGrids);
      rowConst.setPrefHeight(SPACER);
      rowConst.setMinHeight(SPACER);
      rowConst.setMaxHeight(SPACER);
      try {
        zonePane.getRowConstraints().get(i);
        zonePane.getRowConstraints().set(i, rowConst);
      } catch (IndexOutOfBoundsException ex) {
        zonePane.getRowConstraints().add(i, rowConst);
      }
    }
    
 // Now strip the extra rows
    for(int x = (zonePane.getRowConstraints().size() - 1); x > numberYGrids; x --) {
      zonePane.getRowConstraints().remove(x);
    }
    
    // Now strip the extra columns
    for(int x = (zonePane.getColumnConstraints().size() - 1); x > numberXGrids; x --) {
      zonePane.getColumnConstraints().remove(x);
    }
  }

  public Level getEditingLevel() {
    return editingLevel;
  }

  public void setEditingLevel(Level editingLevel) {
    this.editingLevel = editingLevel;
  }
 
}
