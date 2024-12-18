package backend.academy.maze.util;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import org.apache.commons.lang3.ArrayUtils;

public final class MatrixUtils {
    private MatrixUtils() {}

    public static <T> void setAll(T[][] matrix, Supplier<T> generator) {
        for (T[] row: matrix) {
            ArrayUtils.setAll(row, generator);
        }
    }

    public static <T> void mapAll(T[][] matrix, UnaryOperator<T> mapper) {
        for (T[] row: matrix) {
            for (int i = 0; i < row.length; i++) {
                row[i] = mapper.apply(row[i]);
            }
        }
    }
}
