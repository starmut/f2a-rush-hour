import javalib.worldimages.Posn;

import java.awt.Color;

/*
 * Represents a 1x1 piece.
 */
abstract class APointPiece extends AGamePiece {
  APointPiece(Posn pos, Color color) {
    super(new Area(pos, pos), color);
  }
}
