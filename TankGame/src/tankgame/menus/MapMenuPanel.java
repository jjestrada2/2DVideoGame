package tankgame.menus;

import tankgame.Launcher;
import tankgame.constants.GameScreenConstants;
import tankgame.constants.ResourceConstants;
import tankgame.game.GameWorld;

import javax.swing.*;
import java.awt.*;

public class MapMenuPanel extends MenuPanel {

    private GameWorld gamePanel;
    private JButton map1;
    private JButton map2;
    private JButton map3;
    private JButton mainMenu;

    public MapMenuPanel(Launcher lf, GameWorld gamePanel) {
        super(lf, "menu.png");
        this.gamePanel = gamePanel;
        map1 = createMapButtons(ResourceConstants.MAP_1_CSV, 175, 40, 175, 40);
        map2 = createMapButtons(ResourceConstants.MAP_2_CSV, 175, 140, 175, 40);
        map3 = createMapButtons(ResourceConstants.MAP_3_CSV, 175, 280, 175, 40);
        mainMenu = super.createButton("BACK", 24, 175, 400, 175, 40, "start");
        this.add(map1);
        this.add(map2);
        this.add(map3);
        this.add(mainMenu);
    }

    private String truncateMapName(String map) {
        return map.substring(0, map.indexOf("."));
    }

    private JButton createMapButtons(String map, int x, int y, int width, int height) {
        JButton result =  new JButton(this.truncateMapName(map));
        result.setFont(new Font("Courier New", Font.BOLD, 24));
        result.setBounds(x, y, width, height);
        result.setBackground(Color.decode(GameScreenConstants.BUTTON_BACKGROUND_COLOR));
        result.setForeground(Color.decode(GameScreenConstants.BUTTON_FOREGROUND_COLOR));
        result.setFocusPainted(false);
        result.addActionListener((actionEvent -> {
            this.gamePanel.selectMap(map);
            this.lf.setFrame("game");
        }));
        return result;
    }
}
