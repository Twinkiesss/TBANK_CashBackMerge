package academy.cashbackmerge.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class Board {

    public static final int DEFAULT_SIZE = 4;

    private final int size;
    private final Tile[][] cells;

    public Board(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("size должен быть > 0");
        }
        this.size = size;
        this.cells = new Tile[size][size];
    }

    public int size() {
        return size;
    }

    public Optional<Tile> tileAt(int row, int col) {
        checkBounds(row, col);
        return Optional.ofNullable(cells[row][col]);
    }

    public void setTile(int row, int col, Tile tile) {
        checkBounds(row, col);
        cells[row][col] = tile;
    }

    public boolean isEmpty(int row, int col) {
        checkBounds(row, col);
        return cells[row][col] == null;
    }

    public List<Cell> emptyCells() {
        List<Cell> result = new ArrayList<>();
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (cells[r][c] == null) {
                    result.add(new Cell(r, c));
                }
            }
        }
        return result;
    }

    private void checkBounds(int row, int col) {
        if (row < 0 || row >= size || col < 0 || col >= size) {
            throw new IndexOutOfBoundsException("(" + row + ", " + col + ")");
        }
    }
}
