package FrontEnd;

import BackEnd.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This is the class responsible for managing the main view of the Level Editor as well as all the class interactions needed
 * to make it work
 *
 * @author Pat Sambor
 * @author Felix Ifrim
 */
public class LEMainController extends StateLoad {
    LEBoard boardHandler;

    private final String START_SFX = "Assets/SFX/start.mp3";
    private final AudioClip START_AUDIO = new AudioClip(new File(START_SFX).toURI().toString());

    private final String FLOOR_SFX = "Assets/SFX/floor.mp3";
    private final AudioClip FLOOR_AUDIO = new AudioClip(new File(FLOOR_SFX).toURI().toString());

    private final String ROTATE_SFX = "Assets/SFX/board.mp3";
    private final AudioClip ROTATE_AUDIO = new AudioClip(new File(ROTATE_SFX).toURI().toString());

    private final int MENUBAR_SIZE = 31;

    private boolean boolChanges = false;
    private HashMap<String, String> initData;
    int tempNo;
    //Blank ImageView to use when De-Selecting Items
    private final ImageView blankImage = new ImageView();
    //Boolean Variables for the action tiles
    private boolean boolFire = true, boolIce = true, boolDouble = true, boolBack = true;
    //You know, I'm something of a MediaPlayer myself
    MediaPlayer pizzaMusic, pizzaTime;
    Boolean boolPizza = false;
    private int intPlayerSel = 0;

    //Used for loading only as an intermediary
    private Slot[][] arrayBoard = new Slot[9][9];

    //Strings that store the names of the colour files for each of the cars
    String player1Color, player2Color, player3Color, player4Color;

    //Boolean variables for each edit button
    private boolean color1edit = false, color2edit = false, color3edit = false, color4edit = false;

    private String newColor1, newColor2, newColor3, newColor4;

    //Array list of menu items with history
    ArrayList<MenuItem> arrHistory = new ArrayList<>();
    //Making sure the listener doesn't do bad things during loading
    Boolean boolNowLoading = false;
    //Storing the references for the load here for access
    String enumdColor1;
    String enumdColor2;
    String enumdColor3;
    String enumdColor4;

    //Setting an extension here for string additions
    final String EXT = ".png";

    @FXML
    Button btnLeft = new Button(), btnRight = new Button();
    @FXML
    Button btnJukebox = new Button();
    //Creating these to bind the canvas to the size of the screen
    @FXML
    ResizableCanvas mainCanvas;
    @FXML
    ScrollPane paneCanv;
    @FXML
    ImageView imgPlayer1 = new ImageView(), imgPlayer2 = new ImageView(), imgPlayer3 = new ImageView(), imgPlayer4 = new ImageView(), imgCorner = new ImageView(), imgStraight = new ImageView(), imgT = new ImageView(), imgEmpty = new ImageView(), imgGoal = new ImageView(), imgDouble = new ImageView(), imgFire = new ImageView(), imgIce = new ImageView(), imgBacktrack = new ImageView(), imgPlayerSel1 = new ImageView(), imgPlayerSel2 = new ImageView(), imgPlayerSel3 = new ImageView(), imgPlayerSel4 = new ImageView();
    @FXML
    Slider sliWidth = new Slider(), sliHeight = new Slider();
    @FXML
    Label lblWidth = new Label(), lblHeight = new Label(), lblStatus = new Label(), lblRotate = new Label();
    @FXML
    Menu menHistory = new Menu(), menHelp = new Menu(), menFile = new Menu(), menName = new Menu();
    @FXML
    MenuItem menAuthor = new MenuItem();
    @FXML
    Button btnEnableAction, btnDisableAction;
    @FXML
    MenuItem LEHelp;


    //Temp ImageView for Selected Floor Tiles
    String strName;
    String tempSel = "blank";
    ImageView imgVTemp = new ImageView();
    int intRotate = 0;

    //Fields from previous Window
    String strAuthor;
    int width;
    int height;
    boolean loadAgain = false;
    //Bunch of audio stuff
    private final String MAIN_MENU_SFX = "Assets/SFX/mainmenu.mp3";
    private final AudioClip MAIN_MENU_AUDIO = new AudioClip(new File(MAIN_MENU_SFX).toURI().toString());
    private final String RETURN_SFX = "Assets/SFX/return.mp3";
    private final AudioClip RETURN_AUDIO = new AudioClip(new File(RETURN_SFX).toURI().toString());
    private final String ZELDA_SFX = "Assets/SFX/zelda.mp3";
    private final AudioClip ZELDA_AUDIO = new AudioClip(new File(ZELDA_SFX).toURI().toString());
    //I think what's happening here is that the initialise method is being called twice by wl
    //So I'm setting this variable because the first time the method is called the setInitData method hasn't run yet
    //The initialize method is the easiest place to do this so \_()_/
    boolean boolLoaded = false;
    WindowLoader wl;

    //LE Canvas stuff
    LECanvas boardRender;
    PlaceCoords p1Coords;
    PlaceCoords p2Coords;
    PlaceCoords p3Coords;
    PlaceCoords p4Coords;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initData = getInitData();

        /*
        You may notice that the width property is bound to the parent node but the height is not... why is that?

        That is because the height didn't bind properly and instead had a minimum value of 965 or something, this broke the level editor if you tried to use
        it at a resolution below with a height < 965.

        Seeing as how the height is just the height of the screen anyway, this seemed simpler than anything else I could think of
         */
        mainCanvas.widthProperty().bind(
                paneCanv.widthProperty());

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        mainCanvas.setHeight(screenSize.getHeight() - MENUBAR_SIZE);


        //Weird reloading stuff
        if (boolLoaded) {

            FLOOR_AUDIO.setVolume(Double.parseDouble(initData.get("SFXVol")));
            START_AUDIO.setVolume(Double.parseDouble(initData.get("SFXVol")));
            if (initData.get("loaded").equals("true")) {
                setLEValuesLoad();

            } else {
                try {
                    setLEValues();
                } catch (IOException ignored) { }
            }

        }
        boolLoaded = true;

        //Initialisation stuff for the height slider
        sliHeight.setMin(4);
        sliHeight.setMax(9);
        sliHeight.valueProperty().addListener(
                new ChangeListener<Number>() {
                    public void changed(ObservableValue<? extends Number>
                                                observable, Number oldValue, Number newValue) {
                        String intConv = String.valueOf(newValue.intValue());
                        lblHeight.setText(intConv);
                        height = newValue.intValue();
                        lblStatus.setText("Height Changed to: " + intConv);
                        boolChanges = true;
                        if (!boolNowLoading) {
                            setEnumColors();
                            boardHandler.setySize(height);
                        }

                    }
                });

        //Initialisation stuff for the width slider
        sliWidth.setMin(4);
        sliWidth.setMax(9);
        sliWidth.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number>
                                        observable, Number oldValue, Number newValue) {
                String intConv = String.valueOf(newValue.intValue());
                lblWidth.setText(intConv);
                width = newValue.intValue();
                lblStatus.setText("Width Changed to: " + intConv);
                boolChanges = true;
                if (!boolNowLoading) {
                    setEnumColors();
                    boardHandler.setxSize(width);
                }
            }
        });
        if (boolPizza) {
            lblStatus.setText("Pizza is Selected");
        } else {
        }
        lblRotate.setDisable(true);
        btnRight.setDisable(true);
        btnLeft.setDisable(true);
    }
    /**
     * This class sets all the values that the LE Uses that are transferred over from the create new screen
     * Uses the InitData the previous group kindly provided for us
     */
    private void setLEValues() throws IOException {
        START_AUDIO.play();
        initData = getInitData();
        //Width Init
        width = Integer.parseInt(getInitData().get("width"));
        lblWidth.setText(initData.get("width"));
        sliWidth.setValue(width);

        //Height Init
        height = Integer.parseInt(getInitData().get("height"));
        lblHeight.setText(initData.get("height"));
        sliHeight.setValue(height);


        strAuthor = initData.get("author");
        //Setting the author as a menu item
        menAuthor.setText("Author: " + strAuthor);
        strName = initData.get("name");
        menName.setText("Currently Editing: " + strName);

        //Setting the red-highlighted backgrounds to be invisible on first start
        imgPlayerSel1.setVisible(false);
        imgPlayerSel2.setVisible(false);
        imgPlayerSel3.setVisible(false);
        imgPlayerSel4.setVisible(false);

        //Setting the String names of the player cars to what the defaults were before we added colour changing
        player1Color = "playerPINK";
        player2Color = "playerYELLOW";
        player3Color = "playerTURQUOISE";
        player4Color = "playerORANGE";


        boardRender = new LECanvas(mainCanvas);

        lblStatus.setText("Main View Loading...");
        boardHandler = new LEBoard(boardRender, width, height, true);
        //How'd that get in there?
        if (initData.get("pizza").equals("true")) {
            /*Brief rundown of what's actually happening here
            Triggered by holding backspace when creating a new level
            I tried to use the LECanvas class here to draw the secret image but I don't think that works, that's why onMouseCanvas also has a way to do this
            Everything else is just setting the text as well as setting the global boolean to know that the secret is active for later use
             */
            strName = "pizza";
            double volume = JukeboxHelper.mediaPlayer.getVolume();
            JukeboxHelper.mediaPlayer.stop();
            Image pizza = new Image("pizza.png");
            GraphicsContext gc = mainCanvas.getGraphicsContext2D();
            gc.drawImage(pizza, 50, 50);
            Media pizzaSound = new Media(new File("Assets/Music/pizzaTime.mp3").toURI().toString());
            Media sound = new Media(new File("Assets/Music/pizza.mp3").toURI().toString());
            pizzaTime = new MediaPlayer(pizzaSound);
            JukeboxHelper.mediaPlayer = new MediaPlayer(sound);
            pizzaTime.play();
            JukeboxHelper.mediaPlayer.play();
            JukeboxHelper.mediaPlayer.setVolume(volume);
            JukeBoxController.currentlyPlaying = "pizza.mp3";


            //Saving stuff here as a kind of addendum because I think it's funny to have the pizza music play forever
            //If you activated the secret (Least until you change it in the jukebox again)
            String config = String.format(Locale.US, "%f %f" ,
                    JukeboxHelper.mediaPlayer.getVolume() * 100,
                    Double.parseDouble(getInitData().get("SFXVol")) * 100);
            File configFile = new File("SaveData/config.txt");
            FileWriter configWriter = new FileWriter(configFile);
            configWriter.write(config);
            configWriter.write(" ");
            configWriter.write(JukeBoxController.currentlyPlaying);
            configWriter.flush();
            configWriter.close();


            menFile.setText("Pizza Time");
            menHelp.setText("");
            menHistory.setText("");
            menName.setText("");
            boolPizza = true;
            imgPlayer1.setImage(new Image("pizzaMan.png"));
            imgPlayer2.setImage(new Image("pizzaMan.png"));
            imgPlayer3.setImage(new Image("pizzaMan.png"));
            imgPlayer4.setImage(new Image("pizzaMan.png"));

            imgCorner.setImage(new Image("squarePizza.png"));
            imgStraight.setImage(new Image("squarePizza.png"));
            imgT.setImage(new Image("squarePizza.png"));
            imgEmpty.setImage(new Image("squarePizza.png"));
            imgGoal.setImage(new Image("squarePizza.png"));

            btnEnableAction.setText("Enable Pizza");
            btnDisableAction.setText("Disable Pizza");

            imgFire.setImage(new Image("firePizza.png"));
            imgIce.setImage(new Image("frozenPizza.jpg"));
            imgDouble.setImage(new Image("doublePizza.jpg"));
            imgBacktrack.setImage(new Image("backPizza.jpg"));

        }
    }

    /**
     * This method sets all the values for the level editor if a game is being loaded in
     */
    public void setLEValuesLoad() {
        START_AUDIO.play();
        //Making sure the sliders don't try to redraw the board
        boolNowLoading = true;

        boardRender = new LECanvas(mainCanvas);
        initData = getInitData();
        //Width Init
        width = Integer.parseInt(initData.get("width"));
        lblWidth.setText(initData.get("width"));
        sliWidth.setValue(width);
        //Height init
        height = Integer.parseInt(initData.get("height"));
        lblHeight.setText(initData.get("height"));

        sliHeight.setValue(height);

        //Name & Author Init
        strAuthor = initData.get("author");
        menAuthor.setText("Author: " + strAuthor);
        strName = initData.get("name");
        strName = StringTrimmer.trim(strName, ".txt");
        menName.setText("Currently Editing: " + strName);
        //String color init
        player1Color = initData.get("player1col");
        player2Color = initData.get("player2col");
        player3Color = initData.get("player3col");
        player4Color = initData.get("player4col");
        //Image View Init
        imgPlayer1.setImage(Assets.getUnSelected(player1Color));
        imgPlayer2.setImage(Assets.getUnSelected(player2Color));
        imgPlayer3.setImage(Assets.getUnSelected(player3Color));
        imgPlayer4.setImage(Assets.getUnSelected(player4Color));
        //Action Tile Init
        if (initData.get("boolIce").equals("true")) {
            boolIce = true;
            imgIce.setImage(Assets.getUnSelected("frozenEffect"));
        } else {
            boolIce = false;
            imgIce.setImage(Assets.getSelected("frozenEffect"));
        }

        if (initData.get("boolFire").equals("true")) {
            boolFire = true;
            imgFire.setImage(Assets.getUnSelected("fire"));
        } else {
            boolFire = false;
            imgFire.setImage(Assets.getSelected("fire"));
        }

        if (initData.get("boolDouble").equals("true")) {
            boolDouble = true;
            imgDouble.setImage(Assets.getUnSelected("double_move"));
        } else {
            boolDouble = false;
            imgDouble.setImage(Assets.getSelected("double_move"));
        }

        if (initData.get("boolBack").equals("true")) {
            boolBack = true;
            imgBacktrack.setImage(Assets.getUnSelected("backtrack"));
        } else {
            boolBack = false;
            imgBacktrack.setImage(Assets.getSelected("backtrack"));
        }
        setEnumColors();
        lblStatus.setText("Main View Loading...");
        boardHandler = new LEBoard(boardRender, width, height, false);
        boolNowLoading = false;

    }

    /**
     * This method and the ones that follow are the selection methods for all of the items you can insert into a board
     */
    public void onCorner() {
        setTemp(imgCorner, "corner", "Corner Tile Selected");
        intPlayerSel = 0;
    }

    public void onStraight() {
        setTemp(imgStraight, "straight", "Straight Tile Selected");
        intPlayerSel = 0;
    }

    public void onT() {
        setTemp(imgT, "t_shape", "T-Shaped Tile Selected");
        intPlayerSel = 0;
    }

    public void onEmpty() {
        setTemp(imgEmpty, "empty", "Empty Tile Selected");
        intPlayerSel = 0;
    }

    public void onGoal() {
        setTemp(imgGoal, "goal", "Goal Tile Selected");
        intPlayerSel = 0;
    }

    public void onPlayer1() {
        setTemp(imgPlayer1, player1Color, "Player 1 Selected");
        intPlayerSel = 1;
    }

    public void onPlayer2() {
        setTemp(imgPlayer2, player2Color, "Player 2 Selected");
        intPlayerSel = 2;
    }

    public void onPlayer3() {
        setTemp(imgPlayer3, player3Color, "Player 3 Selected");
        intPlayerSel = 3;
    }

    public void onPlayer4() {
        setTemp(imgPlayer4, player4Color, "Player 4 Selected");
        intPlayerSel = 4;
    }

    /**
     * This method and the ones that follow enable and disable the action tiles and set all appropriate values
     */
    public void onDoubleMove() {
        boolDouble = !boolDouble;
        setActionTemp(imgDouble, "double_move", boolDouble);
        if (boolDouble) {
            MAIN_MENU_AUDIO.play(Double.parseDouble(getInitData().get("SFXVol")));
            lblStatus.setText("Double Move Enabled");
        } else {
            lblStatus.setText("Double Move Disabled");
            RETURN_AUDIO.play(Double.parseDouble(getInitData().get("SFXVol")));
        }
        intPlayerSel = 0;
    }

    public void onFire() {
        boolFire = !boolFire;
        setActionTemp(imgFire, "fire", boolFire);
        if (boolFire) {
            MAIN_MENU_AUDIO.play(Double.parseDouble(getInitData().get("SFXVol")));
            lblStatus.setText("Fire Enabled");
        } else {
            lblStatus.setText("Fire Disabled");
            RETURN_AUDIO.play(Double.parseDouble(getInitData().get("SFXVol")));
        }
        intPlayerSel = 0;
    }

    public void onIce() {
        boolIce = !boolIce;
        setActionTemp(imgIce, "frozenEffect", boolIce);
        if (boolIce) {
            MAIN_MENU_AUDIO.play(Double.parseDouble(getInitData().get("SFXVol")));
            lblStatus.setText("Ice Enabled");
        } else {
            lblStatus.setText("Ice Disabled");
            RETURN_AUDIO.play(Double.parseDouble(getInitData().get("SFXVol")));
        }
        intPlayerSel = 0;
    }

    public void onBackTrack() {
        boolBack = !boolBack;
        setActionTemp(imgBacktrack, "backtrack", boolBack);
        if (boolBack) {
            MAIN_MENU_AUDIO.play(Double.parseDouble(getInitData().get("SFXVol")));
            lblStatus.setText("Backtrack Enabled");
        } else {
            lblStatus.setText("Backtrack Disabled");
            RETURN_AUDIO.play(Double.parseDouble(getInitData().get("SFXVol")));
        }
        intPlayerSel = 0;
    }

    /**
     * This method and onDisableAction enable all of the action tiles to be used in the silkbag at once
     */
    public void onEnableAction() {
        MAIN_MENU_AUDIO.play(Double.parseDouble(getInitData().get("SFXVol")));
        boolIce = true;
        boolFire = true;
        boolDouble = true;
        boolBack = true;

        setActionTemp(imgIce, "frozenEffect", boolIce);
        setActionTemp(imgFire, "fire", boolFire);
        setActionTemp(imgDouble, "double_move", boolDouble);
        setActionTemp(imgBacktrack, "backtrack", boolBack);

        lblStatus.setText("All Action Tiles Enabled");
        setHistory();
        intPlayerSel = 0;
    }

    public void onDisableAction() {
        RETURN_AUDIO.play(Double.parseDouble(getInitData().get("SFXVol")));
        boolIce = false;
        boolFire = false;
        boolDouble = false;
        boolBack = false;

        setActionTemp(imgIce, "frozenEffect", boolIce);
        setActionTemp(imgFire, "fire", boolFire);
        setActionTemp(imgDouble, "double_move", boolDouble);
        setActionTemp(imgBacktrack, "backtrack", boolBack);

        lblStatus.setText("All Action Tiles Disabled");
        setHistory();
        intPlayerSel = 0;
    }

    /**
     * This method and the other onColor classes show a new window to be able to select the color of the player
     *
     * @throws IOException
     */
    public void onColor1() throws IOException {
        color1edit = true;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/ChangeCarColor.fxml"));
        LEChangeColorController controller2 = new LEChangeColorController(this);
        loader.setController(controller2);
        Scene scene = new Scene(loader.load());
        Stage primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Change Player 1's Colour!");
        primaryStage.show();
    }
    public void onColor2() throws IOException {
        color2edit = true;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/ChangeCarColor.fxml"));
        LEChangeColorController controller2 = new LEChangeColorController(this);
        loader.setController(controller2);
        Scene scene = new Scene(loader.load());
        Stage primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Change Player 2's Colour!");
        primaryStage.show();
    }
    public void onColor3() throws IOException {
        color3edit = true;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/ChangeCarColor.fxml"));
        LEChangeColorController controller2 = new LEChangeColorController(this);
        loader.setController(controller2);
        Scene scene = new Scene(loader.load());
        Stage primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Change Player 3's Colour!");
        primaryStage.show();
    }
    public void onColor4() throws IOException {
        color4edit = true;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/ChangeCarColor.fxml"));
        LEChangeColorController controller2 = new LEChangeColorController(this);
        loader.setController(controller2);
        Scene scene = new Scene(loader.load());
        Stage primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Change Player 4's Colour!");
        primaryStage.show();
    }
    double mouseX;
    double mouseY;
    /**
     * This class checks for mouse movements on the canvas and finds the mouse coordinate for further use
     */
    public void onMouseCanvas() {
        paneCanv.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mouseX = event.getX();
                mouseY = event.getY();
                //System.out.println(mouseX + " " + mouseY);


            }
        });
        Pair<Integer,Integer> players[] = new Pair[0];
        boardRender.renderHighlights(boardRender.mouseLocConverter(mouseX, mouseY, width, height), players, boardHandler.getBoardAsArray(), tempSel, intRotate);
    }
    //Boolean to check whether the mouse has entered once so the initial set of tiles isn't being constantly redrawn every time the mouse enters
    boolean boolEnteredOnce = false;
    /**
     * There were some issues with having the Canvas load in the board during the initialisation step
     * So instead we made the board draw itself when the mouse enters the Canvas
     * Assigns itself to onMouseEntered in the FXML
     */
    public void onCanvasEntered() {
        initData = getInitData();
        if (!boolEnteredOnce) {
            boardRender.renderBoard(boardHandler.getBoardAsArray(), width, height);
            if (initData.get("loaded").equals("true")) {
                //boardRender.loadBoard(arrayBoard, width, height, p1Coords, p2Coords, p3Coords, p4Coords,
                  //      CarColours.valueOf(enumdColor1.toUpperCase()), CarColours.valueOf(enumdColor2.toUpperCase()),
                    //    CarColours.valueOf(enumdColor3.toUpperCase()), CarColours.valueOf(enumdColor4.toUpperCase()));
                boolEnteredOnce = true;
                lblStatus.setText("Nothing is Selected");
            } else {
                //boardRender.setEmptyBoard(sliHeight, sliWidth);
                boolEnteredOnce = true;
                lblStatus.setText("Nothing is Selected");
            }
        }
        //I have nothing left... Except Spider-Man.
        if (boolPizza) {
            //boardRender.CanvasDrawPizza();
        }
    }
    /**
     * This method handles the logic for when you click inside of the Canvas to draw what you have selected and add it to the array
     */
    @FXML
    public void onCanvasClick(MouseEvent event) {
        boardHandler.placeATile(mouseX, mouseY, tempSel, intRotate);

        boolean boolPlayerBefore = false;
        boolChanges = true;
        boolean tileSelected;
        boolean boolRightClick = false;
        tileSelected = !tempSel.equals("blank");
        //This didn't work without first converting the getButton to a string
        //Don't know why and I don't want to
        //Here for having a right click rotate the tile below
        if (event.getButton().toString().equals("SECONDARY")) {
            boolRightClick = true;
        } else {
            boolRightClick = false;
        }

        if (tileSelected && boolRightClick)  {
            onRight();
            //boardRender.highlighterMethod(mouseX,mouseY,sliWidth,sliHeight, tempSel.toUpperCase(), intRotate, false, this);
            if (tempSel.equals("empty") || tempSel.equals("goal"))  {
                lblStatus.setText("Can't rotate " + tempSel + " tiles!");
            }else   {
                lblStatus.setText("Rotated " + tempSel + " to " + intRotate);
            }

        }else if(tileSelected)    {
            //There were some SFX conflicts here for players so we don't play this one if its a player
            if (!tempSel.contains("player"))    {
                FLOOR_AUDIO.play(Double.parseDouble(getInitData().get("SFXVol")));
            }else   {
                boolPlayerBefore = true;

            }
            //String statusToSet = boardRender.placeOnSlot(mouseX, mouseY, tempSel, sliWidth, sliHeight, tileSelected, intPlayerSel, intRotate, this);
            if (boolPlayerBefore)   {
               // boardRender.highlighterMethod(mouseX,mouseY,sliWidth,sliHeight, null, intRotate, false, this);
            }
            //lblStatus.setText(statusToSet);
        }else if(!tempSel.contains("player")) {
            //boardRender.rotateHoveredTile(mouseX, mouseY, sliWidth, sliHeight, this, false, boolRightClick);
            //ROTATE_AUDIO.play(Double.parseDouble(getInitData().get("SFXVol")));
        } else   {
            //boardRender.rotateHoveredTile(mouseX, mouseY, sliWidth, sliHeight, this, true, boolRightClick);
        }


    }

    //keep the drawing on it(could also replace the cursor image)
    /**
     * This method and onRight are used to rotate the selected image
     */
    public void onLeft() {
        intRotate = intRotate - 90;
        //Validation for goal and empty tile
        if (tempSel.equals("goal") || tempSel.equals("empty")) {
            lblStatus.setText("These tiles don't rotate");
            intRotate = 0;
            return;
        } else if (intRotate < 0) {
            intRotate = 270;
        }
        String strRotate = String.valueOf(intRotate);
        if (intPlayerSel == 0) {
            lblStatus.setText("Rotated " + tempSel + " to " + strRotate);
        } else {
            lblStatus.setText("Rotated Player " + intPlayerSel + " to " + strRotate);
        }

        imgVTemp.setRotate(intRotate);
        setHistory();
        ROTATE_AUDIO.play(Double.parseDouble(getInitData().get("SFXVol")));
    }

    public void onRight() {
        intRotate = intRotate + 90;
        if (tempSel.equals("goal") || tempSel.equals("empty")) {
            lblStatus.setText("These tiles don't rotate");
            intRotate = 0;
            return;
        } else if (intRotate > 270) {
            intRotate = 0;
        }
        String strRotate = String.valueOf(intRotate);
        if (intPlayerSel == 0) {
            lblStatus.setText("Rotated " + tempSel + " to " + strRotate);
        } else {
            lblStatus.setText("Rotated Player " + intPlayerSel + " to " + strRotate);
        }
        imgVTemp.setRotate(intRotate);
        setHistory();
        ROTATE_AUDIO.play(Double.parseDouble(getInitData().get("SFXVol")));
    }

    /**
     * Calls the save function (after setting all the initdata values) when you go to File->Save
     */
    public void onSaveButton() {
        MAIN_MENU_AUDIO.play(Double.parseDouble(getInitData().get("SFXVol")));
        initData.put("name", strName);
        initData.put("width", String.valueOf(width));
        initData.put("height", String.valueOf(height));
        initData.put("player1col", player1Color);
        initData.put("player2col", player2Color);
        initData.put("player3col", player3Color);
        initData.put("player4col", player4Color);
        initData.put("boolDouble", String.valueOf(boolDouble));
        initData.put("boolIce", String.valueOf(boolIce));
        initData.put("boolFire", String.valueOf(boolFire));
        initData.put("boolBack", String.valueOf(boolBack));
        LESave objSave = new LESave(boardRender, getInitData());
        objSave.Save();
        boolChanges = false;
    }

    /**
     * Quits the program back to the main menu (after checking if the user wishes to save)
     */
    public void onQuitButton() {
        RETURN_AUDIO.play(Double.parseDouble(getInitData().get("SFXVol")));
        if (boolChanges) {
            String response;
            response = CustomAlerts.YesNoCancel("Unsaved Changes", "You have unsaved changes, would you like to save?");
            switch (response) {
                case "yes":
                    onSaveButton();
                    wl = new WindowLoader(btnJukebox);
                    wl.load("MenuScreen", getInitData());
                    break;
                case "no":
                    RETURN_AUDIO.play(Double.parseDouble(getInitData().get("SFXVol")));
                    wl = new WindowLoader(btnJukebox);
                    wl.load("MenuScreen", getInitData());
                    break;
                case "cancel":
                    MAIN_MENU_AUDIO.play(Double.parseDouble(getInitData().get("SFXVol")));
                    break;
                //This should never happen but I'm not a prophet so here it is just in case
                default:
                    ZELDA_AUDIO.play(Double.parseDouble(getInitData().get("SFXVol")));
                    CustomAlerts.Warning("You have discovered a new secret!", "I don't know how you did this but here's a haiku: \n Crusader Kings II \n It is really a good game \n Go ahead and play.", true);
                    break;
            }
        } else {
            wl = new WindowLoader(btnJukebox);
            wl.load("MenuScreen", getInitData());
        }

    }

    /**
     * Small method to change the cursor to a closed hand when held on the slider
     */
    public void handCursorChange() {
        sliHeight.setCursor(Cursor.CLOSED_HAND);
        sliWidth.setCursor(Cursor.CLOSED_HAND);
    }

    public void handCursorReset() {
        sliHeight.setCursor(Cursor.OPEN_HAND);
        sliWidth.setCursor(Cursor.OPEN_HAND);
    }

    /**
     * This method sets the temporary variables to the image just clicked
     * @param newImage The new ImageView that will be set as the temp
     * @param newName  The name of the file for newImage
     * @param status   What the status bar will say has happened, in a human readable way
     */
    private void setTemp(ImageView newImage, String newName, String status) {
        //This block of code is for setting UI Elements

        imgVTemp.setImage(Assets.getUnSelected(tempSel));
        imgVTemp.setRotate(0);
        //Disabling Rotation for Goal and Empty Tile
        if (newName.equals("goal") || newName.equals("empty") || newName.contains("player")) {
            lblRotate.setDisable(true);
            btnRight.setDisable(true);
            btnLeft.setDisable(true);
        } else {
            lblRotate.setDisable(false);
            btnLeft.setDisable(false);
            btnRight.setDisable(false);
        }

        //
        if (newName.equals(tempSel)) {
            resetSelection();
            return;
        }
        MAIN_MENU_AUDIO.play(Double.parseDouble(getInitData().get("SFXVol")));
        newImage.setImage(Assets.getSelected(newName));
        tempSel = newName;
        imgVTemp = newImage;
        intRotate = 0;
        lblStatus.setText(status);
        setHistory();
    }


    /**
     * This class sets temporary variables if an ActionTile is clicked, has to be different than the generic one because you can enable/disable them
     * and there's an associated boolean (I didn't want to have to make one for the other clickable stuff because that's a waste of memory)
     * @param newImage    The ImageView that the user wishes to change
     * @param newName     The new name that the
     * @param boolEnabled Whether or not the action tile is enabled already or not
     */
    private void setActionTemp(ImageView newImage, String newName, Boolean boolEnabled) {
        //Resetting previously selected floor tile
        imgVTemp.setImage(Assets.getUnSelected(tempSel));
        imgVTemp.setRotate(0);
        lblRotate.setDisable(true);
        btnRight.setDisable(true);
        btnLeft.setDisable(true);
        tempSel = "blank";
        imgVTemp = blankImage;
        //Fades the image out if it's full and does the opposite if not
        if (!boolEnabled) {
            newImage.setImage(Assets.getSelected(newName));
        } else {
            newImage.setImage(Assets.getUnSelected(newName));
        }
        setHistory();
    }

    /**
     * Simple method here to show a history of actions taken, no undo functionality at the moment
     * Will just use the wording that the status did
     */
    private void setHistory() {
        MenuItem tempMenu = new MenuItem();
        tempMenu.setText(lblStatus.getText());
        arrHistory.add(tempMenu);
        menHistory.getItems().add(arrHistory.get(arrHistory.size() - 1));
    }

    /**
     * Throws open the Jukebox in a separate window,
     * @throws IOException
     */
    public void onJukebox() throws IOException {
        wl = new WindowLoader(btnJukebox);
        wl.popupShower("JukeBox", "The Getaway Jukebox");
        getInitData().put("SFXVol", String.valueOf(JukeboxHelper.SFXVol));
    }

    /**
     * This method opens up a new window to show the level editor guide menu
     * TODO NEED THIS TO BE UNIFIED WITH THE OTHER ONE WITHIN WINDOWLOADER
     *
     * @throws IOException
     */
    public void onLEHelp() throws IOException {
        wl = new WindowLoader(btnJukebox);
        wl.leHowToUse();
    }

    /**
     * Update the color of Player 1.
     * After you switch back from the popout window
     * @param newColorName the new color name.
     */
    public void updateColor1(String newColorName) {
        if (tempSel.equals(player1Color))   {
            imgPlayer1.setImage(Assets.getSelected(newColorName));
            tempSel = newColorName;
        }else   {
            imgPlayer1.setImage(new Image(newColorName + EXT));
        }
        player1Color = newColorName;
        setEnumColors();
        //boardRender.changeColor(CarColours.valueOf(enumdColor1.toUpperCase()), CarColours.valueOf(enumdColor2.toUpperCase()), CarColours.valueOf(enumdColor3.toUpperCase()), CarColours.valueOf(enumdColor4.toUpperCase()));
        lblStatus.setText("P1 changed to " + player1Color.substring(6));
        setHistory();
        color1edit = false;

    }

    /**
     * Update the color of Player 2.
     *After you switch back from the popout window
     * @param newColorName the new color name.
     */
    public void updateColor2(String newColorName) {
        if (tempSel.equals(player2Color))   {
            imgPlayer2.setImage(Assets.getSelected(newColorName));
            tempSel = newColorName;
        }else   {
            imgPlayer2.setImage(new Image(newColorName + EXT));
        }
        player2Color = newColorName;
        setEnumColors();
        //boardRender.changeColor(CarColours.valueOf(enumdColor1.toUpperCase()), CarColours.valueOf(enumdColor2.toUpperCase()), CarColours.valueOf(enumdColor3.toUpperCase()), CarColours.valueOf(enumdColor4.toUpperCase()));
        lblStatus.setText("P2 changed to " + player2Color.substring(6));
        setHistory();
        color2edit = false;
    }

    /**
     * Update the color of Player 3.
     *After you switch back from the popout window
     * @param newColorName the new color name.
     */
    public void updateColor3(String newColorName) {
        if (tempSel.equals(player3Color))   {
            imgPlayer3.setImage(Assets.getSelected(newColorName));
            tempSel = newColorName;
        }else   {
            imgPlayer3.setImage(new Image(newColorName + EXT));
        }
        player3Color = newColorName;
        setEnumColors();
        //boardRender.changeColor(CarColours.valueOf(enumdColor1.toUpperCase()), CarColours.valueOf(enumdColor2.toUpperCase()), CarColours.valueOf(enumdColor3.toUpperCase()), CarColours.valueOf(enumdColor4.toUpperCase()));
        lblStatus.setText("P3 changed to " + player3Color.substring(6));
        setHistory();
        color3edit = false;
    }

    /**
     * Update the color of Player 4.
     *After you switch back from the popout window
     * @param newColorName the new color name.
     */
    public void updateColor4(String newColorName) {
        if (tempSel.equals(player4Color))   {
            imgPlayer4.setImage(Assets.getSelected(newColorName));
            tempSel = newColorName;
        }else   {
            imgPlayer4.setImage(new Image(newColorName + EXT));
        }
        player4Color = newColorName;
        setEnumColors();
        //boardRender.changeColor(CarColours.valueOf(enumdColor1.toUpperCase()), CarColours.valueOf(enumdColor2.toUpperCase()), CarColours.valueOf(enumdColor3.toUpperCase()), CarColours.valueOf(enumdColor4.toUpperCase()));
        lblStatus.setText("P4 changed to " + player4Color.substring(6));
        setHistory();
        color4edit = false;
    }

    /**
     * Checks if the Player 1 color is currently edited or not.
     *After you switch back from the popout window
     * @return The status of player 1 color.
     */
    public boolean getColor1EditStatus() {
        return color1edit;
    }

    /**
     * Set a new value for Player 1 color.
     *
     * @param newEditValue The new color.
     */
    public void setColor1EditStatus(boolean newEditValue) {
        color1edit = newEditValue;
    }

    /**
     * Checks if the Player 2 color is currently edited or not.
     *
     * @return The status of player 2 color.
     */
    public boolean getColor2EditStatus() {
        return color2edit;
    }

    /**
     * Set a new value for Player 2 color.
     *
     * @param newEditValue The new color.
     */
    public void setColor2EditStatus(boolean newEditValue) {
        color2edit = newEditValue;
    }

    /**
     * Checks if the Player 3 color is currently edited or not.
     *
     * @return The status of player 3 color.
     */
    public boolean getColor3EditStatus() {
        return color3edit;
    }

    /**
     * Set a new value for Player 3 color.
     *
     * @param newEditValue The new color.
     */
    public void setColor3EditStatus(boolean newEditValue) {
        color3edit = newEditValue;
    }

    /**
     * Checks if the Player 4 color is currently edited or not.
     *
     * @return The status of player 4 color.
     */
    public boolean getColor4EditStatus() {
        return color4edit;
    }

    /**
     * Set a new value for Player 4 color.
     *
     * @param newEditValue The new color.
     */
    public void setColor4EditStatus(boolean newEditValue) {
        color4edit = newEditValue;
    }

    /**
     * Get the ImageView of Player 1 car.
     *
     * @return ImageView of Player 1 car.
     */
    public ImageView getCar1ImageView() {
        return imgPlayer1;
    }

    /**
     * Set the new color of Player 1 car.
     *
     * @param newColor The new color of Player 1 car.
     */
    public void setCar1ImageView(String newColor) {
        updateColor1(newColor);
    }

    /**
     * Get the ImageView of Player 2 car.
     *
     * @return ImageView of Player 2 car.
     */
    public ImageView getCar2ImageView() {
        return imgPlayer2;
    }

    /**
     * Set the new color of Player 2 car.
     *
     * @param newColor The new color of Player 2 car.
     */
    public void setCar2ImageView(String newColor) {
        updateColor2(newColor);
    }

    /**
     * Get the ImageView of Player 3 car.
     *
     * @return ImageView of Player 3 car.
     */
    public ImageView getCar3ImageView() {
        return imgPlayer3;
    }

    /**
     * Set the new color of Player 3 car.
     *
     * @param newColor The new color of Player 3 car.
     */
    public void setCar3ImageView(String newColor) {
        updateColor3(newColor);
    }

    /**
     * Get the ImageView of Player 4 car.
     *
     * @return ImageView of Player 4 car.
     */
    public ImageView getCar4ImageView() {
        return imgPlayer4;
    }

    /**
     * Set the new color of Player 4 car.
     *
     * @param newColor The new color of Player 4 car.
     */
    public void setCar4ImageView(String newColor) {
        updateColor4(newColor);
    }
    public void setArrayBoard(Slot[][] arrayLoadedBoard) {
        arrayBoard = arrayLoadedBoard;
    }


    /**
     * Sets the variable for player coordinates if they ever need to be referenced directly here
     * @param xCoords An array of X coordinates for the 4 players
     * @param yCoords An array of Y coordinates for the 4 players
     */
    public void setPlayerCoords(String[] xCoords, String[] yCoords) {
        p1Coords = new PlaceCoords(Integer.parseInt(xCoords[0]), Integer.parseInt(yCoords[0]));
        p2Coords = new PlaceCoords(Integer.parseInt(xCoords[1]), Integer.parseInt(yCoords[1]));
        p3Coords = new PlaceCoords(Integer.parseInt(xCoords[2]), Integer.parseInt(yCoords[2]));
        p4Coords = new PlaceCoords(Integer.parseInt(xCoords[3]), Integer.parseInt(yCoords[3]));
    }


    /**
     * Just removes the "player" bit from the variables of the colour that a player currently is
     * Useful when doing CarColors.ValueOf
     * But I didn't just want to call the car colors stuff likE "RED" "PINK" etc. in the file system
     */
    public void setEnumColors() {
        enumdColor1 = StringTrimmer.trim(player1Color, "player");
        enumdColor2 = StringTrimmer.trim(player2Color, "player");
        enumdColor3 = StringTrimmer.trim(player3Color, "player");
        enumdColor4 = StringTrimmer.trim(player4Color, "player");
        //boardRender.setPlayerColours(CarColours.valueOf(enumdColor1), CarColours.valueOf(enumdColor2), CarColours.valueOf(enumdColor3), CarColours.valueOf(enumdColor4));
    }

    /**
     * This method is used to de-select something and resets all appropriate values
     */
    public void resetSelection()    {
        RETURN_AUDIO.play(Double.parseDouble(getInitData().get("SFXVol")));
        imgVTemp = blankImage;
        tempSel = "blank";
        lblStatus.setText("Nothing is Selected");
        intPlayerSel = 0;
        lblRotate.setDisable(true);
        btnRight.setDisable(true);
        btnLeft.setDisable(true);
    }

    /**
     * @return The player currently selected for placement
     */
    public int getIntPlayerSel()    {
        return intPlayerSel;
    }




    //Classes below aren't Javadoced because they do the same things as each other
    //Just enable the hover image and put back the original image after they're done


    public void cornerEntered() {
        imgCorner.setImage(Assets.getHighlight("corner"));
        if (imgVTemp == imgCorner)  {
            lblStatus.setText("De-select Corner Tile");
        }else   {
            lblStatus.setText("Select Corner Tile");
        }
    }
    public void cornerLeft()    {
        if (imgVTemp == imgCorner)  {
            imgCorner.setImage(Assets.getSelected("corner"));
            lblStatus.setText("Corner Tile Selected");
        }else   {
            if (tempSel.equals("blank")) {
                lblStatus.setText("Nothing is Selected");
            }else if (tempSel.contains("player"))   {
                lblStatus.setText("Player " + intPlayerSel + " is Selected");
            }
            else {
                lblStatus.setText(tempSel + " is Selected");
            }
            imgCorner.setImage(new Image("corner.png"));
        }
    }

    public void straightEntered() {
        imgStraight.setImage(Assets.getHighlight("straight"));
        if (imgVTemp == imgStraight)  {
            lblStatus.setText("De-select Straight Tile");
        }else   {
            lblStatus.setText("Select Straight Tile");
        }
    }
    public void straightLeft()    {
        if (imgVTemp == imgStraight)  {
            imgStraight.setImage(Assets.getSelected("straight"));
            lblStatus.setText("Straight Tile Selected");
        }else   {
            if (tempSel.equals("blank")) {
                lblStatus.setText("Nothing is Selected");
            }else if (tempSel.contains("player"))   {
                lblStatus.setText("Player " + intPlayerSel + " is Selected");
            }
            else {
                lblStatus.setText(tempSel + " is Selected");
            }
            imgStraight.setImage(new Image("straight.png"));
        }
    }


    public void tEntered() {
        imgT.setImage(Assets.getHighlight("t_shape"));
        if (imgVTemp == imgT)  {
            lblStatus.setText("De-select T-Shaped Tile");
        }else   {
            lblStatus.setText("Select T-Shaped Tile");
        }
    }
    public void tLeft()    {
        if (imgVTemp == imgT)  {
            imgT.setImage(Assets.getSelected("t_shape"));
            lblStatus.setText("T-Shaped Tile Selected");
        }else   {
            if (tempSel.equals("blank")) {
                lblStatus.setText("Nothing is Selected");
            }else if (tempSel.contains("player"))   {
                lblStatus.setText("Player " + intPlayerSel + " is Selected");
            }
            else {
                lblStatus.setText(tempSel + " Tile Selected");
            }
            imgT.setImage(new Image("t_shape.png"));
        }
    }

    public void emptyEntered() {
        imgEmpty.setImage(Assets.getHighlight("empty"));
        if (imgVTemp == imgEmpty)  {
            lblStatus.setText("De-select Empty Tile");
        }else   {
            lblStatus.setText("Select Empty Tile");
        }
    }
    public void emptyLeft()    {
        if (imgVTemp == imgEmpty)  {
            imgEmpty.setImage(Assets.getSelected("empty"));
            lblStatus.setText("Empty Tile Selected");
        }else   {
            if (tempSel.equals("blank")) {
                lblStatus.setText("Nothing is Selected");
            }else if (tempSel.contains("player"))   {
                lblStatus.setText("Player " + intPlayerSel + " is Selected");
            }
            else {
                lblStatus.setText(tempSel + " Tile Selected");
            }
            imgEmpty.setImage(new Image("empty.png"));
        }
    }

    public void goalEntered() {
        imgGoal.setImage(Assets.getHighlight("goal"));
        if (imgVTemp == imgGoal)  {
            lblStatus.setText("De-select goal Tile");
        }else   {
            lblStatus.setText("Select goal Tile");
        }
    }
    public void goalLeft()    {
        if (imgVTemp == imgGoal)  {
            imgGoal.setImage(Assets.getSelected("goal"));
            lblStatus.setText("Goal Tile Selected");
        }else   {
            if (tempSel.equals("blank")) {
                lblStatus.setText("Nothing is Selected");
            }else if (tempSel.contains("player"))   {
                lblStatus.setText("Player " + intPlayerSel + " is Selected");
            }
            else {
                lblStatus.setText(tempSel + " Tile Selected");
            }
            imgGoal.setImage(new Image("goal.png"));
        }
    }


    public void P1Entered() {
        imgPlayer1.setImage(Assets.getHighlight(player1Color));
        if (imgVTemp == imgPlayer1)  {
            lblStatus.setText("De-select Player 1");
        }else   {
            lblStatus.setText("Select Player 1");
        }
    }
    public void P1Left()    {
        if (imgVTemp == imgPlayer1)  {
            imgPlayer1.setImage(Assets.getSelected(player1Color));
            lblStatus.setText("Player 1 Selected");
        }else   {
            if (tempSel.equals("blank")) {
                lblStatus.setText("Nothing is Selected");
            }else if (tempSel.contains("player"))   {
                lblStatus.setText("Player " + intPlayerSel + " is Selected");
            }
            else {
                lblStatus.setText(tempSel + " Tile Selected");
            }
            imgPlayer1.setImage(new Image(player1Color + ".png"));
        }
    }

    public void P2Entered() {
        imgPlayer2.setImage(Assets.getHighlight(player2Color));
        if (imgVTemp == imgPlayer2)  {
            lblStatus.setText("De-select Player 2");
        }else   {
            lblStatus.setText("Select Player 2");
        }
    }
    public void P2Left()    {
        if (imgVTemp == imgPlayer2)  {
            imgPlayer2.setImage(Assets.getSelected(player2Color));
            lblStatus.setText("Player 2 Selected");
        }else   {
            if (tempSel.equals("blank")) {
                lblStatus.setText("Nothing is Selected");
            }else if (tempSel.contains("player"))   {
                lblStatus.setText("Player " + intPlayerSel + " is Selected");
            }
            else {
                lblStatus.setText(tempSel + " Tile Selected");
            }
            imgPlayer2.setImage(new Image(player2Color + ".png"));
        }
    }


    public void P3Entered() {
        imgPlayer3.setImage(Assets.getHighlight(player3Color));
        if (imgVTemp == imgPlayer3)  {
            lblStatus.setText("De-select Player 3");
        }else   {
            lblStatus.setText("Select Player 3");
        }
    }
    public void P3Left()    {
        if (imgVTemp == imgPlayer3)  {
            imgPlayer3.setImage(Assets.getSelected(player3Color));
            lblStatus.setText("Player 3 Selected");
        }else   {
            if (tempSel.equals("blank")) {
                lblStatus.setText("Nothing is Selected");
            }else if (tempSel.contains("player"))   {
                lblStatus.setText("Player " + intPlayerSel + " is Selected");
            }
            else {
                lblStatus.setText(tempSel + " Tile Selected");
            }
            imgPlayer3.setImage(new Image(player3Color + ".png"));
        }
    }
    public void P4Entered() {
        imgPlayer4.setImage(Assets.getHighlight(player4Color));
        if (imgVTemp == imgPlayer4)  {
            lblStatus.setText("De-select Player 4");
        }else   {
            lblStatus.setText("Select Player 4");
        }
    }
    public void P4Left()    {
        if (imgVTemp == imgPlayer4)  {
            imgPlayer4.setImage(Assets.getSelected(player4Color));
            lblStatus.setText("Player 4 Selected");
        }else   {
            if (tempSel.equals("blank")) {
                lblStatus.setText("Nothing is Selected");
            }else if (tempSel.contains("player"))   {
                lblStatus.setText("Player " + intPlayerSel + " is Selected");
            }
            else {
                lblStatus.setText(tempSel + " Tile Selected");
            }
            imgPlayer4.setImage(new Image(player4Color + ".png"));
        }
    }
    public void fireEntered() {
        imgFire.setImage(Assets.getHighlight("fire"));
        if (boolFire)  {
            lblStatus.setText("Disable Fire Tiles");
        }else   {
            lblStatus.setText("Enable Fire Tiles");
        }
    }
    public void fireLeft()    {
            if (tempSel.equals("blank")) {
                lblStatus.setText("Nothing is Selected");
            }else if (tempSel.contains("player"))   {
                lblStatus.setText("Player " + intPlayerSel + " is Selected");
            }
            else {
                lblStatus.setText(tempSel + " Tile Selected");
            }
        if (boolFire)    {
            imgFire.setImage(new Image("fire.png"));
        }else   {
            imgFire.setImage(Assets.getSelected("fire"));
        }
        }

    public void iceEntered() {
        imgIce.setImage(Assets.getHighlight("frozenEffect"));
        if (imgVTemp == imgIce)  {
            lblStatus.setText("De-select Player 4");
        }else   {
            lblStatus.setText("Select Player 4");
        }
    }
    public void iceLeft()    {
                if (tempSel.equals("blank")) {
                    lblStatus.setText("Nothing is Selected");
                }else if (tempSel.contains("player"))   {
                    lblStatus.setText("Player " + intPlayerSel + " is Selected");
                }
                else {
                    lblStatus.setText(tempSel + " Tile Selected");
                }
                if (boolIce)    {
                    imgIce.setImage(new Image("frozenEffect.png"));
                }else   {
                    imgIce.setImage(Assets.getSelected("frozenEffect"));
                }

    }

    public void doubleEntered() {
        imgDouble.setImage(Assets.getHighlight("double_move"));
        if (imgVTemp == imgDouble)  {
            lblStatus.setText("Disable Double Move");
        }else   {
            lblStatus.setText("Enable Double Move");
        }
    }
    public void doubleLeft()    {
        if (tempSel.equals("blank")) {
            lblStatus.setText("Nothing is Selected");
        }else if (tempSel.contains("player"))   {
            lblStatus.setText("Player " + intPlayerSel + " is Selected");
        }
        else {
            lblStatus.setText(tempSel + " Tile Selected");
        }
        if (boolDouble)    {
            imgDouble.setImage(new Image("double_move.png"));
        }else   {
            imgDouble.setImage(Assets.getSelected("double_move"));
        }
    }

    public void backtrackEntered() {
        imgBacktrack.setImage(Assets.getHighlight("backtrack"));
        if (imgVTemp == imgBacktrack)  {
            lblStatus.setText("Disable Backtrack Tiles");
        }else   {
            lblStatus.setText("Enable Backtrack Tiles");
        }
    }
    public void backtrackLeft()    {
        if (tempSel.equals("blank")) {
            lblStatus.setText("Nothing is Selected");
        }else if (tempSel.contains("player"))   {
            lblStatus.setText("Player " + intPlayerSel + " is Selected");
        }
        else {
            lblStatus.setText(tempSel + " Tile Selected");
        }
        if (boolBack)    {
            imgBacktrack.setImage(new Image("backtrack.png"));
        }else   {
            imgBacktrack.setImage(Assets.getSelected("backtrack"));
        }
    }

    String oldStatus;

    public void enableEntered(){
        oldStatus = lblStatus.getText();
        lblStatus.setText("Enable all Action Tiles");
    }

    public void enableLeft()    {
        lblStatus.setText(oldStatus);
    }

    public void disableEntered()    {
        oldStatus = lblStatus.getText();
        lblStatus.setText("Disable all Action Tiles");
    }

    public void disableLeft()   {
        lblStatus.setText(oldStatus);
    }

    public void onAbout() throws IOException {
        WindowLoader w = new WindowLoader(btnJukebox);
        w.popupShower("LEAbout", "About");
    }

    //I can't be bothered getting rid of this at the moment
    public void onKeyboard()    { }
}

