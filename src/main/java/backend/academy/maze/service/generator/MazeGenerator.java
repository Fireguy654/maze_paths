package backend.academy.maze.service.generator;

import backend.academy.maze.model.Maze;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MazeGenerator {
    private final CellMatrixGenerator cellGenerator;
    private final PathGenerator pathGenerator;

    public Maze generateMaze(int height, int width) {
        Maze cells = new Maze(height, width, cellGenerator.generate(height, width));
        pathGenerator.generatePath(cells);
        return cells;
    }
}
