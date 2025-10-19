package ethanjones.cubes.ui.factory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class LightPanel extends Table {
  public LightPanel(Skin skin) {
    super(skin);
    // Optional: background tint if your Skin has "white" drawable
    // setBackground(skin.newDrawable("white", Color.valueOf("FCFCFC")));
    // pad(8f);
  }
}