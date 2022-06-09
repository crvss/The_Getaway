package FrontEnd;

import BackEnd.JukeboxHelper;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/***
 * This class is used to load new windows.
 * @author Christian Sanger
 */
public class WindowLoader {
    private static final String fileLocation = "Source/FrontEnd/FXML/";
    // Reference to the primary stage, it's public so it can be used with CustomAlerts
    public static Stage w;
	FXMLLoader loader;
	URL fxmlURL;
    /***
     * Creates a window loader that changes the scene shown to the user.
     * @param window any Node object on the stage that you wish control.
     */
    public WindowLoader(Node window) {
        w = (Stage) window.getScene().getWindow();
    }

	/**
	 * Creates a new window loader that can change the current scene in this window
	 * @param primaryStage primary stage as reference to which window to change
	 */
	public WindowLoader(Stage primaryStage) {
		w = primaryStage;
	}

	public LEMainController getController()	{ return loader.getController();}

	/***
	 * swaps the scene for the given scene. Window should file be the scene file name
	 * i.e. to swap to MenuScreen.fxml use "MenuScreen"
	 *
	 * @param window scene name
	 * @param initData state of application
	 */
	public void load(String window, HashMap<String, String> initData) {
		Parent root = null;
		try {
			loader = new FXMLLoader();
			String fxmlFile = fileLocation + window + ".fxml";
			URL fxmlURL_weird = getClass().getClassLoader().getResource(fxmlFile);
			fxmlURL = (new File(fxmlFile).toURI().toURL());
			loader.setLocation(Objects.requireNonNull(fxmlURL));

			root = loader.load();
			StateLoad controller = loader.getController();
			controller.setInitData(initData);
			controller.initialize(null, null);
		} catch (IOException e) {
			CustomAlerts.Warning("User Alert!",window + " Failed to load due to " + e.getMessage(), true);
			e.printStackTrace();
			System.exit(1);
		}
		if (root == null) {
			CustomAlerts.Warning("User Alert!", "Scene loading failed, " + window + " could not be loaded", true);
			System.exit(1);
		} else {
			if (w.getScene() == null) {
				w.setFullScreen(true);
				w.setScene(new Scene(root));
			} else {
				w.setFullScreen(true);
				w.getScene().setRoot(root);
			}
		}
	}

	/**
	 * This class is very similar to the one above but is used specifically for loading a game as there are some methods
	 * Inside of the LEMainController class that need to be called for everything to work properly
	 * @param window scene name
	 * @param initData The HashMap of data that gets transferred across Windows
	 * @param slots
	 * @param startLocationX
	 * @param startLocationY
	 */
	public void loadCustomLE(String window, HashMap<String, String> initData, Slot[][] slots, String[] startLocationX, String[] startLocationY) {
		Parent root = null;
		try {
			loader = new FXMLLoader();
			String fxmlFile = fileLocation + window + ".fxml";
			fxmlURL = (new File(fxmlFile).toURI().toURL());
			loader.setLocation(Objects.requireNonNull(fxmlURL));
			root = loader.load();
			LEMainController controller = loader.getController();
			controller.setInitData(initData);
			controller.setArrayBoard(slots);
			controller.setPlayerCoords(startLocationX, startLocationY);
			controller.initialize(null, null);
		} catch (Exception e) {
			CustomAlerts.Warning("User Alert!",window + " Failed to load due to " + e.getMessage(), true);
			e.printStackTrace();
			System.exit(1);
		}
		if (root == null) {
			CustomAlerts.Warning("User Alert!","Scene loading failed, " + window + " could not be loaded", true);
			System.exit(1);
		} else {
			if (w.getScene() == null) {
				w.setFullScreen(true);
				w.setScene(new Scene(root));
			} else {
				w.setFullScreen(true);
				w.getScene().setRoot(root);
			}
		}
	}

	/**
	 * Custom class to handle all pop-out windows
	 * @param window The name of the FXML file of the window you wanna open
	 * @param title The window Title
	 * @throws IOException If it can't find the FXML file
	 */
	public void popupShower(String window, String title) throws IOException {
		String testing = fileLocation + window + ".fxml";
		FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/" + window + ".fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		Stage popupStage = new Stage();
		popupStage.initOwner(w);
		popupStage.initModality(Modality.APPLICATION_MODAL);
		popupStage.setScene(scene);
		popupStage.setTitle(title);
		popupStage.setResizable(false);

		//TODO Make this more versatile if you need to
		if (window.equals("JukeBox"))	{
			popupStage.getIcons().add(new Image("jukeboxLogo.png"));
		}else	{
			popupStage.getIcons().add(new Image("logo.png"));
		}


		popupStage.showAndWait();
	}

	public void howToPlayStarter() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/HowToPlay/HowToPlay.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		Stage primaryStage = new Stage();
		primaryStage.setMaximized(true);
		primaryStage.setScene(scene);
		primaryStage.setTitle("How to Play");
		primaryStage.getIcons().add(new Image("logo.png"));
		primaryStage.setMinHeight(600);
		primaryStage.setMinWidth(1000);
		primaryStage.show();
	}

	public void leHowToUse() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/LEHowToUse/LEHowToPlay.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		Stage primaryStage = new Stage();
		primaryStage.setMaximized(true);
		primaryStage.setScene(scene);
		primaryStage.setTitle("How to Play");
		primaryStage.getIcons().add(new Image("logo.png"));
		primaryStage.setMinHeight(600);
		primaryStage.setMinWidth(1000);
		primaryStage.show();
	}

	/**
	 * Handles loading of internal How to Play things without reloading the whole window
	 * @param window The name of the FXML file
	 * @param title The title of the window you wish to have, I tend not to change this
	 * @param currentScene The current scene
	 * @throws IOException
	 */
	public void howToPlayLoader(String window, String title, Scene currentScene) throws IOException {
		Stage currentStage = (Stage) currentScene.getWindow();
		FXMLLoader loader;
		if (title.equals("LEHow to Use"))	{
			 loader = new FXMLLoader(getClass().getResource("FXML/LEHowToUse/" + window + ".fxml"));
		}else	{
			 loader = new FXMLLoader(getClass().getResource("FXML/HowToPlay/" + window + ".fxml"));
		}

		Parent root = loader.load();
		currentScene.setRoot(root);
	}

}
