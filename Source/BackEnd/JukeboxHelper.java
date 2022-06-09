package BackEnd;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

/**
 * This class handles all the global variables the the jukebox needs to work
 */
public class JukeboxHelper {
    public static double SFXVol;
    public static double musicVol;
    public static MediaPlayer mediaPlayer;
    //Legacy hardcoded stuff, this gets changed when the Jukebox loads but still needs to be here for some initial checks
    public static String [] musicFiles = {"Assets/Music/MainTheme.mp3","Assets/Music/Battle.mp3","Assets/Music/TokyoDrift.mp3", "Assets/Music/Menu.mp3","Assets/Music/Jump.mp3","Assets/Music/pizza.mp3"};
    public static Media sound;

    public static boolean boolLaby;

    public static boolean boolJukeBoxOpen = false;


    public static String musicPlace;
    /**
     * This method sets musicPlace to use backslashes or forwardSlashes depending on OS Used
     * Thanks Microsoft
     */
    public static void locationOS()    {
        if (System.getProperty("os.name").contains("Windows"))  {
            musicPlace = "Assets\\Music\\";
        }else   {
            musicPlace = "Assets/Music/";
        }
    }
}
