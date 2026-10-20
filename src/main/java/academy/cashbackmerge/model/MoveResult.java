package academy.cashbackmerge.model;

public record MoveResult(boolean changed, long scoreGained) {

    public static MoveResult invalid() {
        return new MoveResult(false, 0L);
    }

    public static MoveResult valid(long scoreGained) {
        return new MoveResult(true, scoreGained);
    }
}
