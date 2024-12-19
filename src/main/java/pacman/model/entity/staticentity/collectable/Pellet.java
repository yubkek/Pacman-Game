package pacman.model.entity.staticentity.collectable;

import javafx.scene.image.Image;
import pacman.model.entity.dynamic.physics.BoundingBox;
import pacman.model.entity.staticentity.StaticEntityImpl;
import pacman.model.maze.Points.PointObserver;
import pacman.model.maze.Points.PointsSubject;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the Pellet in Pac-Man game
 */
public class Pellet extends StaticEntityImpl implements Collectable, PointsSubject {

    private final int points;
    private boolean isCollectable;
    private List<PointObserver> observers;

    public Pellet(BoundingBox boundingBox, Layer layer, Image image, int points) {
        super(boundingBox, layer, image);
        this.points = points;
        this.isCollectable = true;
        observers = new ArrayList<PointObserver>();
    }

    @Override
    public void collect() {
        this.isCollectable = false;
        setLayer(Layer.INVISIBLE);
        notifyObservers();
    }

    @Override
    public void reset() {
        this.isCollectable = true;
        setLayer(Layer.FOREGROUND);
    }

    @Override
    public boolean isCollectable() {
        return this.isCollectable;
    }

    @Override
    public boolean canPassThrough() {
        return true;
    }

    @Override
    public int getPoints() {
        return this.points;
    }

    @Override
    public void addObserver(PointObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(PointObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (PointObserver observer : observers) {
            observer.update(this.points);
        }
    }
}
