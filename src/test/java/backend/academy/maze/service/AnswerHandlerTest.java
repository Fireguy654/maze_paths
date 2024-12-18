package backend.academy.maze.service;

import backend.academy.maze.service.generator.*;
import backend.academy.maze.service.solver.MazeBFS;
import backend.academy.maze.service.solver.MazeDeikstra;
import backend.academy.maze.service.solver.MazeSolver;
import backend.academy.maze.service.solver.SolverType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.stream.Stream;

class AnswerHandlerTest {
    private static AnswerHandler handler;

    @BeforeAll
    static void beforeAll() {
        handler = new AnswerHandler();
    }

    private static Stream<Arguments> provideMGAnswers() {
        return Stream.of(
                Arguments.of(CellsType.PLAIN, new EmptyCellMatrixGenerator()),
                Arguments.of(CellsType.WITH_EFFECTS, new RandomCellMatrixGenerator())
        );
    }

    @ParameterizedTest
    @DisplayName("Check matrix generator answer handling")
    @MethodSource("provideMGAnswers")
    void handleMG(CellsType type, CellMatrixGenerator generator) {
        CellMatrixGenerator ans = handler.handle(type);

        assertThat(ans).isExactlyInstanceOf(generator.getClass());
    }

    private static Stream<Arguments> providePGAnswers() {
        return Stream.of(
                Arguments.of(MazePathType.RANDOM, new RandomPathGenerator()),
                Arguments.of(MazePathType.IDEAL_KRASKAL, new KraskalPathGenerator()),
                Arguments.of(MazePathType.IDEAL_REC_BACK_TRACKING, new RecBTPathGenerator())
        );
    }

    @ParameterizedTest
    @DisplayName("Check path generator answer handling")
    @MethodSource("providePGAnswers")
    void handleGP(MazePathType type, PathGenerator generator) {
        PathGenerator ans = handler.handle(type);

        assertThat(ans).isExactlyInstanceOf(generator.getClass());
    }

    private static Stream<Arguments> provideSAnswers() {
        return Stream.of(
                Arguments.of(SolverType.BY_LENGTH, new MazeBFS()),
                Arguments.of(SolverType.EFFECT_WEIGHTED, new MazeDeikstra())
        );
    }

    @ParameterizedTest
    @DisplayName("Check solver generator answer handling")
    @MethodSource("provideSAnswers")
    void handleS(SolverType type, MazeSolver solver) {
        MazeSolver ans = handler.handle(type);

        assertThat(ans).isExactlyInstanceOf(solver.getClass());
    }
}
