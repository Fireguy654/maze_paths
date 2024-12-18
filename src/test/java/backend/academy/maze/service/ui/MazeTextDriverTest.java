package backend.academy.maze.service.ui;

import backend.academy.maze.model.Cell;
import backend.academy.maze.model.Maze;
import backend.academy.maze.model.Node;
import backend.academy.maze.model.Wall;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.StringReader;
import java.io.StringWriter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class MazeTextDriverTest {
    @Test
    @DisplayName("Correctness of simple displaying")
    void print() {
        assertThatCode(() -> {
            StringWriter writer = new StringWriter();
            MazeTextDriver driver = new MazeTextDriver(getStringConnector(writer));
            Maze maze = new Maze(2, 2,
                new Cell[][]{{Cell.TRAMPOLINE, Cell.SAND}, {Cell.EMPTY, Cell.SWAMP}});
            maze.setWall(0, 0, 0, 1, Wall.ABSENT);
            maze.setWall(0, 1, 1, 1, Wall.ABSENT);
            maze.setWall(1, 1, 1, 0, Wall.ABSENT);
            String answer = """
                \u001B[34m┼\u001B[0m\u001B[34m─\u001B[0m\u001B[34m┼\u001B[0m\u001B[34m─\u001B[0m\u001B[34m┼\u001B[0m
                \u001B[34m│\u001B[0mʘ\u001B[34m \u001B[0m#\u001B[34m│\u001B[0m
                \u001B[34m┼\u001B[0m\u001B[34m─\u001B[0m\u001B[34m┼\u001B[0m\u001B[34m \u001B[0m\u001B[34m┼\u001B[0m
                \u001B[34m│\u001B[0m·\u001B[34m \u001B[0m¤\u001B[34m│\u001B[0m
                \u001B[34m┼\u001B[0m\u001B[34m─\u001B[0m\u001B[34m┼\u001B[0m\u001B[34m─\u001B[0m\u001B[34m┼\u001B[0m
                """;

            driver.print(maze);

            assertThat(writer.toString()).isEqualTo(answer);
        }).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Correctness of path displaying")
    void printWithPath() {
        assertThatCode(() -> {
            StringWriter writer = new StringWriter();
            MazeTextDriver driver = new MazeTextDriver(getStringConnector(writer));
            Maze maze = new Maze(2, 2,
                new Cell[][]{{Cell.TRAMPOLINE, Cell.SAND}, {Cell.EMPTY, Cell.SWAMP}});
            maze.setWall(0, 0, 0, 1, Wall.PATH);
            maze.setWall(0, 1, 1, 1, Wall.PATH);
            maze.setWall(1, 1, 1, 0, Wall.ABSENT);
            String answer = """
                \u001B[34m┼\u001B[0m\u001B[34m─\u001B[0m\u001B[34m┼\u001B[0m\u001B[34m─\u001B[0m\u001B[34m┼\u001B[0m
                \u001B[34m│\u001B[0m\u001B[31mʘ\u001B[0m\u001B[33m─\u001B[0m#\u001B[34m│\u001B[0m
                \u001B[34m┼\u001B[0m\u001B[34m─\u001B[0m\u001B[34m┼\u001B[0m\u001B[33m│\u001B[0m\u001B[34m┼\u001B[0m
                \u001B[34m│\u001B[0m·\u001B[34m \u001B[0m\u001B[32m¤\u001B[0m\u001B[34m│\u001B[0m
                \u001B[34m┼\u001B[0m\u001B[34m─\u001B[0m\u001B[34m┼\u001B[0m\u001B[34m─\u001B[0m\u001B[34m┼\u001B[0m
                """;

            driver.print(maze, new Node(0, 0), new Node(1, 1));

            assertThat(writer.toString()).isEqualTo(answer);
        }).doesNotThrowAnyException();
    }

    private TextIOConnector getStringConnector(StringWriter writer) {
        return new TextIOConnector(new BufferedReader(new StringReader("")), new BufferedWriter(writer));
    }
}
