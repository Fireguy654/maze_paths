package backend.academy.maze.service.generator;

import backend.academy.maze.model.Cell;
import backend.academy.maze.model.Maze;
import backend.academy.maze.model.Node;
import backend.academy.maze.model.Wall;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.FieldSource;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import static org.assertj.core.api.Assertions.assertThat;

class PathGeneratorTest {
    @Test
    @DisplayName("Correctness of generating random path")
    void generateRandomPath() {
        int height = 4;
        int width = 5;
        Maze maze = getSimpleMaze(height, width);
        PathGenerator generator = new RandomPathGenerator();

        generator.generatePath(maze);

        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                if (i < height - 1) {
                    assertThat(maze.getWall(i, j, i + 1, j)).isNotNull();
                }
                if (j < width - 1) {
                    assertThat(maze.getWall(i, j, i, j + 1)).isNotNull();
                }
            }
        }
    }

    private static final List<PathGenerator> IDEAL_GENS = List.of(
        new KraskalPathGenerator(),
        new RecBTPathGenerator()
    );

    @ParameterizedTest
    @DisplayName("Correctness of generating ideal mazes")
    @FieldSource("IDEAL_GENS")
    void generateIdealMazes(PathGenerator generator) {
        int height = 4;
        int width = 5;
        Maze maze = getSimpleMaze(height, width);

        generator.generatePath(maze);

        boolean[][] reachable = fillReachable(height, width, maze);
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                assertThat(reachable[i][j]).isTrue();
            }
        }
    }

    private static boolean[][] fillReachable(int height, int width, Maze maze) {
        boolean[][] reachable = new boolean[height][width];
        reachable[0][0] = true;
        Queue<Node> queue = new ArrayDeque<>();
        queue.add(new Node(0, 0));
        while (!queue.isEmpty()) {
            Node node = queue.remove();
            node.getNeighbours(height, width, vert ->
                    !reachable[vert.row()][vert.col()] && maze.getWall(node, vert) != Wall.EXIST
                ).forEach(vert -> {
                    reachable[vert.row()][vert.col()] = true;
                    queue.add(new Node(vert.row(), vert.col()));
                });
        }
        return reachable;
    }

    private static Maze getSimpleMaze(int height, int width) {
        Cell[][] cells = new Cell[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                cells[i][j] = Cell.EMPTY;
            }
        }
        return new Maze(height, width, cells);
    }
}
