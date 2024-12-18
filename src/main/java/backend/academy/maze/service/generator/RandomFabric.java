package backend.academy.maze.service.generator;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.security.SecureRandom;
import java.util.random.RandomGenerator;

public class RandomFabric {
    private static final RandomGenerator RANDOM = new SecureRandom();

    private RandomFabric() {}

    @SuppressFBWarnings
    public static RandomGenerator getRandom() {
        return RANDOM;
    }
}
