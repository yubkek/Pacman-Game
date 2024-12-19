package pacman.model.entity.dynamic.player;

import javafx.scene.image.Image;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.physics.*;
import pacman.model.entity.dynamic.player.Move.Left;
import pacman.model.entity.dynamic.player.Move.Move;
import pacman.model.entity.dynamic.player.Move.Up;
import pacman.model.entity.staticentity.collectable.Collectable;
import pacman.model.level.Level;
import pacman.model.maze.Maze;

import java.util.*;

public class Pacman implements Controllable {

    public static final int PACMAN_IMAGE_SWAP_TICK_COUNT = 8;
    private Layer layer = Layer.FOREGROUND;
    private final Map<PacmanVisual, Image> images;
    private final BoundingBox boundingBox;
    private final Vector2D startingPosition;
    private KinematicState kinematicState;
    private Image currentImage;
    private Set<Direction> possibleDirections;
    private boolean isClosedImage;

    private Maze maze;
    Map<String, Boolean> walls;
    private Move move;
    private Move nextMove;

    private List<Renderable> pellets;

    public Pacman(
            Image currentImage,
            Map<PacmanVisual, Image> images,
            BoundingBox boundingBox,
            KinematicState kinematicState
    ){
        this.move = new Left();
        this.currentImage = currentImage;
        this.images = images;
        this.boundingBox = boundingBox;
        this.kinematicState = kinematicState;
        this.startingPosition = kinematicState.getPosition();
        this.possibleDirections = new HashSet<>();
        this.isClosedImage = false;
    }

    @Override
    public void setPosition(Vector2D position) {
        this.kinematicState.setPosition(position);
    }

    @Override
    public Image getImage() {
        if (isClosedImage){
            return images.get(PacmanVisual.CLOSED);
        } else {
            return currentImage;
        }
    }

    @Override
    public Vector2D getPosition() {
        return this.kinematicState.getPosition();
    }

    @Override
    public Vector2D getPositionBeforeLastUpdate() {
        return this.kinematicState.getPreviousPosition();
    }

    public void update() {
        this.updateMove();
        this.move.move(this);
        kinematicState.update();
        this.boundingBox.setTopLeft(this.kinematicState.getPosition());
    }

    @Override
    public void setSpeed(double speed){
        this.kinematicState.setSpeed(speed);
    }

    @Override
    public void up() {
        this.kinematicState.up();
        this.currentImage = images.get(PacmanVisual.UP);
    }

    @Override
    public void down() {
        this.kinematicState.down();
        this.currentImage = images.get(PacmanVisual.DOWN);
    }

    @Override
    public void left() {
        this.kinematicState.left();
        this.currentImage = images.get(PacmanVisual.LEFT);
    }

    @Override
    public void right() {
        this.kinematicState.right();
        this.currentImage = images.get(PacmanVisual.RIGHT);
    }

    @Override
    public Layer getLayer() {
        return this.layer;
    }

    public void setLayer(Layer layer) {
        this.layer = layer;
    }

    @Override
    public void collideWith(Level level, Renderable renderable){
        if (level.isCollectable(renderable)){
            Collectable collectable = (Collectable) renderable;
            level.collect(collectable);
            collectable.collect();
        }
    }

    @Override
    public boolean collidesWith(Renderable renderable){
       return boundingBox.collidesWith(kinematicState.getDirection(), renderable.getBoundingBox());
    }

    @Override
    public void reset(){
        this.kinematicState = new KinematicStateImpl.KinematicStateBuilder()
                .setPosition(startingPosition)
                .setSpeed(kinematicState.getSpeed())
                .build();

        // go left by default
        setMove(new Left());
    }

    public List<Renderable> getPellets(){
        return this.pellets;
    }

    @Override
    public void giveMaze(Maze maze) {
        this.maze = maze;
        walls = maze.getWalls();
        pellets = maze.getPellets();
    }

    public Maze getMap() {
        return this.maze;
    }

    @Override
    public BoundingBox getBoundingBox(){
        return this.boundingBox;
    }

    @Override
    public double getHeight() {
        return this.boundingBox.getHeight();
    }

    @Override
    public double getWidth() {
        return this.boundingBox.getWidth();
    }

    @Override
    public void setPossibleDirections(Set<Direction> possibleDirections) {
        this.possibleDirections = possibleDirections;
    }

    @Override
    public Direction getDirection() {
        return this.kinematicState.getDirection();
    }

    @Override
    public Vector2D getCenter(){
        return new Vector2D(boundingBox.getMiddleX(), boundingBox.getMiddleY());
    }

    @Override
    public void switchImage(){
        this.isClosedImage = !this.isClosedImage;
    }

    public void setMove(Move move) {
        //if can move in possible direction, make current move this new one, next move null
        if (possibleDirections.contains(move.getDirection())){
            this.move = move;
            this.nextMove = null;
        }
        //cannot move in possible direction, queue move
        else {
            this.nextMove = move;
        }
    }

    public void updateMove() {
        //if move is queued, try do move asap, then turn next move null
        if (nextMove != null && possibleDirections.contains(nextMove.getDirection())){
            this.move = nextMove;
            nextMove = null;
        }
    }

}
