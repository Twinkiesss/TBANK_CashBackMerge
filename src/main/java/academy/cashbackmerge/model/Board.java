package academy.cashbackmerge.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class Board {

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

    public long move(Direction direction) {
        long totalScore = 0L;
        boolean changed = false;

        for (int line = 0; line < size; line++) {
            Tile[] source = readLine(direction, line);
            LineResult result = slideAndMerge(source);
            if (result.changed()) {
                changed = true;
            }
            totalScore += result.score();
            writeLine(direction, line, result.tiles());
        }

        return changed ? totalScore : -1L;
    }

    private Tile[] readLine(Direction direction, int line) {
        Tile[] result = new Tile[size];
        for (int i = 0; i < size; i++) {
            result[i] = switch (direction) {
                case LEFT -> cells[line][i];
                case RIGHT -> cells[line][size - 1 - i];
                case UP -> cells[i][line];
                case DOWN -> cells[size - 1 - i][line];
            };
        }
        return result;
    }

    private void writeLine(Direction direction, int line, Tile[] tiles) {
        for (int i = 0; i < size; i++) {
            switch (direction) {
                case LEFT -> cells[line][i] = tiles[i];
                case RIGHT -> cells[line][size - 1 - i] = tiles[i];
                case UP -> cells[i][line] = tiles[i];
                case DOWN -> cells[size - 1 - i][line] = tiles[i];
            }
        }
    }

    private LineResult slideAndMerge(Tile[] source) {
        Tile[] compact = new Tile[size];
        int write = 0;
        for (Tile tile : source) {
            if (tile != null) {
                compact[write++] = tile;
            }
        }

        Tile[] merged = new Tile[size];
        long score = 0L;
        int out = 0;
        int i = 0;
        while (i < size) {
            Tile current = compact[i];
            if (current == null) {
                break;
            }
            if (i + 1 < size
                    && compact[i + 1] != null
                    && compact[i + 1].cashback().compareTo(current.cashback()) == 0) {
                Tile combined = current.merged();
                merged[out++] = combined;
                score += combined.cashback().longValue();
                i += 2;
            } else {
                merged[out++] = current;
                i += 1;
            }
        }

        return new LineResult(merged, score, !same(source, merged));
    }

    private boolean same(Tile[] a, Tile[] b) {
        for (int i = 0; i < size; i++) {
            if (a[i] == null && b[i] == null) {
                continue;
            }
            if (a[i] == null || b[i] == null) {
                return false;
            }
            if (a[i].cashback().compareTo(b[i].cashback()) != 0) {
                return false;
            }
        }
        return true;
    }

    private void checkBounds(int row, int col) {
        if (row < 0 || row >= size || col < 0 || col >= size) {
            throw new IndexOutOfBoundsException("(" + row + ", " + col + ")");
        }
    }

    private record LineResult(Tile[] tiles, long score, boolean changed) {}

    public boolean canMove() {
        if (!emptyCells().isEmpty()) {
            return true;
        }
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                Tile current = cells[r][c];
                if (current == null) {
                    continue;
                }

                if (c + 1 < size
                        && cells[r][c + 1] != null
                        && cells[r][c + 1].cashback().compareTo(current.cashback()) == 0) {
                    return true;
                }
                if (r + 1 < size
                        && cells[r + 1][c] != null
                        && cells[r + 1][c].cashback().compareTo(current.cashback()) == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /** Возвращает количество плиток, которые сольются при ходе в эту сторону. */
    public int countMerges(Direction direction) {
        int merges = 0;
        for (int line = 0; line < size; line++) {
            Tile[] source = readLine(direction, line);
            Tile[] compact = new Tile[size];
            int write = 0;
            for (Tile tile : source) {
                if (tile != null) {
                    compact[write++] = tile;
                }
            }
            int i = 0;
            while (i < size - 1) {
                if (compact[i] != null
                        && compact[i + 1] != null
                        && compact[i].cashback().compareTo(compact[i + 1].cashback()) == 0) {
                    merges++;
                    i += 2;
                } else {
                    i++;
                }
            }
        }
        return merges;
    }
}
