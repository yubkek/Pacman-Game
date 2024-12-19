package pacman.view.keyboard;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.player.Move.Down;
import pacman.model.entity.dynamic.player.Move.Left;
import pacman.model.entity.dynamic.player.Move.Right;
import pacman.model.entity.dynamic.player.Move.Up;
import pacman.model.entity.dynamic.player.Pacman;
import pacman.model.entity.staticentity.collectable.Pellet;

import java.util.List;

/**
 * Responsible for handling keyboard input from player
 */
public class KeyboardInputHandler {
    private Pacman pacman;
    private List<Renderable> pellets;

    public KeyboardInputHandler(Pacman pacman) {
        this.pacman = pacman;
        pellets = this.pacman.getPellets();
    }

    public void handlePressed(KeyEvent keyEvent) {
        KeyCode keyCode = keyEvent.getCode();
        switch (keyCode) {
            case LEFT:
                pacman.setMove(new Left());
                break;
            case RIGHT:
                pacman.setMove(new Right());
                break;
            case DOWN:
                pacman.setMove(new Down());
                break;
            case UP:
                pacman.setMove(new Up());
                break;
            case R:
                pacman.reset();
                break;
            case P:
                for (Renderable r : pellets) {
                    Pellet pellet = (Pellet) r;
                    pellet.collect();
                }
        };
    }
}
