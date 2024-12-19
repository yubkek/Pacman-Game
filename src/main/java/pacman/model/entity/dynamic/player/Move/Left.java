package pacman.model.entity.dynamic.player.Move;

import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.player.Pacman;

public class Left implements Move {
    Direction direction = Direction.LEFT;
    @Override
    public void move(Pacman pacman) {
        pacman.left();
    }

    @Override
    public Direction getDirection() {
        return direction;
    }
}
