package backend.academy.maze.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class QuitResponseTest {
    @Test
    @DisplayName("Correctness of check if quit response is quit")
    void isQuit() {
        QuitResponse<Integer> a = QuitResponse.of(1);
        QuitResponse<Integer> b = QuitResponse.quit();

        boolean resA = a.isQuit();
        boolean resB = b.isQuit();

        assertThat(resA).isFalse();
        assertThat(resB).isTrue();
    }

    @Test
    @DisplayName("Correctness of containing response")
    void response() {
        QuitResponse<Integer> a = QuitResponse.of(1);
        QuitResponse<Integer> b = QuitResponse.of(null);

        Integer resA = a.response();
        Integer resB = b.response();

        assertThat(resA).isEqualTo(1);
        assertThat(resB).isNull();
    }
}
