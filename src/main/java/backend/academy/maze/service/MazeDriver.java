package backend.academy.maze.service;

import backend.academy.maze.model.Maze;
import backend.academy.maze.model.Node;
import backend.academy.maze.service.generator.CellsType;
import backend.academy.maze.service.generator.MazeGenerator;
import backend.academy.maze.service.generator.MazePathType;
import backend.academy.maze.service.solver.MazeSolver;
import backend.academy.maze.service.solver.SolverType;
import backend.academy.maze.service.ui.MazeGraphicalDriver;
import backend.academy.maze.service.ui.MazeTextDriver;
import backend.academy.maze.service.ui.UserConnector;
import backend.academy.maze.util.QuitResponse;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.io.IOException;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor
public class MazeDriver {
    private final UserConnector conn;

    private static final String QUIT_WORD = "stop";
    private static final String SIZE_PAIR_ERR =
        "Incorrect input. Print sizes in format '<height> <width>'. Both values must be positive.";
    private static final String PATH_ERR =
        "Incorrect input. Print coordinates in format '<row1> <col1> <row2> <col2>'.";
    private static final Predicate<String> SIZE_PAIR_ANS = isFixedAmount(2);
    private static final Predicate<String> PATH_ANS = isFixedAmount(4);
    private static final Function<String, QuitResponse<SizePair>> TO_SIZE_PAIR = getTFromInts(
        list -> new SizePair(list.getInt(0), list.getInt(1)),
        list -> list.getInt(0) > 0 && list.getInt(1) > 0
    );

    public void start() {
        AnswerHandler handler = new AnswerHandler();
        try {
            MazeGenerator gen = new MazeGenerator(
                handler.handle(conn.getChosen("cells type", Arrays.asList(CellsType.values()))),
                handler.handle(conn.getChosen("maze path generator", Arrays.asList(MazePathType.values())))
            );
            MazeSolver solver = handler.handle(conn.getChosen("solver type", Arrays.asList(SolverType.values())));
            makeMazes(gen, solver);
        } catch (IOException e) {
            throw new IllegalStateException("Can't use input/output", e);
        }
    }

    private void makeMazes(MazeGenerator gen, MazeSolver solver) throws IOException {
        while (true) {
            postQuitAnsForm("Print height and width of maze you want to generate in format '<height> <width>'.");
            QuitResponse<SizePair> sizes = getSizes();
            if (sizes.isQuit()) {
                break;
            }
            makeSolutions(solver, gen.generateMaze(sizes.response().height(), sizes.response().width()));
        }
    }

    private void makeSolutions(MazeSolver solver, Maze maze) throws IOException {
        MazeGraphicalDriver driver = new MazeTextDriver(conn);

        driver.print(maze);
        while (true) {
            postQuitAnsForm("Print cells between which you want to find path in format '<row1> <col1> <row2> <col2>'."
                    + getMazeRestriction(maze));
            QuitResponse<PathPair> pathPair = getPathQuery(maze);
            if (pathPair.isQuit()) {
                break;
            }
            maze.clearPath();
            conn.post(solver.findPath(maze, pathPair.response().start(), pathPair.response().end()));
            driver.print(maze, pathPair.response().start(), pathPair.response().end());
        }
    }

    private QuitResponse<SizePair> getSizes() throws IOException {
        return conn.getAns(TO_SIZE_PAIR, SIZE_PAIR_ERR, SIZE_PAIR_ANS);
    }

    @SuppressWarnings("MagicNumber")
    private QuitResponse<PathPair> getPathQuery(Maze maze) throws IOException {
        return conn.getAns(
            getTFromInts(
                list -> new PathPair(
                    new Node(list.getInt(0), list.getInt(1)),
                    new Node(list.getInt(2), list.getInt(3))
                ),
                list -> maze.checkCell(list.getInt(0), list.getInt(1))
                        && maze.checkCell(list.getInt(2), list.getInt(3))
            ),
            PATH_ERR + getMazeRestriction(maze),
            PATH_ANS
        );
    }

    private void postQuitAnsForm(String ansForm) throws IOException {
        conn.post(ansForm);
        conn.post(String.format("Or print '%s' to stop.", QUIT_WORD));
    }

    @SuppressWarnings("WhitespaceAround")
    private static <T> Function<String, QuitResponse<T>> getTFromInts(
        Function<IntArrayList, T> mapper,
        Predicate<IntArrayList> checker
    ) {
        return ans -> {
            if (QUIT_WORD.equals(ans)) {
                return QuitResponse.quit();
            }
            String[] tokens = StringUtils.split(ans);
            try {
                IntArrayList intList = IntArrayList.toList(Arrays.stream(tokens).mapToInt(Integer::parseInt));
                if (checker.test(intList)) {
                    return QuitResponse.of(mapper.apply(intList));
                }
            } catch (NumberFormatException e) {}
            return null;
        };
    }

    private static String getMazeRestriction(Maze maze) {
        return String.format("Coordinates must be in format [0, %d) × [0, %d).", maze.height(), maze.width());
    }

    private static Predicate<String> isFixedAmount(int amount) {
        return str -> QUIT_WORD.equals(str) || StringUtils.split(str).length == amount;
    }

    private record SizePair(int height, int width) {}

    private record PathPair(Node start, Node end) {}
}
