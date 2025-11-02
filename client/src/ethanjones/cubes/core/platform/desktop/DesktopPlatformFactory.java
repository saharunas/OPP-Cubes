package ethanjones.cubes.core.platform.desktop;

import ethanjones.cubes.core.logging.LogWriter;
import ethanjones.cubes.core.logging.loggers.SysOutLogWriter;
import ethanjones.cubes.core.platform.*;


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

  @Override
    public LogWriter createLogWriter() {
     // No Compatibility access here!
    return new SysOutLogWriter();
  }
}