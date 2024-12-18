package backend.academy.maze.service.ui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class TextIOConnector implements UserConnector {
    private final BufferedReader reader;
    private final BufferedWriter writer;

    @Override
    public <T> T getChosen(String name, List<T> choices) throws IOException {
        return getChosen(name, choices, TextIOConnector::nullSupplier);
    }

    @Override
    public <T> T getChosen(String name, List<T> choices, Supplier<T> defaultElem) throws IOException {
        println(String.format("Choose the '%s':", name));
        for (int i = 0; i < choices.size(); i++) {
            println(String.format("%d. %s", i + 1, choices.get(i)));
        }
        println("Type the number of your choice.");
        int ans = getCheckedAnswer(
                (num) -> {
                    try {
                        int res = Integer.parseInt(num);
                        return (res < 0 || res > choices.size()) ? null : res;
                    } catch (NumberFormatException e) {
                        return null;
                    }
                },
                String.format("Incorrect input. Type the number between %d and %d", 1, choices.size()),
                (str) -> !str.isBlank(),
                ZERO
        );
        return ans == 0 ? defaultElem.get() : choices.get(ans - 1);
    }

    @Override
    public <T> T getAns(
            Function<String, T> convert,
            String mistakeMessage,
            Predicate<String> isCorrect
    ) throws IOException {
        return getAns(convert, mistakeMessage, isCorrect, TextIOConnector::nullSupplier);
    }

    @Override
    public <T> T getAns(
            Function<String, T> convert,
            String mistakeMessage,
            Predicate<String> isCorrect,
            Supplier<T> defaultAns
    ) throws IOException {
        return getCheckedAnswer(convert, mistakeMessage, isCorrect, defaultAns);
    }

    @Override
    public void ignoreInput() throws IOException {
        reader.readLine();
    }

    @Override
    public void post(String message) throws IOException {
        println(message);
    }

    @Override
    public void close() throws IOException {
        writer.close();
        reader.close();
    }

    private void println(String string) throws IOException {
        writer.write(string);
        writer.newLine();
        writer.flush();
    }

    private <T> T getCheckedAnswer(
            Function<String, T> convert, String mistakeMessage, Predicate<String> isCorrect,
            Supplier<T> defaultAnswer
    ) throws IOException {
        T res = attempt(isCorrect, convert, defaultAnswer);
        while (res == null) {
            println(mistakeMessage);
            res = attempt(isCorrect, convert, defaultAnswer);
        }
        return res;
    }

    @Nullable
    private <T> T attempt(
            Predicate<String> isCorrect,
            Function<String, T> convert,
            Supplier<T> defaultAnswer
    ) throws IOException {
        String ans = reader.readLine();
        if (isCorrect.test(ans)) {
            return convert.apply(ans);
        }
        return ans.isBlank() ? defaultAnswer.get() : null;
    }

    private static final Supplier<Integer> ZERO = () -> 0;

    private static <T> T nullSupplier() {
        return null;
    }
}

