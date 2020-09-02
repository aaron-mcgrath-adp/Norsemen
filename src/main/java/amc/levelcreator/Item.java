package amc.levelcreator;

import java.io.Serializable;

import amc.objects.GameObjectWithStatusEffects;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Item extends Label implements Serializable, Cloneable {
  
  private static final long serialVersionUID = -5025711714170164329L;
  
  private GameObjectWithStatusEffects gameObject;
    
  private String name;
  
  private String imageResource;
  
  public Item(String name, String imageResource) {
    this.setGameObject(new GameObjectWithStatusEffects(0, 0));
    
    this.setName(name);
    this.setImageResource(imageResource);
  }

  public Object clone() {
    Item cloned = new Item(getName(), getImageResource());
    cloned.setGameObject((GameObjectWithStatusEffects) getGameObject().clone());
    
    return cloned;
  }
  
  public String getName() {
    return name;
  }

  public String getImageResource() {
    return imageResource;
  }

  public void setName(String name) {
    this.name = name;
    getGameObject().setName(name);
    super.setText(name);
  }

  public void setImageResource(String imageResource) {
    this.imageResource = imageResource;
    if(imageResource != null) {
      Image image = new Image(this.getClass().getClassLoader().getResourceAsStream(this.imageResource), 40d, 40d, true, true);
      ImageView imageView = new ImageView(image);
      super.setGraphic(imageView);
      
      getGameObject().setIdleImageResource(getImageResource());
    }
  }
  
  public Item loadToLabel() {
    this.setName(this.getName());
    this.setImageResource(this.getImageResource());
    
    return this;
  }

  public GameObjectWithStatusEffects getGameObject() {
    return gameObject;
  }

  public void setGameObject(GameObjectWithStatusEffects gameObject) {
    this.gameObject = gameObject;
  }

}
