package amc;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter {

  private ObjectHandler objectHandler;
  
  public KeyInput(ObjectHandler objectHandler) {
    this.setObjectHandler(objectHandler);
  }
  
  public void keyPressed(KeyEvent event) {
    int key = event.getKeyCode();
    
    if(key == KeyEvent.VK_W)  getObjectHandler().setUp(true);
    if(key == KeyEvent.VK_S)  getObjectHandler().setDown(true);
    if(key == KeyEvent.VK_A)  getObjectHandler().setLeft(true);
    if(key == KeyEvent.VK_D)  getObjectHandler().setRight(true);
    
  }
  
  public void keyReleased(KeyEvent event) {
    int key = event.getKeyCode();
    
    if(key == KeyEvent.VK_W)  getObjectHandler().setUp(false);
    if(key == KeyEvent.VK_S)  getObjectHandler().setDown(false);
    if(key == KeyEvent.VK_A)  getObjectHandler().setLeft(false);
    if(key == KeyEvent.VK_D)  getObjectHandler().setRight(false);
  }

  public ObjectHandler getObjectHandler() {
    return objectHandler;
  }

  public void setObjectHandler(ObjectHandler objectHandler) {
    this.objectHandler = objectHandler;
  }
  
}
