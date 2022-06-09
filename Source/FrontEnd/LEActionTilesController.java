package FrontEnd;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class is used to control the action tiles page of the level editor guide.
 * @author Felix Ifrim
 */

public class LEActionTilesController extends StateLoad {

    //These variables are used to help with the display of the action tiles menu.
    @FXML
    private Button backButton;

    @FXML
    private ImageView imageview4;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imageview4.setImage(new Image("LETutorial_ActionTiles.png"));
    }

    /**
     * This method is used to go back to the guide menu and close the current window.
     */
    public void onBackButton() throws IOException {
        WindowLoader w = new WindowLoader(backButton);
        w.howToPlayLoader("LEHowToPlay", "LEHow to Use", backButton.getScene());
    }
}
