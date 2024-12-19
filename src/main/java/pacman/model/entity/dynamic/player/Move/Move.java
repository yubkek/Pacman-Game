package pacman.model.entity.dynamic.player.Move;

import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.player.Pacman;

public interface Move {
    void move(Pacman pacman);

    Direction getDirection();
}
