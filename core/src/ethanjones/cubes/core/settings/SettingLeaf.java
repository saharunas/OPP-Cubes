package ethanjones.cubes.core.settings;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

/** Composite Pattern "Leaf" - represents a single Setting key. */
public final class SettingLeaf implements SettingNode {

  private final String key;

  public SettingLeaf(String key) {
    this.key = key;
  }

  @Override
  public String getKey() {
    return key;
  }

  @Override
  public boolean isGroup() {
    return false;
  }

  @Override
  public boolean shouldDisplay() {
    Setting setting = Settings.getSetting(key);
    return setting != null && setting.shouldDisplay();
  }

  @Override
  public Actor getActor(VisualSettingManager visualSettingManager) {
    Setting setting = Settings.getSetting(key);
    if (setting == null) {
      // Defensive: avoids a crash if a key is wrong
      return new Label("Missing setting: " + key, visualSettingManager.getSkin());
    }
    return setting.getActor(key, visualSettingManager);
  }
}
