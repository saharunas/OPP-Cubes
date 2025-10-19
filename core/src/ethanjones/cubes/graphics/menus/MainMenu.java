package ethanjones.cubes.graphics.menus;

import ethanjones.cubes.ui.factory.ThemeManager;
import ethanjones.cubes.ui.factory.UIThemeFactory;

import ethanjones.cubes.core.localization.Localization;
import ethanjones.cubes.core.logging.Log;
import ethanjones.cubes.core.platform.Adapter;
import ethanjones.cubes.core.platform.Compatibility;
import ethanjones.cubes.core.system.Branding;
import ethanjones.cubes.graphics.assets.Assets;
import ethanjones.cubes.graphics.menu.Menu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;

import static ethanjones.cubes.graphics.Graphics.GUI_HEIGHT;
import static ethanjones.cubes.graphics.Graphics.GUI_WIDTH;

public class MainMenu extends Menu {

  private final UIThemeFactory theme;

  private static Value cellHeight = new Value() {
    @Override
    public float get(Actor context) {
      return GUI_HEIGHT / 8;
    }
  };
  private static Value cellWidth = new Value() {
    @Override
    public float get(Actor context) {
      return GUI_WIDTH / 2;
    }
  };

  Image logo;
  Label version;
  Label author;
  Table buttons;
  TextButton singleplayer;
  TextButton multiplayer;
  TextButton settings;
  TextButton quit;

  public MainMenu() {
    //false = Light, true = Dark  
    this.theme = ThemeManager.get(skin, true);

    logo = new Image(new TextureRegionDrawable(Assets.getTextureRegion("core:logo.png")), Scaling.fillY, Align.center);
    version = theme.createSmallLabel(Branding.DEBUG);
    author  = theme.createSmallLabel(Branding.AUTHOR);

    buttons = theme.createPanel();
    buttons.defaults().height(cellHeight).width(cellWidth).pad(4).fillX().fillY();

    buttons.add(singleplayer = theme.createButton(Localization.get("menu.main.singleplayer"))).row();
    singleplayer.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        if (Compatibility.get().functionModifier()) {
          Adapter.setMenu(new SingleplayerTemporarySaveMenu());
        } else {
          Adapter.setMenu(new SingleplayerSavesMenu());
        }
      }
    });

    // FIXED: use multiplayer key, and no skin arg
    buttons.add(multiplayer = theme.createButton(Localization.get("menu.main.multiplayer"))).row();
    multiplayer.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        Adapter.setMenu(new MultiplayerConnectMenu());
      }
    });

    // FIXED: no skin arg
    buttons.add(settings = theme.createButton(Localization.get("menu.main.settings"))).row();
    settings.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        Adapter.setMenu(new SettingsMenu());
      }
    });

    // FIXED: use quit key, and no skin arg
    buttons.add(quit = theme.createButton(Localization.get("menu.main.quit"))).row();
    quit.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        Log.debug("Quit pressed");
        Adapter.quit();
      }
    });

    stage.addActor(logo);
    stage.addActor(version);
    stage.addActor(author);
    stage.addActor(buttons);

    logo.addListener(new ActorGestureListener() {
      @Override
      public boolean longPress(Actor actor, float x, float y) {
        Adapter.setMenu(new LogMenu());
        return true;
      }
    });
  }

  @Override
  public void resize(float width, float height) {
    super.resize(width, height);
    logo.setBounds(0, (height / 4 * 3) + 2, width, height / 6);
    version.setBounds(2, 2, author.getPrefWidth(), author.getPrefHeight());
    version.setAlignment(Align.left);
    author.setBounds(width - author.getPrefWidth() - 2, 2, author.getPrefWidth(), author.getPrefHeight());
    author.setAlignment(Align.right);
    buttons.setBounds(0, 0, width, (height / 4 * 3) - 2);
    buttons.align(Align.top);
    buttons.layout();
  }
}