import javalib.worldimages.Posn;

/**
 * Represents a move made by the player of a piece in a direction.
 */
class Move {
  ATile tile;
  Posn delta;

  Move(ATile tile, Posn delta) {
    this.tile = tile;
    this.delta = delta;
  }

  /**
   * Makes this move on the board.
   * EFFECT: changes the moved tile's position to perform the move.
   *
   * @return whether the move was successful
   */
  boolean perform() {
    return this.tile.move(this.delta);
  }

  /**
   * Returns a new move with the opposite effect of this one.
   *
   * @return Negated move
   */
  Move negate() {
    return new Move(this.tile, new Posn(-this.delta.x, -this.delta.y));
  }

  ATile getTile() {
    return tile;
  }
}
