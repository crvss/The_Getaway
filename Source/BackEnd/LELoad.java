package BackEnd;

import BackEnd.TileType;
import FrontEnd.CustomAlerts;
import FrontEnd.Slot;
import FrontEnd.WindowLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import FrontEnd.Slot;
import javafx.stage.Window;

/**
 * The Level Editor Load class. It will load custom designed boards for a new game.
 * @author Calum Atkins, Ventsislav Yordanov
 */
public class LELoad {

    WindowLoader wl;
    private int height;
    private int width;

    private int numberOfPlayers;
    private HashMap<String, String> initData;

    public void LELoad() {
    }

    /**
     * Method to read from a text file and load up a board based on the data.
     * @param fileName The name of file to be loaded.
     */
    public void loadForLE(String fileName, HashMap<String, String> id, Scene scene, Stage prevStage) throws IOException {
        initData = id;
        try {
            File fileToLoad = new File("Gameboards/" + fileName);
            Scanner reader = new Scanner(fileToLoad);
            String loadMsg = "Load Complete";
            reader.nextLine();
            initData.put("name", fileName);
            String line = reader.nextLine();
            String[] heightWidth = line.split(",");
            height = Integer.parseInt(heightWidth[0]);
            width = Integer.parseInt(heightWidth[1]);
            initData.put("height", String.valueOf(height));
            initData.put("width", String.valueOf(width));

            String character = null;
            Slot[][] slots = new Slot[9][9];
            for (int i = 0; i <= 8; i++) {
                Scanner lineScan = new Scanner(reader.nextLine());
                lineScan.useDelimiter(",");
                for (int j = 0; j <= 8; j++) {
                    character = lineScan.next();

                    switch (character) {
                        case "_": slots[j][i] = new Slot(TileType.EMPTY, i, j, 0); break;
                        case "`": slots[j][i] = new Slot(TileType.CORNER, i, j, 270); break;
                        case "¬": slots[j][i] = new Slot(TileType.CORNER, i, j, 0); break;
                        case "'": slots[j][i] = new Slot(TileType.CORNER, i, j, 90); break;
                        case "&": slots[j][i] = new Slot(TileType.CORNER, i, j, 180); break;
                        case "3": slots[j][i] = new Slot(TileType.STRAIGHT, i, j, 0); break;
                        case "2": slots[j][i] = new Slot(TileType.STRAIGHT, i, j, 90); break;
                        case "%": slots[j][i] = new Slot(TileType.STRAIGHT, i, j, 180); break;
                        case "1": slots[j][i] = new Slot(TileType.STRAIGHT, i, j, 270); break;
                        case "£": slots[j][i] = new Slot(TileType.T_SHAPE, i, j, 180); break;
                        case "4": slots[j][i] = new Slot(TileType.T_SHAPE, i, j, 270); break;
                        case "$": slots[j][i] = new Slot(TileType.T_SHAPE, i, j, 0); break;
                        case "5": slots[j][i] = new Slot(TileType.T_SHAPE, i, j, 90); break;
                        case "*": slots[j][i] = new Slot(TileType.GOAL, i, j, 0);; break;
                        default: CustomAlerts.Warning("User Alert", "Something is wrong with the structure of the tiles in your saved level, please try a different one.", true); return;
                    }
                }
                lineScan.close();
            }


            numberOfPlayers = 4;
;
            String[] startLocationX = new String[4];
            String[] startLocationY = new String[4];
            for (int i = 0; i < numberOfPlayers; i++) {
                Scanner locScan = new Scanner(reader.nextLine());
                locScan.useDelimiter(",");
                startLocationX[i] = locScan.next();
                startLocationY[i] = locScan.next();
                locScan.close();

            }



            String author = reader.nextLine();
            initData.put("author", author);
            //System.out.println(author);
            String boolFire = reader.nextLine();
            String boolIce = reader.nextLine();
            String boolBack = reader.nextLine();
            String boolDouble = reader.nextLine();
            initData.put("boolFire", boolFire);
            initData.put("boolIce", boolIce);
            initData.put("boolBack", boolBack);
            initData.put("boolDouble", boolDouble);

            String player1Col = reader.nextLine();
            String player2Col = reader.nextLine();
            String player3Col = reader.nextLine();
            String player4Col = reader.nextLine();
            initData.put("player1col", "player" + player1Col);
            initData.put("player2col", "player" + player2Col);
            initData.put("player3col", "player" + player3Col);
            initData.put("player4col", "player" + player4Col);
            initData.put("loaded", "true");

            //get values and return array board
            reader.close();
            WindowLoader wl;
            wl = new WindowLoader(prevStage);
            wl.loadCustomLE("LEMain", initData,slots, startLocationX,startLocationY);

        }
        catch (FileNotFoundException e) {
            CustomAlerts.Warning("User Alert!", "The File you're trying to load couldn't be found : (", true);
            e.printStackTrace();
        }

    }


}
