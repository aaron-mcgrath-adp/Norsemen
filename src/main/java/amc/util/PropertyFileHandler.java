package amc.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class PropertyFileHandler {

  public static <T> List<T> loadPropertiesFile(String propertiesFileName, Class<T> type) {
    try {
      FileInputStream fileOutputStream = new FileInputStream(propertiesFileName);
      ObjectInputStream objectInputStream = new ObjectInputStream(fileOutputStream);
      
      @SuppressWarnings("unchecked")
      ArrayList<T> returnList = (ArrayList<T>) objectInputStream.readObject();
      
      objectInputStream.close();
      
      return returnList;
    } catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
  }
  
  public static void savePropertyFile(String propertiesFileName, List<?> data) {
    try {
      File file = new File(propertiesFileName);
      if(file.exists()) {
        file.delete();
        file.createNewFile();
      }

      FileOutputStream fileOutputStream = new FileOutputStream(propertiesFileName);
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
      objectOutputStream.writeObject(data);
      objectOutputStream.flush();
      objectOutputStream.close();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  
  public static void saveBinaryFile(String propertiesFileName, Object object) {
    try {
      File file = new File(propertiesFileName);
      if(file.exists()) {
        file.delete();
        file.createNewFile();
      }

      FileOutputStream fileOutputStream = new FileOutputStream(propertiesFileName);
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
      objectOutputStream.writeObject(object);
      objectOutputStream.flush();
      objectOutputStream.close();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  
}
