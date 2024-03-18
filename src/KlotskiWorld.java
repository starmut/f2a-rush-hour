import javalib.worldimages.Posn;

import java.util.List;

class KlotskiWorld extends AGameWorld {
  /**
   * Creates a KlotskiWorld from a string and a position of the goal tile.
   * The string must be of the format:
   * String demoLevel = ""
   * + "+----+\n"
   * + "|CS C|\n"
   * + "|    |\n"
   * + "|Cc C|\n"
   * + "| .. |\n"
   * + "|.  .|\n"
   * + "+-XX-+";
   *
   * @param level            the level string as described above
   * @param targetTilePos the position of the target tile
   * @throws IllegalArgumentException if any two game pieces intersect at the start of the game,
   *                                  or there are illegal characters in the string
   */
  KlotskiWorld(String level, Posn targetTilePos) {
    super(level, targetTilePos);
  }

  /**
   * Return the list of allowed movements of the tile.
   * Horizontal tiles can move 1 unit in the x direction.
   * Vertical tiles can move 1 unit in the y direction.
   *
   * @param c character representing the tile, as defined by the parse format
   * @return list of allowed movements of the tile
   */
  @Override
  List<Posn> computeAllowedMovements(char c) {
    // the shape of tile doesn't matter for klotski.
    // all tiles can move in all 4 directions
    return List.of(
        new Posn(0, 1),
        new Posn(0, -1),
        new Posn(1, 0),
        new Posn(-1, 0));
  }
}
