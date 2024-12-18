package backend.academy.maze.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import static org.assertj.core.api.Assertions.assertThat;

class MatrixUtilsTest {
    @Test
    @DisplayName("Correctness of setting all elements in matrix.")
    void setAll() {
        Integer[][] matrix = new Integer[][]{{1, 2, 3}, {10, 20, 30}};
        Supplier<Integer> supplier = () -> 100;

        MatrixUtils.setAll(matrix, supplier);

        assertThat(matrix).isDeepEqualTo(new Integer[][]{{100, 100, 100}, {100, 100, 100}});
    }

    @Test
    @DisplayName("Correctness of mapping all elements in matrix.")
    void mapAll() {
        Integer[][] matrix = new Integer[][]{{1, 2, 3}, {10, 20, 30}};
        UnaryOperator<Integer> func = num -> num + 2;

        MatrixUtils.mapAll(matrix, func);

        assertThat(matrix).isDeepEqualTo(new Integer[][]{{3, 4, 5}, {12, 22, 32}});
    }
}
