package ethanjones.cubes.networking.transport;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.net.ServerSocketHints;
import ethanjones.cubes.networking.transport.adapters.GdxTransportServerAdapter;
import ethanjones.cubes.networking.transport.adapters.JavaTransportServerAdapter;

public final class TransportServers {
  private TransportServers() {}

  public static TransportServer bind(int port) {
    boolean hasGdx = (Gdx.app != null) && !"HeadlessApplication".equals(Gdx.app.getType().name());
    if (hasGdx) {
      ServerSocketHints hints = new ServerSocketHints();
      hints.acceptTimeout = 0;
      return new GdxTransportServerAdapter(port, hints);
    } else {
      return new JavaTransportServerAdapter(port, 50);
    }
  }
}
