package ethanjones.cubes.ui.factory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class DarkPanel extends Table {
  public DarkPanel(Skin skin) {
    super(skin);
    // Optional background if you have a 1x1 "white" drawable in the Skin
    // setBackground(skin.newDrawable("white", Color.valueOf("161616")));
    // pad(8f);
  }
}