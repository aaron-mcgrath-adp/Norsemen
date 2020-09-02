package amc;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import amc.objects.Bullet;
import amc.objects.Player;

public class MouseInput extends MouseAdapter {

  private ObjectHandler objectHandler;
  private Camera camera;
  
  public MouseInput(ObjectHandler objectHandler, Camera camera) {
    this.setObjectHandler(objectHandler);
    this.setCamera(camera);
  }

  public void mousePressed(MouseEvent event) {
    int mx = (int) (event.getX() + getCamera().getX());
    int my = (int) (event.getY() + getCamera().getY());
    
    GameObject player = getObjectHandler().getPlayer();
    
    if(((Player) player).getAmmo() > 0) {
      getObjectHandler().addGameObject(new Bullet(player.getX(), (int) (player.getY() + (player.getBounds().getHeight() / 2)), getObjectHandler(), mx, my));
      ((Player) player).setAmmo(((Player) player).getAmmo() - 1);
    }
    
  }
  

  public ObjectHandler getObjectHandler() {
    return objectHandler;
  }

  public void setObjectHandler(ObjectHandler objectHandler) {
    this.objectHandler = objectHandler;
  }

  public Camera getCamera() {
    return camera;
  }

  public void setCamera(Camera camera) {
    this.camera = camera;
  }
  
}
