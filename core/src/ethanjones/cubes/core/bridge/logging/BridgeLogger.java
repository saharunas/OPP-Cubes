package ethanjones.cubes.core.bridge.logging;

import ethanjones.cubes.core.logging.LogLevel;
import ethanjones.cubes.core.logging.LogWriter;

/** Bridge: Abstraction */
public abstract class BridgeLogger {
  protected final LogWriter impl; // the Implementor

  protected BridgeLogger(LogWriter impl) {
    this.impl = impl;
  }

  public void info(String msg)    { impl.log(LogLevel.info,    format(msg)); }
  public void warning(String msg) { impl.log(LogLevel.warning, format(msg)); }
  public void error(String msg)   { impl.log(LogLevel.error,   format(msg)); }

  protected abstract String format(String msg);
}