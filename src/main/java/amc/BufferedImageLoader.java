package amc;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;

public class BufferedImageLoader {
  
  private static Map<String, BufferedImage> staticCache = new HashMap<>();

  public BufferedImageLoader() {

  }
  
  public static BufferedImage loadCachedImage(String resource) {
    BufferedImage bufferedImage = staticCache.get(resource);
    if(bufferedImage != null)
      return bufferedImage;
    else {
      BufferedImage loadImage = loadImage(resource);
      staticCache.put(resource, loadImage);
      return loadImage;
    }
  }

  public static BufferedImage loadImage(String resource) {
    try {
      return toBufferedImage(makeColorTransparent(ImageIO.read(BufferedImageLoader.class.getClassLoader().getResourceAsStream(resource)), Color.white));
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }
  
  public static BufferedImage loadImageNoModify(String resource) {
    try {
      return ImageIO.read(BufferedImageLoader.class.getClassLoader().getResourceAsStream(resource));
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  private static Image makeColorTransparent(final BufferedImage im, final Color color) {
    final ImageFilter filter = new RGBImageFilter() {
      // the color we are looking for (white)... Alpha bits are set to opaque
      public int markerRGB = color.getRGB() | 0xFFFFFFFF;

      public final int filterRGB(final int x, final int y, final int rgb) {
        if ((rgb | 0xFF000000) == markerRGB) {
          // Mark the alpha bits as zero - transparent
          return 0x00FFFFFF & rgb;
        } else {
          // nothing to do
          return rgb;
        }
      }
    };

    final ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
    return Toolkit.getDefaultToolkit().createImage(ip);
  }

  public static BufferedImage toBufferedImage(Image img) {
    if (img instanceof BufferedImage) {
      return (BufferedImage) img;
    }

    // Create a buffered image with transparency
    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

    // Draw the image on to the buffered image
    Graphics2D bGr = bimage.createGraphics();
    bGr.drawImage(img, 0, 0, null);
    bGr.dispose();

    // Return the buffered image
    return bimage;
  }
  
  public static javafx.scene.image.Image toFxImage(BufferedImage bufferedimage) {
    return SwingFXUtils.toFXImage(bufferedimage, null );
  }
  
  public static void clearCache() {
    staticCache = new HashMap<>();
  }
}
