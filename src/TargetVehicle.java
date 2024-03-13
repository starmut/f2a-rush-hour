import javalib.worldimages.Posn;

import java.awt.Color;
import java.util.List;

/**
 * Represents the target vehicle in a game of rush hour
 */
class TargetVehicle extends Vehicle {
  TargetVehicle(Area a, List<Posn> allowedMovements) {
    super(a, allowedMovements, Color.RED);
  }

  /**
   * Determines if this piece is a target overlapping an exit.
   *
   * @param pieces list of vehicles to check through
   * @return if the game is won
   */
  @Override
  boolean hasWon(List<AGamePiece> pieces) {
    return pieces.stream().anyMatch(p -> p.containsWinningPiece(this));
  }
}
