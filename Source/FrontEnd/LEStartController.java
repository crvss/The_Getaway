package FrontEnd;

import BackEnd.JukeboxHelper;
import BackEnd.LELoad;
import MessageOfTheDay.MessageOfTheDay;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * This class manages the start screen of the level editor
 */
public class LEStartController extends StateLoad {
    @FXML
    Button loadLevel = new Button();
    @FXML
    Label MoTD = new Label();
    @FXML
    ComboBox selLoadLevel = new ComboBox();

    private final String MAIN_MENU_SFX = "Assets/SFX/mainmenu.mp3";
    private final AudioClip MAIN_MENU_AUDIO = new AudioClip(new File(MAIN_MENU_SFX).toURI().toString());
    private final String RETURN_SFX = "Assets/SFX/return.mp3";
    private final AudioClip RETURN_AUDIO = new AudioClip(new File(RETURN_SFX).toURI().toString());

    WindowLoader wl;

    /**
     * Initialisation method for this class, sets MoTD and all that
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String message;
        try {
            message = MessageOfTheDay.puzzle();
        } catch (Exception e) {
            message = "";
        }
        MoTD.setText(message);
        loadLevel.setDisable(true);

        File[] files = new File("Gameboards").listFiles();
        //If this pathname does not denote a directory, then listFiles() returns null.
        ArrayList< String > comboList = new ArrayList < >();
        try {
            for (File file : files) {
                //If its one of the pre-made ones, don't give it as an option to load
                if(file.getName().contains("Random"))    {

                }else if (file.isFile()) {
                    comboList.add(file.getName());
                }
            }
            ObservableList< String > oListCombo = FXCollections.observableArrayList(comboList);
            selLoadLevel.setItems(oListCombo);
            selLoadLevel.setPromptText("Select Level!");
        //Just for when the box is empty so it doesn't crash
        }catch (NullPointerException e) {
            selLoadLevel.setPromptText("No Levels");
            return;
        }

    }

    /**
     * Calls the new level method
     */
    public void onNewLevel() {
        wl = new WindowLoader(loadLevel);
        wl.load("LENew", getInitData());
        MAIN_MENU_AUDIO.play(Double.parseDouble(getInitData().get("SFXVol")));
    }

    /**
     * Calls the load level method
     * @throws IOException
     */
    public void onLoadLevel() throws IOException {
        LELoad objLoad = new LELoad();
        objLoad.loadForLE((String) selLoadLevel.getValue(), getInitData(), loadLevel.getScene(), (Stage) loadLevel.getScene().getWindow());
        getInitData().put("loaded", "true");

        /*wl = new WindowLoader(loadLevel);
        wl.load("LEMain", getInitData());*/
        MAIN_MENU_AUDIO.play(Double.parseDouble(getInitData().get("SFXVol")));
    }

    /**
     * Returns back to the main menu
     */
    public void onBack() {
        wl = new WindowLoader(loadLevel);
        wl.load("MenuScreen", getInitData());
        RETURN_AUDIO.play(Double.parseDouble(getInitData().get("SFXVol")));
    }

    /**
     * Enables the load level button if you've selected an item in the combobox
     */
    public void onLoadBox() {
        loadLevel.setDisable(false);
    }

    /**
     * Opens the LevelEditor guide
     * @throws IOException
     */
    public void onLEHelp() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/LEHowToUse/LEHowToPlay.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage primaryStage = new Stage();
        primaryStage.setMaximized(true);
        primaryStage.setScene(scene);
        primaryStage.setTitle("LEHow to Use");
        primaryStage.getIcons().add(new Image("logo.png"));
        primaryStage.show();
    }
}
