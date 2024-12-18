package backend.academy.maze.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public record Node(int row, int col) implements Comparable<Node> {
    public List<Node> getNeighbours(int height, int width, Predicate<Node> predicate) {
        return getNeighbours(height, width).stream().filter(predicate).toList();
    }

    public List<Node> getNeighbours(int height, int width) {
        List<Node> deciding = new ArrayList<>();
        if (row > 0) {
            deciding.add(new Node(row - 1, col));
        }
        if (row < height - 1) {
            deciding.add(new Node(row + 1, col));
        }
        if (col > 0) {
            deciding.add(new Node(row, col - 1));
        }
        if (col < width - 1) {
            deciding.add(new Node(row, col + 1));
        }
        return deciding;
    }

    @Override
    public int compareTo(Node o) {
        if (row != o.row) {
            return row - o.row;
        }
        return col - o.col;
    }
}
