package FrontEnd;

import BackEnd.*;

/**
 * This is what we store our tiles in for use in the level editor
 * @author Adilet Eshimkanov
 */
public class Slot {

    private TileType tileType;
    int LocX;
    int LocY;
    int rotate;

    /**
     * Creates a new slot
     * @param tileType What tile type we wish to store
     * @param LocX X coordinate relative to the board
     * @param LocY Y Coordinate relative to the board
     * @param rotate The rotation of the tile we're storing
     */
    public Slot( TileType tileType, int LocX, int LocY, int rotate) {
        this.tileType = tileType;
        this.rotate = rotate;
        this.LocX = LocX;
        this.LocY = LocY;

    }

    //SETTERS & GETTERS

    public void setTileType(TileType tileType) {
        this.tileType = tileType;
    }

    public TileType getTileType() {
        return tileType;
    }

    public int getLocX() {
        return LocX;
    }

    public void setLocX(int locX) {
        LocX = locX;
    }

    public int getLocY() {
        return LocY;
    }

    public void setLocY(int locY) {
        LocY = locY;
    }

    public void setRotate(int rotate) {
        this.rotate = rotate;
    }

    public int getRotate() {
        return rotate;
    }
}
