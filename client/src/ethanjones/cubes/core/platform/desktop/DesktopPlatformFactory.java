package ethanjones.cubes.core.platform.desktop;

import ethanjones.cubes.core.logging.LogWriter;
import ethanjones.cubes.core.logging.loggers.FileLogWriter;
import ethanjones.cubes.core.platform.*;

import java.io.File;

public class DesktopPlatformFactory implements PlatformFactory {
  private final ClientLauncher launcher;
  private final String[] args;

  public DesktopPlatformFactory(ClientLauncher launcher, String[] args) {
    this.launcher = launcher;
    this.args = args;
  }

  @Override public Compatibility createCompatibility() {
    // ClientCompatibility(ClientLauncher, String[] args)
    return new ClientCompatibility(launcher, args);
  }

  @Override public Launcher createLauncher() {
    return launcher; // the launcher instance itself
  }

  @Override public LogWriter createLogWriter() {
    // mirror what Log.java does: write to base folder log.txt
    File logFile = new File(Compatibility.get().getBaseFolder().file(), "log.txt");
    return new FileLogWriter(logFile);
  }
}