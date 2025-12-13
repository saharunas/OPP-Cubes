package ethanjones.cubes.networking.transport.adapters;

import ethanjones.cubes.networking.transport.TransportServer;
import ethanjones.cubes.networking.transport.TransportSocket;

import java.io.IOException;

public class JavaTransportServerAdapter implements TransportServer {
  private final java.net.ServerSocket server;

  public JavaTransportServerAdapter(int port, int backlog) {
    try { this.server = new java.net.ServerSocket(port, backlog); }
    catch (IOException e) { throw new RuntimeException(e); }
  }

  @Override public TransportSocket accept() {
    try {
      java.net.Socket raw = server.accept();
      return new JavaTransportSocketAdapter(raw);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override public int getPort() { return server.getLocalPort(); }

  @Override public void close() {
    try { server.close(); } catch (IOException ignored) {}
  }
}
