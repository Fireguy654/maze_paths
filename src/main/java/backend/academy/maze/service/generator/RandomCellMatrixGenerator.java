package backend.academy.maze.service.generator;

import backend.academy.maze.model.Cell;
import backend.academy.maze.util.MatrixUtils;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.random.RandomGenerator;

/**
 * Generates matrix of cells with given height and width.
 * It randomly chooses the type of cell from {@link Cell} enumeration.
 */
public class RandomCellMatrixGenerator implements CellMatrixGenerator {
    private static final RandomGenerator RANDOM = RandomFabric.getRandom();

    @SuppressFBWarnings
    @Override
    public Cell[][] generate(int height, int width) {
        Cell[][] cellArray = new Cell[height][width];
        MatrixUtils.setAll(cellArray, RandomCellMatrixGenerator::randomCell);
        return cellArray;
    }

    private static Cell randomCell() {
        return Cell.values()[RANDOM.nextInt(Cell.values().length)];
    }
}
