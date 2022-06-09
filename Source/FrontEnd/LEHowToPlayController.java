package FrontEnd;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class is used to control the Level editor guide.
 * @author Felix Ifrim
 */

public class LEHowToPlayController extends StateLoad {

    //These variables are used to help with the display of the guide menu.
    @FXML
    private Button leChangeColorButton;

    @FXML
    private Button leTilesButton;

    @FXML
    private Button leStartingPositionsButton;

    @FXML
    private Button leActionTilesButton;

    @FXML
    private Button backToLE;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }


    /**
     * This method is used to open the change color guide.
     */
    public void onChangeColorButton() throws IOException {
        WindowLoader w = new WindowLoader(backToLE);
        w.howToPlayLoader("LEColor", "LEHow to Use", backToLE.getScene());
    }

    /**
     * This method is used to open the tiles guide.
     */
    public void onLETilesButton() throws IOException {
        WindowLoader w = new WindowLoader(backToLE);
        w.howToPlayLoader("LETiles", "LEHow to Use", backToLE.getScene());
    }

    /**
     * This method is used to open the starrting positions guide.
     */
    public void onLEStartingPositions() throws IOException {
        WindowLoader w = new WindowLoader(backToLE);
        w.howToPlayLoader("StartingPositions", "LEHow to Use", backToLE.getScene());
    }

    /**
     * This method is used to open the action tiles guide
     */
    public void onLEActionTiles() throws IOException {
        WindowLoader w = new WindowLoader(backToLE);
        w.howToPlayLoader("LEActionTiles", "LEHow to Use", backToLE.getScene());
    }

    /**
     * This method is used to go back to the level editor and close the guide window
     */
    public void onBackToLE() throws IOException {
        Stage menuStage = (Stage) backToLE.getScene().getWindow();
        menuStage.close();
    }
}
