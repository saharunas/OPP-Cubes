package ethanjones.cubes.networking.transport.adapters;

import com.badlogic.gdx.net.Socket;
import ethanjones.cubes.networking.transport.TransportSocket;

import java.io.InputStream;
import java.io.OutputStream;

public class GdxTransportSocketAdapter implements TransportSocket {
  private final Socket socket;
  public GdxTransportSocketAdapter(Socket socket) { this.socket = socket; }

  @Override public InputStream getInputStream() { return socket.getInputStream(); }
  @Override public OutputStream getOutputStream() { return socket.getOutputStream(); }
  @Override public String getRemoteAddress() { return String.valueOf(socket.getRemoteAddress()); }
  @Override public boolean isConnected() { return true; } // libGDX doesn't expose state; treat as open until disposed
  @Override public void close() { socket.dispose(); }
  @Override public void setSoTimeout(int millis) {}

  public Socket unwrap() { return socket; } // optional
}
