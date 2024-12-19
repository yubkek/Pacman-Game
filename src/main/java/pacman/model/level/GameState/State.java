package pacman.model.level.GameState;

import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.physics.BoundingBox;
import pacman.model.entity.dynamic.physics.BoundingBoxImpl;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.maze.Maze;

import java.util.*;

public class State implements GameStateSubject, Renderable {

    private GameState state;
    private String screentext;
    private Vector2D position;
    private WritableImage image;
    private BoundingBox boundingBox;

    private List<GameStateObserver> observers;

    public State() {
        this.state = GameState.READY;
        this.screentext = "READY!";
        this.position = new Vector2D(205, 320);
        updateImage();
        this.boundingBox = new BoundingBoxImpl(position, 205, 320);
        this.observers = new ArrayList<>();
    }

    public GameState getState() {
        return state;
    }

    @Override
    public Image getImage() {
        return image;
    }

    @Override
    public double getWidth() {
        return image.getWidth();
    }

    @Override
    public double getHeight() {
        return image.getHeight();
    }

    @Override
    public Vector2D getPosition() {
        return position;
    }

    @Override
    public Layer getLayer() {
        return Layer.FOREGROUND;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    @Override
    public void reset() {
        this.update(GameState.READY);
        updateImage();
    }

    @Override
    public void giveMaze(Maze maze) {

    }

    private void updateImage() {
        Text text = new Text(screentext);
        text.setFont(Font.loadFont("PressStart2P-Regular.ttf", 40));
        text.setStyle("-fx-font-size: 100;");

        if (state == GameState.READY) {
            text.setFill(Color.YELLOW);
        }
        if (state == GameState.WIN) {
            text.setFill(Color.WHITE);
        }
        if (state == GameState.GAME_OVER) {
            text.setFill(Color.RED);
        }

        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        this.image = text.snapshot(params, null);

        this.boundingBox = new BoundingBoxImpl(position, 205, 320);
    }

    public void update(GameState gameState) {
        this.state = gameState;
        switch(gameState){
            case READY:
                this.screentext = "READY!";
                break;
            case INPROG:
                this.screentext = "";
                break;
            case GAME_OVER:
                this.screentext = "GAME OVER";
                this.position = new Vector2D(190, 320);
                this.boundingBox = new BoundingBoxImpl(position, 190, 320);
                break;
            case WIN:
                this.screentext = "YOU WIN!";
                this.position = new Vector2D(197, 320);
                this.boundingBox = new BoundingBoxImpl(position, 197, 320);
                break;
        }
        updateImage();
    }

    @Override
    public void addGameObserver(GameStateObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeGameObserver(GameStateObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyGameObservers(GameState gameState) {
        for (GameStateObserver observer : observers) {
            observer.updateGameState(gameState);
        }
    }
}
