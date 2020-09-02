package amc;

import amc.objects.Door;
import amc.objects.EntryPortal;
import amc.objects.ExitPortal;
import amc.objects.Player;
import amc.objects.RedChest;
import amc.objects.RedKey;
import amc.objects.WallBlock;

public enum AvailableGameObjects {

  WallBlock("132132132") {
    @Override
    public GameObject createObjectInstance(int x, int y, ObjectHandler objectHandler) {
      return new WallBlock(x, y);
    }
  },
  
  Player("25500") {
    @Override
    public GameObject createObjectInstance(int x, int y, ObjectHandler objectHandler) {
      return new Player(x, y, objectHandler);
    }
  },
  
  EntryPortal("16373164") {
    @Override
    public GameObject createObjectInstance(int x, int y, ObjectHandler objectHandler) {
      return new EntryPortal(x, y, objectHandler);
    }
  },
  
  ExitPortal("163050") {
    @Override
    public GameObject createObjectInstance(int x, int y, ObjectHandler objectHandler) {
      return new ExitPortal(x, y);
    }
  },
  
  RedChest("25520114") {
    @Override
    public GameObject createObjectInstance(int x, int y, ObjectHandler objectHandler) {
      return new RedChest(x, y);
    }
  },
  
  Door("112146190") {
    @Override
    public GameObject createObjectInstance(int x, int y, ObjectHandler objectHandler) {
      return new Door(x, y);
    }
  },
  
  RedKey("3417776") {
    @Override
    public GameObject createObjectInstance(int x, int y, ObjectHandler objectHandler) {
      return new RedKey(x, y, objectHandler);
    }
  };
  
  private String code;
  
  AvailableGameObjects(String code) {
    this.code = code;
  }
  
  public String getCode() {
    return code;
  }
  
  public abstract GameObject createObjectInstance(int x, int y, ObjectHandler objectHandler);
  
  static AvailableGameObjects findObject(String code) {
    AvailableGameObjects returnvalue = null;    
    for(AvailableGameObjects object : AvailableGameObjects.values()) {
      if(object.getCode().equals(code)) {
        returnvalue = object;
        break;
      }
    }
    return returnvalue;
  }
}
