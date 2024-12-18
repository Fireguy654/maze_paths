package backend.academy.maze.service.generator;

import backend.academy.maze.model.Maze;
import backend.academy.maze.model.Wall;
import backend.academy.maze.model.dsf.Tree;
import backend.academy.maze.util.MatrixUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Generates an ideal maze(one path between every two cells) in given template maze(where every wall exists).
 * The collects every wall in a list, shuffles it and then goes throw it.
 * If current wall is between parts of the maze, which are not connected yet,
 * then algorithm deletes this wall.
 */
public class KraskalPathGenerator implements PathGenerator {
    @Override
    public void generatePath(Maze field) {
        Tree[][] dsf = new Tree[field.height()][field.width()];
        MatrixUtils.setAll(dsf, Tree::new);
        List<Edge> edges = getAllEdges(field.height(), field.width());
        Collections.shuffle(edges);

        for (Edge edge : edges) {
            Tree first = edge.getFirstTree(dsf);
            Tree second = edge.getSecondTree(dsf);
            if (first.inSameTree(second)) {
                continue;
            }
            field.setWall(edge.row1(), edge.col1(), edge.row2(), edge.col2(), Wall.ABSENT);
            first.merge(second);
        }
    }

    private List<Edge> getAllEdges(int height, int width) {
        List<Edge> edges = new ArrayList<>();
        for (int row = 0; row < height; ++row) {
            for (int col = 0; col < width - 1; ++col) {
                edges.add(new Edge(row, col, row, col + 1));
            }
        }
        for (int row = 0; row < height - 1; ++row) {
            for (int col = 0; col < width; ++col) {
                edges.add(new Edge(row, col, row + 1, col));
            }
        }
        return edges;
    }

    private record Edge(int row1, int col1, int row2, int col2) {
        private Tree getFirstTree(Tree[][] trees) {
            return trees[row1][col1];
        }

        private Tree getSecondTree(Tree[][] trees) {
            return trees[row2][col2];
        }
    }
}
