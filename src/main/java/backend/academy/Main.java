package backend.academy;

import backend.academy.maze.service.MazeDriver;
import backend.academy.maze.service.ui.TextIOConnector;
import backend.academy.maze.service.ui.UserConnector;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import lombok.experimental.UtilityClass;

/**
 * Starts a maze visualizer, which can generate random mazes and find and show paths in them.
 * After starting maze visualizer follow simple instructions:
 * <ul>
 * <li>If you see a choosing box, send your choice by writing its index</li>
 * <li>If visualizer asked you for parameters in specific form,
 * write integers in this form for every {@code <>} you see</li>
 * </ul>
 * Visualizer will ask for:
 * <ul>
 * <li>Types of cells: are they the same(PLAIN: {@link backend.academy.maze.service.generator.EmptyCellMatrixGenerator})
 * or different(WITH_EFFECTS: {@link backend.academy.maze.service.generator.RandomCellMatrixGenerator})</li>
 * <li>Types of generators: random walls(RANDOM: {@link backend.academy.maze.service.generator.RandomPathGenerator}),
 * ideal by kraskal algorithm({@link backend.academy.maze.service.generator.KraskalPathGenerator})
 * or by recursive back tracking algorithm{@link backend.academy.maze.service.generator.RecBTPathGenerator}</li>
 * <li>Types of solvers: cost-dependent algorithm(WEIGHTED: {@link backend.academy.maze.service.solver.MazeDeikstra}
 * or length-dependent algorithm(BY_LENGTH: {@link backend.academy.maze.service.solver.MazeBFS})</li>
 * <li>Sizes of the maze: for example '10 15' will create a maze consisting 10 cells in height and 15 in width</li>
 * <li>Cells between which you want to find a path: for example '0 0 9 19' will find path between upper left corner
 * ans bottom-right corner of the maze with sizes '10 20'</li>
 * </ul>
 */
@UtilityClass
public class Main {
    public static void main(String[] args) {
        try (UserConnector conn = new TextIOConnector(
            new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8)),
            new BufferedWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8))
        )) {
            MazeDriver mazeDriver = new MazeDriver(conn);
            mazeDriver.start();
        } catch (IOException e) {
            System.err.println("Can't close the connection: " + e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        }
    }
}
