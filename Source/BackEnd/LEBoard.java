package BackEnd;

import BackEnd.TileType;
import FrontEnd.CustomAlerts;
import FrontEnd.LECanvas;
import FrontEnd.Slot;
import javafx.util.Pair;

/**
 * This class handles the backend logic for what's drawn on the Canvas within the level editor
 * So this stores the board as programming values and modifies them as needed
 * This method
 */
public class LEBoard {
    private Slot[][] boardAsArray = new Slot[9][9];

    private LECanvas boardRender;

    private int xSize;
    private int ySize;

    public LEBoard(LECanvas drawingMachine, int boardWidth, int boardHeight, boolean isNew)  {
        boardRender = drawingMachine;
        xSize = boardWidth;
        ySize = boardHeight;
        if (isNew)  {
            for (int y = 0; y<9; y++)   {
                for (int x = 0; x<9; x++)   {
                    boardAsArray[x][y] = new Slot(TileType.EMPTY, x+1, y+1, 0);
                }
            }
        }else   {
            System.exit(0);
        }
    }

    public void placeATile(double mouseX, double mouseY, String tempSel, int tileRotation)    {
        Pair<Integer, Integer> placeCoords = boardRender.mouseLocConverter(mouseX, mouseY, xSize, ySize);

        if (tempSel == "blank" || placeCoords.getKey() == 0 || placeCoords.getValue() == 0 || tempSel.contains("player")) {
            return;
        }else   {
            boardAsArray[placeCoords.getKey() - 1][placeCoords.getValue() - 1] = new Slot(TileType.valueOf(tempSel.toUpperCase()), placeCoords.getKey(), placeCoords.getValue(), tileRotation);
            boardRender.renderBoard(boardAsArray, xSize, ySize);
            Pair<Integer, Integer> playersTemp[] = new Pair[0];
            //Re-Rendering the highlight so you don't have to move your mouse after placing
            boardRender.renderHighlights(boardRender.mouseLocConverter(mouseX, mouseY, xSize, ySize), playersTemp,boardAsArray, "placing", 0);
        }
    }

    public void setxSize(int newSize)   {
        xSize = newSize;
        boardRender.renderBoard(boardAsArray, xSize, ySize);
    }

    public void setySize(int newSize)   {
        ySize = newSize;
        boardRender.renderBoard(boardAsArray, xSize, ySize);
    }

    public Slot[][] getBoardAsArray() {
        return boardAsArray;
    }
}
