import javalib.worldimages.Posn;

import java.awt.Color;

/*
 * Represents a wall.
 */
class Wall extends APointPiece {
  Wall(Posn basePoint) {
    super(basePoint, Color.GRAY);
  }
}
