package backend.academy.maze.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Cell {
    TRAMPOLINE(1, "ʘ"),
    EMPTY(2, "·"),
    SAND(3, "#"),
    SWAMP(4, "¤");

    @Getter
    private final int speed;
    private final String sign;

    @Override
    public String toString() {
        return sign;
    }
}
