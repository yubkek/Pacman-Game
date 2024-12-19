package pacman.model.entity.dynamic.player.Move;

import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.player.Pacman;

public class Down implements Move {
    Direction direction = Direction.DOWN;
    @Override
    public void move(Pacman pacman) {
        pacman.down();
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

}
