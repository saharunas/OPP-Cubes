package ethanjones.cubes.side.client;

import ethanjones.cubes.core.logging.Log;
import ethanjones.cubes.core.mod.ModManager;
import ethanjones.cubes.core.mod.event.StartingClientEvent;
import ethanjones.cubes.core.mod.event.StoppingClientEvent;
import ethanjones.cubes.core.performance.Performance;
import ethanjones.cubes.core.performance.PerformanceTags;
import ethanjones.cubes.core.platform.Adapter;
import ethanjones.cubes.core.system.CubesException;
import ethanjones.cubes.entity.living.player.Player;
import ethanjones.cubes.entity.living.player.strategies.AdventureStrategy;
import ethanjones.cubes.entity.living.player.strategies.CreativeStrategy;
import ethanjones.cubes.entity.living.player.strategies.GamemodeStrategy;
import ethanjones.cubes.entity.living.player.strategies.SpectatorStrategy;
import ethanjones.cubes.entity.living.player.strategies.SurvivalStrategy;
import ethanjones.cubes.graphics.hud.inv.InventoryManager;
import ethanjones.cubes.graphics.menus.PauseMenu;
import ethanjones.cubes.graphics.menus.WorldLoadingMenu;
import ethanjones.cubes.graphics.rendering.Renderer;
import ethanjones.cubes.graphics.world.other.RainRenderer;
import ethanjones.cubes.input.InputChain;
import ethanjones.cubes.networking.NetworkingManager;
import ethanjones.cubes.side.common.Cubes;
import ethanjones.cubes.side.common.Side;
import ethanjones.cubes.world.client.WorldClient;
import ethanjones.cubes.world.collision.PlayerCollision;
import ethanjones.cubes.world.save.Gamemode;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import java.util.UUID;

public class CubesClient extends Cubes implements ApplicationListener {

  public static UUID uuid;

  public Player player;
  public InputChain inputChain;
  public Renderer renderer;
  public Gamemode gamemode;
  public GamemodeStrategy gamemodeStrategy = new SurvivalStrategy();
  public long frameStart;
  public float worldProgress = 0f;
  public boolean worldReady = false;
  private long nextTickTime;
  private int behindTicks;

  public CubesClient() {
    super(Side.Client);
    if (Adapter.isDedicatedServer()) throw new CubesException("Cannot run client on server");
  }

  public void setGamemode(Gamemode _gamemode){
    this.gamemode = _gamemode;

    switch (_gamemode){
        case survival:
          this.gamemodeStrategy = new SurvivalStrategy();
          break;
        case creative:
          this.gamemodeStrategy = new CreativeStrategy();
          break;
        case spectator:
          this.gamemodeStrategy = new SpectatorStrategy();
          break;
        case adventure:
          this.gamemodeStrategy = new AdventureStrategy();
          break;
    }
  }

  @Override
  public void create() {
    if (state.isSetup()) return;
    super.create();
    NetworkingManager.clientInit();

    inputChain = new InputChain();
    renderer = new Renderer();
    player = new Player(renderer.worldRenderer.camera);
    renderer.guiRenderer.playerCreated();

    inputChain.setup();
    Gdx.input.setInputProcessor(InputChain.getInputMultiplexer());

    world = new WorldClient();

    ModManager.postModEvent(new StartingClientEvent());
    Side.getSidedEventBus().register(new PlayerCollision());

    ClientDebug.setup();

    nextTickTime = System.currentTimeMillis() + tickMS;

    state.setup();
  }

  @Override
  public void render() {
    frameStart = System.nanoTime();
    if (shouldReturn()) return;
    if (worldReady) {
      Adapter.setMenu(null);
      worldReady = false;
    }
    if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && !(Adapter.getMenu() instanceof WorldLoadingMenu)) {
      if (InventoryManager.isInventoryOpen()) {
        InventoryManager.hideInventory();
      } else if (Adapter.getMenu() instanceof PauseMenu) {
        Adapter.setMenu(null);
      } else {
        Adapter.setMenu(new PauseMenu());
      }
    }
    Performance.start(PerformanceTags.CLIENT_FRAME);

    ClientDebug.frameStart();

    long diff = nextTickTime - System.currentTimeMillis();
    if (diff < -16) {
      int change = 1 + (int) (-(diff + 16) / tickMS);
      behindTicks += change;
      nextTickTime += change * tickMS;
      diff = nextTickTime - System.currentTimeMillis();
    }
    if (diff < 2) {
      tick();
      nextTickTime += tickMS;
    } else if (behindTicks > 0) {
      tick();
      behindTicks--;
    }
    if (behindTicks >= (1000 / tickMS)) {
      Log.warning("Skipping " + behindTicks + " ticks");
      behindTicks = 0;
      nextTickTime += behindTicks * tickMS;
    }

    this.update();
    inputChain.beforeRender();
    renderer.render();
    inputChain.afterRender();
    Performance.stop(PerformanceTags.CLIENT_FRAME);
  }

  @Override
  protected void update() {
    super.update();
  }

  @Override
  protected void tick() {
    Performance.start(PerformanceTags.CLIENT_TICK);
    super.tick();
    inputChain.tick();
    RainRenderer.tick();
    Performance.stop(PerformanceTags.CLIENT_TICK);
  }

  @Override
  public void stop() {
    if (state.hasStopped() || !state.isSetup()) return;
    ModManager.postModEvent(new StoppingClientEvent());
    super.stop();
    renderer.dispose();
    inputChain.dispose();
  }

  @Override
  public void resize(int width, int height) {
    if (shouldReturn()) return;
    renderer.resize();
  }

  @Override
  public void pause() {

  }

  @Override
  public void resume() {

  }
}
