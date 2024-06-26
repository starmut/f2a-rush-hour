import javalib.worldimages.OutlineMode;
import javalib.worldimages.Posn;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.WorldImage;

import java.awt.Color;
import java.util.List;

/**
 * Represents a tile in the game.
 */
abstract class ATile implements IConfigAware {
  Area area;
  Color color;

  ATile(Area area, Color color) {
    this.area = area;
    this.color = color;
  }

  /**
   * Draw this tile as an image. The resulting image has a pinhole such
   * that overlaying it with a background with a pinhole in the top left
   * places the piece correctly.
   *
   * @param isSelected is the piece selected
   * @return this piece's image
   */
  WorldImage draw(boolean isSelected) {
    return this.drawWithColorAndInset(this.color, 0);
  }

  /**
   * Draw this tile as an image. The resulting image has a pinhole such
   * that overlaying it with a background with a pinhole in the top left
   * places the tile correctly. Takes a color to draw with, as well as
   * the amount of space to leave blank around the tile.
   *
   * @param c the color to drqw this piece with
   * @param gap the thickness of the blank border around the piece
   * @return this piece's image
   */
  WorldImage drawWithColorAndInset(Color c, int gap) {
    return new RectangleImage(
        SCALE * this.getWidth() - gap,
        SCALE * this.getHeight() - gap,
        OutlineMode.SOLID, c)
        .movePinhole(
            -this.area.minX() * SCALE - this.getWidth() * SCALE / 2.0,
            -this.area.minY() * SCALE - this.getHeight() * SCALE / 2.0
        );
  }


  /**
   * Does this tile contain the Posn?
   *
   * @param p position to check
   * @return if this piece is on that Posn
   */
  boolean contains(Posn p) {
    return this.area.contains(p);
  }

  /**
   * Does this tile overlap with the other tile?
   *
   * @param other other tile to check
   * @return if these pieces overlap
   */
  boolean overlaps(ATile other) {
    return this.area.overlaps(other.area);
  }

  /**
   * Return the width of this tile.
   *
   * @return width
   */
  int getWidth() {
    return this.area.getWidth();
  }

  /**
   * Return the height of this tile.
   *
   * @return height
   */
  int getHeight() {
    return this.area.getHeight();
  }

  /**
   * gets the x coordinate of the rightmost edge of this tile.
   *
   * @return the maximum x coordinate
   */
  int getMaxX() {
    return this.area.maxX();
  }

  /**
   * gets the y coordinate of the bottom edge of this tile.
   *
   * @return the maximum y coordinate
   */
  int getMaxY() {
    return this.area.maxY();
  }

  /**
   * Determines if this tile is a target overlapping an exit.
   *
   * @param pieces the exits to check against
   * @return if the game is won
   */
  boolean hasWon(List<ATile> pieces) {
    return false;
  }

  /**
   * Determines if this is an Exit and contains the given tile.
   *
   * @param o the tile to consider for having won the game
   * @return if the game is won
   */
  boolean containsWinningTile(ATile o) {
    return false;
  }

  /**
   * Moves this tile by the deltas specified by the given Posn.
   *
   * @param delta the amount by which to move
   * @return whether the move was successful
   */
  boolean move(Posn delta) {
    // a general tile cannot be moved, only MovableTile can
    return false;
  }
}
