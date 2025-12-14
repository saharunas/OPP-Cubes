package ethanjones.cubes.core.platform.desktop;

import ethanjones.cubes.core.logging.Log;

public class ClientLauncher implements DesktopLauncher {

  public static void main(String[] arg) {
    new ClientLauncher(arg).start();
  }

  private void start() {
    DesktopCompatibility.setup();
    Log.performanceTest(10_000);
    Log.performanceTest(100_000);
    Log.performanceTest(500_000);
    Log.performanceTest(1_000_000);
    Log.performanceTest(5_000_000);
    Log.printPerformanceSummary();

    new ClientCompatibility(this, arg).startCubes();
  }

  private final String[] arg;

  private ClientLauncher(String[] arg) {
    this.arg = arg;
  }
}