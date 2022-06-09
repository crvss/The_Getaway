package FrontEnd;

import BackEnd.JukeboxHelper;
import BackEnd.StringTrimmer;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

import static BackEnd.JukeboxHelper.musicFiles;
import static BackEnd.JukeboxHelper.sound;
import static BackEnd.JukeboxHelper.musicPlace;

import BackEnd.MathHelper;
/**
 * This class handles the Jukebox Scene
 * @author Aqiel PG Metusin
 * @author Mohammed Tukur
 * @author Pat Sambor
 */
public class JukeBoxController extends StateLoad {
    //FXML Stuff here
    @FXML
    Slider progressBar = new Slider();
    @FXML
    Slider musicSlider = new Slider();
    @FXML
    ComboBox<String> musicBox = new ComboBox<String>();
    @FXML
    private Button exitButton = new Button();
    @FXML
    ImageView stopMusic;
    @FXML
    ImageView playMusic;
    @FXML
    Label lblTime = new Label();
    @FXML
    Slider sliSFX;
    @FXML
    Label lblMusicText, lblSFXText;
    @FXML
    Text lblTitle;


    static String currentlyPlaying;

    //private stuff here
    private final String MAIN_MENU_SFX = "Assets/SFX/mainmenu.mp3";
    private final AudioClip MAIN_MENU_AUDIO = new AudioClip(new File(MAIN_MENU_SFX).toURI().toString());
    private final String RETURN_SFX = "Assets/SFX/return.mp3";
    private final AudioClip RETURN_AUDIO = new AudioClip(new File(RETURN_SFX).toURI().toString());
    Image playOrig = new Image("playbtnV3.png");
    Image pauseOrig = new Image("pausebtnV3.png");

    Image playLaby = new Image("Laby.png");
    Image pauseLaby = new Image("LabyPause.png");

    Image playImage = new Image("playbtnV3.png");
    Image stopImage = new Image("stopbtnV3.png");
    Image pauseImage = new Image("pausebtnV3.png");

    private boolean boolIsPaused = false;
    private boolean boolIsStopped = false;

    File[] files;

    //Public stuff here

    public String mainTheme = "MainTheme.mp3";
    public String battle = "Battle.mp3";
    public String jump = "Jump.mp3";
    public String pizza = "pizza.mp3";
    public String tokyoDrift = "TokyoDrift.mp3";
    public String menu = "Menu.mp3";
    public Media sound2;
    public Duration total;
    public Duration length;
    public int pause_unpause;
    public String x;
    WindowLoader wl;
    Double dblMusicVol = 0.0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        musicSlider.setValue(JukeboxHelper.musicVol);
        sliSFX.setValue(JukeboxHelper.SFXVol);

        ArrayList<String> listMusic = new ArrayList<>();

        files = new File("Assets/Music").listFiles();
        //If this pathname does not denote a directory, then listFiles() returns null.
        for (File file : files) {
            if (file.isFile()) {
                if (file.toString().equals(musicPlace + "pizza.mp3") || file.toString().equals(musicPlace + "pizzaTime.mp3")) {

                } else {
                    listMusic.add(file.getName());
                }

            }
        }
        ObservableList<String> oListMusic = FXCollections.observableArrayList(listMusic);
        musicBox.setItems(oListMusic);
        musicBox.setValue(currentlyPlaying);
        //Making sure that the duration on the slider matches the actual length of the song
        //Makes a temp mediaplayer with what's currently playing, waits until its ready and sets the max then
        //This solution kinda balooned in size but there is no other way to find durations AFAIK
        try {
            Media mdiDurationHelper = null;
            for (int i = 0; i < musicFiles.length; i++) {
                x = musicFiles[i];
                //if the combobox = the musicfiles[i] then create a new sound and play it
                if (x == musicBox.getValue()) {
                    mdiDurationHelper = new Media(new File(musicFiles[i]).toURI().toString());
                }
            }
            Media finalMdiDurationHelper = mdiDurationHelper;
            MediaPlayer plyDurationHelper = new MediaPlayer(finalMdiDurationHelper);
            plyDurationHelper.setOnReady(new Runnable() {
                @Override
                public void run() {
                    total = finalMdiDurationHelper.getDuration();
                    progressBar.setMax(total.toSeconds());
                }
            });
        } catch (NullPointerException e) {
            //Empty catch here so that the program doesn't crash if no music is playing
        }


        playMusic.setImage(pauseImage);

        if (musicBox.getValue().equals("Classic.mp3") || musicBox.getValue().equals("Labyrinthian.mp3")) {
            stopMusic.setImage(playLaby);
            lblTitle.setText("LabyRacer!");
            lblTitle.setFont(Font.font("Century Gothic", FontWeight.BOLD, 85));
        } else if (musicBox.getValue().equals("pizza.mp3")) {
            lblTitle.setText("Spiderbox");
            stopMusic.setImage(new Image("squarePizza.png"));
            lblTitle.setFont(Font.font("Skyfall", FontWeight.BOLD, 85));
        } else {
            stopMusic.setImage(stopImage);
            lblTitle.setText("Jukebox");
            lblTitle.setFont(Font.font("Skyfall", FontWeight.BOLD, 85));
        }

        try {
            onBackgroundChange();
        } catch (IOException e) {
            e.printStackTrace();
        }
        getMusicProgressBar();
        total = sound.getDuration();
        progressBar.setMax(total.toSeconds());

        //Setting the onEndOfMedia here too because the initial song isn't played through the Jukebox so the looping properties have to be set properly if the jukebox is opened but the song isn't changed
        JukeboxHelper.mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                try {
                    playHelper();
                } catch (IOException ignored) {
                }
            }
        });
        //Sliders are very precise things so here we're rounding the value of the slider to 0 decimal places
        //And then removing the ".0" off the end of the value

        lblMusicText.setText(StringTrimmer.trim(String.valueOf(MathHelper.round(musicSlider.getValue(), 0)), ".0"));
        lblSFXText.setText(StringTrimmer.trim(String.valueOf(MathHelper.round(sliSFX.getValue(), 0)), ".0"));

        //Setting the cursors to be default values just in case the initialise messes with them (Which it does)
        handCursorReset();

        if (musicBox.getValue().equals("Classic.mp3") || musicBox.getValue().equals("Labyrinthian.mp3")) {
            stopMusic.setImage(playLaby);
            lblTitle.setText("LabyRacer!");
        } else if (musicBox.getValue().equals("pizza.mp3")) {
            lblTitle.setText("Spiderbox");
            stopMusic.setImage(new Image("squarePizza.png"));
        } else {
            stopMusic.setImage(stopImage);
            lblTitle.setText("Jukebox");
        }

    }


    /**
     * Method to exit out of the Jukebox if you don't like using the X button
     *
     * @throws IOException
     */
    public void onExit() throws IOException {
        Stage thisStage = (Stage) exitButton.getScene().getWindow();
        thisStage.close();
    }

    /**
     * Method to change the Volume using the Slider.
     */
    public void onBackgroundChange() throws IOException {
        JukeboxHelper.mediaPlayer.setVolume(musicSlider.getValue() / 100);
        configSaver();
        double newValue = MathHelper.round(musicSlider.getValue(), 0);
        lblMusicText.setText(String.valueOf((int) newValue));
        handCursorChange();


    }

    /**
     * Method to get the music to play
     */
    public String getMusic() {
        //comboBox -> getMusic -> play
        String song = musicBox.getValue();
        return song;
    }

    /**
     * Method to play music that is selected from the ComboBox(musicBox)
     */
    public void onPlay() throws IOException {
        if (boolIsPaused) {
            JukeboxHelper.mediaPlayer.play();
            boolIsPaused = false;
            boolIsStopped = false;
            playMusic.setImage(pauseImage);
        } else if (boolIsStopped) {
            playHelper();
            boolIsPaused = false;
            boolIsStopped = false;
            playMusic.setImage(pauseImage);
        } else {
            JukeboxHelper.mediaPlayer.pause();
            boolIsPaused = true;
            playMusic.setImage(playImage);
        }
        //plays music

    }

    /**
     * Method to stop music that is currently running
     */
    public void onStop() {
        //stop music
        JukeboxHelper.mediaPlayer.stop();
        progressBar.setValue(0);
        boolIsStopped = true;
        playMusic.setImage(playImage);
    }

    /**
     * Method that helps the onPlay() by getting the Progress Bar, plays the mediaPlayer and set the currently
     * playing song/music.
     */
    public void playHelper() throws IOException {
        x = getMusic();

        for (int i = 0; i < files.length; i++) {
            //if the combobox = the musicfiles[i] then create a new sound and play it
            if (files[i].toString().equals(musicPlace + musicBox.getValue())) {
                sound = new Media(files[i].toURI().toString());
                JukeboxHelper.mediaPlayer = new MediaPlayer(sound);
            }
        }
        JukeboxHelper.mediaPlayer.setOnReady(new Runnable() {
            @Override
            public void run() {
                total = sound.getDuration();
                progressBar.setMax(total.toSeconds());
            }
        });
        currentlyPlaying = musicBox.getValue();
        JukeboxHelper.mediaPlayer.play();
        JukeboxHelper.mediaPlayer.setVolume(musicSlider.getValue() / 100);
        getMusicProgressBar();


        configSaver();

        //Moving onto the next song if the current one finishes
        JukeboxHelper.mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                try {
                    playHelper();
                } catch (IOException ignored) {
                }
            }
        });
    }

    /**
     * Method that gets the Progress Bar of a song/music file.
     */
    public void getMusicProgressBar() {
        JukeboxHelper.mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                progressBar.setValue(newValue.toSeconds());
                int minutes = (int) newValue.toMinutes();
                int seconds = (int) newValue.toSeconds();
                seconds = seconds - (minutes * 60);
                if (seconds < 10 && newValue.toMinutes() < 10) {
                    lblTime.setText("0" + minutes + ":" + "0" + seconds);
                } else if (newValue.toMinutes() < 10) {
                    lblTime.setText("0" + minutes + ":" + seconds);
                } else if (newValue.toSeconds() < 10) {
                    lblTime.setText("0" + minutes + ":" + seconds);
                } else {
                    lblTime.setText(minutes + ":" + seconds);
                }


            }
        });
    }

    /**
     * Method for the Music Bar(Progress Bar) when pressed will seek to that value.
     */
    public void onMousePressedMB() {
        progressBar.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                JukeboxHelper.mediaPlayer.seek(Duration.seconds(progressBar.getValue()));
            }
        });
        handCursorChange();
    }

    /**
     * Method for the Music Bar(Progress Bar) when dragged will seek to that value.
     */
    public void onMouseDraggedMB() {
        progressBar.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                JukeboxHelper.mediaPlayer.seek(Duration.seconds(progressBar.getValue()));
            }
        });
    }

    /**
     * Updating the current SFX sound level
     */
    public void onSFXChange() throws IOException {
        MAIN_MENU_AUDIO.setVolume(sliSFX.getValue() / 100);
        MAIN_MENU_AUDIO.play();
        JukeboxHelper.SFXVol = sliSFX.getValue();
        configSaver();
        double newValue = MathHelper.round(sliSFX.getValue(), 0);
        lblSFXText.setText(String.valueOf((int) newValue));
        handCursorReset();
        return;

    }

    /**
     * Saves the current volume of music & SFX as well as setting the default song to the currently playing track
     *
     * @throws IOException
     */
    private void configSaver() throws IOException {
        String config = String.format(Locale.US, "%f %f",
                musicSlider.getValue(),
                sliSFX.getValue());
        File configFile = new File("SaveData/config.txt");
        FileWriter configWriter = new FileWriter(configFile);


        configWriter.write(config);
        configWriter.write(" ");
        configWriter.write(currentlyPlaying);
        configWriter.flush();
        configWriter.close();
    }

    /**
     * This method starts playing a newly selected song immediately after it is selected
     *
     * @throws IOException If the file isn't found (But unless someone messes with the files they'll always be there)
     */
    public void onSelectionBox() throws IOException {
        JukeboxHelper.mediaPlayer.stop();
        playHelper();
        boolIsPaused = false;
        boolIsStopped = false;
        playMusic.setImage(pauseImage);

        //Shout out to CS230!
        if (musicBox.getValue().equals("Classic.mp3") || musicBox.getValue().equals("Labyrinthian.mp3")) {
            stopMusic.setImage(playLaby);
            lblTitle.setText("LabyRacer!");
        } else  {
            stopMusic.setImage(stopImage);
            lblTitle.setText("Jukebox");
        }
    }

    /**
     * This and handCursorReset are inherited from LEMainController and handle cursor changes when you go over a slider
     */
    public void handCursorChange() {
        progressBar.setCursor(Cursor.CLOSED_HAND);
        musicSlider.setCursor(Cursor.CLOSED_HAND);
        sliSFX.setCursor(Cursor.CLOSED_HAND);
    }

    public void handCursorReset() {
        progressBar.setCursor(Cursor.OPEN_HAND);
        musicSlider.setCursor(Cursor.OPEN_HAND);
        sliSFX.setCursor(Cursor.OPEN_HAND);
    }

    /**
     * onSFXChange only updates onMouseReleased so I added this to live update the number value for SFX
     */
    public void sfxLabelUpdater()   {
        double newValue = MathHelper.round(sliSFX.getValue(), 0);
        lblSFXText.setText(String.valueOf((int) newValue));
    }

}

