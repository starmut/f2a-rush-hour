import javalib.impworld.World;
import javalib.impworld.WorldScene;
import javalib.worldimages.Posn;
import javalib.worldimages.TextImage;
import javalib.worldimages.WorldImage;

import java.util.List;
import java.awt.Color;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

abstract class AGameWorld extends World implements IConfigAware {
  Game game;

  RuntimeException parseError =
          new IllegalArgumentException("Provided character was not a valid piece type");

  /**
   * Creates a RushHourWorld from a string and a position of the goal tile.
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
   * @param targetTilePos the position of the target tile
   * @throws IllegalArgumentException if any two game pieces intersect at the start of the game,
   *                                  or there are illegal characters in the string
   */
  AGameWorld(String level, Posn targetTilePos) {
    List<ATile> pieces = new ArrayList<>();

    float hue = 0.1f;
    // uses a lambda to count the number of tiles in the input string
    // to do evenly-spaced HSV colors for the tiles
    float inc = 0.8f / level.chars()
            .filter(ch -> Stream.of('C', 'c', 'T', 't', 'S', '.')
                .map(Integer::valueOf)
                .collect(Collectors.toList())
                .contains(ch))
            .count();

    int x = 0;
    int y = 0;

    for (char c : level.toCharArray()) {
      Posn pos = new Posn(x, y);

      switch (c) {
        case '+':
        case '-':
        case '|': {
          pieces.add(new Wall(new Posn(x, y)));
          break;
        }

        case 'X': {
          pieces.add(new Exit(pos));
          break;
        }

        case '\n': {
          x = -1;
          y += 1;
          break;
        }

        case ' ':
          break;

        default: {
          if (new Posn(x, y).equals(targetTilePos)) {
            pieces.add(new TargetTile(
                    new Area(pos, this.computeBottomRight(pos, c)),
                    this.computeAllowedMovements(c)
            ));
          } else {
            pieces.add(
                    new MovableTile(
                            new Area(pos, this.computeBottomRight(pos, c)),
                            this.computeAllowedMovements(c),
                            Color.getHSBColor(hue, 1, 1)));
          }
          hue += inc;
          break;
        }
      }

      x += 1;
    }

    this.game = new Game(pieces);
  }

  /**
   * Return the list of allowed movements of the tile.
   * Horizontal tiles can move 1 unit in the x direction.
   * Vertical tiles can move 1 unit in the y direction.
   *
   * @param c character representing the tile, as defined by the parse format
   * @return list of allowed movements of the tile
   */
  abstract List<Posn> computeAllowedMovements(char c);

  /**
   * Compute the value for the bottom-right of the tile's area.
   *
   * @param topLeft top-left of the tile's area
   * @param c       character representing the tile, as defined by the parse format
   * @return position of the bottom-right of the tile's area
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
   * Draw the game as a WorldScene for rendering.
   *
   * @return scene
   */
  @Override
  public WorldScene makeScene() {
    WorldImage d = game.draw();
    WorldScene scene = new WorldScene((int) d.getWidth(), (int) d.getHeight());
    scene.placeImageXY(d, 0, 0);

    return scene;
  }

  /**
   * Select the tile at the given Posn on the board.
   *
   * @param pos position to select at
   */
  @Override
  public void onMouseClicked(Posn pos) {
    this.game.selectPiece(new Posn(pos.x / SCALE, pos.y / SCALE));
  }

  /**
   * Handle key presses and makes the appropriate move (or undo).
   * Also checks if the game has been won, and end the world if so.
   *
   * @param key key pressed
   */
  @Override
  public void onKeyEvent(String key) {
    Posn delta;

    // also allowing WASD movement
    switch (key) {
      case "up":
      case "w":
        delta = new Posn(0, -1);
        break;

      case "down":
      case "s":
        delta = new Posn(0, 1);
        break;

      case "left":
      case "a":
        delta = new Posn(-1, 0);
        break;

      case "right":
      case "d":
        delta = new Posn(1, 0);
        break;

      case "u":
        this.game.undoPreviousMove();
        return;

      default:
        return;
    }

    this.game.moveSelectedBy(delta);

    if (this.game.isWon()) {
      this.endOfWorld("won");
    }
  }

  /**
   * Since the game has been won, display text to the player saying
   * they won.
   *
   * @param s world ending reason (currently only possible reason is "won")
   * @return final world scene
   */
  @Override
  public WorldScene lastScene(String s) {
    WorldImage d = game.draw();
    WorldScene scene = new WorldScene((int) d.getWidth(), (int) d.getHeight());
    scene.placeImageXY(d, 0, 0);
    scene.placeImageXY(new TextImage("You Win!", Color.BLACK),
        (int) d.getWidth() / 2, (int) d.getHeight() / 2);

    return scene;
  }
}
