package pacman.model.entity.staticentity.collectable;

import javafx.scene.image.Image;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.physics.BoundingBox;
import pacman.model.entity.dynamic.physics.BoundingBoxImpl;
import pacman.model.entity.dynamic.physics.Vector2D;

public class PelletSmall implements PelletFactory{
    @Override
    public Pellet createPellet(double x, double y) {
        Vector2D vec = new Vector2D(x, y);
        BoundingBox boundingBox = new BoundingBoxImpl(vec, 16, 16);
        Image image = new Image("maze/pellet.png");
        return new Pellet(boundingBox, Renderable.Layer.FOREGROUND, image, 100);
    }
}
