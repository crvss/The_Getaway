//Java fun facts!!
//Fun fact 1: The Project Language Level and Project SDK Can never be different. Why is it an option? Because we love vague errors
//Fun fact 2: A JavaFX Canvas will return the wrong height if that height is too small

package FrontEnd;

import BackEnd.JukeboxHelper;
import  javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

import static BackEnd.JukeboxHelper.*;

/***
 * FrontEnd.Main class for this app, starts the window and opens up the 'Start' window
 * @author Christian Sanger
 *
 */

//this is my push <3
//my push is amazing
//calum was here

public class Main extends Application {
    //These variables are meant to be used throughout the program, I know encapsulation and all that however sometimes it's not feasible
    //These are used mostly for the mediaPlayer and Jukebox methods as those have to be available throughout the whole program always

    private static final double DEFAULT_SOUND_LEVEL = 10.0;
    private static int track;
    private final String MAIN_THEME = "MainTheme.mp3";

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        WindowLoader wl = new WindowLoader(primaryStage);
        HashMap<String, String> initData;initData = new HashMap<>();
        Scanner in;
        //  Default settings if no config file.
        double backgroundVol;
        double sfxVol;
        try {
            File configFile = new File("SaveData/config.txt");
            in = new Scanner(configFile);
            backgroundVol = Double.parseDouble(in.next());
            sfxVol = Double.parseDouble(in.next());

            JukeBoxController.currentlyPlaying = in.next();
            in.close();
        } catch (NumberFormatException e) {
            CustomAlerts.Warning("User Alert!", "Your config file is in an incorrect format, using defaults", false);
            backgroundVol = DEFAULT_SOUND_LEVEL;
            sfxVol = DEFAULT_SOUND_LEVEL;
            JukeBoxController.currentlyPlaying = MAIN_THEME;
        } catch  (FileNotFoundException e) {
            CustomAlerts.Warning("User Alert!", "A config.txt file wasn't found, using defaults", false);
            backgroundVol = DEFAULT_SOUND_LEVEL;
            sfxVol = DEFAULT_SOUND_LEVEL;
            JukeBoxController.currentlyPlaying = MAIN_THEME;
        }
        //Setting OS dependant Location
        JukeboxHelper.locationOS();

        //Setting JukeBoxHelper Vol Values
        musicVol = backgroundVol;
        SFXVol = sfxVol;

        primaryStage.setFullScreen(true);
        primaryStage.setTitle("The Getaway");
        primaryStage.getIcons().add(new Image("logo.png"));
        primaryStage.setResizable(true);
        //primaryStage.initStyle(StageStyle.UNDECORATED);






        initData.put("BackgroundVol", String.valueOf(backgroundVol));
        initData.put("SFXVol", String.valueOf(sfxVol / 100));

        wl.load("StartScreen", initData);
        primaryStage.show();
        /*Current solution to that horrible GLib problem
          Investigation has found that it only occurs on Unity DE, and only for playing sounds
          So we run a dpkg command to check if Unity is installed, if a 'final' version is ever made I'll uncomment the Alert
          For now it'll just trot along without playing audio
         */
        try {
            Process p = Runtime.getRuntime().exec("dpkg -s unity");
            BufferedReader inDE = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            inDE.readLine();
            String DE = inDE.readLine();
            if (DE.contains("install ok installed"))  {
                //CustomAlerts.Warning("User Alert!", "Audio playback is not supported on the Unity DE, there is nothing we can do about this.");
            }else   {
                playMusic();
            }
            //dpkg doesn't exist on Windows so the IOException is expected here on that platform
        }catch (IOException ignored)    {
            playMusic();
        }
    }

    /**
     * This method goes through a playlist and and plays each song after the previous ones finished.
     */
    public static void playMusic() throws FileNotFoundException {
        Scanner in;
        double musicVol;
        double sfxvol;
        try {
            in = new Scanner(new File("SaveData/config.txt"));
            in.useDelimiter(" ");
            //Setting these as variables because I came up with the default song idea later (So it's at the end of the file) and changing the config format was a bit of a hassle
            //Another thing to note is that for some reason, near the end of the project, Java decided it was gonna throw me an InputMismatch
            //When trying to use in.nextDouble, but it works if I parse the String as a double? Who knows
            musicVol = Double.parseDouble(in.next());
            sfxvol = Double.parseDouble(in.next());

            sound = new Media(new File("Assets/Music/" + in.next()).toURI().toString());
        }catch (FileNotFoundException e)    {
            File config = new File("SaveData/config.txt");
            sound = new Media(new File("Assets/Music/MainTheme.mp3").toURI().toString());
            musicVol = 100;
            sfxvol = 100;
        }


        try {
            JukeboxHelper.mediaPlayer = new MediaPlayer(sound);
        } catch (MediaException e) {
            if (System.getProperty("os.name").equals("Linux")) {
                CustomAlerts.Warning("User Alert!", "Application requires FFMPEG version 54+", true);
            } else {
                System.err.println(e.getMessage());
            }
            System.exit(1);

        }
        JukeboxHelper.mediaPlayer.setCycleCount(0);

        JukeboxHelper.mediaPlayer.setVolume(musicVol / 100);
        JukeboxHelper.SFXVol = (sfxvol / 100);

        JukeboxHelper.mediaPlayer.play();
        JukeboxHelper.mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                try {
                    playMusic();
                } catch (FileNotFoundException e) {
                    CustomAlerts.Warning("User Alert!", "There was no music found, so enjoy the sound of silence!", true);
                }
            }
        });
    }

    /***
     * Only starts javaFX
     * @param args doesn't use any arguments right now.
     */

    public static void main(String[] args) {
        launch(args);
    }


}
