package amc;

import java.awt.Graphics;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ObjectHandler {

  private GameObject player;
    
  private List<GameObject> gameObjects;
  
  boolean left, up, right, down;
  
  private Object gameObjectListLock = new Object();
  
  public ObjectHandler() {
    this.setGameObjects(Collections.synchronizedList(new LinkedList<GameObject>()));
  }
  
  public void tick() {
    if(getPlayer() != null)
    getPlayer().tick();
    // TODO CCME
    for(int counter = 0; counter < getGameObjects().size(); counter ++) {
      getGameObjects().get(counter).tick();
    }
  }
  
  public void render(Graphics graphics) {
    if(getPlayer() != null)
      getPlayer().render(graphics);
    synchronized (gameObjectListLock) {
      this.getGameObjects().forEach(gameObject -> {
        gameObject.render(graphics);
      });
    }
  }
  
  public void addGameObject(GameObject object) {
    this.getGameObjects().add(object);
  }
  
  public void removeGameObject(GameObject object) {
    synchronized (gameObjectListLock) {
      this.getGameObjects().remove(object);
    }
  }

  public List<GameObject> getGameObjects() {
    return gameObjects;
  }

  public void setGameObjects(List<GameObject> gameObjects) {
    this.gameObjects = gameObjects;
  }

  public GameObject getPlayer() {
    return player;
  }

  public void setPlayer(GameObject player) {
    this.player = player;
  }

  public boolean isLeft() {
    return left;
  }

  public boolean isUp() {
    return up;
  }

  public boolean isRight() {
    return right;
  }

  public boolean isDown() {
    return down;
  }

  public void setLeft(boolean left) {
    this.left = left;
  }

  public void setUp(boolean up) {
    this.up = up;
  }

  public void setRight(boolean right) {
    this.right = right;
  }

  public void setDown(boolean down) {
    this.down = down;
  }
  
}
