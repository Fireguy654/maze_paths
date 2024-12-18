package backend.academy.maze.service.generator;

import backend.academy.maze.model.Cell;
import backend.academy.maze.util.MatrixUtils;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.function.Supplier;

/**
 * Generates a cell matrix with given height and width in which every cell is empty.
 */
public class EmptyCellMatrixGenerator implements CellMatrixGenerator {
    private static final Supplier<Cell> EMPTY_CELL = () -> Cell.EMPTY;

    @SuppressFBWarnings
    @Override
    public Cell[][] generate(int height, int width) {
        Cell[][] res = new Cell[height][width];
        MatrixUtils.setAll(res, EMPTY_CELL);
        return res;
    }
}
