package backend.academy.maze.model;

public enum Wall {
    EXIST,
    ABSENT,
    PATH;

    public String getSign(boolean isHorizontal) {
        return switch (this) {
            case EXIST -> isHorizontal ? "─" : "│";
            case ABSENT -> " ";
            case PATH -> isHorizontal ? "│" : "─";
        };
    }
}
