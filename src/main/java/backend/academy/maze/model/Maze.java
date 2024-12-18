package backend.academy.maze.model;

import backend.academy.maze.util.MatrixUtils;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import lombok.Getter;

public class Maze {
    @Getter
    private final int height;
    @Getter
    private final int width;
    private final Cell[][] cells;
    private final Wall[][] verticalWalls;
    private final Wall[][] horizontalWalls;

    private static final Supplier<Wall> DEFAULT_WALL = () -> Wall.EXIST;
    private static final UnaryOperator<Wall> CLEARING_PATH = (Wall w) -> w == Wall.PATH ? Wall.ABSENT : w;

    public Maze(int height, int width, Cell[][] cells) {
        this.height = height;
        this.width = width;
        this.cells = cells;
        this.verticalWalls = new Wall[height][width - 1];
        MatrixUtils.setAll(this.verticalWalls, DEFAULT_WALL);
        this.horizontalWalls = new Wall[height - 1][width];
        MatrixUtils.setAll(this.horizontalWalls, DEFAULT_WALL);
    }

    public Cell getCell(int row, int col) {
        expectCell(row, col);
        return cells[row][col];
    }

    public Cell getCell(Node node) {
        return getCell(node.row(), node.col());
    }

    public Wall getWall(int row1, int col1, int row2, int col2) {
        WallPos res = getWallPos(row1, col1, row2, col2);
        return getWall(res);
    }

    public Wall getWall(Node first, Node second) {
        return getWall(first.row(), first.col(), second.row(), second.col());
    }

    private Wall getWall(WallPos pos) {
        return pos.isHorizontal ? horizontalWalls[pos.row][pos.col] : verticalWalls[pos.row][pos.col];
    }

    public void setWall(int row1, int col1, int row2, int col2, Wall wall) {
        WallPos res = getWallPos(row1, col1, row2, col2);
        setWall(res, wall);
    }

    public void setWall(Node first, Node second, Wall wall) {
        setWall(first.row(), first.col(), second.row(), second.col(), wall);
    }

    private void setWall(WallPos pos, Wall wall) {
        if (pos.isHorizontal) {
            horizontalWalls[pos.row][pos.col] = wall;
        } else {
            verticalWalls[pos.row][pos.col] = wall;
        }
    }

    public void setWalls(Supplier<Wall> wallSupplier) {
        MatrixUtils.setAll(this.horizontalWalls, wallSupplier);
        MatrixUtils.setAll(this.verticalWalls, wallSupplier);
    }

    public void mapWalls(UnaryOperator<Wall> wallFunction) {
        MatrixUtils.mapAll(this.horizontalWalls, wallFunction);
        MatrixUtils.mapAll(this.verticalWalls, wallFunction);
    }

    public void clearPath() {
        mapWalls(CLEARING_PATH);
    }

    public boolean checkCell(int row, int col) {
        return 0 <= row && row < height && 0 <= col && col < width;
    }

    public boolean checkCell(Node node) {
        return checkCell(node.row(), node.col());
    }

    private WallPos getWallPos(int row1, int col1, int row2, int col2) {
        expectNeighbourCells(row1, col1, row2, col2);
        return new WallPos(Math.min(row1, row2), Math.min(col1, col2), col1 == col2);
    }

    private void expectNeighbourVals(int a, int b) {
        if (Math.min(a, b) + 1 != Math.max(a, b)) {
            throw new IllegalArgumentException(String.format("%d and %d must differ by 1", a, b));
        }
    }

    private void expectCell(int row, int col) {
        if (!checkCell(row, col)) {
            throw new IllegalArgumentException(
                String.format("(%d, %d) must be in [0, %d) × [0, %d) to be valid coordinates", row, col, height, width)
            );
        }
    }

    private void expectNeighbourCells(int row1, int col1, int row2, int col2) {
        IllegalArgumentException caught = null;
        try {
            expectCell(row1, col1);
            expectCell(row2, col2);
            boolean neighbours = false;
            if (row1 == row2) {
                expectNeighbourVals(col1, col2);
                neighbours = true;
            }
            if (col1 == col2) {
                expectNeighbourVals(row1, row2);
                neighbours = true;
            }
            if (neighbours) {
                return;
            }
        } catch (IllegalArgumentException e) {
            caught = e;
        }
        throw new IllegalArgumentException(
            String.format("(%d, %d), (%d, %d) aren't valid neighbour cells", row1, col1, row2, col2), caught
        );
    }

    private record WallPos(int row, int col, boolean isHorizontal) {}
}
