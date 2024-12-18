package backend.academy.maze.service.ui;

import com.google.common.base.Predicates;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.function.Function;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class TextIOConnectorTest {
    @Test
    @DisplayName("The correctness of choosing in case of right input")
    void getGoodChosen() {
        assertThatCode(() -> {
            StringReader reader = new StringReader("2");
            StringWriter writer = new StringWriter();
            List<Integer> options = List.of(10, 20, 30);
            TextIOConnector conn = getStringConnector(reader, writer);

            Integer res = conn.getChosen("number", options);

            assertThat(res).isEqualTo(20);
            assertThat(writer.toString()).contains(options.stream().map(num -> Integer.toString(num)).toList());
        }).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("The correctness of default value getting in choosing")
    void getDefaultChoosing() {
        assertThatCode(() -> {
            StringReader reader = new StringReader(System.lineSeparator());
            StringWriter writer = new StringWriter();
            List<Integer> options = List.of(10, 20, 30);
            TextIOConnector conn = getStringConnector(reader, writer);

            Integer res = conn.getChosen("number", options, () -> 40);

            assertThat(res).isEqualTo(40);
            assertThat(writer.toString()).contains(options.stream().map(num -> Integer.toString(num)).toList());
        }).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("The correctness of incorrect input behaviour in choosing")
    void workWithIncorrectInputChoosing() {
        assertThatCode(() -> {
            StringReader reader = new StringReader("""
                    fdasfasd
                    90
                    -1
                    3
                    """);
            StringWriter writer = new StringWriter();
            List<Integer> options = List.of(10, 20, 30);
            TextIOConnector conn = getStringConnector(reader, writer);

            Integer res = conn.getChosen("number", options);

            assertThat(res).isEqualTo(30);
            assertThat(writer.toString()).contains(options.stream().map(num -> Integer.toString(num)).toList());
            assertThat(writer.toString().split(System.lineSeparator())).areExactly(3, INCORRECT_INPUT);
        }).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("The correctness of answering in case of right input")
    void getGoodAnswer() {
        assertThatCode(() -> {
            StringReader reader = new StringReader("yes");
            StringWriter writer = new StringWriter();
            TextIOConnector conn = getStringConnector(reader, writer);

            boolean res = conn.getAns(
                    "yes"::equalsIgnoreCase,
                    "Print yes or no",
                    (ans) -> "yes".equalsIgnoreCase(ans) || "no".equalsIgnoreCase(ans)
            );

            assertThat(res).isTrue();
        }).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("The correctness of default value getting in answering")
    void getDefaultAnswering() {
        assertThatCode(() -> {
            StringReader reader = new StringReader(System.lineSeparator());
            StringWriter writer = new StringWriter();
            TextIOConnector conn = getStringConnector(reader, writer);

            boolean res = conn.getAns(
                    "yes"::equalsIgnoreCase,
                    "Print yes or no",
                    (ans) -> "yes".equalsIgnoreCase(ans) || "no".equalsIgnoreCase(ans),
                    () -> false
            );

            assertThat(res).isFalse();
        }).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("The correctness of incorrect input behaviour in answering")
    void workWithIncorrectInputAnswering() {
        assertThatCode(() -> {
            StringReader reader = new StringReader("""
                    fdasfasd
                    90
                    -1
                    no
                    """);
            StringWriter writer = new StringWriter();
            TextIOConnector conn = getStringConnector(reader, writer);

            boolean res = conn.getAns(
                    "yes"::equalsIgnoreCase,
                    "Incorrect input. Print yes or no",
                    (ans) -> "yes".equalsIgnoreCase(ans) || "no".equalsIgnoreCase(ans),
                    () -> false
            );

            assertThat(res).isFalse();
            assertThat(writer.toString().split(System.lineSeparator())).areExactly(3, INCORRECT_INPUT);
        }).doesNotThrowAnyException();
    }


    @Test
    @DisplayName("The correctness of ignoring next line")
    void ignoreInput() {
        assertThatCode(() -> {
            StringReader reader = new StringReader("""
                    Some not useful information
                    Some useful information
                    """);
            StringWriter writer = new StringWriter();
            TextIOConnector conn = getStringConnector(reader, writer);

            conn.ignoreInput();
            String res = conn.getAns(Function.identity(), "No message", Predicates.alwaysTrue());

            assertThat(res).isEqualTo("Some useful information");
        }).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("The correctness of posting")
    void post() {
        assertThatCode(() -> {
            StringWriter writer = new StringWriter();
            TextIOConnector conn = getStringConnector(writer);

            conn.post("something");

            assertThat(writer.toString()).isEqualTo("something" + System.lineSeparator());
        }).doesNotThrowAnyException();
    }

    private static final Condition<String> INCORRECT_INPUT = new Condition<>(
            (String str) -> str.contains("Incorrect input"),
            "Incorrect input"
    );

    private TextIOConnector getStringConnector(StringWriter writer) {
        return getStringConnector(new StringReader(""), writer);
    }

    private TextIOConnector getStringConnector(StringReader reader, StringWriter writer) {
        return new TextIOConnector(new BufferedReader(reader), new BufferedWriter(writer));
    }
}
