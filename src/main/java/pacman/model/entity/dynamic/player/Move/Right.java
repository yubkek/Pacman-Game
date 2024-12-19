package pacman.model.entity.dynamic.player.Move;

import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.player.Pacman;

public class Right implements Move {
    Direction direction = Direction.RIGHT;
    @Override
    public void move(Pacman pacman) {
        pacman.right();
    }

    @Override
    public Direction getDirection() {
        return direction;
    }
}
