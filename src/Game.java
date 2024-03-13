import javalib.worldimages.*;

import java.awt.Color;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Represents a game of rush hour.
 * Any configuration of trucks and cars is allowed.
 * Walls can be anywhere on the board.
 * the goal position can be placed anywhere.
 */
class Game implements IConfigAware {
  List<ATile> pieces;

  Optional<ATile> selectedPiece;

  /**
   * Constructs a game from a list of pieces
   *
   * @param pieces the pieces in the game
   * @throws IllegalArgumentException if the game has collisions between pieces to start
   */
  Game(List<ATile> pieces) {
    this.pieces = pieces;
    this.selectedPiece = Optional.empty();

    if (this.hasOverlappingPieces()) {
      throw new IllegalArgumentException("Game must not have any collisions to start!");
    }
  }

  /**
   * Draws the game as an image.
   */
  WorldImage draw() {
    // computes the maximum x coordinate of any piece or wall
    int width = 0;
    int height = 0;

    for (ATile p : this.pieces) {
      width = Math.max(p.getMaxX(), width);
      height = Math.max(p.getMaxY(), height);
    }

    WorldImage image = new RectangleImage(width, height, OutlineMode.SOLID, Color.WHITE)
        .movePinholeTo(new Posn(-width / 2, -height / 2));

    for (ATile p : this.pieces) {
      image = new OverlayImage(
          p.draw(this.selectedPiece.map(x -> p == x).orElse(false)),
          image);
    }

    return image;
  }

  /**
   * Determines if any two pieces in this game overlap,
   *
   * @return if any pieces overlap
   */
  boolean hasOverlappingPieces() {
    for (int i = 0; i < this.pieces.size(); i += 1) {
      for (int j = i + 1; j < this.pieces.size(); j += 1) {
        ATile iPiece = this.pieces.get(i);
        ATile jPiece = this.pieces.get(j);

        if (iPiece.overlaps(jPiece)) {
          return true;
        }
      }
    }

    return false;
  }

  /**
   * Determines if any piece flagged as a target has reached a goal,
   * i.e. is the game finished.
   *
   * @return is the game finished?
   */
  boolean isWon() {
    return this.pieces.stream().anyMatch(p -> p.hasWon(this.pieces));
  }

  /**
   * Given a click position and the size of the grid that was drawn previously,
   * updates the selection state to select the newly clicked piece (if any)
   * and de-select all other previously selected pieces.
   *
   * @param pos position of selection (in-game coordinates)
   */
  void selectPiece(Posn pos) {
    this.selectedPiece = Optional.empty();
    for (ATile piece : this.pieces) {
      if (piece.contains(pos)) {
        this.selectedPiece = Optional.of(piece);
      }
    }
  }

  /**
   * Moves the selected piece by a given amount.
   * If the move causes collisions, does not do anything.
   *
   * @param delta the amount to move the selected piece by
   */
  void moveSelectedBy(Posn delta) {
    this.selectedPiece.ifPresent(p -> p.move(delta));

    // if the move results in overlapping pieces, undo the move.
    if (this.hasOverlappingPieces() && !this.isWon()) {
      this.selectedPiece.ifPresent(p -> p.move(new Posn(-delta.x, -delta.y)));
    }
  }
}
