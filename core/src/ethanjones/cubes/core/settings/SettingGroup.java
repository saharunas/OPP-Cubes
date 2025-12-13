package ethanjones.cubes.core.settings;

import ethanjones.cubes.core.localization.Localization;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.ArrayList;

public class SettingGroup implements SettingNode {

  // Composite children: can contain SettingLeaf (leaf) or SettingGroup (composite)
  private final ArrayList<SettingNode> children;
  private String unlocalizedName = "";

  public SettingGroup() {
    children = new ArrayList<SettingNode>();
  }

  /** Add a leaf Setting by its key. */
  public SettingGroup add(String notLocalised) {
    children.add(new SettingLeaf(notLocalised));
    return this;
  }

  /** Add a child group (composite). */
  public SettingGroup add(String name, SettingGroup settingGroup) {
    settingGroup.unlocalizedName = name;
    children.add(settingGroup);
    return this;
  }

  @Override
  public String getKey() {
    return unlocalizedName;
  }

  @Override
  public boolean isGroup() {
    return true;
  }

  @Override
  public boolean shouldDisplay() {
    return true;
  }

  /**
   * For groups, the menu shows a button that opens the group.
   */
  @Override
  public Actor getActor(final VisualSettingManager visualSettingManager) {
    final TextButton textButton = new TextButton(Localization.get("menu.settings.open_group"), visualSettingManager.getSkin());
    textButton.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        visualSettingManager.setSettingGroup(SettingGroup.this);
      }
    });
    return textButton;
  }

  /** Composite children (leaf + group together). */
  public ArrayList<SettingNode> getChildren() {
    return children;
  }

  public String getUnlocalizedName() {
    return unlocalizedName;
  }

    /** Find a direct child group by key (unlocalized name). Returns null if not found. */
  public SettingGroup getChildGroup(String key) {
      for (SettingNode n : children) {
        if (n.isGroup() && key.equals(n.getKey())) {
          return (SettingGroup) n;
        }
      }
      return null;
  }
  
}
