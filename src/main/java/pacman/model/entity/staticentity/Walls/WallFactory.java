package pacman.model.entity.staticentity.Walls;

import pacman.model.entity.staticentity.StaticEntityImpl;

public interface WallFactory {
    StaticEntityImpl createWall(double x, double y);
}
