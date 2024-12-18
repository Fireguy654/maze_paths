package backend.academy.maze.service.solver;

import backend.academy.maze.model.Maze;
import backend.academy.maze.model.Node;
import backend.academy.maze.model.Wall;

public interface MazeSolver {
    String NOT_FOUND_RESPONSE = "Path not found";

    default String findPath(Maze maze, int startRow, int startCol, int endRow, int endCol) {
        return findPath(maze, new Node(startRow, startCol), new Node(endRow, endCol));
    }

    String findPath(Maze maze, Node start, Node end);

    default void setPath(Maze maze, Node startNode, Node endNode, Node[][] par) {
        Node cur = endNode;
        while (!cur.equals(startNode)) {
            maze.setWall(cur, par[cur.row()][cur.col()], Wall.PATH);
            cur = par[cur.row()][cur.col()];
        }
    }
}
