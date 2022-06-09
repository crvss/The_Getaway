package BackEnd;

import FrontEnd.CustomAlerts;
import FrontEnd.LECanvas;
import javafx.util.Pair;

/**
 * This class handles all the player information during a given Level Editor Session
 * @Author Pat Sambor
 */
public class LEPlayer {
    private Pair<Integer, Integer>[] playerLocations = new Pair[4];
    private CarColours[] playerColors = new CarColours[4];
    LECanvas boardRender;

    public LEPlayer(LECanvas boardRender, int boardWidth, int boardHeight)   {
        this.boardRender = boardRender;
        //Setting default locations and colours for a new board
        playerLocations[0] = new Pair(0, 0);
        playerLocations[1] = new Pair(boardWidth - 1, 0);
        playerLocations[2] = new Pair(0, boardHeight - 1);
        playerLocations[3] = new Pair(boardWidth - 1, boardHeight - 1);

        playerColors[0] = CarColours.PINK;
        playerColors[1] = CarColours.YELLOW;
        playerColors[2] = CarColours.TURQUOISE;
        playerColors[3] = CarColours.ORANGE;
    }

    public LEPlayer(Pair<Integer,Integer>[] locationsOfPlayers, LECanvas boardRender, CarColours[] colorsOfPlayers) {
        if (locationsOfPlayers.length > 4 || colorsOfPlayers.length > 4)    {
            CustomAlerts.Warning("User Alert", "There is an invalid number of players in your saved level! \n\n Error code 400", true);
            System.exit(400);
        }
        playerLocations = locationsOfPlayers;
        playerColors = colorsOfPlayers;
        this.boardRender = boardRender;
    }

    public void setPlayerLocations(int playerToSet, Pair<Integer,Integer> locationToSet)   {
        if (playerToSet > 4 || playerToSet < 0)    {
            CustomAlerts.Warning("User Alert!", "You tried to set the location of an invalid player \n\n Error code 400", true);
        }
        playerLocations[playerToSet - 1] = locationToSet;
    }

    public void setPlayerColors(int playerToset, CarColours colorToSet) {
        playerColors[playerToset - 1] = colorToSet;
    }
}
