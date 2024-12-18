package backend.academy.maze.service.ui;

import backend.academy.maze.model.Maze;
import backend.academy.maze.model.Node;
import java.io.IOException;

public interface MazeGraphicalDriver {
    void print(Maze maze) throws IOException;

    void print(Maze maze, Node startNode, Node endNode) throws IOException;
}
