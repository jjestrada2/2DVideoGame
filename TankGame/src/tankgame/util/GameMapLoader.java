package tankgame.util;

import tankgame.constants.GameObjectID;
import tankgame.game.GameObject;
import tankgame.game.GameObjectFactory;
import tankgame.game.GameWorld;
import tankgame.game.dynamicObjects.DynamicObject;
import tankgame.game.immobileObjects.ImmobileObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

public class GameMapLoader {

    private static GameMapLoader instance;
    private static final ArrayList<int[]> emptySpaces = new ArrayList<>();

    private GameMapLoader() {}

    public static GameMapLoader getInstance() {
        if(instance == null)
            instance = new GameMapLoader();
        return instance;
    }

    public ArrayList<int[]> getEmptySpaces() {
        return emptySpaces;
    }

    public void initializeMap(GameWorld gw, String map) {
        try(BufferedReader mapReader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(GameWorld.class.getClassLoader().getResourceAsStream("maps/" + map))))) {
            GameObjectFactory gameObjectFactory = new GameObjectFactory();
            for(int col = 0; mapReader.ready(); col++) {
                String[] items = mapReader.readLine().split(",");
                for(int row = 0; row < items.length; row++) {
                    String gameObjectID = items[row].replaceAll("\\s+", "");
                    gameObjectID = gameObjectID.toUpperCase();
                    GameObject gameObject = gameObjectFactory.createGameObject(gameObjectID, row, col, gw);

                    if(GameObjectID.BORDER.equalsIgnoreCase(gameObjectID))
                        gw.addToCollisionlessGameObjectCollections(gameObject);
                    else if(gameObject instanceof ImmobileObject)
                        gw.addToStationaryGameObjectCollections((ImmobileObject) gameObject);
                    else if(gameObject instanceof DynamicObject)
                        gw.addToMovableGameObjectCollections((DynamicObject) gameObject);
                    else if(gameObject == null) {
                        emptySpaces.add(new int[]{col, row});
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e);
            System.exit(-2);
        }
    }
}
