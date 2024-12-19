package pacman.model.level.GameState;

public interface GameStateSubject {
    void addGameObserver(GameStateObserver observer);
    void removeGameObserver(GameStateObserver observer);
    void notifyGameObservers(GameState gameState);
}
