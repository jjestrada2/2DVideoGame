package tankrotationexample.game;


import tankrotationexample.GameConstants;
import tankrotationexample.Launcher;
import tankrotationexample.Resources.ResourcesManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author anthony-pc
 */
public class GameWorld extends JPanel implements Runnable {

    private BufferedImage world;// entire world image
    private Tank t1;
    private Tank t2;
    private final Launcher lf; // go back to the screens I need (pause function)?
    private long tick = 0;

    List <GameObject> gobjs = new ArrayList<>(800);

    /**
     *
     */
    public GameWorld(Launcher lf) {
        this.lf = lf;

    }

    @Override
    public void run() {
        try {
            while (true) {
                this.tick++;
                this.t1.update(); // update tank
                this.t2.update();
                //her i need to add checkcolitions
                this.checkCollisions();

                this.repaint();   // redraw game it comes from componet.java
                /*
                 * Sleep for 1000/144 ms (~6.9ms). This is done to have our 
                 * loop run at a fixed rate per/sec. 
                */
                Thread.sleep(1000 / 144);
            }
        } catch (InterruptedException ignored) {
            System.out.println(ignored);
        }
    }

    private void checkCollisions() {
        for(int i = 0; i < this.gobjs.size();i++){
            GameObject obj1 = this.gobjs.get(i);
            if(obj1 instanceof Wall|| obj1 instanceof BreakableWall || obj1 instanceof Health || obj1 instanceof Speed|| obj1 instanceof Shield){
                continue;
            }
            for(int j = 0;j< this.gobjs.size();j++){
                if(i==j) continue;
                GameObject obj2 = this.gobjs.get(j);
                if(obj1.getHitBox().intersects(obj2.getHitBox())){
                    obj1.collides(obj2);
                }
            }
        }
    }

    /**
     * Reset game to its initial state.
     */
    public void resetGame() {
        this.tick = 0;
        this.t1.setX(300);
        this.t1.setY(300);
        //mising respawn power up respawn walls, lives...
    }

    /**
     * Load all resources for Tank Wars Game. Set all Game Objects to their
     * initial state as well.
     */
    public void InitializeGame() {//read resources build world
        this.world = new BufferedImage(GameConstants.GAME_WORLD_WIDTH,
                GameConstants.GAME_WORLD_HEIGHT,
                BufferedImage.TYPE_INT_RGB);
    /*
    * 0 -> nothing
    * 9 -> unbreakable
    *
    * */
        InputStreamReader isr = new InputStreamReader(Objects.requireNonNull(ResourcesManager.class.getClassLoader().getResourceAsStream("maps/map1.csv")));
        try(BufferedReader mapReader = new BufferedReader(isr)){
            int row = 0;
            String[] gameItems;
            while(mapReader.ready()){
                gameItems = mapReader.readLine().strip().split(",");
                for(int col = 0;col< gameItems.length;col++){
                    String gameObject = gameItems[col];
                    if("0".equals(gameObject)) continue;
                        this.gobjs.add(GameObject.newInstance(gameObject,col*30,row*30));

                }
                row++;
            }
        }catch (IOException e){
            throw new RuntimeException(e);
        }

        t1 = new Tank(300, 300, 0, 0, (short) 0, ResourcesManager.getSprite("tank1"));
        TankControl tc1 = new TankControl(t1, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE,KeyEvent.VK_X);
        this.lf.getJf().addKeyListener(tc1);

        t2 = new Tank(400, 300, 0, 0, (short) 180, ResourcesManager.getSprite("tank2"));
        TankControl tc2 = new TankControl(t2, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_NUMPAD0,KeyEvent.VK_L);
        this.lf.getJf().addKeyListener(tc2);

        this.gobjs.add(t1);
        this.gobjs.add(t2);
    }

    private void drawFloor(Graphics2D buffer){
        BufferedImage floor = ResourcesManager.getSprite("floor");
        for(int i = 0; i < GameConstants.GAME_WORLD_WIDTH;i+=320){
            for(int j = 0; j < GameConstants.GAME_WORLD_HEIGHT;j+=240){
                buffer.drawImage(floor,i,j,null);
            }
        }
    }

    private void renderMiniMap(Graphics2D g2, BufferedImage world){
        BufferedImage mm =world.getSubimage(0,0,GameConstants.GAME_WORLD_WIDTH,GameConstants.GAME_WORLD_HEIGHT );
        g2.scale(.2,.2);
        g2.drawImage(mm,
                (GameConstants.GAME_SCREEN_WIDTH*5)/2-(GameConstants.GAME_WORLD_WIDTH/2),
                GameConstants.GAME_SCREEN_HEIGHT,null);

    }
    private void renderSplitScreens(Graphics2D g2, BufferedImage world){
        BufferedImage lh = world.getSubimage((int)this.t1.getScreen_x(),(int) this.t1.getScreen_y(),GameConstants.GAME_SCREEN_WIDTH/2,GameConstants.GAME_SCREEN_HEIGHT*2/3);
        BufferedImage rh = world.getSubimage((int)this.t2.getScreen_x(),(int) this.t2.getScreen_y(),GameConstants.GAME_SCREEN_WIDTH/2,GameConstants.GAME_SCREEN_HEIGHT*2/3);

        //g2.drawImage(world, 0, 0, null);

        g2.drawImage(lh,0,0,null);
        g2.drawImage(rh,GameConstants.GAME_SCREEN_WIDTH/2+4,0,null);

    }
    @Override
    //Overwrite the look of the Jpanel
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Graphics2D buffer = world.createGraphics();
        this.drawFloor(buffer);

        //buffer.fillRect(0,0,GameConstants.GAME_SCREEN_WIDTH,GameConstants.GAME_SCREEN_HEIGHT );


        this.gobjs.forEach(gameObject -> gameObject.drawImage(buffer));
        this.t1.drawImage(buffer);
        this.t2.drawImage(buffer);
        //g2.drawImage(world, 0, 0, null);
        buffer.setColor(Color.BLACK);
        renderSplitScreens(g2,world);
        renderMiniMap(g2,world);


    }
}
