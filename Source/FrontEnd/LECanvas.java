package FrontEnd;

import BackEnd.TileType;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.util.Pair;
import javafx.scene.Cursor;

import java.awt.*;

/**
 * This method handles the drawing of tiles on the level editor
 * Ideally this method should be called very little times directly from LEMainController, calls should come from LEBoard and LEPlayers
 * The object will still exist in LEMainController however as it needs to be shared between LEBoard and LEPlayers
 * @Author Pat Sambor
 */
public class LECanvas   {
    Image[] tileImages = new Image[5];
    Image highlightImage;

    private final String EXT = ".png";

    private ResizableCanvas mainCanvas;

    private int minBoardSizePX;
    int topLeftX = 0;
    int topLeftY = 0;
    int tileSize = 0;

    GraphicsContext gc;

    /**
     * Constructor that loads in all the images we need and sets a graphics context
     * @param canvImport The Canvas from LEMainController that we'll be working with
     */
    public LECanvas(Canvas canvImport)  {
        mainCanvas = (ResizableCanvas) canvImport;

        tileImages[0] = new Image("empty.png");
        tileImages[1] = new Image("corner.png");
        tileImages[2] = new Image("straight.png");
        tileImages[3] = new Image("t_shape.png");
        tileImages[4] = new Image("goal.png");

        

        gc = mainCanvas.getGraphicsContext2D();

        highlightImage = new Image("selection_box.png");
    }

    /**
     * This method draws a board on the canvas, note that (other than slot info to know what to draw) no operations on the board itself happen here, this is just drawing it
     * @param boardAsArray The entire board (consisting of slots) as a 2 Dimensional array
     * @param boardWidth Self-Explanatory
     * @param boardHeight Also Self-Explanatory
     */
    public void renderBoard(Slot[][] boardAsArray, int boardWidth, int boardHeight)    {
        mainCanvas.drawme();

        //Grabbing the smallest size property of the canvas
        minBoardSizePX = Math.min((int) mainCanvas.getWidth(), (int) mainCanvas.getHeight());

        boolean heightCheck = false;

        //Splitting pixel size here into even squares depending on board size
        if (boardWidth > boardHeight && (mainCanvas.getWidth() / boardWidth) * boardHeight <= mainCanvas.getHeight())   {
            tileSize = (int) mainCanvas.getWidth() / boardWidth;
            heightCheck = false;
        }else   {
            tileSize = (int) mainCanvas.getHeight() / boardHeight;
            heightCheck = true;
        }

        //Finding the top left of X and Y to make sure everything is centered properly
        //Basically we find what the size of the board itself is in pixels, and then halve the remainder
        topLeftX = (int) (mainCanvas.getWidth() - (tileSize * boardWidth)) /2;
        topLeftY = (int) (mainCanvas.getHeight() - (tileSize * boardHeight)) /2;

        if (boardWidth > boardHeight && tileSize * boardHeight <= mainCanvas.getHeight())  { topLeftX = 0; } else   { topLeftY = 0; }

        //Declaring initial pixel drawing positions
        int whereXDraw = topLeftX;
        int whereYDraw = topLeftY;
        for (int y = 0; y<boardHeight; y++)  {
            for (int x = 0; x<boardWidth; x++)  {
                int tileToDraw = whichTile(boardAsArray[x][y].getTileType());
                if (tileToDraw == 5)    { return; }
                drawAThing(boardAsArray[x][y].getRotate(), whereXDraw, whereYDraw, tileImages[tileToDraw]);
                whereXDraw += tileSize;
            }
            whereXDraw = topLeftX;
            whereYDraw += tileSize;
        }
    }

    /**
     * This method takes in a slot from LEBoard and gives a number corresponding to that tile's image in tileImages
     * @param typeToCheck The TileType you're converting
     * @return An integer value corresponding to this
     */
    public int whichTile(TileType typeToCheck)  {
        switch(typeToCheck)   {
            case EMPTY:
                return 0;
            case CORNER:
                return 1;
            case STRAIGHT:
                return 2;
            case T_SHAPE:
                return 3;
            case GOAL:
                return 4;
            default:
                CustomAlerts.Warning("User Alert!", "The board you're trying to load has an incorrect tile type, either contact me about this or mess around with your save file until it works", true);
                return 5;
        }
    }

    /**
     * Intermediary method used to rotate the images on canvas before drawing.
     *
     * @param gc    Graphics context
     * @param angle angle of rotfation
     * @param px    location x to draw on
     * @param py    location y to draw on
     */
    public void rotateMethod(GraphicsContext gc, double angle, double px, double py) {
        Rotate r = new Rotate(angle, px, py);
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
    }

    /**
     * This method converts a mouse's pixel location to a corresponding tile on the board
     * @param mouseX X Pixel Coordinates of mouse
     * @param mouseY Y Pixel Coordinates of mouse
     * @return A Pair of integers that represent coordinates (X is 1st, Y is 2nd), if the return is 0 that means the mouse is out of bounds
     */
    public Pair<Integer,Integer> mouseLocConverter(double mouseX, double mouseY, int boardWidth, int boardHeight) {
        int xCoord = 0;
        int yCoord = 0;
        if (mouseX < topLeftX || mouseY < topLeftY || mouseX > (tileSize * boardWidth) + topLeftX || mouseY > (tileSize * boardHeight) + topLeftY) {
            mainCanvas.setCursor(Cursor.DEFAULT);
            return new Pair<>(xCoord,yCoord);
        }else   {
            mainCanvas.setCursor(Cursor.HAND);
        }
        //If the mouse coordinate is within the bounds of the board we check the mouse coordinate to see if it corresponds to tileSize * the tile you're checking
        for (int x = 1; x<=9; x++)  {
            if (mouseX <= (tileSize * x) + topLeftX && boardWidth > x - 1) {
                xCoord = x;
                break;
            }
        }
        //ditto
        for (int y = 0; y<=9; y++)  {
            if (mouseY <= (tileSize * y) + topLeftY && boardHeight > y-1)   {
                yCoord = y;
                break;
            }
        }
        return new Pair<>(xCoord, yCoord);
    }

    public void renderPlayers() {

    }

    double prevXDrawn = -1;
    double prevYDrawn = -1;
    int prevRotate = -1;
    Image prevImage = new Image("blank.png");

    /**
     * Draws the highlight square over a tile when you hover over it
     * @param renderLocation
     * @param playerLocations Player locations in an array so they are highlighted instead of a tile if you have nothing in tempSel
     * @param boardAsArray
     */
    public void renderHighlights(Pair<Integer,Integer> renderLocation, Pair<Integer, Integer>[] playerLocations, Slot[][] boardAsArray, String tempSel, int hoverTileRotate)  {
        if ((prevXDrawn != -1 || prevYDrawn != -1) && tempSel != "placing")   {
            drawAThing(prevRotate, prevXDrawn, prevYDrawn, prevImage);
        }

        //If these values are 0 that means the mouse is out of bounds so we don't want to draw anything
        if (renderLocation.getKey() == 0 || renderLocation.getValue() == 0) {
            prevXDrawn = -1;
            prevYDrawn = -1;
            prevImage = new Image("blank.png");
            prevRotate = -1;
            return;
        }

        double whereXDrawn = ((renderLocation.getKey() * tileSize) - tileSize) + topLeftX;
        double whereYDrawn = ((renderLocation.getValue() * tileSize) - tileSize) + topLeftY;

        //If a tile is selected, we draw that tile first and then the highlighter square
        if (tempSel != "blank" && !tempSel.contains("player") && tempSel != "placing") {
            int tileNo = whichTile(TileType.valueOf(tempSel.toUpperCase()));
            drawAThing(hoverTileRotate, whereXDrawn, whereYDrawn, tileImages[tileNo]);
        }

        drawAThing(0, whereXDrawn, whereYDrawn, highlightImage);

        prevXDrawn = whereXDrawn;
        prevYDrawn = whereYDrawn;
        int prevTileArray = whichTile(boardAsArray[renderLocation.getKey() - 1][renderLocation.getValue() - 1].getTileType());
        prevImage = tileImages[prevTileArray];
        prevRotate = boardAsArray[renderLocation.getKey() - 1][renderLocation.getValue() - 1].getRotate();
    }

    //You're trash LECanvas
    public void renderPizza()   {

    }

    /**
     * This method draws something at the specified location you wish it to be drawn
     * @param rotate The rotation at which you'd like to display your image
     * @param whereXDrawn X coordinate of drawing relative to screen
     * @param whereYDrawn Y coordinate of drawing relative to screen
     * @param imgToDraw The image you wish to draw
     */
    public void drawAThing(int rotate, double whereXDrawn, double whereYDrawn, Image imgToDraw) {
        //If we somehow get here despite all the checks in the main highlighter method, just fall back.
        if(whereXDrawn == -1)    {
            return;
        }
        gc.save();
        rotateMethod(gc, rotate, whereXDrawn + tileSize / 2, whereYDrawn + tileSize / 2);
        gc.drawImage(imgToDraw, whereXDrawn, whereYDrawn, tileSize, tileSize);
        gc.restore();
    }
}