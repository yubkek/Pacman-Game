package pacman.model.entity.dynamic.player;

import javafx.scene.image.Image;
import pacman.model.entity.dynamic.physics.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class PacmanMain implements PacmanFactory{
    @Override
    public Pacman createPacman(double x, double y) {
        Map<PacmanVisual, Image> images = new HashMap<>();
        images.put(PacmanVisual.RIGHT, new Image("maze/pacman/playerRight.png"));
        images.put(PacmanVisual.LEFT, new Image("maze/pacman/playerLeft.png"));
        images.put(PacmanVisual.UP, new Image("maze/pacman/playerUp.png"));
        images.put(PacmanVisual.DOWN, new Image("maze/pacman/playerDown.png"));
        images.put(PacmanVisual.CLOSED, new Image("maze/pacman/playerClosed.png"));

        Vector2D vec = new Vector2D(x, y);
        BoundingBox boundingBox = new BoundingBoxImpl(vec, 22, 22);

        KinematicState kinematicState = new KinematicStateImpl.KinematicStateBuilder()
                .setPosition(vec)
                .build();

        return new Pacman(images.get(PacmanVisual.RIGHT), images, boundingBox, kinematicState);
    }

}
