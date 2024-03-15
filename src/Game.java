import javalib.worldimages.*;

import java.awt.Color;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

/**
 * Represents a game of rush hour or klotski.
 * Any configuration of trucks and cars is allowed.
 * Walls can be anywhere on the board.
 * the goal position can be placed anywhere.
 */
class Game implements IConfigAware {
  List<ATile> pieces;

  Optional<ATile> selectedPiece;

  Stack<Move> moves;

  Optional<ATile> lastMoved;

  int score;

  /**
   * Constructs a game from a list of pieces
   *
   * @param pieces the pieces in the game
   * @throws IllegalArgumentException if the game has collisions between pieces to start
   */
  Game(List<ATile> pieces) {
    this.pieces = pieces;
    this.selectedPiece = Optional.empty();
    this.lastMoved = Optional.empty();
    this.moves = new Stack<>();
    this.score = 0;

    if (this.hasOverlappingPieces()) {
      throw new IllegalArgumentException("Game must not have any collisions to start!");
    }
  }

  /**
   * Draws the game as an image.
   */
  WorldImage draw() {
    WorldImage image = new EmptyImage();

    for (ATile p : this.pieces) {
      image = new OverlayImage(
          p.draw(this.selectedPiece.map(x -> p == x).orElse(false)),
          image);
    }

    WorldImage scoreImg = new TextImage("Score:  " + this.score, 32, Color.BLACK);

    image = new OverlayOffsetAlign(AlignModeX.CENTER, AlignModeY.TOP, scoreImg, 0, 0, image)
        .movePinholeTo(new Posn((int) (-image.getWidth() / 2), 0));

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
   * Undo the last move
   * EFFECT: potentially updates score if the undo is undoing a new piece
   */
  void undoLastMove() {
    if (this.moves.isEmpty()) {
      return;
    }

    Move m = this.moves.pop();
    this.makeMove(m.negate());
    // this.makeMove just pushed the negated move onto the stack
    // so we pop again to remove that one.
    this.moves.pop();
  }

  /**
   * Makes a move on this board.
   * If the move results in overlapping pieces, un
   * EFFECT: adds the move to the stack of previous moves.
   * EFFECT: increases the score if this moves a new piece from last time.
   *
   * @param m the move to make
   */
  void makeMove(Move m) {
    m.perform();

    // if the move results in overlapping pieces, undo the move.
    if (this.hasOverlappingPieces() && !this.isWon()) {
      m.negate().perform();
    } else {
      this.moves.push(m);
      this.updateScore(m);
      this.lastMoved = Optional.of(m.getTile());
    }
  }

  /**
   * Increments the score if the move applies to a different piece from what was last moved
   * EFFECT: increases score by 1 if the piece moved is different from last time
   *
   * @param move the move that was just made
   */
  void updateScore(Move move) {
    // if there was a previously moved tile, the current move
    // should have a _different_ tile (or this should be the first move)
    // only in this case do we increment the score.

    if (this.lastMoved
        .map(tile -> move.getTile() != tile)
        .orElse(true)
    ) {
      this.score += 1;
    }
  }

  /**
   * Moves the selected piece by a given amount.
   * If the move causes collisions, does not do anything.
   *
   * @param delta the amount to move the selected piece by
   */
  void moveSelectedBy(Posn delta) {
    this.selectedPiece
        .ifPresent(t -> this.makeMove(new Move(t, delta)));
  }
}
