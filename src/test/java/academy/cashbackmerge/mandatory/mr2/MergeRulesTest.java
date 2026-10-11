package academy.cashbackmerge.mandatory.mr2;

import static org.assertj.core.api.Assertions.assertThat;

import academy.cashbackmerge.model.Board;
import academy.cashbackmerge.model.Direction;
import academy.cashbackmerge.model.Tile;
import academy.cashbackmerge.support.BoardFixtures;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("MR2. Слияние плиток")
class MergeRulesTest {

    @ParameterizedTest
    @CsvSource({
        "'1,1,.,.', '2,.,.,.'",
        "'1,1,1,.', '2,1,.,.'",
        "'1,1,1,1', '2,2,.,.'",
        "'2,2,4,4', '4,8,.,.'",
    })
    @DisplayName("Сдвиг влево: {0} -> {1}")
    void rowShiftsLeftAndMerges(String before, String expected) {
        Board board = new Board(4);
        BoardFixtures.fillRow(board, 0, before);

        board.move(Direction.LEFT);

        assertThat(BoardFixtures.readRow(board, 0)).isEqualTo(expected);
    }

    @Test
    @DisplayName("Одна плитка не сливается дважды за ход: [1,1,2,.] -> [2,2,.,.]")
    void tileDoesNotMergeTwiceInOneMove() {
        Board board = new Board(4);
        BoardFixtures.fillRow(board, 0, "1,1,2,.");

        board.move(Direction.LEFT);

        assertThat(BoardFixtures.readRow(board, 0)).isEqualTo("2,2,.,.");
    }

    @Test
    @DisplayName("Слияние работает во всех четырёх направлениях")
    void mergeWorksInAllDirections() {
        Board left = new Board(4);
        BoardFixtures.fillRow(left, 0, "2,2,.,.");
        left.move(Direction.LEFT);
        assertThat(BoardFixtures.readRow(left, 0)).isEqualTo("4,.,.,.");

        Board right = new Board(4);
        BoardFixtures.fillRow(right, 0, "2,2,.,.");
        right.move(Direction.RIGHT);
        assertThat(BoardFixtures.readRow(right, 0)).isEqualTo(".,.,.,4");

        Board up = new Board(4);
        up.setTile(0, 0, new Tile(new BigDecimal("2")));
        up.setTile(1, 0, new Tile(new BigDecimal("2")));
        up.move(Direction.UP);
        assertThat(up.tileAt(0, 0).orElseThrow().cashback()).isEqualByComparingTo("4");
        assertThat(up.tileAt(1, 0)).isEmpty();

        Board down = new Board(4);
        down.setTile(2, 0, new Tile(new BigDecimal("2")));
        down.setTile(3, 0, new Tile(new BigDecimal("2")));
        down.move(Direction.DOWN);
        assertThat(down.tileAt(3, 0).orElseThrow().cashback()).isEqualByComparingTo("4");
        assertThat(down.tileAt(2, 0)).isEmpty();
    }
}
