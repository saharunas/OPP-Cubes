package ethanjones.cubes.core.bridge.logging;

/** Bridge: Refined Abstraction #1 */
public class GameplayLogger extends BridgeLogger {
  public GameplayLogger(ethanjones.cubes.core.logging.LogWriter impl) { super(impl); }

  @Override
  protected String format(String msg) {
    return "[GAME] " + msg;
  }
}
