package backend.academy.maze.model.dsf;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class TreeTest {
    @Test
    @DisplayName("Correctness of checking if nodes are in same tree without operations")
    void simpleInSameTree() {
        Tree a = new Tree();
        Tree b = new Tree();

        boolean res1 = a.inSameTree(a);
        boolean res2 = a.inSameTree(b);

        assertThat(res1).isTrue();
        assertThat(res2).isFalse();
    }

    @Test
    @DisplayName("Correctness of merging two nodes")
    void simpleMerge() {
        Tree a = new Tree();
        Tree b = new Tree();

        a.merge(b);

        assertThat(a.inSameTree(b)).isTrue();
        assertThat(b.inSameTree(a)).isTrue();
    }

    @Test
    @DisplayName("Correctness of complex merge")
    void complexMerge() {
        Tree a = new Tree();
        Tree a1 = new Tree();
        Tree a2 = new Tree();
        a.merge(a1);
        a2.merge(a1);
        Tree b = new Tree();
        Tree b1 = new Tree();
        Tree b2 = new Tree();
        b.merge(b1);
        b2.merge(b1);

        a.merge(b);

        assertThat(a2.inSameTree(b2)).isTrue();
        assertThat(b2.inSameTree(a2)).isTrue();
    }
}
