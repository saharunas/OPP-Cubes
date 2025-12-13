package ethanjones.cubes.networking.transport.adapters;

import ethanjones.cubes.networking.transport.TransportSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class JavaTransportSocketAdapter implements TransportSocket {
  private final Socket socket;
  public JavaTransportSocketAdapter(Socket socket) { this.socket = socket; }

  @Override public InputStream getInputStream() {
    try { return socket.getInputStream(); } catch (IOException e) { throw new RuntimeException(e); }
  }
  @Override public OutputStream getOutputStream() {
    try { return socket.getOutputStream(); } catch (IOException e) { throw new RuntimeException(e); }
  }
  @Override public String getRemoteAddress() { return socket.getRemoteSocketAddress().toString(); }
  @Override public boolean isConnected() { return socket.isConnected() && !socket.isClosed(); }
  @Override public void close() {
    try { socket.close(); } catch (IOException ignored) {}
  }
  @Override public void setSoTimeout(int millis) {
    try { socket.setSoTimeout(millis); } catch (IOException e) { throw new RuntimeException(e); }
  }

  public Socket unwrap() { return socket; } // optional
}
