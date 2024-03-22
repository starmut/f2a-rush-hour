import javalib.worldimages.OutlineMode;
import javalib.worldimages.Posn;
import javalib.worldimages.RectangleImage;
import tester.Tester;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class ExamplesArea {
  Area a1 = new Area(new Posn(0, 0), new Posn(0, 0));
  Area a2 = new Area(new Posn(0, 0), new Posn(1, 1));
  Area a3 = new Area(new Posn(0, 0), new Posn(0, 1));

  Area a7 = new Area(new Posn(3, 2), new Posn(4, 7));

  void testConstructor(Tester t) {
    t.checkConstructorNoException(
        "valid arguments",
        "Area",
        new Posn(0, 0),
        new Posn(1, 1)
    );
    t.checkConstructorNoException(
        "same coords",
        "Area",
        new Posn(0, 0),
        new Posn(0, 0)
    );
    t.checkConstructorExceptionType(
        IllegalArgumentException.class,
        "Area",
        new Posn(0, 0),
        new Posn(-1, 0)
    );
    t.checkConstructorExceptionType(
        IllegalArgumentException.class,
        "Area",
        new Posn(3, 4),
        new Posn(3, 2)
    );
    t.checkConstructorExceptionType(
        IllegalArgumentException.class,
        "Area",
        new Posn(3, 4),
        new Posn(2, 3)
    );
  }

  void testWidth(Tester t) {
    t.checkExpect(a1.getWidth(), 1);
    t.checkExpect(a2.getWidth(), 2);
    t.checkExpect(a3.getWidth(), 1);
  }

  void testHeight(Tester t) {
    t.checkExpect(a1.getHeight(), 1);
    t.checkExpect(a2.getHeight(), 2);
    t.checkExpect(a3.getHeight(), 2);
  }

  void testContains(Tester t) {
    t.checkExpect(a1.contains(new Posn(0, 0)), true);
    t.checkExpect(a2.contains(new Posn(0, 0)), true);
    t.checkExpect(a3.contains(new Posn(1, 1)), false);
    t.checkExpect(a2.contains(new Posn(2, 1)), false);
  }

  void testOverlaps(Tester t) {
    Area a4 = new Area(new Posn(3, 2), new Posn(5, 2));
    Area a5 = new Area(new Posn(4, 2), new Posn(4, 3));
    Area a6 = new Area(new Posn(3, 1), new Posn(3, 3));

    t.checkExpect(a1.overlaps(a2), true);
    t.checkExpect(a2.overlaps(a3), true);
    t.checkExpect(a4.overlaps(a5), true);
    t.checkExpect(a4.overlaps(a6), true);
    t.checkExpect(a4.overlaps(a1), false);
    t.checkExpect(a1.overlaps(a4), false);
  }

  void testMinX(Tester t) {
    t.checkExpect(a1.minX(), 0);
    t.checkExpect(a2.minX(), 0);
    t.checkExpect(a3.minX(), 0);
    t.checkExpect(a7.minX(), 3);
  }

  void testMaxX(Tester t) {
    t.checkExpect(a1.maxX(), 0);
    t.checkExpect(a2.maxX(), 1);
    t.checkExpect(a3.maxX(), 0);
    t.checkExpect(a7.maxX(), 4);
  }

  void testMinY(Tester t) {
    t.checkExpect(a1.minY(), 0);
    t.checkExpect(a2.minY(), 0);
    t.checkExpect(a3.minY(), 0);
    t.checkExpect(a7.minY(), 2);
  }

  void testMaxY(Tester t) {
    t.checkExpect(a1.maxY(), 0);
    t.checkExpect(a2.maxY(), 1);
    t.checkExpect(a3.maxY(), 1);
    t.checkExpect(a7.maxY(), 7);
  }

  void testOffset(Tester t) {
    Area a4 = new Area(new Posn(3, 2), new Posn(5, 2));
    Area a5 = new Area(new Posn(4, 2), new Posn(4, 3));
    Area a6 = new Area(new Posn(3, 1), new Posn(3, 3));

    a4.offset(new Posn(3, 1));
    t.checkExpect(a4, new Area(new Posn(6, 3), new Posn(8, 3)));
    t.checkExpect(a4.getWidth(), 3);

    a5.offset(new Posn(0, 0));
    t.checkExpect(a5, new Area(new Posn(4, 2), new Posn(4, 3)));

    a6.offset(new Posn(-1, -1));
    t.checkExpect(a6, new Area(new Posn(2, 0), new Posn(2, 2)));
  }
}

class ExamplesGame {
  // formatter refuses to keep the strings looking rectangular
  String demoLevel =
      "+------+\n" +
          "|      |\n" +
          "|  C T |\n" +
          "|c    CX\n" +
          "|t     |\n" +
          "|CCC c |\n" +
          "|    c |\n" +
          "+------+";

  String demoWin =
      "+------+\n" +
          "|CCC  C|\n" +
          "|      |\n" +
          "|     cX\n" +
          "|t   T |\n" +
          "|c C   |\n" +
          "|c     |\n" +
          "+------+";

  String unsolvable =
      "+------+\n" +
          "|     T|\n" +
          "|      |\n" +
          "|c     X\n" +
          "|     T|\n" +
          "|      |\n" +
          "|      |\n" +
          "+------+";

  String noTarget = "X";
  String multiTarget = "XX";
  String noExit =
      "+---+\n" +
          "|   |\n" +
          "+---+";
  String invalidChar =
      "+------+\n" +
          "|     T|\n" +
          "|      |\n" +
          "|c  b  X\n" +
          "|     T|\n" +
          "|      |\n" +
          "|      |\n" +
          "+------+";

  String klotskiOnlyValidChar =
      "+------+\n" +
          "|     T|\n" +
          "|      |\n" +
          "|c  S  X\n" +
          "|     T|\n" +
          "|  .   |\n" +
          "|      |\n" +
          "+------+";

  String klotskiLevel = ""
      + "+----+\n"
      + "|CS C|\n"
      + "|    |\n"
      + "|Cc C|\n"
      + "| .. |\n"
      + "|.  .|\n"
      + "+-XX-+";

  String klotskiOnlyValidChar =
      "+------+\n" +
          "|     T|\n" +
          "|      |\n" +
          "|c  S  X\n" +
          "|     T|\n" +
          "|  .   |\n" +
          "|      |\n" +
          "+------+";

  String klotskiLevel = ""
      + "+----+\n"
      + "|CS C|\n"
      + "|    |\n"
      + "|Cc C|\n"
      + "| .. |\n"
      + "|.  .|\n"
      + "+-XX-+";

  String won =
      "+---+\n" +
          "|   |\n" +
          "| t X\n" +
          "+---|";

  void testParse(Tester t) {
    t.checkConstructorNoException(
        "valid level",
        "RushHourWorld",
        demoLevel, new Posn(1, 3)
    );
    t.checkConstructorNoException(
        "unsolvable but valid",
        "RushHourWorld",
        unsolvable, new Posn(1, 3)
    );
    t.checkConstructorNoException(
        "non-rectangular board",
        "RushHourWorld",
        "+---+\n| |\n+-", new Posn(0, 0)
    );
    t.checkConstructorNoException(
        "no exits",
        "RushHourWorld",
        noExit, new Posn(0, 0)
    );
    t.checkConstructorNoException(
        "many exits",
        "RushHourWorld",
        multiTarget, new Posn(0, 0)
    );
    t.checkConstructorNoException(
        "no target",
        "RushHourWorld",
        noTarget, new Posn(0, 0)
    );
    t.checkConstructorException(
        new IllegalArgumentException("Provided character was not a valid piece type"),
        "RushHourWorld",
        invalidChar, new Posn(0, 0)
    );

    t.checkConstructorNoException(
        "rush hour valid char",
        "RushHourWorld",
        klotskiOnlyValidChar, new Posn(0, 0)
    );

    t.checkConstructorNoException(
        "klotski only valid char",
        "KlotskiWorld",
        klotskiOnlyValidChar, new Posn(0, 0)
    );
  }

  // note: to avoid suffering, make a world and get the game field instead of making
  // a game directly, since the game has no parsing support (and should not).

  void testDraw(Tester t) {
    // this is covered by integration tests in ExamplesRushHourWorld and ExamplesKlotskiWorld
    // run the game and look if the output looks plausible
  }

  void testOverlappingPieces(Tester t) {
    Game g = new KlotskiWorld(""
        + "+----+\n"
        + "|CS C|\n"
        + "|    |\n"
        + "|Cc C|\n"
        + "| .. |\n"
        + "|.  .|\n"
        + "+-XX-+", new Posn(2, 1)).game;

    // in these tests, I literally cannot construct a game which has overlapping pieces
    // because the design is smart enough to just stop me. Therefore, we do a bunch of things
    // that otherwise would cause overlaps, and check that the game is using hasOverlappingPieces
    // to correctly to prevent me from making those changes.
    t.checkExpect(g.hasOverlappingPieces(), false);
    g.makeMove(new Move(g.pieces.get(0), new Posn(0, 1)));
    t.checkExpect(g.hasOverlappingPieces(), false);
    g.makeMove(new Move(g.pieces.get(3), new Posn(-1, 0)));
    t.checkExpect(g.hasOverlappingPieces(), false);
    g.makeMove(new Move(g.pieces.get(6), new Posn(1, 0)));
    t.checkExpect(g.hasOverlappingPieces(), false);
    g.makeMove(new Move(g.pieces.get(15), new Posn(0, -1)));
    t.checkExpect(g.hasOverlappingPieces(), false);

    // we can also check that the constructor errors when given overlapping pieces
    t.checkConstructorException(
        new IllegalArgumentException("Game must not have any collisions to start!"),
        "KlotskiWorld", ""
            + "+----+\n"
            + "|C SC|\n"
            + "|    |\n"
            + "|Cc C|\n"
            + "| .. |\n"
            + "|.  .|\n"
            + "+-XX-+", new Posn(2, 1));

    t.checkConstructorException(
        new IllegalArgumentException("Game must not have any collisions to start!"),
        "KlotskiWorld", ""
            + "+----+\n"
            + "|CS C|\n"
            + "|    |\n"
            + "|Ct C|\n"
            + "| .. |\n"
            + "|.  .|\n"
            + "+-XX-+", new Posn(2, 1));

    t.checkConstructorException(
        new IllegalArgumentException("Game must not have any collisions to start!"),
        "KlotskiWorld", ""
            + "+----+\n"
            + "|CS C|\n"
            + "|    |\n"
            + "|Ct  |\n"
            + "| .S |\n"
            + "|.  .|\n"
            + "+-XX-+", new Posn(2, 1));
  }

  void testIsWon(Tester t) {
    t.checkExpect(
        new KlotskiWorld(""
            + "+----+\n"
            + "|CS C|\n"
            + "|    |\n"
            + "|Cc C|\n"
            + "| .. |\n"
            + "|.  .|\n"
            + "+-XX-+", new Posn(2, 1)).game.isWon(), false);

    t.checkExpect(
        new KlotskiWorld(""
            + "+----+\n"
            + "|CS C|\n"
            + "|  X |\n"
            + "|Cc C|\n"
            + "| .. |\n"
            + "|.  .|\n"
            + "+-XX-+", new Posn(2, 1)).game.isWon(), true);

    t.checkExpect(
        new KlotskiWorld(""
            + "+----+\n"
            + "|CSXC|\n"
            + "| XX |\n"
            + "|Cc C|\n"
            + "| .. |\n"
            + "|.  .|\n"
            + "+-XX-+", new Posn(2, 1)).game.isWon(), true);
  }

  void testSelectPiece(Tester t) {
    Game g = new KlotskiWorld(""
        + "+----+\n"
        + "|CS C|\n"
        + "|    |\n"
        + "|Cc C|\n"
        + "| .. |\n"
        + "|.  .|\n"
        + "+-XX-+", new Posn(2, 1)).game;

    t.checkExpect(g.selectedTile.isPresent(), false);
    g.selectPiece(new Posn(0, 0));
    t.checkExpect(g.selectedTile, Optional.of(new Wall(new Posn(0, 0))));
    g.selectPiece(new Posn(2, 5));
    t.checkExpect(g.selectedTile.isPresent(), false);
    g.selectPiece(new Posn(2, 6));
    t.checkExpect(g.selectedTile, Optional.of(new Exit(new Posn(2, 6))));
    g.selectPiece(new Posn(0, 0));
    t.checkExpect(g.selectedTile, Optional.of(new Wall(new Posn(0, 0))));
    g.selectPiece(new Posn(-1, 5));
    t.checkExpect(g.selectedTile.isPresent(), false);
  }

  void testMakeMove(Tester t) {
    Game g = new KlotskiWorld(""
        + "+----+\n"
        + "|CS C|\n"
        + "|    |\n"
        + "|Cc C|\n"
        + "| .. |\n"
        + "|.  .|\n"
        + "+-XX-+", new Posn(2, 1)).game;

    t.checkExpect(g.makeMove())
  }
}

class ExamplesATile implements IConfigAware {
  // AGamePiece is abstract so we mostly use Wall (it's the closest to the abstract class)
  ATile w1 = new Wall(new Posn(0, 0));
  ATile w2 = new Wall(new Posn(1, 1));

  ATile v1 = new MovableTile(
      new Area(new Posn(0, 0), new Posn(2, 3)),
      new ArrayList<>(),
      Color.GREEN);

  void testDraw(Tester t) {
    t.checkExpect(w1.draw(false), w1.draw(true));
    t.checkExpect(w1.draw(false),
        new RectangleImage(
            SCALE,
            SCALE,
            OutlineMode.SOLID, Color.GRAY)
            .movePinhole(
                -SCALE / 2.0,
                -SCALE / 2.0
            ));
    t.checkExpect(w2.draw(false),
        new RectangleImage(
            SCALE,
            SCALE,
            OutlineMode.SOLID, Color.GRAY)
            .movePinhole(
                -3 * SCALE / 2.0,
                -3 * SCALE / 2.0
            ));
    t.checkExpect(w1.draw(true), w1.drawWithColorAndInset(Color.GRAY, 0));
    t.checkExpect(w2.draw(true), w2.drawWithColorAndInset(Color.GRAY, 0));
  }

  void testDrawWithColorAndInset(Tester t) {
    t.checkExpect(w1.drawWithColorAndInset(Color.RED, 0),
        new RectangleImage(
            SCALE,
            SCALE,
            OutlineMode.SOLID, Color.RED)
            .movePinhole(
                -SCALE / 2.0,
                -SCALE / 2.0
            ));
    t.checkExpect(w1.drawWithColorAndInset(Color.BLUE, 0),
        new RectangleImage(
            SCALE,
            SCALE,
            OutlineMode.SOLID, Color.BLUE)
            .movePinhole(
                -SCALE / 2.0,
                -SCALE / 2.0
            ));
    t.checkExpect(w1.drawWithColorAndInset(Color.RED, 10),
        new RectangleImage(
            SCALE - 10,
            SCALE - 10,
            OutlineMode.SOLID, Color.RED)
            .movePinhole(
                -SCALE / 2.0,
                -SCALE / 2.0
            ));
  }

  void testContains(Tester t) {
    t.checkExpect(w1.contains(new Posn(10, 2)), false);
    t.checkExpect(w1.contains(new Posn(0, 0)), true);
    t.checkExpect(w2.contains(new Posn(0, 0)), false);
    t.checkExpect(w2.contains(new Posn(1, 1)), true);
    t.checkExpect(v1.contains(new Posn(-1, 1)), false);
    t.checkExpect(v1.contains(new Posn(2, 1)), true);
    t.checkExpect(v1.contains(new Posn(2, 4)), false);
  }

  void testOverlaps(Tester t) {
    t.checkExpect(w1.overlaps(w1), true);
    t.checkExpect(w2.overlaps(w1), false);
    t.checkExpect(w1.overlaps(v1), true);
    t.checkExpect(v1.overlaps(v1), true);
    t.checkExpect(v1.overlaps(w2), true);
  }

  void testGetWidth(Tester t) {
    t.checkExpect(w1.getWidth(), 1);
    t.checkExpect(w2.getWidth(), 1);
    t.checkExpect(v1.getWidth(), 3);
  }

  void testGetHeight(Tester t) {
    t.checkExpect(w1.getHeight(), 1);
    t.checkExpect(w2.getHeight(), 1);
    t.checkExpect(v1.getHeight(), 4);
  }

  void testGetMaxX(Tester t) {
    t.checkExpect(w1.getMaxX(), 0);
    t.checkExpect(w2.getMaxX(), 1);
    t.checkExpect(v1.getMaxX(), 2);
  }

  void testGetMaxY(Tester t) {
    t.checkExpect(w1.getMaxY(), 0);
    t.checkExpect(w2.getMaxY(), 1);
    t.checkExpect(v1.getMaxY(), 3);
  }

  void testHasWon(Tester t) {
    t.checkExpect(w1.hasWon(List.of()), false);
    t.checkExpect(w2.hasWon(List.of(v1, v1, w1, w2)), false);
    t.checkExpect(v1.hasWon(List.of(w2, w1)), false);
  }

  void testContainsWinningPiece(Tester t) {
    t.checkExpect(w1.containsWinningTile(w1), false);
    t.checkExpect(w1.containsWinningTile(w2), false);
    t.checkExpect(w1.containsWinningTile(v1), false);
    t.checkExpect(w2.containsWinningTile(w2), false);
    t.checkExpect(w2.containsWinningTile(v1), false);
    t.checkExpect(v1.containsWinningTile(v1), false);
  }

  void testMove(Tester t) {
    ATile w1p = new Wall(new Posn(0, 0));
    ATile w2p = new Wall(new Posn(1, 1));

    t.checkExpect(w1, w1p);
    w1.move(new Posn(1, 1));
    t.checkExpect(w1, w1p);
    w1.move(new Posn(-1, 0));
    t.checkExpect(w1, w1p);
    w1.move(new Posn(0, 3));
    t.checkExpect(w1, w1p);

    t.checkExpect(w2, w2p);
    w1.move(new Posn(1, 1));
    t.checkExpect(w2, w2p);
    w1.move(new Posn(-1, 0));
    t.checkExpect(w2, w2p);
    w1.move(new Posn(0, 3));
    t.checkExpect(w2, w2p);
  }
}

class ExamplesAPointTile {
  // APointPiece is abstract, but it has two (mostly very small) inheritors
  // the only real defining property of APointPiece is that the pieces are 1 x 1.

  APointTile p1 = new Wall(new Posn(0, 0));
  APointTile p2 = new Exit(new Posn(3, 0));

  void testWidth(Tester t) {
    t.checkExpect(p1.getWidth(), 1);
    t.checkExpect(p2.getWidth(), 1);
  }

  void testHeight(Tester t) {
    t.checkExpect(p1.getHeight(), 1);
    t.checkExpect(p2.getHeight(), 1);
  }
}

class ExamplesWall {
  // There is _no_ additional functionality defined in Wall, so
  // the tests for AGamePiece and APointPiece entirely cover this class.
}

class ExamplesExit {
  Exit exit = new Exit(new Posn(3, 0));
  ATile otherPiece = new MovableTile(
      new Area(new Posn(0, 0), new Posn(3, 0)),
      List.of(new Posn(1, 0)),
      Color.GREEN);
  ATile target = new TargetTile(
      new Area(new Posn(4, 0), new Posn(4, 0)),
      List.of(new Posn(-1, 0)));

  void testContainsWinningPiece(Tester t) {
    // a piece does not have to be a target to be eligible to "win" from Exit's point of view.
    t.checkExpect(exit.containsWinningTile(exit), true);
    t.checkExpect(exit.containsWinningTile(otherPiece), true);
    t.checkExpect(exit.containsWinningTile(target), false);
  }
}

class ExamplesMovableTile implements IConfigAware {
  MovableTile unmovable = new MovableTile(
      new Area(new Posn(0, 0), new Posn(0, 0)),
      List.of(),
      Color.GREEN);
  MovableTile right = new MovableTile(
      new Area(new Posn(0, 0), new Posn(3, 0)),
      List.of(new Posn(1, 0)),
      Color.BLUE);

  void testDraw(Tester t) {
    t.checkExpect(unmovable.draw(false),
        unmovable.drawWithColorAndInset(Color.GREEN, GRID_GAP));
    t.checkExpect(unmovable.draw(true),
        unmovable.drawWithColorAndInset(Color.YELLOW, IConfigAware.GRID_GAP));
  }

  void testMove(Tester t) {
    int oldX = right.getMaxX();
    int oldY = right.getMaxY();
    right.move(new Posn(1, 0));
    t.checkExpect(right.getMaxX(), oldX + 1);
    t.checkExpect(right.getMaxY(), oldY);

    oldX = right.getMaxX();
    right.move(new Posn(-4, 9)); // not in allowed motions
    t.checkExpect(right.getMaxX(), oldX); // nothing happened
    t.checkExpect(right.getMaxY(), oldY);
  }

  void testUnmovablePiece(Tester t) {
    int oldX = unmovable.getMaxX();
    int oldY = unmovable.getMaxY();

    unmovable.move(new Posn(1, 0));
    t.checkExpect(unmovable.getMaxX(), oldX);
    t.checkExpect(unmovable.getMaxY(), oldY);

    unmovable.move(new Posn(3, -4));
    t.checkExpect(unmovable.getMaxX(), oldX);
    t.checkExpect(unmovable.getMaxY(), oldY);
  }
}

class ExamplesTargetTile {
  TargetTile t1 = new TargetTile(
      new Area(new Posn(0, 0), new Posn(0, 2)),
      List.of());

  ATile e1 = new Exit(new Posn(0, 1));
  ATile e2 = new Exit(new Posn(1, 0));

  void testTargetCreationColor(Tester t) {
    t.checkExpect(t1.color, Color.RED);
  }

  void testHasWon(Tester t) {
    t.checkExpect(t1.hasWon(List.of(e1, e2)), true);
    t.checkExpect(t1.hasWon(List.of(e1, t1)), true);
    t.checkExpect(t1.hasWon(List.of(e2, t1)), false);
    t.checkExpect(t1.hasWon(List.of(e1, e2, t1)), true);
  }
}

class ExamplesRushHourWorld implements IConfigAware {
  void testGame(Tester t) {
    new RushHourWorld(""
        + "+------+\n"
        + "|   +  |\n"
        + "|  C T |\n"
        + "|c    CX\n"
        + "|t     |\n"
        + "|CCC c |\n"
        + "|    c |\n"
        + "+------+", new Posn(1, 3)).bigBang(SCALE * 8, SCALE * 8);
  }

  void testGameNonRectangular(Tester t) {
    new RushHourWorld(""
        + "   ++\n"
        + "  +  +\n"
        + " +    +\n"
        + "+c  c  X\n"
        + "|     |\n"
        + " +    +\n"
        + " | c  |\n"
        + " +----+ ", new Posn(1, 3)).bigBang(SCALE * 8, SCALE * 8);
  }
}

class ExamplesKlotskiWorld implements IConfigAware {
  void testKlotski(Tester t) {
    new KlotskiWorld(""
        + "+----+\n"
        + "|CS C|\n"
        + "|    |\n"
        + "|Cc C|\n"
        + "| .. |\n"
        + "|.  .|\n"
        + "+-XX-+", new Posn(2, 1)).bigBang(SCALE * 6, SCALE * 7);
  }
}
