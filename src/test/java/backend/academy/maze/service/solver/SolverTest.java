package backend.academy.maze.service.solver;

import backend.academy.maze.model.Cell;
import backend.academy.maze.model.Maze;
import backend.academy.maze.model.Node;
import backend.academy.maze.model.Wall;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.FieldSource;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

class SolverTest {
    private static final MazeSolver BFS_SOLVER = new MazeBFS();
    private static final MazeSolver DEIKSTRA_SOLVER = new MazeDeikstra();
    private static final List<MazeSolver> SOLVERS = List.of(BFS_SOLVER, DEIKSTRA_SOLVER);

    @Test
    @DisplayName("Correctness of finding path using bfs")
    void findPathBfs() {
        Maze maze = new Maze(3, 3, new Cell[][]{
            {Cell.EMPTY, Cell.SWAMP, Cell.SWAMP},
            {Cell.TRAMPOLINE, Cell.EMPTY, Cell.EMPTY},
            {Cell.TRAMPOLINE, Cell.TRAMPOLINE, Cell.TRAMPOLINE}
        });
        List<Node> up = List.of(
            new Node(0, 0),
            new Node(0, 1),
            new Node(0, 2),
            new Node(1, 2),
            new Node(1, 1)
        );
        setPath(maze, up.getFirst(), up.subList(1, up.size()));
        List<Node> down = List.of(
            new Node(0, 0),
            new Node(1, 0),
            new Node(2, 0),
            new Node(2, 1),
            new Node(2, 2),
            new Node(1, 2),
            new Node(1, 1));
        setPath(maze, down.getFirst(), down.subList(1, down.size()));

        String res = BFS_SOLVER.findPath(maze, up.getFirst(), up.getLast());

        assertThat(res).isEqualTo("Found path with total length %d", up.size());
        checkPath(maze, up.getFirst(), up.subList(1, up.size()));
    }

    @Test
    @DisplayName("Correctness of finding path using deikstra")
    void findPathDeikstra() {
        Maze maze = new Maze(3, 3, new Cell[][]{
            {Cell.EMPTY, Cell.SWAMP, Cell.SWAMP},
            {Cell.TRAMPOLINE, Cell.EMPTY, Cell.EMPTY},
            {Cell.TRAMPOLINE, Cell.TRAMPOLINE, Cell.TRAMPOLINE}
        });
        List<Node> up = List.of(
            new Node(0, 0),
            new Node(0, 1),
            new Node(0, 2),
            new Node(1, 2),
            new Node(1, 1)
        );
        setPath(maze, up.getFirst(), up.subList(1, up.size()));
        List<Node> down = List.of(
            new Node(0, 0),
            new Node(1, 0),
            new Node(2, 0),
            new Node(2, 1),
            new Node(2, 2),
            new Node(1, 2),
            new Node(1, 1));
        setPath(maze, down.getFirst(), down.subList(1, down.size()));

        String res = DEIKSTRA_SOLVER.findPath(maze, down.getFirst(), down.getLast());

        assertThat(res).isEqualTo("Found path with total cost %d",
            Cell.EMPTY.speed() * 3 + Cell.TRAMPOLINE.speed() * 4);
        checkPath(maze, down.getFirst(), down.subList(1, down.size()));
    }

    @ParameterizedTest
    @DisplayName("Correctness of not finding path")
    @FieldSource("SOLVERS")
    void notFindPath(MazeSolver solver) {
        Maze maze = new Maze(2, 2, new Cell[][]{{Cell.EMPTY, Cell.EMPTY}, {Cell.EMPTY, Cell.EMPTY}});

        String ans = solver.findPath(maze, 0, 0, 1, 1);

        assertThat(ans).isEqualTo(MazeSolver.NOT_FOUND_RESPONSE);
    }

    private void setPath(Maze maze, Node start, List<Node> nodes) {
        for (Node node : nodes) {
            maze.setWall(start, node, Wall.ABSENT);
            start = node;
        }
    }

    private void checkPath(Maze maze, Node start, List<Node> nodes) {
        SoftAssertions.assertSoftly(softly -> {
            Node cur = start;
            for (Node node : nodes) {
                softly.assertThat(maze.getWall(cur, node)).as("Expected path between %s and %s", start, node).isEqualTo(Wall.PATH);
                cur = node;
            }
        });
    }
}
