package amc.levelcreator;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Monster extends Label {

  private String name;

  private String imageResource;

  public Monster(String name, String imageResource) {
    this.setName(name);
    this.setImageResource(imageResource);
  }

  public String getName() {
    return name;
  }

  public String getImageResource() {
    return imageResource;
  }

  public void setName(String name) {
    this.name = name;
    super.setText(name);
  }

  public void setImageResource(String imageResource) {
    this.imageResource = imageResource;
    super.setGraphic(
        new ImageView(new Image(this.getClass().getClassLoader().getResourceAsStream(this.imageResource))));
  }
  
  public Monster loadToLabel() {
    this.setName(this.getName());
    this.setImageResource(this.getImageResource());
    
    return this;
  }

}
