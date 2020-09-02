package amc;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import amc.objects.Player;
import amc.objects.Sword;

public class GameLoop implements Runnable {
  
  private Canvas canvas;
  
  private ObjectHandler objectHandler;
  
  private BufferedImage backgroundImage;
  
  private Camera camera;
  
  private int framesPerSecond;
  
  private static final String HUD_SPRITE = GamePreferences.getInstance().getStringPreference(GamePreferencesEnum.HUD_SPRITE);
  private static final String HUD_EMPTY_SLOT_SPRITE = GamePreferences.getInstance().getStringPreference(GamePreferencesEnum.HUD_EMPTY_SLOT_SPRITE);
  private static final String HUD_FILLED_SLOT_SPRITE = GamePreferences.getInstance().getStringPreference(GamePreferencesEnum.HUD_FILLED_SLOT_SPRITE);
  
  private static final String FLOOR_SPRITE = GamePreferences.getInstance().getStringPreference(GamePreferencesEnum.FLOOR_SPRITE);
  
  private Image wallSprite = BufferedImageLoader.loadImage(FLOOR_SPRITE);
  
  private Image hudSprite = BufferedImageLoader.loadImage(HUD_SPRITE);
  private Image hudEmptySlotSprite = BufferedImageLoader.loadImage(HUD_EMPTY_SLOT_SPRITE);
  private Image hudFilledSlotSprite = BufferedImageLoader.loadImage(HUD_FILLED_SLOT_SPRITE);

  public GameLoop(Canvas canvas) {
    this.setCanvas(canvas);
    this.setObjectHandler(new ObjectHandler());
    this.setBackgroundImage(new BufferedImage(GamePreferences.getInstance().getIntPreference(GamePreferencesEnum.WINDOW_WIDTH), GamePreferences.getInstance().getIntPreference(GamePreferencesEnum.WINDOW_HEIGHT), BufferedImage.TYPE_INT_RGB));
  }

  @Override
  public void run() {
    GameState gameState = GameState.getInstance();
    gameState.setRunning(true);
    
    Level level = new Level("Level1.png", "Level1.wav");
    gameState.setCurrentLevel(level);
    level.loadLevel(getObjectHandler());
    
    this.setCamera(new Camera(0, 0));
    this.getCamera().setCurrentLevel(level);
    
    getCanvas().addKeyListener(new KeyInput(getObjectHandler()));
    getCanvas().addMouseListener(new MouseInput(getObjectHandler(), getCamera()));
        
    long lastTime = System.nanoTime();
    double amountOfTicks = 60.0d;
    double ns = 1000000000 / amountOfTicks;
    double delta = 0;
    long timer = System.currentTimeMillis();
    int frames = 0;
    
    while(gameState.isRunning()) {
      getCanvas().requestFocus();
      
      long now = System.nanoTime();
      delta += (now - lastTime) / ns;
      lastTime = now;
      
      while(delta >= 1) {
        tick();
        delta --;
      }
      render();
      frames ++;
      
      if(System.currentTimeMillis() - timer > 1000) {
        timer += 1000;
        framesPerSecond = frames;
        frames = 0;
      }
    }
  }
  
  private void tick() {
    this.getCamera().tick(this.getObjectHandler().getPlayer());
    
    this.getObjectHandler().tick();
  }
  
  private void render() {
    BufferStrategy bufferStrategy = getCanvas().getBufferStrategy();
    
    if(bufferStrategy == null) {
      getCanvas().createBufferStrategy(3);
      return;
    }
    
    Graphics graphics = bufferStrategy.getDrawGraphics();
    Graphics2D graphics2d = (Graphics2D) graphics;
    
    // ***********************************
    
    int height = GamePreferences.getInstance().getIntPreference(GamePreferencesEnum.WINDOW_HEIGHT);
    int width = GamePreferences.getInstance().getIntPreference(GamePreferencesEnum.WINDOW_WIDTH);
    
    graphics.drawImage(getBackgroundImage(), 0, 0, width, height, null);
    
    graphics2d.translate(-camera.getX(), -camera.getY());
    
    int x = GameState.getInstance().getCurrentLevel().getMapImage().getWidth();
    int y = GameState.getInstance().getCurrentLevel().getMapImage().getHeight();
    for(int counterX = 0 ; counterX < x; counterX ++) {
      for(int counterY = 0 ; counterY < y; counterY ++) {
        graphics.drawImage(wallSprite, counterX * 32, counterY *32, null);
      }
    }
    
    this.getObjectHandler().render(graphics);
    
    graphics2d.translate(camera.getX(), camera.getY());
    
    graphics.setColor(Color.YELLOW);
    graphics.drawString("FPS: " + framesPerSecond, width - 75, 20);
    
    this.drawHud(graphics);
    
    // ***********************************
    
    graphics.dispose();
    bufferStrategy.show();
  }
  
  private void drawHud(Graphics graphics) {
    Player player = ((Player) getObjectHandler().getPlayer());
    
    int height = GamePreferences.getInstance().getIntPreference(GamePreferencesEnum.WINDOW_HEIGHT);
    int width = GamePreferences.getInstance().getIntPreference(GamePreferencesEnum.WINDOW_WIDTH);
    
    Font font = new Font ("Courier New", 1, 17);
    graphics.setFont (font);
    
    graphics.setColor(Color.black);
    graphics.fillRect(0, height - 200, width, 200);
    
 // Health Bar
    graphics.setColor(Color.gray);
    graphics.fillRect(100, height - 180, 260, 32);
    
    graphics.setColor(Color.GREEN);
    int playerHealthPercentage = (100 /  player.getHitPoints()) * player.getHealth();
    int fillBarPercentage = (int) ((260f / 100f) * new Float(playerHealthPercentage));
    
    graphics.fillRect(100, height - 180, fillBarPercentage, 32);
    
    graphics.drawImage(hudSprite, 0, height - 210, null);
    
    int filledX = 250;
    int filledY = height - 165;
    
    for(GameObject item : player.getInventory()) {
      graphics.drawImage(hudFilledSlotSprite, filledX, filledY, null);
      if(item instanceof Sword) {
        graphics.drawImage(item.getIdleImage(), filledX + 20, filledY + 20, null);
        graphics.drawString(Integer.toString(((Player) getObjectHandler().getPlayer()).getAmmo()), filledX + 55, filledY + 30);
      } else {
        graphics.drawImage(item.getIdleImage(), filledX + 20, filledY + 20, null);
      }
      
      filledX += 150;
    }

  }

  public void stopGameLoop() {
    
  }

  public Canvas getCanvas() {
    return canvas;
  }

  public void setCanvas(Canvas canvas) {
    this.canvas = canvas;
  }

  public ObjectHandler getObjectHandler() {
    return objectHandler;
  }

  public void setObjectHandler(ObjectHandler objectHandler) {
    this.objectHandler = objectHandler;
  }

  public BufferedImage getBackgroundImage() {
    return backgroundImage;
  }

  public void setBackgroundImage(BufferedImage backgroundImage) {
    this.backgroundImage = backgroundImage;
  }

  public Camera getCamera() {
    return camera;
  }

  public void setCamera(Camera camera) {
    this.camera = camera;
  }

}
