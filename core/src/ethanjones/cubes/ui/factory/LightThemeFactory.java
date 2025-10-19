package ethanjones.cubes.ui.factory;

import ethanjones.cubes.ui.factory.UIThemeFactory;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import ethanjones.cubes.ui.factory.LightButton;
import ethanjones.cubes.ui.factory.LightLabel;
import ethanjones.cubes.ui.factory.LightPanel;

public class LightThemeFactory implements UIThemeFactory {
  private final Skin skin;

  public LightThemeFactory(Skin baseSkin) {
    this.skin = baseSkin;
  }

  @Override
  public TextButton createButton(String text) {
    return new LightButton(text, skin);
  }

  @Override
  public Label createSmallLabel(String text) {
    return new LightLabel(text, skin);
  }

  @Override
  public Table createPanel() {
    return new LightPanel(skin);
  }
}