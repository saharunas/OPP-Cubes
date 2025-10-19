package ethanjones.cubes.ui.factory;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import ethanjones.cubes.ui.factory.UIThemeFactory;
import ethanjones.cubes.ui.factory.DarkThemeFactory;
import ethanjones.cubes.ui.factory.LightThemeFactory;

public final class ThemeManager {
  private ThemeManager() {}

  /** Choose a theme factory based on the flag. false = Light, true = Dark. */
  public static UIThemeFactory get(Skin skin, boolean darkMode) {
    return darkMode ? new DarkThemeFactory(skin) : new LightThemeFactory(skin);
  }
}