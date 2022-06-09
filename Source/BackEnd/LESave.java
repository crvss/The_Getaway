package BackEnd;

import FrontEnd.CustomAlerts;
import FrontEnd.LECanvas;
import FrontEnd.Slot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 * The Level Editor Save class. It will save the custom designed board.
 * @author Calum Atkins, Ventsislav Yordanov
 */
public class LESave {
    private File gameFile;
    private LECanvas canvas;
    private Slot[][] slots;
    private int height;
    private int width;
    private String player1Location;
    private String player2Location;
    private String player3Location;
    private String player4Location;
    private String player1Colour;
    private String player2Colour;
    private String player3Colour;
    private String player4Colour;
    private int playerCounter = 0;
    private boolean boolIce = false;
    private boolean boolDouble = false;
    private boolean boolFire = false;
    private boolean boolBack = false;
    private String boardAuthor;
    private String fileName;
    private HashMap<String, String> initData;

    /**
     * Method to save the custom board with the player colors and the enabled/disabled action tiles.
     * @param c the canvas
     * @param id Hashmap id for all the variables needed to save the board.
     */
    public LESave(LECanvas c, HashMap<String, String> id) {
        canvas = c;
        initData = id;
        fileName = id.get("name");
        height = Integer.parseInt(id.get("height"));
        width = Integer.parseInt(id.get("width"));
        boardAuthor = id.get("author");
        player1Colour = id.get("player1col");
        player2Colour = id.get("player2col");
        player3Colour = id.get("player3col");
        player4Colour = id.get("player4col");
        boolIce = Boolean.parseBoolean(id.get("boolIce"));
        boolFire = Boolean.parseBoolean(id.get("boolFire"));
        boolDouble = Boolean.parseBoolean(id.get("boolDouble"));
        boolBack = Boolean.parseBoolean(id.get("boolBack"));
    }

    /**
     * This class will write all the data about a custom board from the level editor to a txt file.
     */
    public void Save() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("Gameboards/" + fileName + ".txt"));

            //slots = canvas.getArrayBoard();
            bw.write("true");
            bw.newLine();
            bw.write(height + "," + width);
            bw.newLine();

            for (int i = 0; i <= 8; i++) {
                for (int j = 0; j <= 8; j++) {

                    String currentTile = null;

                    if (slots[j][i] != null) {
                        Slot tile = slots[j][i];
                        String tileType = tile.getTileType().toString();
                        int rotation = tile.getRotate();

                        //Filling out the rest of the save file with empty tiles to fix LELoad
                        if (tileType.equals("EMPTY") || (j >= height && i >= width)) {
                            bw.write("_");
                        } else if (tileType.equals("CORNER") && rotation == 270) {
                            bw.write("`");
                        } else if (tileType.equals("CORNER") && rotation == 0) {
                            bw.write("¬");
                        } else if (tileType.equals("CORNER") && rotation == 90) {
                            bw.write("'");
                        } else if (tileType.equals("CORNER") && rotation == 180) {
                            bw.write("&");
                        } else if (tileType.equals("STRAIGHT") && rotation == 0) {
                            bw.write("3");
                        } else if (tileType.equals("STRAIGHT") && rotation == 90) {
                            bw.write("2");
                        } else if (tileType.equals("STRAIGHT") && rotation == 180) {
                            bw.write("%");
                        } else if (tileType.equals("STRAIGHT") && rotation == 270) {
                            bw.write("1");
                        } else if (tileType.equals("T_SHAPE") && rotation == 180) {
                            bw.write("£");
                        } else if (tileType.equals("T_SHAPE") && rotation == 270) {
                            bw.write("4");
                        } else if (tileType.equals("T_SHAPE") && rotation == 0) {
                            bw.write("$");
                        } else if (tileType.equals("T_SHAPE") && rotation == 90) {
                            bw.write("5");
                        } else if (tileType.equals("GOAL")) {
                            bw.write("*");
                        }
                        bw.write(",");

                        //TODO PLAYER LOCATION SAVING
                    }
                }
                bw.newLine();
            }

            // Writing the 4 player start locations to the txt file
            bw.write(player1Location); bw.newLine();
            bw.write(player2Location); bw.newLine();
            bw.write(player3Location); bw.newLine();
            bw.write(player4Location); bw.newLine();

            // Writing the author to the file
            bw.write(boardAuthor); bw.newLine();

            // Writing the boolean values for the 4 different action tiles
            bw.write(String.valueOf(boolFire)); bw.newLine();
            bw.write(String.valueOf(boolIce)); bw.newLine();
            bw.write(String.valueOf(boolBack)); bw.newLine();
            bw.write(String.valueOf(boolDouble)); bw.newLine();


            //The following block will write the colour's for each of the players car.
            //Modified to remove "player" to clean up some integration code
            bw.write(player1Colour.substring(6)); bw.newLine();
            bw.write(player2Colour.substring(6)); bw.newLine();
            bw.write(player3Colour.substring(6)); bw.newLine();
            bw.write(player4Colour.substring(6)); bw.newLine();

            CustomAlerts.Warning("Saving File", "File " + fileName + " successfully saved.", true);
            bw.close();
        } catch (IOException e) {
            if (fileName.contains("?") || fileName.contains(".") || fileName.contains("\"") || fileName.contains("/") || fileName.contains("/") || fileName.contains("[") || fileName.contains("]") || fileName.contains(":") || fileName.contains(";") || fileName.contains("|") || fileName.contains(","))   {
                CustomAlerts.Warning("Saving File", "Error while saving " + fileName + " this is because your filename contains characters not allowed by Windows. There are * . \" / / [ ] : ; | ,", true);
            }else   {
                CustomAlerts.Warning("Saving File", "Unknown error when saving " + fileName + ".", true);
            }
            e.printStackTrace();
        }
    }
}
