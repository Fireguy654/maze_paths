package backend.academy.maze.service.generator;

import backend.academy.maze.model.Maze;
import backend.academy.maze.model.Node;
import backend.academy.maze.model.Wall;
import java.util.ArrayList;
import java.util.List;
import java.util.random.RandomGenerator;

/**
 * Generates an ideal maze(one path between every two cells) in given template maze(where every wall exists)
 * It remembers all the cells it visited and currently not finished cells.
 * If it is currently in some cell, it checks if nearby cells are visited and chooses randomly unvisited next cells
 * and deletes a wall between the current and chosen cells and continues in the chosen one.
 * If there are no unvisited cells it remembers the current cell as finished
 * and comes back to cell from which algorithm came.
 *
 */
public class RecBTPathGenerator implements PathGenerator {
    private static final RandomGenerator RANDOM = RandomFabric.getRandom();

    @Override
    public void generatePath(Maze field) {
        int height = field.height();
        int width = field.width();

        Node cur = new Node(RANDOM.nextInt(height), RANDOM.nextInt(width));
        List<Node> unfinished = new ArrayList<>();
        unfinished.add(cur);
        boolean[][] visited = new boolean[height][width];
        visited[cur.row()][cur.col()] = true;

        while (!unfinished.isEmpty()) {
            cur = unfinished.getLast();
            Node next = chooseUnvisitedRandomly(cur, visited, height, width);
            if (next != null) {
                unfinished.add(next);
                visited[next.row()][next.col()] = true;
                field.setWall(cur.row(), cur.col(), next.row(), next.col(), Wall.ABSENT);
            } else {
                unfinished.removeLast();
            }
        }
    }

    private Node chooseUnvisitedRandomly(Node node, boolean[][] visited, int height, int width) {
        List<Node> deciding = node.getNeighbours(height, width, vert -> !visited[vert.row()][vert.col()]);
        return deciding.isEmpty() ? null : deciding.get(RANDOM.nextInt(deciding.size()));
    }
}
