package pacman.model.entity.dynamic.ghost;

import javafx.scene.image.Image;
import pacman.model.entity.dynamic.physics.*;

public class Inky implements GhostFactory{
    @Override
    public Ghost createGhost(double x, double y) {
        Vector2D vec = new Vector2D(x + 4, y - 4);
        Image image = new Image("maze/ghosts/ghost.png");
        BoundingBox boundingBox = new BoundingBoxImpl(vec, image.getHeight(), image.getWidth());
        KinematicState kinematicState = new KinematicStateImpl.KinematicStateBuilder()
                .setPosition(vec)
                .build();
        GhostMode ghostMode = GhostMode.SCATTER;
        Vector2D targetCorner = new Vector2D(28*16, 34*16);
        Direction currentDirection = Direction.LEFT;

        return new GhostImpl(image, boundingBox, kinematicState, ghostMode, targetCorner, currentDirection);
    }
}
