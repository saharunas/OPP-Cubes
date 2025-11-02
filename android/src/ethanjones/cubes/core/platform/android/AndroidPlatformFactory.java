package ethanjones.cubes.core.platform.android;

import ethanjones.cubes.core.logging.LogWriter;
import ethanjones.cubes.core.platform.*;

public class AndroidPlatformFactory implements PlatformFactory {
  private final AndroidLauncher launcher;

  public AndroidPlatformFactory(AndroidLauncher launcher) {
    this.launcher = launcher;
  }

  @Override public Compatibility createCompatibility() {
    // AndroidCompatibility has a protected ctor taking AndroidLauncher
    return new AndroidCompatibility(launcher);
  }

  @Override public Launcher createLauncher() { 
    return launcher; // the launcher instance itself
  }

  @Override public LogWriter createLogWriter() {
    return new AndroidLogWriter(); // already in android/
  }
}