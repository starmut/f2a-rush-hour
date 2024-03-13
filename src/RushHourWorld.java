import javalib.impworld.World;
import javalib.impworld.WorldScene;
import javalib.worldimages.Posn;
import javalib.worldimages.TextImage;
import javalib.worldimages.WorldImage;

import java.awt.*;

class RushHourWorld extends World implements IConfigAware {
  RushHour game;

  RushHourWorld(String level, Posn targetVehiclePos) {
    this.game = new RushHour(level, targetVehiclePos);
  }

  @Override
  public WorldScene makeScene() {
    WorldImage d = game.draw();
    WorldScene scene = new WorldScene((int) d.getWidth(), (int) d.getHeight());
    scene.placeImageXY(d, 0, 0);

    return scene;
  }

  @Override
  public void onMouseClicked(Posn pos) {
    this.game.selectPiece(new Posn(pos.x / SCALE, pos.y / SCALE));
  }

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

      default:
        return;
    }

    this.game.moveSelectedBy(delta);

    if (this.game.isWon()) {
      this.endOfWorld("won");
    }
  }

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