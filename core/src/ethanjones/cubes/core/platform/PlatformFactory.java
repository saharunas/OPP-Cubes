package ethanjones.cubes.core.platform;

import ethanjones.cubes.core.logging.LogWriter;

public interface PlatformFactory {
  Compatibility createCompatibility();
  Launcher createLauncher();
  LogWriter createLogWriter();
}
