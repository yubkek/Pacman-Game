package pacman.model.entity.staticentity.Walls;

import javafx.scene.image.Image;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.physics.BoundingBox;
import pacman.model.entity.dynamic.physics.BoundingBoxImpl;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.entity.staticentity.StaticEntityImpl;

public class ULWall implements WallFactory {
    @Override
    public StaticEntityImpl createWall(double x, double y) {
        Image image = new Image("maze/walls/upLeft.png");
        Vector2D vec = new Vector2D(x, y);
        BoundingBox boundingBox = new BoundingBoxImpl(vec, 16, 16);
        Renderable.Layer layer = Renderable.Layer.FOREGROUND;
        return new StaticEntityImpl(boundingBox, layer, image);
    }
}