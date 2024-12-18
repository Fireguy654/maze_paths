package backend.academy.maze.service.generator;

import backend.academy.maze.model.Maze;

public interface PathGenerator {
    void generatePath(Maze field);
}
