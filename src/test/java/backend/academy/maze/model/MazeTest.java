package backend.academy.maze.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.function.Predicate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MazeTest {
    @Test
    @DisplayName("Correctness of setting a wall")
    void setWall() {
        Maze maze = getSimpleMaze(3, 3, Cell.EMPTY);
        Maze mazeForCoords = getSimpleMaze(3, 3, Cell.EMPTY);
        Node first = new Node(1, 1);
        Node second = new Node(2, 1);

        maze.setWall(first, second, Wall.PATH);
        mazeForCoords.setWall(first.row(), first.col(), second.row(), second.col(), Wall.PATH);

        assertThat(maze.getWall(first, second)).isEqualTo(Wall.PATH);
        assertThat(mazeForCoords.getWall(first.row(), first.col(), second.row(), second.col())).isEqualTo(Wall.PATH);
    }

    @Test
    @DisplayName("Correctness of setting all walls")
    void setWalls() {
        Maze maze = getSimpleMaze(3, 3, Cell.EMPTY);

        maze.setWalls(() -> Wall.ABSENT);

        assertAllWalls(maze, wall -> wall == Wall.ABSENT);
    }

    @Test
    @DisplayName("Correctness of mapping all walls")
    void mapWalls() {
        Maze maze = getSimpleMaze(3, 3, Cell.EMPTY);

        maze.mapWalls(wall -> wall == Wall.EXIST ? Wall.ABSENT : Wall.PATH);

        assertAllWalls(maze, wall -> wall == Wall.ABSENT);
    }

    @Test
    @DisplayName("Correctness of clearing all path walls")
    void clearPath() {
        Maze maze = getSimpleMaze(3, 3, Cell.EMPTY);
        maze.setWall(1, 1, 1, 2, Wall.PATH);
        maze.setWall(0, 0, 1, 0, Wall.PATH);

        maze.clearPath();

        assertAllWalls(maze, wall -> wall != Wall.PATH);
    }

    @Test
    @DisplayName("Correctness of checking if node is a cell")
    void checkCell() {
        Maze maze = getSimpleMaze(3, 3, Cell.EMPTY);

        boolean res1 = maze.checkCell(new Node(2, 2));
        boolean coordRes1 = maze.checkCell(2, 2);
        boolean res2 = maze.checkCell(new Node(3, 2));
        boolean coordRes2 = maze.checkCell(3, 2);

        assertThat(res1).isTrue();
        assertThat(coordRes1).isTrue();
        assertThat(res2).isFalse();
        assertThat(coordRes2).isFalse();
    }

    @Test
    @DisplayName("Throwing IllegalStateException of accessing wrong coordinates")
    void checkIncorrectCoordinates() {
        Maze maze = getSimpleMaze(3, 3, Cell.EMPTY);

        assertThatThrownBy(() -> maze.getCell(3, 2)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> maze.getWall(2, 2, 3, 2)).isInstanceOf(IllegalArgumentException.class);
    }

    private static Maze getSimpleMaze(int height, int width, Cell cell) {
        Cell[][] cells = new Cell[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                cells[i][j] = cell;
            }
        }
        return new Maze(height, width, cells);
    }

    private static void assertAllWalls(Maze maze, Predicate<Wall> predicate) {
        for (int i = 0; i < maze.height(); ++i) {
            for (int j = 0; j < maze.width(); ++j) {
                if (i < maze.height() - 1 ) {
                    assertThat(maze.getWall(i, j, i + 1, j))
                        .as("Checking node between (%d, %d) and (%d, %d)", i, j, i + 1, j)
                        .matches(predicate);
                }
                if (j < maze.width() - 1 ) {
                    assertThat(maze.getWall(i, j, i, j + 1))
                        .as("Checking node between (%d, %d) and (%d, %d)", i, j, i, j + 1)
                        .matches(predicate);
                }
            }
        }
    }
}
