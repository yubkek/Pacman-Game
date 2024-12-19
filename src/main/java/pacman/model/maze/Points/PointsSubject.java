package pacman.model.maze.Points;

public interface PointsSubject {
    void addObserver(PointObserver observer);
    void removeObserver(PointObserver observer);
    void notifyObservers();
}
