package ethanjones.cubes.core.settings;

import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Composite Pattern "Component" for settings menu entries.
 * A SettingNode is either a leaf (a Setting) or a composite (a SettingGroup).
 */
public interface SettingNode {

  /** Unlocalized key, used for localization (e.g. "graphics" or "graphics.viewDistance"). */
  String getKey();

  /** True for a group/composite node, false for a leaf/setting node. */
  boolean isGroup();

  /** Should this node be shown in the menu? */
  boolean shouldDisplay();

  /** Actor shown on the right side of the row (button for groups, editor widget for settings). */
  Actor getActor(VisualSettingManager visualSettingManager);
}
