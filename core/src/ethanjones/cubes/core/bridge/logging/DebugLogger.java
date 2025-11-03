package ethanjones.cubes.core.bridge.logging;

/** Bridge: Refined Abstraction #2 */
public class DebugLogger extends BridgeLogger {
  public DebugLogger(ethanjones.cubes.core.logging.LogWriter impl) { super(impl); }

  @Override
  protected String format(String msg) {
    long t = System.currentTimeMillis();
    return "[DEBUG @" + t + "] " + msg;
  }
}
