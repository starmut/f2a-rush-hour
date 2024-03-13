import javalib.worldimages.Posn;

import java.awt.Color;

/**
 * Represents the game's exit.
 */
class Exit extends APointPiece {
  Exit(Posn basePoint) {
    super(basePoint, Color.LIGHT_GRAY);
  }

  /**
   * Determines if this is an Exit and contains the given piece.
   *
   * @param o the piece to consider for having won the game
   * @return if the game is won
   */
  @Override
  boolean containsWinningPiece(AGamePiece o) {
    return this.overlaps(o);
  }

  /**
   * Is this piece a physical obstacle that can collide with other pieces?
   *
   * @return if this pieces collides
   */
  @Override
  boolean isCollidable() {
    return false;
  }
}