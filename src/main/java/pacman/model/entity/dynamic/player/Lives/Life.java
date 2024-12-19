package pacman.model.entity.dynamic.player.Lives;

import javafx.scene.image.Image;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.physics.BoundingBox;
import pacman.model.entity.dynamic.physics.BoundingBoxImpl;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.maze.Maze;

import java.util.*;

public class Life implements Renderable, LivesSubject {
    private Image image;
    private BoundingBox boundingBox;
    private Vector2D vec;
    private List<LifeObserver> observers;


    public Life(int x, int y) {
        this.image = new Image("maze/pacman/playerRight.png");
        vec = new Vector2D(x, y);
        this.boundingBox = new BoundingBoxImpl(vec, 24, 24);
        this.observers = new ArrayList<>();
    }

    @Override
    public void addObserver(LifeObserver o) {
        this.observers.add(o);
    }

    @Override
    public void removeObserver(LifeObserver o) {
        this.observers.add(o);
    }

    @Override
    public void notifyObservers() {
        for (LifeObserver o : observers) {
            o.updateLives();
        }
    }

    @Override
    public Image getImage() {
        return this.image;
    }

    @Override
    public double getWidth() {
        return 24;
    }

    @Override
    public double getHeight() {
        return 24;
    }

    @Override
    public Vector2D getPosition() {
        return vec;
    }

    @Override
    public Layer getLayer() {
        return Layer.FOREGROUND;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return this.boundingBox;
    }

    @Override
    public void reset() {

    }

    @Override
    public void giveMaze(Maze maze) {

    }
}
