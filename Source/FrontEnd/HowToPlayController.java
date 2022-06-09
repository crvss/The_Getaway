package FrontEnd;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class is used to display instructions on how to play the game.
 *
 * @author Daniel Jones Ortega, David Landmaid & Pat Sambor.
 * @version 1.0
 */
public class HowToPlayController extends StateLoad {

    /* These final variables are used for the game's Sound Effects (SFX) */

    private final String MAIN_MENU_SFX = "Assets/SFX/mainmenu.mp3";
    private final AudioClip MAIN_MENU_AUDIO = new AudioClip(new File(MAIN_MENU_SFX).toURI().toString());
    private final String RETURN_SFX = "Assets/SFX/return.mp3";
    private final AudioClip RETURN_AUDIO = new AudioClip(new File(RETURN_SFX).toURI().toString());


    /*
    These variables are used to help with the display of the how to play page.
     */
    @FXML
    private Button newGameButton;
    private WindowLoader wl;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    /**
     * This method is used to make to create a new windowLoader, and load the gameSetup
     * button.
     */
    public void onNewGame() {
        wl = new WindowLoader(newGameButton);
        wl.load("GameSetup", getInitData());
    }

    /**
     * This method is is used to create a new button for tiles.
     */
    public void onTiles() throws IOException {
        wl = new WindowLoader(newGameButton);
        wl.howToPlayLoader("Tiles", "Floor Tiles", newGameButton.getScene());
    }

    /**
     * This method is is used to create a new button for tiles.
     */
    public void onTurns() throws IOException {
        wl = new WindowLoader(newGameButton);
        wl.howToPlayLoader("Turns", "Turns", newGameButton.getScene());
    }

    /**
     * This method is is used to create a new button for Movement.
     */
    public void onMovement() throws IOException {
        wl = new WindowLoader(newGameButton);
        wl.howToPlayLoader("Movement", "Action Tiles", newGameButton.getScene());
    }

    /**
     * This method is is used to create a new button for ActionTiles.
     */
    public void onActionTiles() throws IOException {
        wl = new WindowLoader(newGameButton);
        wl.howToPlayLoader("ActionTiles", "Action Tiles", newGameButton.getScene());
    }

    /**
     * This method is is used to create a new button for HowToPlay.
     */
    public void onBackTo() throws IOException {
        wl = new WindowLoader(newGameButton);
        wl.howToPlayLoader("HowToPlay", "How to Play",  newGameButton.getScene());
    }

    /**
     * This method is is used to create a new button for Basics.
     */
    public void onBasics() throws IOException {
        wl = new WindowLoader(newGameButton);
        wl.howToPlayLoader("Basics", "Basics", newGameButton.getScene());
    }

    /**
     * This method is is used to create a new button to go back to the menu screen. Also has sound effects when
     * pressed.
     */
    public void onBackToMenu() {
        Stage thisStage = (Stage) newGameButton.getScene().getWindow();
        thisStage.close();
    }
}
