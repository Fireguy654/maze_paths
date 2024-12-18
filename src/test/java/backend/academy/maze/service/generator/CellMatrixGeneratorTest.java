package backend.academy.maze.service.generator;

import backend.academy.maze.model.Cell;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class CellMatrixGeneratorTest {
    @Test
    @DisplayName("Correctness of empty cell matrix generator")
    void generateEmpty() {
        EmptyCellMatrixGenerator generator = new EmptyCellMatrixGenerator();

        Cell[][] res = generator.generate(3, 4);

        assertThat(res).hasDimensions(3, 4);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                assertThat(res[i][j]).isEqualTo(Cell.EMPTY);
            }
        }
    }

    @Test
    @DisplayName("Correctness of random cell matrix generator")
    void generateRandom() {
        RandomCellMatrixGenerator generator = new RandomCellMatrixGenerator();

        Cell[][] res = generator.generate(3, 4);

        assertThat(res).hasDimensions(3, 4);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                assertThat(res[i][j]).isNotNull();
            }
        }
    }
}
