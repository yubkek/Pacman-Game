package pacman.model.maze;

import pacman.model.entity.dynamic.ghost.Blinky;
import pacman.model.entity.dynamic.ghost.Clyde;
import pacman.model.entity.dynamic.ghost.Inky;
import pacman.model.entity.dynamic.ghost.Pinky;
import pacman.model.entity.dynamic.player.Pacman;
import pacman.model.entity.dynamic.player.PacmanMain;
import pacman.model.entity.staticentity.Walls.*;
import pacman.model.entity.staticentity.collectable.Pellet;
import pacman.model.entity.staticentity.collectable.PelletSmall;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static java.lang.System.exit;
import static java.lang.System.setOut;

/**
 * Responsible for creating renderables and storing it in the Maze
 */
public class MazeCreator {

    private final String fileName;
    public static final int RESIZING_FACTOR = 16;

    public MazeCreator(String fileName){
        this.fileName = fileName;
    }

    public Maze createMaze(){
        File f = new File(this.fileName);
        Maze maze = new Maze();

        try {
            Scanner scanner = new Scanner(f);

            int y = 0;

            while (scanner.hasNextLine()){

                String line = scanner.nextLine();
                char[] row = line.toCharArray();
                int ghostNo = 0;

                for (int x = 0; x < row.length; x++){
                    /**
                     * TO DO: Implement Factory Method Pattern
                     */
                    DLWall dlWall = new DLWall();
                    DRWall drWall = new DRWall();
                    HWall hWall = new HWall();
                    ULWall ulWall = new ULWall();
                    URWall urWall = new URWall();
                    VWall vWall = new VWall();
                    PelletSmall pelletSmall = new PelletSmall();
                    PacmanMain pacmanMain = new PacmanMain();
                    Blinky blinky = new Blinky();
                    Clyde clyde = new Clyde();
                    Inky inky = new Inky();
                    Pinky pinky = new Pinky();

                    switch (row[x]) {
                        case '1':
                            maze.addRenderable(hWall.createWall(x*RESIZING_FACTOR, y*RESIZING_FACTOR), RenderableType.HORIZONTAL_WALL, x, y);
                            break;
                        case '2':
                            maze.addRenderable(vWall.createWall(x*RESIZING_FACTOR, y*RESIZING_FACTOR), RenderableType.VERTICAL_WALL, x, y);
                            break;
                        case '3':
                            maze.addRenderable(ulWall.createWall(x*RESIZING_FACTOR, y*RESIZING_FACTOR), RenderableType.UP_LEFT_WALL, x, y);
                            break;
                        case '4':
                            maze.addRenderable(urWall.createWall(x*RESIZING_FACTOR, y*RESIZING_FACTOR), RenderableType.UP_RIGHT_WALL, x, y);
                            break;
                        case '5':
                            maze.addRenderable(dlWall.createWall(x*RESIZING_FACTOR, y*RESIZING_FACTOR), RenderableType.DOWN_LEFT_WALL, x, y);
                            break;
                        case '6':
                            maze.addRenderable(drWall.createWall(x*RESIZING_FACTOR, y*RESIZING_FACTOR), RenderableType.DOWN_RIGHT_WALL, x, y);
                            break;
                        case '7':
                            maze.addRenderable(pelletSmall.createPellet(x*RESIZING_FACTOR, y*RESIZING_FACTOR), RenderableType.PELLET, x, y);
                            break;
                        case 'p':
                            maze.addRenderable(pacmanMain.createPacman(x*RESIZING_FACTOR, y*RESIZING_FACTOR), RenderableType.PACMAN, x, y);
                            break;
                        case 'g':
                            if (ghostNo == 0) {
                                maze.addRenderable(blinky.createGhost(x*RESIZING_FACTOR, y*RESIZING_FACTOR), RenderableType.GHOST, x, y);
                                ghostNo = 1;
                            }
                            else if (ghostNo == 1) {
                                maze.addRenderable(clyde.createGhost(x*RESIZING_FACTOR, y*RESIZING_FACTOR), RenderableType.GHOST, x, y);
                                ghostNo = 2;
                            }
                            else if (ghostNo == 2) {
                                maze.addRenderable(inky.createGhost(x*RESIZING_FACTOR, y*RESIZING_FACTOR), RenderableType.GHOST, x, y);
                                ghostNo = 3;
                            }
                            else if (ghostNo == 3) {
                                maze.addRenderable(pinky.createGhost(x*RESIZING_FACTOR, y*RESIZING_FACTOR), RenderableType.GHOST, x, y);
                                ghostNo = 4;
                            }
                            break;
                        default:
                            break;
                    }

                }
                
                y += 1;
            }

            scanner.close();
        }
        catch (FileNotFoundException e){
            System.out.println("No maze file was found.");
            exit(0);
        } catch (Exception e){
            System.out.println("Error");
            System.out.println(e.getMessage());
            exit(0);
        }

        return maze;
    }
}
