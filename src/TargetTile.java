import javalib.worldimages.Posn;

import java.awt.Color;
import java.util.List;

/**
 * Represents the target tile in a game of rush hour
 */
class TargetTile extends MovableTile {
  TargetTile(Area a, List<Posn> allowedMovements) {
    super(a, allowedMovements, Color.RED);
  }

  /**
   * Determines if this piece is a target overlapping an exit.
   *
   * @param pieces list of tiles to check through
   * @return if the game is won
   */
  @Override
  boolean hasWon(List<ATile> pieces) {
    return pieces.stream().anyMatch(p -> p.containsWinningPiece(this));
  }
}
