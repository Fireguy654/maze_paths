package backend.academy.maze.service.ui;

import backend.academy.maze.model.Cell;
import backend.academy.maze.model.Maze;
import backend.academy.maze.model.Node;
import backend.academy.maze.model.Wall;
import java.io.IOException;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MazeTextDriver implements MazeGraphicalDriver {
    private final UserConnector connector;

    private static final String RESET_COLOUR = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String SEP = BLUE + "┼" + RESET_COLOUR;

    private static final Function<Wall, String> HOR_WALL_MAPPER = getWallMapper(true);
    private static final Function<Wall, String> VERT_WALL_MAPPER = getWallMapper(false);

    private static final IntFunction<Wall> EXIST = ignored -> Wall.EXIST;

    @Override
    public void print(Maze maze) throws IOException {
        print(maze, node -> maze.getCell(node).toString());
    }

    @Override
    public void print(Maze maze, Node startNode, Node endNode) throws IOException {
        print(maze, node -> {
            Cell cell = maze.getCell(node);
            if (node.equals(startNode)) {
                return RED + cell + RESET_COLOUR;
            }
            if (node.equals(endNode)) {
                return GREEN + cell + RESET_COLOUR;
            }
            return cell.toString();
        });
    }

    private void print(Maze maze, Function<Node, String> cellMapper) throws IOException {
        printWallRow(maze);
        for (int row = 0; row < maze.height(); ++row) {
            connector.post(collectCellRow(maze, row, cellMapper));
            if (row + 1 != maze.height()) {
                printWallRow(maze, row);
            }
        }
        printWallRow(maze);
    }

    private String collectCellRow(Maze maze, int row, Function<Node, String> cellMapper) {
        StringBuilder rowBuilder = new StringBuilder();
        rowBuilder.append(VERT_WALL_MAPPER.apply(Wall.EXIST));
        for (int col = 0; col < maze.width(); ++col) {
            rowBuilder.append(cellMapper.apply(new Node(row, col)));
            if (col + 1 != maze.width()) {
                rowBuilder.append(VERT_WALL_MAPPER.apply(maze.getWall(row, col, row, col + 1)));
            }
        }
        rowBuilder.append(VERT_WALL_MAPPER.apply(Wall.EXIST));
        return rowBuilder.toString();
    }

    private void printWallRow(Maze maze) throws IOException {
        printWallRow(IntStream.range(0, maze.width()).mapToObj(EXIST));
    }

    private void printWallRow(Maze maze, int row) throws IOException {
        printWallRow(IntStream.range(0, maze.width()).mapToObj(col -> maze.getWall(row, col, row + 1, col)));
    }

    private void printWallRow(Stream<Wall> walls) throws IOException {
        connector.post(walls.map(HOR_WALL_MAPPER).collect(Collectors.joining(SEP, SEP, SEP)));
    }

    private static Function<Wall, String> getWallMapper(boolean isHor) {
        return wall -> wall == Wall.PATH
                        ? YELLOW + wall.getSign(isHor) + RESET_COLOUR
                        : BLUE + wall.getSign(isHor) + RESET_COLOUR;
    }
}
