package pacman.model.entity.dynamic.player.Move;

import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.player.Pacman;

public class Up implements Move {
    Direction direction = Direction.UP;
    @Override
    public void move(Pacman pacman) {
        pacman.up();
    }

    @Override
    public Direction getDirection() {
        return direction;
    }
}
