package backend.academy.maze.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

class NodeTest {
    @Test
    @DisplayName("Correctness of getting neighbours in all of 4 sides")
    void getSimpleNeighbors() {
        Node node = new Node(1, 1);

        List<Node> neighbors = node.getNeighbours(3, 3);

        assertThat(neighbors).containsExactlyInAnyOrder(new Node(0, 1), new Node(1, 0),
                                                        new Node(2, 1), new Node(1, 2));
    }

    @Test
    @DisplayName("Correctness of getting neighbours when there is no one of them")
    void getNoNeighbors() {
        Node node = new Node(0, 0);

        List<Node> neighbors = node.getNeighbours(1, 1);

        assertThat(neighbors).isEmpty();
    }

    @Test
    @DisplayName("Correctness of getting neighbours with predicate")
    void getNeighborPredicate() {
        Node node = new Node(1, 1);

        List<Node> neighbors = node.getNeighbours(3, 3,
            vert -> vert.row() > node.row() || vert.col() > node.col());

        assertThat(neighbors).containsExactlyInAnyOrder(new Node(2, 1), new Node(1, 2));
    }

    @Test
    @DisplayName("Simple comparison test")
    void compareTo() {
        Node a0 = new Node(0, 0);
        Node b0 = new Node(0, 1);
        Node b1 = new Node(1, 0);
        Node a2 = new Node(1, 10);
        Node b2 = new Node(2, 0);

        int res0 = a0.compareTo(a0);
        int res1 = a0.compareTo(b0);
        int res2 = a0.compareTo(b1);
        int res3 = a2.compareTo(b2);

        assertThat(res0).isZero();
        assertThat(res1).isNegative();
        assertThat(res2).isNegative();
        assertThat(res3).isNegative();
    }
}
