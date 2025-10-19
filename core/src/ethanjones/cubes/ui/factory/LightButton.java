package ethanjones.cubes.ui.factory;

import ethanjones.cubes.graphics.menu.Fonts;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class LightButton extends TextButton {
  public LightButton(String text, Skin skin) {
    super(text, buildStyle(skin));
  }

  private static TextButtonStyle buildStyle(Skin skin) {
    TextButtonStyle base = skin.get(TextButtonStyle.class);
    TextButtonStyle s = new TextButtonStyle();

    // Base drawables (can be tinted)
    s.up = base.up;
    s.down = base.down;
    s.over = base.over;
    s.checked = base.checked;

    // Fonts and colors for light theme
    s.font = Fonts.smallHUD;
    s.fontColor = Color.valueOf("141414");    // normal text
    s.overFontColor = Color.valueOf("000000");// hover text
    s.disabledFontColor = Color.GRAY;         // disabled text

    // Optional: tint backgrounds if Skin has "white" drawable
    // s.up   = skin.newDrawable("white", Color.valueOf("F0F0F0"));
    // s.over = skin.newDrawable("white", Color.valueOf("E8E8E8"));
    // s.down = skin.newDrawable("white", Color.valueOf("E0E0E0"));

    return s;
  }
}