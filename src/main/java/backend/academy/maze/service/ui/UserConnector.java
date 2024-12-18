package backend.academy.maze.service.ui;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface UserConnector extends Closeable {
    <T> T getChosen(String name, List<T> choices) throws IOException;

    <T> T getChosen(String name, List<T> choices, Supplier<T> defaultElem) throws IOException;

    <T> T getAns(Function<String, T> convert, String mistakeMessage, Predicate<String> isCorrect) throws IOException;

    <T> T getAns(Function<String, T> convert, String mistakeMessage, Predicate<String> isCorrect,
                 Supplier<T> defaultAns) throws IOException;

    void ignoreInput() throws IOException;

    void post(String message) throws IOException;
}
