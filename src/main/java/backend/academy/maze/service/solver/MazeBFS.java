package backend.academy.maze.service.solver;

import backend.academy.maze.model.Maze;
import backend.academy.maze.model.Node;
import backend.academy.maze.model.Wall;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Finds the shortest path between given cells in given maze.
 * It collects all the not visited cells which are connected by one step to visited ones starting with start cell.
 * It goes throw them, marks them as visited ones, finds new one-step cells
 * and starts over again until reaching end cell. Also, it counts the length of the path.
 */
public class MazeBFS implements MazeSolver {
    @Override
    public String findPath(Maze maze, Node start, Node end) {
        Node[][] par = new Node[maze.height()][maze.width()];
        par[start.row()][start.col()] = start;
        Queue<Node> inProgress = new ArrayDeque<>();
        Queue<Node> found = new ArrayDeque<>();
        found.add(start);
        int length = 1;

        while (!found.isEmpty()) {
            inProgress.addAll(found);
            found.clear();
            while (!inProgress.isEmpty()) {
                Node cur = inProgress.remove();

                if (cur.equals(end)) {
                    setPath(maze, start, end, par);
                    return String.format("Found path with total length %d", length);
                }

                cur.getNeighbours(
                        maze.height(),
                        maze.width(),
                        vert -> par[vert.row()][vert.col()] == null && maze.getWall(cur, vert) != Wall.EXIST
                ).forEach(vert -> {
                    par[vert.row()][vert.col()] = cur;
                    found.add(vert);
                });
            }
            ++length;
        }

        return NOT_FOUND_RESPONSE;
    }
}
