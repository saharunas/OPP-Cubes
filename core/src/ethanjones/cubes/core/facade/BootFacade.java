package ethanjones.cubes.core.facade;

import ethanjones.cubes.core.logging.Log;
import ethanjones.cubes.core.platform.Adapter;
import ethanjones.cubes.core.platform.Compatibility;

/**
 * Facade that hides Cubes boot sequence from clients (Desktop/Android).
 * Subsystems used: Log, Adapter (factory), Compatibility.
 */
public final class BootFacade {
  private BootFacade() {}

  /** Initialize logging and start the game via platform Compatibility. */
  public static void launch() {
    // 1) initialize logging (uses the platform-specific LogWriter via your Abstract Factory)
    Log.initFromFactory();
    Log.info("BootFacade: logging initialized");

    // 2) create platform Compatibility and start the game
    Compatibility compatibility = Adapter.getFactory().createCompatibility();
    Log.info("BootFacade: starting Cubes");
    compatibility.startCubes();
  }
  
}