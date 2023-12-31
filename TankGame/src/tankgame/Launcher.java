package tankgame;


import tankgame.constants.GameScreenConstants;
import tankgame.game.GameWorld;
import tankgame.menus.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

public class Launcher {

    /*
     * Main panel in JFrame, the layout of this panel
     * will be card layout, this will allow us to switch
     * to sub-panels depending on game state.
     */
    private JPanel mainPanel;
    /*
     * start panel will be used to view the start menu. It will contain
     * two buttons start and exit.
     */
    private JPanel startPanel;
    private JPanel singInPanel;
    /**
     * map menu panel will be used to display the given CSVs as selectable maps
     * NOTE: This is not scalable and if you add new map CSVs there will not be new buttons created
     */
    private JPanel mapMenuPanel;

    /*
     * game panel is used to show our game to the screen. inside this panel
     * also contains the game loop. This is where out objects are updated and
     * redrawn. This panel will execute its game loop on a separate thread.
     * This is to ensure responsiveness of the GUI. It is also a bad practice to
     * run long running loops(or tasks) on Java Swing's main thread. This thread is
     * called the event dispatch thread.
     */
    private GameWorld gamePanel;

    private JPanel splashPanel;

    private JPanel endPanel;
    /*
     * JFrame used to store our main panel. We will also attach all event
     * listeners to this JFrame.
     */
    private JFrame jf;
    /*
     * CardLayout is used to manage our sub-panels. This is a layout manager
     * used for our game. It will be attached to the main panel.
     */
    private CardLayout cl;

    public Launcher(){
        this.jf = new JFrame();             // creating a new JFrame object
        this.jf.setTitle("Warzone"); // setting the title of the JFrame window.
        this.jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // when the GUI is closed, this will also shutdown the VM
    }

    private void initUIComponents(){
        this.mainPanel = new JPanel(); // create a new main panel
       /* this.splashPanel = new SplashPanel(this);
        this.singInPanel = new SingInMenuPanel(this);*/
        this.startPanel = new StartMenuPanel(this); // create a new start panel
        this.gamePanel = new GameWorld(this); // create a new game panel
        this.mapMenuPanel = new MapMenuPanel(this, gamePanel); // create a new map menu panel
        this.gamePanel.InitializeGame(); // initialize game, but DO NOT start game
        this.endPanel = new EndGamePanel(this); // create a new end game panel;
        cl = new CardLayout(); // creating a new CardLayout Panel
        this.mainPanel.setLayout(cl); // set the layout of the main panel to our card layout
       /* this.mainPanel.add(splashPanel,"splash");
        this.mainPanel.add(singInPanel,"singIn");*/
        this.mainPanel.add(startPanel, "start"); //add the start panel to the main panel
        this.mainPanel.add(mapMenuPanel, "maps"); // add the map menu panel to the main panel
        this.mainPanel.add(gamePanel, "game");   //add the game panel to the main panel
        this.mainPanel.add(endPanel, "end");    // add the end game panel to the main panel
        this.jf.add(mainPanel); // add the main panel to the JFrame
        this.jf.setResizable(false); //make the JFrame not resizable
        this.setFrame("start"); // set the current panel to start panel
    }

    public void setFrame(String type){
        this.jf.setVisible(false); // hide the JFrame
        switch (type) {
          /*  case "splash", "singIn" ->
                    this.jf.setSize(GameScreenConstants.START_MENU_SCREEN_WIDTH, GameScreenConstants.START_MENU_SCREEN_HEIGHT);
*/
            case "start" ->
                // set the size of the jFrame to the expected size for the start panel
                    this.jf.setSize(GameScreenConstants.START_MENU_SCREEN_WIDTH, GameScreenConstants.START_MENU_SCREEN_HEIGHT);
            case "maps" ->
                // set the size of the jFrame to the expected size for the map menu panel
                    this.jf.setSize(GameScreenConstants.MAP_MENU_SCREEN_WIDTH, GameScreenConstants.MAP_MENU_SCREEN_HEIGHT);
            case "game" -> {
                // set the size of the jFrame to the expected size for the game panel
                this.jf.setSize(GameScreenConstants.GAME_SCREEN_WIDTH, GameScreenConstants.GAME_SCREEN_HEIGHT);
                //this.gamePanel.InitializeGame();
                //start a new thread for the game to run. This will ensure our JFrame is responsive and
                // not stuck executing the game loop.
                (new Thread(this.gamePanel)).start();
            }
            case "end" -> {
                // set the size of the jFrame to the expected size for the end panel
                ((EndGamePanel)this.endPanel).updateWinnerStatus();
                this.jf.setSize(GameScreenConstants.END_MENU_SCREEN_WIDTH, GameScreenConstants.END_MENU_SCREEN_HEIGHT);
            }
        }
        this.cl.show(mainPanel, type); // change current panel shown on main panel tp the panel denoted by type.
        this.jf.setVisible(true); // show the JFrame
    }


    public JFrame getJf() {
        return jf;
    }

    public void closeGame(){
        this.jf.dispatchEvent(new WindowEvent(this.jf, WindowEvent.WINDOW_CLOSING));
    }


    public static void main(String[] args) {
        (new Launcher()).initUIComponents();
    }

}
