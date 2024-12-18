package backend.academy.maze.service.generator;

import backend.academy.maze.model.Cell;

public interface CellMatrixGenerator {
    Cell[][] generate(int height, int width);
}
