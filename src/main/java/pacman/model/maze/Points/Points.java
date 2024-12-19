package pacman.model.maze.Points;

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

public class Points implements Renderable, PointObserver {

    private int points;
    private Vector2D position;
    private WritableImage image;
    private BoundingBox boundingBox;

    private static Points pointInstance;

    public Points() {
        this.points = 0;
        this.position = new Vector2D(10, 10);
        updateImage();
        this.boundingBox = new BoundingBoxImpl(position, 400, 400);
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
        points = 0;
        updateImage();
    }

    @Override
    public void giveMaze(Maze maze) {

    }

    private void updateImage() {
        Text text = new Text(Integer.toString(points));
        text.setFont(Font.loadFont("maze/PressStart2P-Regular.ttf", 40));
        text.setFill(Color.WHITE);
        text.setStyle("-fx-font-size: 100;");

        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        this.image = text.snapshot(params, null);

        this.boundingBox = new BoundingBoxImpl(position, image.getWidth(), image.getHeight());
    }

    public void update(int points) {
        this.points += points;
        updateImage();
    }

    public static Points getInstance() {
        if (pointInstance == null) {
            pointInstance = new Points();
        }
        return pointInstance;
    }
}