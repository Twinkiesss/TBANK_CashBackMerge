package academy.cashbackmerge.support;

import academy.cashbackmerge.model.Board;
import academy.cashbackmerge.model.Tile;
import java.math.BigDecimal;

public final class BoardFixtures {

    private static final String EMPTY = ".";
    private static final String SEPARATOR = ",";

    private BoardFixtures() {}

    public static void fillRow(Board board, int row, String spec) {
        String[] parts = spec.split(SEPARATOR, -1);
        for (int c = 0; c < parts.length; c++) {
            String value = parts[c].trim();
            if (!EMPTY.equals(value)) {
                board.setTile(row, c, new Tile(new BigDecimal(value)));
            }
        }
    }

    public static String readRow(Board board, int row) {
        StringBuilder sb = new StringBuilder();
        for (int c = 0; c < board.size(); c++) {
            if (c > 0) {
                sb.append(SEPARATOR);
            }
            sb.append(
                    board.tileAt(row, c).map(t -> t.cashback().toPlainString()).orElse(EMPTY));
        }
        return sb.toString();
    }
}
