package ethanjones.cubes.ui.factory;

import ethanjones.cubes.graphics.menu.Fonts;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class DarkButton extends TextButton {
  public DarkButton(String text, Skin skin) {
    super(text, buildStyle(skin));
  }

  private static TextButtonStyle buildStyle(Skin skin) {
    TextButtonStyle base = skin.get(TextButtonStyle.class);
    TextButtonStyle s = new TextButtonStyle();

    // Base drawables
    s.up = base.up;
    s.down = base.down;
    s.over = base.over;
    s.checked = base.checked;

    // Font + colors
    s.font = Fonts.smallHUD;
    s.fontColor = Color.valueOf("E6E6E6");   // normal
    s.overFontColor = Color.WHITE;           // hover
    s.disabledFontColor = Color.GRAY;        // disabled

    // Optional background tint (requires "white" drawable in Skin)
    // s.up   = skin.newDrawable("white", Color.valueOf("222222"));
    // s.over = skin.newDrawable("white", Color.valueOf("2C2C2C"));
    // s.down = skin.newDrawable("white", Color.valueOf("1E1E1E"));

    return s;
  }
}