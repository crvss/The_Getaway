package FrontEnd;

import BackEnd.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import static BackEnd.TileType.*;

/**
 * This class is used to cache assets and control reading image files.
 * improves loading times as images are loaded only when they
 * are first needed.
 *
 * @author Christian Sanger and Pat Sambor
 * @version 1.0
 */
public class Assets {
    private static final String EXT = ".png";
    private static final HashMap<String, Image> cache = new HashMap<>();

    /**
     * Gets a Image with the matching name.
     *
     * @param name name of image needed.
     * @return loaded image.
     */
    public static Image get(String name)  {
        return new Image (name + EXT);
    }

    /**
     * This method gets a red-highlighted version of the image you're searching for
     * Used in the level editor
     * Requires that there be a selected variant inside the "Assets" folder
     * @param name The name of the Image you're trying to select
     * @return Selected variant of the image
     */
    public static Image getSelected(String name)    {
        return new Image(name + "-selected" + EXT);
    }

    public static Image getHighlight(String name)    {
        return new Image(name + "-highlight" + EXT);
    }
    /**
     * Inverse of the previous method, returns the normal variant of a selected image
     * Used in the level editor
     * Requires that there be a selected variant inside the "Assets" folder
     * @param name The name of the Image you're trying to select
     * @return Selected variant of the image
     */
    public static Image getUnSelected(String name)  {
        return new Image (name + EXT);
    }

    /**
     * Creates an ImageView of this tile.
     *
     * @param tile to create ImageView of.
     * @param x    the X coordinate of this tile.
     * @param y    the Y coordinate of this tile.
     * @return The view of that tile.
     */
    public static Pane getFloorTileImage(FloorTile tile, int x, int y) {
        Pane tileView = new Pane();
        ImageView tileImage;
        try {
            tileImage = new ImageView(get(tile.getType().toString().toLowerCase()));
            tileImage.setFitWidth(GameScreenController.tileWidth);
            tileImage.setFitHeight(GameScreenController.tileWidth);
            tileView.getChildren().add(tileImage);
            tileView.setTranslateX(x * GameScreenController.tileWidth);
            tileView.setTranslateY(y * GameScreenController.tileWidth);
        }
        catch (NullPointerException e) {
            tileImage = new ImageView(get(tile.getType().toString()));
        }
        if (tile.onFire()) {
            ImageView fireImage = new ImageView(get("tilefire"));
            fireImage.setFitHeight(GameScreenController.tileWidth);
            fireImage.setFitWidth(GameScreenController.tileWidth);
            tileView.getChildren().add(fireImage);
        }
        if (tile.isFrozen()) {
            ImageView fireImage = new ImageView(get("frozen"));
            fireImage.setFitHeight(GameScreenController.tileWidth);
            fireImage.setFitWidth(GameScreenController.tileWidth);
            tileView.getChildren().add(fireImage);
        }
        if (tile.isFixed()) {
            ImageView lockImage = new ImageView(get("fixed"));
            lockImage.setFitWidth(GameScreenController.tileWidth);
            lockImage.setFitHeight(GameScreenController.tileWidth);
            tileView.getChildren().add(lockImage);
        }

        switch (tile.getRotation()) {
            case UP:
                tileView.setRotate(0); // Not needed but just for consistency
                break;
            case RIGHT:
                tileView.setRotate(90);
                break;
            case DOWN:
                tileView.setRotate(180);
                break;
            case LEFT:
                tileView.setRotate(270);
                break;
        }

        if (tile.getType() == TileType.GOAL) {
            tileView.setRotate(0);
        }
        tileView.setId("tile " + x + " " + y);
        return tileView;
    }

    /**
     * Creates an ImageView of this tile.
     *
     * @param tile       to create ImageView of
     * @param coordinate the coordinate of this tile
     * @return view of that tile.
     */
    public static Pane getFloorTileImage(FloorTile tile, Coordinate coordinate) {
        return getFloorTileImage(tile, coordinate.getX(), coordinate.getY());
    }

    /**
     * Creates an imageView of an arrow.
     *
     * @return the imageView of the arrow.
     */
    public static ImageView makeArrow() {
        ImageView arrow = new ImageView(get("slide_arrow"));
        arrow.setId("slide_arrow");
        arrow.setFitHeight(GameScreenController.tileWidth);
        arrow.setFitWidth(GameScreenController.tileWidth);
        arrow.setOnMouseEntered(e -> arrow.setEffect(new Bloom(0.1)));
        arrow.setOnMouseExited(e -> arrow.setEffect(new Bloom(999)));
        return arrow;
    }

    /**
     * Creates the Card which the user is holding.
     *
     * @param tile The tile that should be on the card.
     * @return Card.fxml Object.
	 * @throws IOException if null card is input
     */
    public static Node createCard(Tile tile) throws IOException {
        final Node newCard;
        if (tile == null) {
            throw new NullPointerException("Cannot create null tile");
        }
        HashMap<TileType, String> labels = new HashMap<>();
        labels.put(FIRE, "Fire Tile (Block Moves)");
        labels.put(FROZEN, "Ice Tile (Block Tiles)");
        labels.put(DOUBLE_MOVE, "Double Move");
        labels.put(BACKTRACK, "Backtrack");
        labels.put(CORNER, "Corner");
        labels.put(GOAL, "Goal");
        labels.put(STRAIGHT, "Straight");
        labels.put(T_SHAPE, "T-Shape");
        newCard = FXMLLoader.load(Objects.requireNonNull(GameScreenController.class.getClassLoader().getResource("FrontEnd/FXML/Card.fxml")));
        ImageView newCardImage = ((ImageView) newCard.lookup("#image"));
        newCardImage.setImage(Assets.get(tile.getType().toString().toLowerCase()));
        ImageView backing = ((ImageView) newCard.lookup("#backing"));
        Label text = ((Label) newCard.lookup("#text"));
        text.setText(labels.get(tile.getType()));
        backing.setImage(Assets.get("cardback"));
        newCard.setOnMouseEntered(
                e -> newCard.setEffect(new DropShadow(20, 0, 20, Color.BLACK)));
        newCard.setOnMouseExited(e -> newCard.setEffect(null));
        return newCard;
    }

	/**
	 * Get an imageView of that player.
	 *
     * @param colour The colour of the car you are getting
	 * @return the imageView of the specified player.
	 */
	public static ImageView getPlayer(CarColours colour) {
		Image playerModel = get("player" + colour.toString());
		ImageView player = new ImageView(playerModel);
		player.setFitHeight(GameScreenController.tileWidth);
		player.setFitWidth(GameScreenController.tileWidth);
		player.setId("player " + colour.toString());
		return player;
	}

	/**
	 * Get an imageView of a location arrow, that matches the size of the tiles.
	 *
	 * @return the imageView of the location arrow.
	 */
	public static ImageView getLocationArrow() {
		Image arrow = get("location_arrow");
		ImageView locationArrow = new ImageView(arrow);
		locationArrow.setFitWidth(GameScreenController.tileWidth);
		locationArrow.setFitHeight(GameScreenController.tileWidth);
		locationArrow.setId("locationarrow");
		return locationArrow;
	}
	/**
	 * Get an imageView of a fire effect, that matches the size of a square of 3 by 3 tiles.
	 *
	 * @return the imageView of the fire effect.
	 */
	public static Node getFireEffect() {
		Image fire = get("fireEffect");
		ImageView fireEffect = new ImageView(fire);
		fireEffect.setFitWidth(GameScreenController.tileWidth * 3);
		fireEffect.setFitHeight(GameScreenController.tileWidth * 3);
		return fireEffect;
	}

	/**
	 * Get an imageView of a freeze effect, that matches the size of a square of 3 by 3 tiles.
	 *
	 * @return the imageView of the freeze effect.
	 */
	public static Node getFrozenEffect() {
		Image frozen = get("frozenEffect");
		ImageView frozenEffect = new ImageView(frozen);
		frozenEffect.setFitWidth(GameScreenController.tileWidth * 3);
		frozenEffect.setFitHeight(GameScreenController.tileWidth * 3);
		return frozenEffect;
	}

	/**
	 * Get an imageView of a player's profile.
	 *
	 * @param profile of the player whose profile is required.
	 * @return The specified player's profile.
	 */
	public static Node getProfile(Profile profile) {
		ImageView profileView = new ImageView(get(profile.getIcon()));
		profileView.setFitWidth(100);
		profileView.setFitHeight(100);
		return profileView;
	}


}
