package FrontEnd;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.net.URISyntaxException;
import java.util.Optional;
/**
 * This class is a place to store common popup dialogs and display them in the program
 * @author Pat Sambor
 */
public class CustomAlerts {
    /**
     * A method to display a dialogue box with Yes, No anc Cancel
     * @param title The title of the dialog
     * @param bodyText The body text of the dialog
     * @return Whatever the user pressed
     */
    public static String YesNoCancel(String title, String bodyText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(WindowLoader.w);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(bodyText);
        DialogPane dialogPane = alert.getDialogPane();
        dialogCustomizer(alert.getDialogPane());

        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");
        ButtonType buttonTypeCancel = new ButtonType("Cancel");

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo, buttonTypeCancel);

        Optional< ButtonType > result = alert.showAndWait();
            if (result.get() == buttonTypeYes) {
                return "yes";
            } else if (result.get() == buttonTypeNo) {
                return "no";
            } else {
                return "cancel";
            }
    }

    /**
     * Creates a warning box with an ok buttoon
     * @param title The title of the box
     * @param content The content of the box
     * @Param
     */
    public static void Warning(String title, String content, boolean isModal)    {
        Alert alertNo = new Alert(Alert.AlertType.WARNING);
        if (isModal)    {
            alertNo.initOwner(WindowLoader.w);
            alertNo.initModality(Modality.APPLICATION_MODAL);
        }
        alertNo.setTitle(title);
        alertNo.setHeaderText(null);
        alertNo.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alertNo.setContentText(content);
        dialogCustomizer(alertNo.getDialogPane());


        alertNo.showAndWait();
    }

    /**
     * styles the dialog being created, has to be done programmatically because of issues finding the CSS file
     * @param dialogPane The main pane of the dialog box
     */
    public static void dialogCustomizer(DialogPane dialogPane) {
        dialogPane.getStylesheets().add("dialogStyle.css");
        System.out.println(dialogPane.getStylesheets().toString());
    }
}