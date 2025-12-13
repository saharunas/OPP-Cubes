package ethanjones.cubes.networking.transport.adapters;

import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.NetJavaServerSocketImpl;
import ethanjones.cubes.networking.transport.TransportServer;
import ethanjones.cubes.networking.transport.TransportSocket;

public class GdxTransportServerAdapter implements TransportServer {
  private final ServerSocket server;
  private final int boundPort;

  public GdxTransportServerAdapter(int port, ServerSocketHints hints) {
    this.server = new NetJavaServerSocketImpl(Protocol.TCP, port, hints);
    this.boundPort = port;
  }

  @Override public TransportSocket accept() {
    Socket raw = server.accept(null); // pass hints if you need per-accept options
    return new GdxTransportSocketAdapter(raw);
  }

  @Override public int getPort() { return boundPort; }

  @Override public void close() { server.dispose(); }
}
