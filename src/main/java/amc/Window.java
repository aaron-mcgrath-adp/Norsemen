package amc;

import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;

public class Window {
  
  private Canvas canvas;
  
  public Window(int height, int width, String title, boolean exitOnClose) {
    
    this.setCanvas(new Canvas());
    
    JFrame window = new JFrame(title);
    window.setPreferredSize(new Dimension(width, height));
    window.setMaximumSize(new Dimension(width, height));
    window.setMinimumSize(new Dimension(width, height));
    
    window.add(getCanvas());
    
    window.addWindowListener(new java.awt.event.WindowAdapter() {
      @Override
      public void windowClosing(java.awt.event.WindowEvent windowEvent) {
        GameState.getInstance().setRunning(false);
      }
    });
    
    window.setResizable(false);
    if(exitOnClose)
      window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.setLocationRelativeTo(null);
    window.setVisible(true);
  }

  public Canvas getCanvas() {
    return canvas;
  }

  public void setCanvas(Canvas canvas) {
    this.canvas = canvas;
  }

}
