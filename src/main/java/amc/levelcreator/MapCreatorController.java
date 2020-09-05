package amc.levelcreator;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import amc.Level;
import amc.util.PropertyFileHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

public class MapCreatorController {
  
  private static final int LEVEL_WIDTH = 200;
  
  private static final int LEVEL_HEIGHT = 100;
  
  private static final String MAP_FILE = "./config/map.bin";
  
  private static final String LEVELS_DIR = "./config/zones/";
  
  @FXML
  private GridPane mapGridPane;
  
  @FXML
  private ListView<LevelLabel> zoneListView;
  private ObservableList<LevelLabel> zoneListData;
  
  @FXML
  private Button bAdd, bSave;
  
  private Map<String, LevelPane> onScreenLevelsMap;
  
  private LevelLabel draggedItem;
  
  public static DataFormat dataFormat =  new DataFormat("mycell");
  
  private String dragOverGrid;
  
  private LevelPane dragOverHighlight;
  
  @FXML
  public void initialize() {
    zoneListData = FXCollections.observableList(new ArrayList<LevelLabel>());
    zoneListView.setItems(zoneListData);
    onScreenLevelsMap = new HashMap<>();
            
    zoneListView.setOnMouseClicked( event -> {
      if(event.getButton().equals(MouseButton.MIDDLE)) {
        if(zoneListView.getSelectionModel().getSelectedIndex() >= 0) {
          openZoneCreatorWindow(zoneListView.getSelectionModel().getSelectedItem().getLevel());
        }
      } else if(event.getClickCount() == 2) {
        if(zoneListView.getSelectionModel().getSelectedIndex() >= 0) {
          openZoneCreatorWindow(zoneListView.getSelectionModel().getSelectedItem().getLevel());
        }
      }
    });
    
    zoneListView.setOnDragDetected( event -> {
      Dragboard db = zoneListView.startDragAndDrop(TransferMode.ANY);
      draggedItem = (LevelLabel) zoneListData.get(zoneListView.getSelectionModel().getSelectedIndex()).clone();
      /* Put a string on a dragboard */
      ClipboardContent content = new ClipboardContent();
      if (draggedItem != null)
        content.put(dataFormat, draggedItem.toString());
      else
        content.put(dataFormat, "XData");
      db.setContent(content);
      
      WritableImage snapshot = new LevelPane(draggedItem.getLevel()).loadMe().snapshot(new SnapshotParameters(), null);
      zoneListView.getScene().setCursor(new ImageCursor(snapshot));
      
      event.consume();
    });
    
    zoneListView.setOnDragDone( event -> {
      zoneListView.getScene().setCursor(Cursor.DEFAULT);
      dragOverGrid = null;
    });
    
    mapGridPane.setOnDragOver( event -> {
      event.acceptTransferModes(TransferMode.ANY);
      
      int gridX = (int) (event.getX() / LEVEL_WIDTH);
      int gridY = (int) (event.getY() / LEVEL_HEIGHT);
      
      if((dragOverGrid == null) || (!dragOverGrid.equals(gridX + ":" + gridY))) {
        if(dragOverHighlight != null)
          mapGridPane.getChildren().remove(dragOverHighlight);
        
        dragOverHighlight = new LevelPane(draggedItem.getLevel()).loadMe();
        dragOverHighlight.setOpacity(0.5);
        mapGridPane.add(dragOverHighlight, gridX, gridY);
        dragOverGrid = gridX + ":" + gridY;
      }
    });
    
    mapGridPane.setOnDragDropped( event -> {
      int gridX = (int) (event.getX() / LEVEL_WIDTH);
      int gridY = (int) (event.getY() / LEVEL_HEIGHT);
      
      if(onScreenLevelsMap.get(gridX + ":" + gridY) == null) {
        LevelPane newLevel = new LevelPane(draggedItem.getLevel());
        onScreenLevelsMap.put(gridX + ":" + gridY, newLevel);
        mapGridPane.add(newLevel, gridX, gridY);
      } else
        System.out.println("Level already in this position, skipping.");
    });
    
    bAdd.setOnMouseClicked( event -> {
      openZoneCreatorWindow(null);
    });
    
    bSave.setOnMouseClicked( event -> {
      saveZones();
      saveMap();
    });
    
    redrawGrid(10, 10);
    
    LoadZonesIntoGrid();
    loadZones();
  }
  
  public void closeWindow() {
    Stage stage = (Stage) bSave.getScene().getWindow();
    stage.close();
  }

  
  @SuppressWarnings("unchecked")
  private void LoadZonesIntoGrid() {
    File file = new File(MAP_FILE);
    if(!file.exists()) {
      saveMap();
    }
    
    onScreenLevelsMap = (Map<String, LevelPane>) PropertyFileHandler.loadBinaryFile(MAP_FILE);
    onScreenLevelsMap.forEach( (key, level) -> {
      String[] split = key.split(":");
      mapGridPane.add(level, Integer.parseInt(split[0]), Integer.parseInt(split[1]));
    });
  }


  private void loadZones() {
    File file = new File(LEVELS_DIR);
    if(!file.exists())
      file.mkdir();
    
    zoneListData.clear();
    
    for(File zoneFile : file.listFiles()) {
      Level loadedBinaryLevel = (Level) PropertyFileHandler.loadBinaryFile(LEVELS_DIR + zoneFile.getName());
      zoneListData.add(new LevelLabel(loadedBinaryLevel));
    }
  }
  
  private void saveZones() {
    File baseDir = new File(LEVELS_DIR);
    if(!baseDir.exists())
      baseDir.mkdir();
    
    zoneListData.forEach( levelLabel -> {
      PropertyFileHandler.saveBinaryFile(baseDir + levelLabel.getLevel().getName() + ".bin", levelLabel.getLevel());
    });
  }

  private void saveMap() {
    PropertyFileHandler.saveBinaryFile(MAP_FILE, onScreenLevelsMap);
  }

  private void openZoneCreatorWindow(Level level) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("ZoneCreator.fxml"));
      ZoneCreatorController zoneCreatorController = new ZoneCreatorController();
      
      if(level != null)
        zoneCreatorController.setEditingLevel(level);
      
      fxmlLoader.setController(zoneCreatorController);
      Parent root = (Parent)fxmlLoader.load();
      Scene scene = new Scene(root);
      scene.getStylesheets().add("/zone-creator.css");
      Stage stage = new Stage();
      stage.setTitle("Zone Editor");
      stage.setScene(scene);
      stage.showAndWait();
      
      if(level != null) {
        zoneListView.getSelectionModel().getSelectedItem().setLevel(zoneCreatorController.getEditingLevel());
      } else {
        zoneListData.add(new LevelLabel(zoneCreatorController.getEditingLevel()));
      }
        
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  
  private void redrawGrid(int numberXGrids, int numberYGrids) {    
    for (int i = 0; i < numberXGrids; i++) {
      ColumnConstraints colConst = new ColumnConstraints();
//      colConst.setPercentWidth(100.0 / numberXGrids);
      colConst.setPrefWidth(LEVEL_WIDTH);
      colConst.setMinWidth(LEVEL_WIDTH);
      colConst.setMaxWidth(LEVEL_WIDTH);
      try {
        mapGridPane.getColumnConstraints().get(i);
        mapGridPane.getColumnConstraints().set(i, colConst);
      } catch (IndexOutOfBoundsException ex) {
        mapGridPane.getColumnConstraints().add(i, colConst);
      }
    }
    
    for (int i = 0; i < numberYGrids; i++) {
      RowConstraints rowConst = new RowConstraints();
//      rowConst.setPercentHeight(100.0 / numberYGrids);
      rowConst.setPrefHeight(LEVEL_HEIGHT);
      rowConst.setMinHeight(LEVEL_HEIGHT);
      rowConst.setMaxHeight(LEVEL_HEIGHT);
      try {
        mapGridPane.getRowConstraints().get(i);
        mapGridPane.getRowConstraints().set(i, rowConst);
      } catch (IndexOutOfBoundsException ex) {
        mapGridPane.getRowConstraints().add(i, rowConst);
      }
    }
    
 // Now strip the extra rows
    for(int x = (mapGridPane.getRowConstraints().size() - 1); x > numberYGrids; x --) {
      mapGridPane.getRowConstraints().remove(x);
    }
    
    // Now strip the extra columns
    for(int x = (mapGridPane.getColumnConstraints().size() - 1); x > numberXGrids; x --) {
      mapGridPane.getColumnConstraints().remove(x);
    }
  }
  
  static class LevelLabel extends Label implements Cloneable{
    
    private Level level;
    
    LevelLabel(Level level) {
      setLevel(level);
      setText(level.getName());
    }

    Level getLevel() {
      return level;
    }

    void setLevel(Level level) {
      this.level = level;
    }
    
    public Object clone() {
      LevelLabel cloned = new LevelLabel(getLevel());
      return cloned;
    }
  }
}
