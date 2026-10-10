package academy.cashbackmerge.cli;

import academy.cashbackmerge.model.Board;
import academy.cashbackmerge.model.Tile;
import java.util.Optional;

public final class BoardPrinter {

    private BoardPrinter() {}

    public static String print(Board board) {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < board.size(); r++) {
            for (int c = 0; c < board.size(); c++) {
                Optional<Tile> tile = board.tileAt(r, c);
                sb.append(tile.map(Tile::toString).orElse("."));
                if (c < board.size() - 1) {
                    sb.append('\t');
                }
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
