import javalib.worldimages.Posn;
import javalib.worldimages.WorldImage;

import java.awt.Color;
import java.util.List;

/**
 * Represents a movable, selectable, tile.
 */
class MovableTile extends ATile {
  List<Posn> allowedMovements;

  MovableTile(Area a, List<Posn> allowedMovements, Color color) {
    super(a, color);
    this.allowedMovements = allowedMovements;
  }

  /**
   * Draw this piece as an image. The resulting image has a pinhole such
   * that overlaying it with a background with a pinhole in the top left
   * places the piece correctly.
   *
   * @param isSelected is the piece selected
   * @return this piece's image
   */
  @Override
  WorldImage draw(boolean isSelected) {
    if (isSelected) {
      return this.drawWithColorAndInset(Color.YELLOW, GRID_GAP);
    }

    return super.drawWithColorAndInset(this.color, GRID_GAP);
  }

  /**
   * Moves this piece by the deltas specified by the given Posn.
   *
   * @param delta the amount by which to move
   */
  @Override
  boolean move(Posn delta) {
    if (!this.allowedMovements.contains(delta)) {
      return false;
    }

    this.area.offset(delta);
    return true;
  }
}
