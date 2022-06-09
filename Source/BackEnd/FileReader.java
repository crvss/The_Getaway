package BackEnd;

import FrontEnd.CustomAlerts;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

/**
 * This class is a file reader class which will take in a given level file format text file, verify it
 * and create the gameboard, along with the fixed tiles in their correct locations, the players in their correct
 * locations, and the amount and type of each floor/action tile that will populate the silk bag.
 *
 * @author Christian Sanger, Atif Ishaq, Joshua Oladitan & Pat Sambor.
 * @version 1.0
 */

public class FileReader {

    /*
    These static variables hold information about the number of players and the number
    of different tile types.
     */
    private static final int NUM_OF_TILE_TYPES = TileType.values().length;
    private static final int MAX_NUM_OF_PLAYERS = 4;

    /**
     * This method takes in the given level format file,
     * and create a gameboard and players for that game.
     *
     * @param filename    The name of the level file format text file.
     * @param silkBagSeed The seed used for this game.
     * @param initData    The initialisation data, has to be passed around like a
     * @return A list of elements (replaces a pair that was here before), if it's not a custom level
     * @throws Exception issue with gameboard file
     */
    public static ArrayList<Object> gameSetup(String filename, int silkBagSeed, HashMap<String, String> initData) throws Exception {
        File input = new File("Gameboards/" + filename);
        if (!input.exists()) {
            throw (new FileNotFoundException(filename));
        }
        Scanner in = new Scanner(input);
        Scanner currentLine;
        Scanner customLine;
        SilkBag silkBag = new SilkBag(silkBagSeed);
        //Holder for the first line of the file, so that if the file isn't custom the reader isn't a line ahead
        String lineHold = in.nextLine();
        //// board config
        currentLine = new Scanner(lineHold);
        customLine = new Scanner(lineHold);
        //Added a "true" to the top of each file that is custom made so they can be distinguished after being put into the same folder
        //Didn't make sense to me to have them be in different folders, they're both boards after all and will use the same classes to load.
        if(customLine.next().equals("true"))   {
            currentLine.close();
            customLine.close();
            ArrayList<Object> listOfLoading = FileReader.LEGameplayLoader(filename, initData, in);
            return listOfLoading;
        }else   {
            customLine.close();
        }
        int width = currentLine.nextInt();
        int height = currentLine.nextInt();
        Gameboard gameboard = new Gameboard(width, height, silkBag);

        //// Creating players
        Player[] players = new Player[MAX_NUM_OF_PLAYERS];
        Coordinate[] playerPos = new Coordinate[MAX_NUM_OF_PLAYERS];
        gameboard.setNumOfPlayers(MAX_NUM_OF_PLAYERS);
        for (int i = 0; i < MAX_NUM_OF_PLAYERS; i++) {
            String nextLine = in.nextLine();
            currentLine = new Scanner(nextLine);
            int x = currentLine.nextInt();
            int y = currentLine.nextInt();
            playerPos[i] = new Coordinate(x, y);
            gameboard.setPlayerPos(i, new Coordinate(x, y));
            players[i] = new Player(silkBag, gameboard);
        }

        //// Filling SilkBag
        int[] tileTypeCount = new int[NUM_OF_TILE_TYPES];
        // Reading how many of each tile
        for (int tileType = 0; tileType < NUM_OF_TILE_TYPES - 1; tileType++) {
            currentLine = new Scanner(in.nextLine());
            tileTypeCount[tileType] = currentLine.nextInt();
        }
        // putting them in the bag
        // for each tile type
        for (int tileType = 0; tileType < NUM_OF_TILE_TYPES - 1; tileType++) {
            int numberOfThisTile = tileTypeCount[tileType];
            // for each tile that need to be added to silkbag
            for (int i = 0; i < numberOfThisTile; i++) {
                Tile newTile = Tile.createTile(TileType.values()[tileType]);
                silkBag.insertTile(newTile);
            }
        }

        //// Fill with random tiles
        Random r = new Random(silkBagSeed);
        ArrayList<Coordinate> slideLocations = gameboard.getSlideLocations();

        while (gameboard.isBoardNotFull()) {
            Coordinate toSlide = null;
            if (slideLocations.size() == 0) {
                throw new Exception("No slide locations");
            }

            for (int i = 0; i < slideLocations.size(); i++) {
                FloorTile tile = silkBag.getFloorTile();
                tile.setRotation(Rotation.values()[r.nextInt(4)]);
                toSlide = slideLocations.get(r.nextInt(slideLocations.size()));
                if (toSlide == null) {
                    throw new Exception("Null Slide location");
                }
                gameboard.playFloorTile(toSlide, tile);
            }
        }
        //// Fixed tiles
        currentLine = new Scanner(in.nextLine());
        int numberOfFixedTiles = currentLine.nextInt();
        for (int i = 0; i < numberOfFixedTiles; i++) {
            currentLine = new Scanner(in.nextLine());
            TileType tileType = TileType.valueOf(currentLine.next().toUpperCase());
            int x = currentLine.nextInt();
            int y = currentLine.nextInt();
            Coordinate location = new Coordinate(x, y);
            int rotationInt = currentLine.nextInt();
            Rotation rotation = Rotation.values()[rotationInt];
            FloorTile tile = new FloorTile(tileType, rotation);
            gameboard.placeFixedTile(tile, location);
        }

        // Place players back in correct location as they would have been shifted when tiles have been placed
        for (int i = 0; i < MAX_NUM_OF_PLAYERS; i++) {
            for (int j = 0; j < 4; j++) {
                gameboard.setPlayerPos(i, playerPos[i]);
            }
        }
        ArrayList<Object> newGameList = new ArrayList<>();

        newGameList.add(gameboard);
        newGameList.add(players);
        //NOT a custom board
        newGameList.add(false);
        return  newGameList;
    }

    /**
     * This method takes in the given level format file, and checks to see that the file exists.
     *
     * @param gameBoard The name of the level file format text file.
     * @return in The scanner that iterates through the file.
     * @throws Exception if cannot create gameSetup
     */
    public static ArrayList<Object> gameSetup(String gameBoard) throws Exception {
        return gameSetup(gameBoard, (new Random().nextInt()), (new HashMap<String, String>()));

    }

    /**
     * This method reads in the data from a custom level file and turns it into a list of objects that are to be processed.
     * @param fileName The name of the file we're reading from
     * @param initData Initialisation Data
     * @param reader The scanner created by the previous method
     * @return A list of all the necesarry objects to get the game to play
     * @throws FileNotFoundException This exception is protected against as part of the main menu UI so it should never trigger (fingers crossed)
     */
    private static ArrayList<Object> LEGameplayLoader(String fileName, HashMap<String, String> initData, Scanner reader) throws FileNotFoundException {

        String line = reader.nextLine();

        //Setting the width and height of the board
        String[] heightWidth = line.split(",");
        int height = Integer.parseInt(heightWidth[0]);
        int width = Integer.parseInt(heightWidth[1]);
        FloorTile tileToAdd;
        ArrayList<FloorTile> theTiles = new ArrayList<>();

        //Setting the TileType of each tile
        for (int i = 0; i < height; i++) {
            Scanner lineScan = new Scanner(reader.nextLine());
            lineScan.useDelimiter(",");
            for (int j = 0; j < width; j++) {
                String character = lineScan.next();

                switch (character) {
                    case "_":
                        tileToAdd = new FloorTile(TileType.EMPTY);
                        tileToAdd.setRotation(Rotation.UP);
                        tileToAdd.setLocation(new Coordinate(j, i));
                        theTiles.add(tileToAdd);
                        break;
                    case "`":
                        tileToAdd = new FloorTile(TileType.CORNER);
                        tileToAdd.setRotation(Rotation.LEFT);
                        tileToAdd.setLocation(new Coordinate(j, i));
                        theTiles.add(tileToAdd);
                        break;
                    case "¬":
                        tileToAdd = new FloorTile(TileType.CORNER);
                        tileToAdd.setRotation(Rotation.UP);
                        tileToAdd.setLocation(new Coordinate(j, i));
                        theTiles.add(tileToAdd);
                        break;
                    case "'":
                        tileToAdd = new FloorTile(TileType.CORNER);
                        tileToAdd.setRotation(Rotation.RIGHT);
                        tileToAdd.setLocation(new Coordinate(j, i));
                        theTiles.add(tileToAdd);
                        break;
                    case "&":
                        tileToAdd = new FloorTile(TileType.CORNER);
                        tileToAdd.setRotation(Rotation.DOWN);
                        tileToAdd.setLocation(new Coordinate(j, i));
                        theTiles.add(tileToAdd);
                        break;
                    case "3":
                        tileToAdd = new FloorTile(TileType.STRAIGHT);
                        tileToAdd.setRotation(Rotation.UP);
                        tileToAdd.setLocation(new Coordinate(j, i));
                        theTiles.add(tileToAdd);
                        break;
                    case "2":
                        tileToAdd = new FloorTile(TileType.STRAIGHT);
                        tileToAdd.setRotation(Rotation.RIGHT);
                        tileToAdd.setLocation(new Coordinate(j, i));
                        theTiles.add(tileToAdd);
                        break;
                    case "%":
                        tileToAdd = new FloorTile(TileType.STRAIGHT);
                        tileToAdd.setRotation(Rotation.DOWN);
                        tileToAdd.setLocation(new Coordinate(j, i));
                        theTiles.add(tileToAdd);
                        break;
                    case "1":
                        tileToAdd = new FloorTile(TileType.STRAIGHT);
                        tileToAdd.setRotation(Rotation.LEFT);
                        tileToAdd.setLocation(new Coordinate(j, i));
                        theTiles.add(tileToAdd);
                        break;
                    case "£":
                        tileToAdd = new FloorTile(TileType.T_SHAPE);
                        tileToAdd.setRotation(Rotation.DOWN);
                        tileToAdd.setLocation(new Coordinate(j, i));
                        theTiles.add(tileToAdd);
                        break;
                    case "4":
                        tileToAdd = new FloorTile(TileType.T_SHAPE);
                        tileToAdd.setRotation(Rotation.LEFT);
                        tileToAdd.setLocation(new Coordinate(j, i));
                        theTiles.add(tileToAdd);
                        break;
                    case "$":
                        tileToAdd = new FloorTile(TileType.T_SHAPE);
                        tileToAdd.setRotation(Rotation.UP);
                        tileToAdd.setLocation(new Coordinate(j, i));
                        theTiles.add(tileToAdd);
                        break;
                    case "5":
                        tileToAdd = new FloorTile(TileType.T_SHAPE);
                        tileToAdd.setRotation(Rotation.RIGHT);
                        tileToAdd.setLocation(new Coordinate(j, i));
                        theTiles.add(tileToAdd);
                        break;
                    case "*":
                        tileToAdd = new FloorTile(TileType.GOAL);
                        tileToAdd.setRotation(Rotation.UP);
                        tileToAdd.setLocation(new Coordinate(j, i));
                        theTiles.add(tileToAdd);
                        break;
                    default: CustomAlerts.Warning("User Alert", "Something is wrong with the structure of the tiles in your saved level, please try a different one.", true); return null;
                }
            }
            lineScan.close();
        }

        //Player location setting
        Coordinate[] theCoordinates = new Coordinate[4];
        for (int i = 0; i < 4; i++) {
            Scanner locScan = new Scanner(reader.nextLine());
            locScan.useDelimiter(",");
            theCoordinates[i] = new Coordinate(locScan.nextInt(), locScan.nextInt());
            locScan.close();
        }

        //Skipping the map author
        reader.nextLine();

        //ActionTile status checker
        ArrayList<Boolean> boolActionTiles = new ArrayList<>();
        for (int i = 0; i<4; i++)   {
            Scanner lineScan = new Scanner(reader.nextLine());
            boolActionTiles.add(lineScan.nextBoolean());
        }

        //SilkBag Creation and filling it with tiles
        SilkBag bagOfSilk = new SilkBag((int) (Math.random() * 100));

        //// Filling SilkBag
        int[] tileTypeCount = new int[NUM_OF_TILE_TYPES];
        // Checking which action tiles were enabled
        int boolActionCounter = 0;
        for (int tileType = 0; tileType < NUM_OF_TILE_TYPES - 1; tileType++) {
            //The 3 Movement Tiles
            if (tileType<3) {
                tileTypeCount[tileType] = 100;
            //The Action Tiles
            }else if (tileType<7)   {
                if (boolActionTiles.get(boolActionCounter)) {
                    tileTypeCount[tileType] = 100;
                }else   {
                    tileTypeCount[tileType] = 0;
                }
                boolActionCounter+=1;
            //Goal & Empty never go into the SilkBag
            }else   {
                tileTypeCount[tileType] = 0;
            }
        }
        // putting tiles in the bag
        // for each tile type
        for (int tileType = 0; tileType < NUM_OF_TILE_TYPES - 1; tileType++) {
            int numberOfThisTile = tileTypeCount[tileType];
            // for each tile that need to be added to silkbag
            for (int i = 0; i < numberOfThisTile; i++) {
                Tile newTile = Tile.createTile(TileType.values()[tileType]);
                bagOfSilk.insertTile(newTile);
            }
        }
        Gameboard gameboard = new Gameboard(width, height, bagOfSilk,theTiles, theCoordinates[0], theCoordinates[1], theCoordinates[2], theCoordinates[3]);
        Player[] thePlayers = new Player[4];
        thePlayers[0] = new Player(bagOfSilk, gameboard);
        thePlayers[1] = new Player(bagOfSilk, gameboard);
        thePlayers[2] = new Player(bagOfSilk, gameboard);
        thePlayers[3] = new Player(bagOfSilk, gameboard);

        ArrayList<Object> LEList = new ArrayList<>();

        LEList.add(gameboard);
        LEList.add(thePlayers);
        //Signal to the classes that handle stuff after this that custom stuff is happening
        LEList.add(true);
        //Now we read in the CarColours as actual CarColour ENUMS

        LEList.add(CarColours.valueOf(reader.nextLine()));
        LEList.add(CarColours.valueOf(reader.nextLine()));
        LEList.add(CarColours.valueOf(reader.nextLine()));
        LEList.add(CarColours.valueOf(reader.nextLine()));
        return LEList;

    }
}