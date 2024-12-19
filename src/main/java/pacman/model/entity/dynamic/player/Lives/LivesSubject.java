package pacman.model.entity.dynamic.player.Lives;

import java.util.Observer;

public interface LivesSubject {
    void addObserver(LifeObserver o);
    void removeObserver(LifeObserver o);
    void notifyObservers();
}
