package amc.levelcreator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MonsterList {
  
  private static MonsterList INSTANCE;
  
  private static final String ITEMS_FILE = "monsters.properties";

  private ObservableList<Monster> items;
  
  private MonsterList() {
    items = FXCollections.observableArrayList();
  }
  
  public static MonsterList getInstance() {
    if(INSTANCE == null) {
      INSTANCE = new MonsterList();
      INSTANCE.loadItems();
    }
      
    return INSTANCE;
  }
  
  public void loadItems() {
    Properties props = new Properties();
    try {
      InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(ITEMS_FILE);
      props.load(resourceAsStream);
      
      props.forEach( (key, value) -> {
        this.getItems().add(new Monster((String) key, (String) value));
      });
      
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public ObservableList<Monster> getItems() {
    return items;
  }

  public void setItems(ObservableList<Monster> items) {
    this.items = items;
  }

}
