package FrontEnd;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class is used to control the change color page of the level editor guide.
 * @author Felix Ifrim
 */

public class LEColorController extends StateLoad {

    //These variables are used to help with the display of the color menu.
    @FXML
    private Button backButton;

    @FXML
    private ImageView imageview5, imageview6;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imageview5.setImage(new Image("LETutorial_Car_1.png"));
        imageview6.setImage(new Image("LETutorial_ChangeColor.png"));
    }

    /**
     * This method is used to go back to the guide menu and close the current window.
     */
    public void onBackButton() throws IOException {
        WindowLoader w = new WindowLoader(backButton);
        w.howToPlayLoader("LEHowToPlay", "LEHow to Use", backButton.getScene());
    }
}
