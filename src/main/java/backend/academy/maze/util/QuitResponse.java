package backend.academy.maze.util;

import lombok.Getter;

@Getter public class QuitResponse<T> {
    private final T response;

    private QuitResponse(T response) {
        this.response = response;
    }

    public boolean isQuit() {
        return response == null;
    }

    public static <T> QuitResponse<T> of(T response) {
        return new QuitResponse<>(response);
    }

    public static <T> QuitResponse<T> quit() {
        return of(null);
    }
}
