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
class RushHour implements IConfigAware {
  List<AGamePiece> pieces;

  RuntimeException parseError =
      new IllegalArgumentException("Provided character was not a valid piece type");

  Optional<AGamePiece> selectedPiece;

  RushHour(List<AGamePiece> pieces) {
    this.pieces = pieces;
    this.selectedPiece = Optional.empty();
  }

  /**
   * Creates a game from a string and a position of the goal piece.
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
  RushHour(String level, Posn targetVehiclePos) {
    this(new ArrayList<>()); // we will add to this list shortly

    float hue = 0.1f;
    // uses a lambda to count the number of vehicles in the input string
    // to do evenly-spaced HSV colors for the pieces
    float inc = 0.8f / level.chars()
        .filter(ch -> ch == 'C' || ch == 'c' || ch == 'T' || ch == 't')
        .count();

    int x = 0;
    int y = 0;

    for (char c : level.toCharArray()) {
      Posn pos = new Posn(x, y);

      switch (c) {
        case '+':
        case '-':
        case '|': {
          this.pieces.add(new Wall(new Posn(x, y)));
          break;
        }

        case 'C':
        case 'c':
        case 'T':
        case 't': {
          if (new Posn(x, y).equals(targetVehiclePos)) {
            this.pieces.add(new TargetVehicle(
                new Area(pos, this.computeBottomRight(pos, c)),
                this.computeAllowedMovements(c)
            ));
          } else {
            this.pieces.add(
                new Vehicle(
                    new Area(pos, this.computeBottomRight(pos, c)),
                    this.computeAllowedMovements(c),
                    Color.getHSBColor(hue, 1, 1)));
          }
          hue += inc;
          break;
        }

        case 'X': {
          this.pieces.add(new Exit(pos));
          break;
        }

        case '\n': {
          x = -1;
          y += 1;
          break;
        }

        case ' ':
          break;

        default:
          throw new IllegalArgumentException("Illegal character \"" + c + "\" in level string!");
      }

      x += 1;
    }

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

    for (AGamePiece p : this.pieces) {
      width = Math.max(p.getMaxX(), width);
      height = Math.max(p.getMaxY(), height);
    }

    WorldImage image = new RectangleImage(width, height, OutlineMode.SOLID, Color.WHITE)
        .movePinholeTo(new Posn(-width / 2, -height / 2));

    for (AGamePiece p : this.pieces) {
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
        AGamePiece iPiece = this.pieces.get(i);
        AGamePiece jPiece = this.pieces.get(j);

        if (iPiece.isCollidable() && jPiece.isCollidable() && iPiece.overlaps(jPiece)) {
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
    for (AGamePiece piece : this.pieces) {
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
    if (this.hasOverlappingPieces()) {
      this.selectedPiece.ifPresent(p -> p.move(new Posn(-delta.x, -delta.y)));
    }
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
    switch (c) {
      case 'C':
      case 'T':
        return List.of(new Posn(0, -1), new Posn(0, 1));
      case 'c':
      case 't':
        return List.of(new Posn(1, 0), new Posn(-1, 0));
      default:
        throw this.parseError;
    }
  }
}
