package ethanjones.cubes.core.facade;

import ethanjones.cubes.core.bridge.logging.DebugLogger;
import ethanjones.cubes.core.bridge.logging.GameplayLogger;
import ethanjones.cubes.core.logging.loggers.SysOutLogWriter;
import ethanjones.cubes.core.logging.loggers.FileLogWriter;
import ethanjones.cubes.core.platform.Adapter;
import ethanjones.cubes.core.platform.Compatibility;


public final class BootFacade {
  private BootFacade() {}

  public static void launch() {
  
    ethanjones.cubes.core.logging.Log.initFromFactory();

    GameplayLogger gameLog = new GameplayLogger(new SysOutLogWriter()); // Implementor #1
    DebugLogger debugLog   = new DebugLogger(new FileLogWriter(
        new java.io.File(System.getProperty("user.dir"), "bridge-debug.txt"))); // Implementor #2

    gameLog.info("BootFacade: starting Cubes via Bridge facade...");
    debugLog.info("Bridge is active (DebugLogger -> FileLogWriter)");

    Compatibility compatibility = Adapter.getFactory().createCompatibility();
    compatibility.startCubes();
  }

  public static void shutdown() {
    ethanjones.cubes.core.logging.Log.dispose();
  }
}