package ethanjones.cubes.networking.transport;

import static ethanjones.cubes.networking.Networking.socketHints;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.SocketHints;
import ethanjones.cubes.networking.transport.adapters.GdxTransportSocketAdapter;
import ethanjones.cubes.networking.transport.adapters.JavaTransportSocketAdapter;

public final class TransportSockets {
  private TransportSockets() {}

  // Client-side connect that chooses libGDX when graphics are present,
  // and plain Java when headless/server.
  public static TransportSocket connect(String host, int port) {
    boolean hasGdx = (Gdx.app != null);
    if (hasGdx) {
      return new GdxTransportSocketAdapter(Gdx.net.newClientSocket(Protocol.TCP, host, port, socketHints));
    } else {
      try {
        java.net.Socket s = new java.net.Socket(host, port);
        s.setSoTimeout(socketHints.connectTimeout);
        return new JavaTransportSocketAdapter(s);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }

  // Wrap a socket you already have (e.g., from ServerSocket.accept()).
  public static TransportSocket wrap(java.net.Socket socket) {
    return new JavaTransportSocketAdapter(socket);
  }

  // Wrap a libGDX socket you already have (e.g., from a libGDX ServerSocket).
  public static TransportSocket wrap(com.badlogic.gdx.net.Socket socket) {
    return new GdxTransportSocketAdapter(socket);
  }
}
