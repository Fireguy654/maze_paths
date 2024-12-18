package backend.academy.maze.service.generator;

import backend.academy.maze.model.Maze;
import backend.academy.maze.model.Wall;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;

/**
 * Generates a maze in given template maze(where every wall exists).
 * It randomizes the absence of every wall.
 */
public class RandomPathGenerator implements PathGenerator {
    private static final RandomGenerator RANDOM = RandomFabric.getRandom();
    private static final Supplier<Wall> RANDOM_WALL = () -> RANDOM.nextBoolean() ? Wall.EXIST : Wall.ABSENT;

    @Override
    public void generatePath(Maze field) {
        field.setWalls(RANDOM_WALL);
    }
}
