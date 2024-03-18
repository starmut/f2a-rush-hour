import javalib.worldimages.Posn;

import java.awt.Color;

/*
 * Represents a 1x1 tile.
 */
abstract class APointTile extends ATile {
  APointTile(Posn pos, Color color) {
    super(new Area(pos, pos), color);
  }
}
