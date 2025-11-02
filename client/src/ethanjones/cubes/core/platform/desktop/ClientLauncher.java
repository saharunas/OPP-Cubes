package ethanjones.cubes.core.platform.desktop;

import ethanjones.cubes.core.platform.Adapter;
import ethanjones.cubes.core.facade.BootFacade;


public class ClientLauncher implements DesktopLauncher {

  public static void main(String[] arg) {
    new ClientLauncher(arg).start();
  }

 private void start() {
    DesktopCompatibility.setup();

    Adapter.setFactory(new DesktopPlatformFactory(this, arg));
    BootFacade.launch();
  }

  private final String[] arg;

  private ClientLauncher(String[] arg) {
    this.arg = arg;
  }
}
