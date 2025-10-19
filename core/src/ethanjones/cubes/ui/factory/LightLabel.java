package ethanjones.cubes.ui.factory;

import ethanjones.cubes.graphics.menu.Fonts;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class LightLabel extends Label {
  public LightLabel(String text, Skin skin) {
    super(text, new LabelStyle(Fonts.smallHUD, Color.valueOf("141414")));
  }
}