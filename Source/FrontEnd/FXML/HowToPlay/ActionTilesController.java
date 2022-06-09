package FrontEnd.FXML.HowToPlay;

import FrontEnd.StateLoad;
import FrontEnd.WindowLoader;
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
 * This class is used to display the different action tiles and their uses.
 *
 * @author Daniel Jones Ortega
 * @version 1.0.
 */
public class ActionTilesController extends StateLoad {

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
     * This method is used to create a new button.
     */
    public void onNewGame() {
        wl = new WindowLoader(newGameButton);
        wl.load("GameSetup", getInitData());
    }

    /**
     * This method is used to create a new button Ice.
     */
    public void onIce() throws IOException {
        wl = new WindowLoader(newGameButton);
        wl.howToPlayLoader("Ice", "Freeze Tiles", newGameButton.getScene());
    }

    /**
     * This method is used to create a new button Fire.
     */
    public void onFire() throws IOException {
        wl = new WindowLoader(newGameButton);
        wl.howToPlayLoader("Fire", "Fire Tiles", newGameButton.getScene());
    }

    /**
     * This method is used to create a new button Double Move.
     */
    public void onDoubleMove() throws IOException {
        wl = new WindowLoader(newGameButton);
        wl.howToPlayLoader("Double Move", "Double Move Tiles", newGameButton.getScene());
    }

    /**
     * This method is used to create a new button Backtrack.
     */
    public void onBacktrack() throws IOException {
        wl = new WindowLoader(newGameButton);
        wl.howToPlayLoader("Backtrack", "Backtrack Tiles", newGameButton.getScene());

    }

    /**
     * This method is used to create a new button ActionTiles.
     */
    public void onBack() throws IOException {
        wl = new WindowLoader(newGameButton);
        wl.howToPlayLoader("ActionTiles", "Action Tiles", newGameButton.getScene());
    }

    /**
     * This method is used to create a new button HowToPlay.
     */
    public void onBackToMenu() throws IOException {
        wl = new WindowLoader(newGameButton);
        wl.howToPlayLoader("HowToPlay", "How to Play",  newGameButton.getScene());
    }
}
