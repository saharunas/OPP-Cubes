package ethanjones.cubes.ui.factory;

import com.badlogic.gdx.scenes.scene2d.ui.*;

public interface UIThemeFactory {
  TextButton createButton(String text);
  Label     createSmallLabel(String text);
  Table     createPanel();
}
