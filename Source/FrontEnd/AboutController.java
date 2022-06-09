package FrontEnd;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * This class handles the about screen during gameplay
 */
public class AboutController {
    @FXML
    Text txtTitle;
    @FXML
    Button btnOk;

    /**
     * Sets the font of the title text during loading
     */
    public void initialize()    {
        txtTitle.setFont(Font.font("Skyfall", FontWeight.BOLD, 55));

    }

    /**
     * Exits the about screen
     */
    public void onOk()  {
        Stage thisStage = (Stage) btnOk.getScene().getWindow();
        thisStage.close();
    }
}
