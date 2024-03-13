import javalib.worldimages.Posn;

/**
 * Represents a rectangular area of the game board.
 * Areas are a fully closed interval of the top-left and
 * bottom-right Posns.
 * Top-left coordinates must be less than bottom-right coordinates.
 */
class Area {
  Posn topLeft;
  Posn botRight;

  /**
   * Construct an Area with the coordinates for top-left and bottom-right.
   *
   * @param topLeft top-left Posn
   * @param botRight bottom-right Posn
   * @throws IllegalArgumentException if the coordinates for topLeft are below or
   *     to the right of botRight
   */
  Area(Posn topLeft, Posn botRight) {
    if (topLeft.x > botRight.x || topLeft.y > botRight.y) {
      throw new IllegalArgumentException("topLeft must have x, y <= botRight");
    }

    this.topLeft = topLeft;
    this.botRight = botRight;
  }

  /**
   * Does this area contain the given Posn?
   * @param p position to check
   * @return whether it's in the area
   */
  boolean contains(Posn p) {
    return this.topLeft.x <= p.x && p.x <= this.botRight.x
        && this.topLeft.y <= p.y && p.y <= this.botRight.y;
  }

  /**
   * Does this area overlap with the given area?
   * @param other area to check
   * @return whether they overlap
   */
  boolean overlaps(Area other) {
    return !(this.botRight.x < other.topLeft.x
        || this.botRight.y < other.topLeft.y
        || this.topLeft.x > other.botRight.x
        || this.topLeft.y > other.botRight.y);
  }

  /**
   * Move this area by the offset specified by the given Posn.
   * @param delta how much to move in each direction
   */
  void offset(Posn delta) {
    this.topLeft = this.topLeft.offset(delta.x, delta.y);
    this.botRight = this.botRight.offset(delta.x, delta.y);
  }

  /**
   * Return the width of this area.
   * @return how wide the area is
   */
  int getWidth() {
    return this.botRight.x - this.topLeft.x + 1;
  }

  /**
   * Return the height of this area.
   * @return how tall the area is
   */
  int getHeight() {
    return this.botRight.y - this.topLeft.y + 1;
  }

  /**
   * Return the smallest x-coordinate this area occupies.
   * @return minimum x-coordinate
   */
  int minX() {
    return this.topLeft.x;
  }

  /**
   * Return the biggest x-coordinate this area occupies.
   * @return maximum x-coordinate
   */
  int maxX() {
    return this.botRight.x;
  }

  /**
   * Return the smallest y-coordinate this area occupies.
   * @return minimum y-coordinate
   */
  int minY() {
    return this.topLeft.y;
  }

  /**
   * Return the biggest y-coordinate this area occupies.
   * @return maximum y-coordinate
   */
  int maxY() {
    return this.botRight.y;
  }
}
