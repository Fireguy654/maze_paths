package backend.academy.maze.service;

import backend.academy.maze.service.generator.CellMatrixGenerator;
import backend.academy.maze.service.generator.CellsType;
import backend.academy.maze.service.generator.EmptyCellMatrixGenerator;
import backend.academy.maze.service.generator.KraskalPathGenerator;
import backend.academy.maze.service.generator.MazePathType;
import backend.academy.maze.service.generator.PathGenerator;
import backend.academy.maze.service.generator.RandomCellMatrixGenerator;
import backend.academy.maze.service.generator.RandomPathGenerator;
import backend.academy.maze.service.generator.RecBTPathGenerator;
import backend.academy.maze.service.solver.MazeBFS;
import backend.academy.maze.service.solver.MazeDeikstra;
import backend.academy.maze.service.solver.MazeSolver;
import backend.academy.maze.service.solver.SolverType;

public class AnswerHandler {
    public CellMatrixGenerator handle(CellsType cellsType) {
        return switch (cellsType) {
            case PLAIN -> new EmptyCellMatrixGenerator();
            case WITH_EFFECTS -> new RandomCellMatrixGenerator();
        };
    }

    public PathGenerator handle(MazePathType mazePathType) {
        return switch (mazePathType) {
            case RANDOM -> new RandomPathGenerator();
            case IDEAL_KRASKAL -> new KraskalPathGenerator();
            case IDEAL_REC_BACK_TRACKING -> new RecBTPathGenerator();
        };
    }

    public MazeSolver handle(SolverType solverType) {
        return switch (solverType) {
            case EFFECT_WEIGHTED -> new MazeDeikstra();
            case BY_LENGTH -> new MazeBFS();
        };
    }
}
