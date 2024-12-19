package pacman.model.level;

import org.json.simple.JSONObject;
import pacman.ConfigurationParseException;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.DynamicEntity;
import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.ghost.GhostImpl;
import pacman.model.entity.dynamic.ghost.GhostMode;
import pacman.model.entity.dynamic.physics.PhysicsEngine;
import pacman.model.entity.dynamic.player.Controllable;
import pacman.model.entity.dynamic.player.Lives.Life;
import pacman.model.entity.dynamic.player.Lives.LifeObserver;
import pacman.model.entity.dynamic.player.Lives.LivesSubject;
import pacman.model.entity.dynamic.player.Pacman;
import pacman.model.entity.staticentity.StaticEntity;
import pacman.model.entity.staticentity.collectable.Collectable;
import pacman.model.entity.staticentity.collectable.Pellet;
import pacman.model.level.GameState.GameState;
import pacman.model.level.GameState.GameStateObserver;
import pacman.model.level.GameState.GameStateSubject;
import pacman.model.level.GameState.State;
import pacman.model.maze.Maze;
import pacman.model.maze.Points.Points;
import pacman.model.maze.RenderableType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Concrete implement of Pac-Man level
 */
public class LevelImpl implements Level, LifeObserver, GameStateObserver {

    private static final int START_LEVEL_TIME = 200;
    private final Maze maze;
    private List<Renderable> renderables;
    private Controllable player;
    private List<Ghost> ghosts;
    private int tickCount;
    private Map<GhostMode, Integer> modeLengths;
    private int numLives;
    private List<Renderable> collectables;
    private GhostMode currentGhostMode;

    private List<Life> lives;

    private State state;
    private int stateTickCounter;

    public LevelImpl(JSONObject levelConfiguration,
                     Maze maze) {
        this.renderables = new ArrayList<>();
        this.maze = maze;
        this.tickCount = 0;
        this.modeLengths = new HashMap<>();
        this.currentGhostMode = GhostMode.SCATTER;
        this.lives = new ArrayList<>();
        this.stateTickCounter = 0;

        initLevel(new LevelConfigurationReader(levelConfiguration));
    }

    private void initLevel(LevelConfigurationReader levelConfigurationReader) {
        // Fetch all renderables for the level
        this.renderables = maze.getRenderables();

        // Set up player
        if (!(maze.getControllable() instanceof Controllable)) {
            throw new ConfigurationParseException("Player entity is not controllable");
        }
        this.player = (Controllable) maze.getControllable();
        this.player.setSpeed(levelConfigurationReader.getPlayerSpeed());
        setNumLives(maze.getNumLives());

        // Set up ghosts
        this.ghosts = maze.getGhosts().stream()
                .map(element -> (Ghost) element)
                .collect(Collectors.toList());
        Map<GhostMode, Double> ghostSpeeds = levelConfigurationReader.getGhostSpeeds();

        for (Ghost ghost : this.ghosts) {
            ghost.setSpeeds(ghostSpeeds);
            ghost.setGhostMode(this.currentGhostMode);
        }
        this.modeLengths = levelConfigurationReader.getGhostModeLengths();

        // Set up collectables
        this.collectables = new ArrayList<>(maze.getPellets());

        for (Renderable pellet : collectables) {
            Pellet pelletEntity = (Pellet) pellet;
            pelletEntity.addObserver(Points.getInstance());
        }

        // Set up lives
        int startX = 10;
        for (int i = 0; i < numLives; i++) {
            Life newLife = new Life(startX + 28*i, 16*34);
            lives.add(newLife);
            newLife.addObserver(this);
            maze.addRenderable(newLife, RenderableType.LIFE, startX + 16*i, 16*17);
        }

        // set up observer
        this.state = new State();
        maze.addRenderable(state, RenderableType.OTHER, 300, 300);
    }



    @Override
    public List<Renderable> getRenderables() {
        return this.renderables;
    }

    private List<DynamicEntity> getDynamicEntities() {
        return renderables.stream().filter(e -> e instanceof DynamicEntity).map(e -> (DynamicEntity) e).collect(
                Collectors.toList());
    }

    private List<StaticEntity> getStaticEntities() {
        return renderables.stream().filter(e -> e instanceof StaticEntity).map(e -> (StaticEntity) e).collect(
                Collectors.toList());
    }

    @Override
    public void tick() {
        //game state handlers
        if (state.getState() == GameState.READY) {
            stateTickCounter++;
            if (stateTickCounter == 60) {
                updateGameState(GameState.INPROG);
                stateTickCounter = 0;
            } else {
                return;
            }
        }
        if (state.getState() == GameState.GAME_OVER) {
            invisPlayerGhost();
            stateTickCounter++;
            if (stateTickCounter > 300) {
                handleGameEnd();
            }
            while (stateTickCounter > 0) {
                return;
            }
        }
        if (state.getState() == GameState.WIN) {
            invisPlayerGhost();
            stateTickCounter++;
            while (stateTickCounter > 0) {
                return;
            }
        }

        if (tickCount == modeLengths.get(currentGhostMode)) {

            // update ghost mode
            this.currentGhostMode = GhostMode.getNextGhostMode(currentGhostMode);
            for (Ghost ghost : this.ghosts) {
                ghost.setGhostMode(this.currentGhostMode);
            }

            tickCount = 0;
        }

        if (tickCount % Pacman.PACMAN_IMAGE_SWAP_TICK_COUNT == 0) {
            this.player.switchImage();
        }

        // Update the dynamic entities

        //give ghost the player position
        for (Ghost i : ghosts) {
            i.setPlayerPosition(player.getPosition());
        }

        List<DynamicEntity> dynamicEntities = getDynamicEntities();

        for (DynamicEntity dynamicEntity : dynamicEntities) {
            maze.updatePossibleDirections(dynamicEntity);
            dynamicEntity.update();
        }

        for (int i = 0; i < dynamicEntities.size(); ++i) {
            DynamicEntity dynamicEntityA = dynamicEntities.get(i);

            // handle collisions between dynamic entities
            for (int j = i + 1; j < dynamicEntities.size(); ++j) {
                DynamicEntity dynamicEntityB = dynamicEntities.get(j);

                if (dynamicEntityA.collidesWith(dynamicEntityB) ||
                        dynamicEntityB.collidesWith(dynamicEntityA)) {
                    dynamicEntityA.collideWith(this, dynamicEntityB);
                    dynamicEntityB.collideWith(this, dynamicEntityA);
                }
            }

            // handle collisions between dynamic entities and static entities
            for (StaticEntity staticEntity : getStaticEntities()) {
                if (dynamicEntityA.collidesWith(staticEntity)) {
                    dynamicEntityA.collideWith(this, staticEntity);
                    PhysicsEngine.resolveCollision(dynamicEntityA, staticEntity);
                }
            }
        }

        if (checkWin()) {
            //if win
            updateGameState(GameState.WIN);
        }

        tickCount++;
    }

    public void invisPlayerGhost() {
        Pacman pac = (Pacman) player;
        pac.setLayer(Renderable.Layer.INVISIBLE);
        for (Ghost ghost : ghosts) {
            GhostImpl gh = (GhostImpl) ghost;
            gh.setLayer(Renderable.Layer.INVISIBLE);
        }
    }

    public boolean checkWin() {
        boolean finish = false;
        for (Renderable renderable : collectables) {
            Pellet pellet = (Pellet) renderable;
            if (!pellet.isCollectable()) {
                finish = true;
            } else {
                finish = false;
                break;
            }
        }
        return finish;
    }

    @Override
    public boolean isPlayer(Renderable renderable) {
        return renderable == this.player;
    }

    @Override
    public boolean isCollectable(Renderable renderable) {
        return maze.getPellets().contains(renderable) && ((Collectable) renderable).isCollectable();
    }

    @Override
    public void moveLeft() {
        player.left();
    }

    @Override
    public void moveRight() {
        player.right();
    }

    @Override
    public void moveUp() {
        player.up();
    }

    @Override
    public void moveDown() {
        player.down();
    }

    @Override
    public boolean isLevelFinished() {
        return collectables.isEmpty();
    }

    @Override
    public int getNumLives() {
        return this.numLives;
    }

    private void setNumLives(int numLives) {
        this.numLives = numLives;
    }

    @Override
    public void handleLoseLife() {
        player.reset();
        for (Ghost ghost : ghosts) {
            ghost.reset();
        }
        numLives -= 1;
        for (int i = lives.size() - 1; i > 0; --i) {
            lives.get(i).notifyObservers();
        }
        if (numLives <= 0) {
            updateGameState(GameState.GAME_OVER);
        } else {
            updateGameState(GameState.READY);
        }
    }

    @Override
    public void handleGameEnd() {
        System.exit(0);
    }

    @Override
    public void collect(Collectable collectable) {

    }

    @Override
    public void updateLives() {
        for (int i = maze.getRenderables().size() - 1; i >= 0; --i) {
            if (maze.getRenderables().get(i) instanceof Life) {
                maze.getRenderables().remove(maze.getRenderables().get(i));
            }
        }

        int startX = 10;
        for (int i = 0; i < numLives; i++) {
            Life newLife = new Life(startX + 28*i, 16*34);
            lives.add(newLife);
            newLife.addObserver(this);
            maze.addRenderable(newLife, RenderableType.LIFE, startX + 16*i, 16*17);
        }
    }


    @Override
    public void updateGameState(GameState gameState) {
        this.state.update(gameState);
    }
}
