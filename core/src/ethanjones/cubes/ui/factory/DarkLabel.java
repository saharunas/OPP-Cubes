package ethanjones.cubes.ui.factory;
import ethanjones.cubes.graphics.menu.Fonts;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class DarkLabel extends Label {
  public DarkLabel(String text, Skin skin) {
    super(text, new LabelStyle(Fonts.smallHUD, Color.valueOf("E6E6E6")));
  }
}