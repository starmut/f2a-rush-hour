import javalib.worldimages.Posn;

import java.util.List;

class KlotskiWorld extends AGameWorld {

  /**
   * Creates a RushHourWorld from a string and a position of the goal piece.
   * The string must be of the format:
   * String demoLevel = ""
   * + "+------+\n"
   * + "|      |\n"
   * + "|  C T |\n"
   * + "|c    CX\n"
   * + "|t     |\n"
   * + "|CCC c |\n"
   * + "|    c |\n"
   * + "+------+";
   *
   * @param level            the level string as described above
   * @param targetVehiclePos the position of the target vehicle
   * @throws IllegalArgumentException if any two game pieces intersect at the start of the game,
   *                                  or there are illegal characters in the string
   */
  KlotskiWorld(String level, Posn targetVehiclePos) {
    super(level, targetVehiclePos);
  }

  /**
   * Compute the value for the bottom-right of the vehicle's area.
   *
   * @param topLeft top-left of the vehicle's area
   * @param c       character representing the vehicle, as defined by the parse format
   * @return position of the bottom-right of the vehicle's area
   */
  Posn computeBottomRight(Posn topLeft, char c) {
    switch (c) {
      case 'C':
        return topLeft.offset(0, 1);
      case 'c':
        return topLeft.offset(1, 0);
      case 'T':
        return topLeft.offset(0, 2);
      case 't':
        return topLeft.offset(2, 0);
      case '.':
        return topLeft;
      case 'S':
        return topLeft.offset(1, 1);
      default:
        throw this.parseError;
    }
  }

  /**
   * Return the list of allowed movements of the vehicle.
   * Horizontal vehicles can move 1 unit in the x direction.
   * Vertical vehicles can move 1 unit in the y direction.
   *
   * @param c character representing the vehicle, as defined by the parse format
   * @return list of allowed movements of the vehicle
   */
  List<Posn> computeAllowedMovements(char c) {
    return List.of(
            new Posn(0, 1),
            new Posn(0, -1),
            new Posn(1, 0),
            new Posn(-1, 0));
  }
}