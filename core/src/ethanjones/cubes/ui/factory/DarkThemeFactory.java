package ethanjones.cubes.ui.factory;

import ethanjones.cubes.ui.factory.UIThemeFactory;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import ethanjones.cubes.ui.factory.DarkButton;
import ethanjones.cubes.ui.factory.DarkLabel;
import ethanjones.cubes.ui.factory.DarkPanel;

public class DarkThemeFactory implements UIThemeFactory {
  private final Skin skin;

  public DarkThemeFactory(Skin baseSkin) {
    this.skin = baseSkin;
  }

  @Override
  public TextButton createButton(String text) {
    return new DarkButton(text, skin);
  }

  @Override
  public Label createSmallLabel(String text) {
    return new DarkLabel(text, skin);
  }

  @Override
  public Table createPanel() {
    return new DarkPanel(skin);
  }
}