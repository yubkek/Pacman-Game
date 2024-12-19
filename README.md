# Running the Game
- enter "gradle run" in terminal or run with green play button at top
- play as normal 
- pacman gets stuck rarely, press r to reset
- pressing p collects all pellets on board - allows "instant win"

# Design Patterns
## Factory 
- GhostFactory interface, Blinky, Inky, Pinky, Clyde, are the concrete factory classes that create their respective ghost
- PacmanFactory interface, PacmanMain is the concrete class that creates the pacman
- PelletFactory interface, PelletSmall is the concrete class that creates the pellets
- WallFactory interface, DLWall, DRWall, HWall, VWall, ULWall, URWall, are the concrete classes that create the respective type of wall
## Observer
- GameStateObserver interface, GameStateSubject interface, GameState enum helps to describe the game state, State implements GameStateSubject, LevelImpl implements GameStateObserver
- LifeObserver interface, LivesSubject interface, Life implements LivesSubject, LevelImpl implements LifeObserver
- PointObserver interface, PointsSubject interface, Points implements PointObserver, Pellet implements PointsSubject
## Singleton
- Points class
## Command
- Move interface, Down, Left, Right, Up classes are concrete classes that implement the move interface
