import javalib.worldimages.Posn;

import java.awt.Color;

/**
 * Represents an exit tile in the game.
 */
class Exit extends APointTile {
  Exit(Posn basePoint) {
    super(basePoint, Color.LIGHT_GRAY);
  }

  /**
   * Determines if this is an Exit and contains the given tile.
   *
   * @param o the tile to consider for having won the game
   * @return if the game is won
   */
  @Override
  boolean containsWinningTile(ATile o) {
    return this.overlaps(o);
  }
}
