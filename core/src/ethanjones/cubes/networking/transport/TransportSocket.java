package ethanjones.cubes.networking.transport;

import java.io.InputStream;
import java.io.OutputStream;

public interface TransportSocket extends AutoCloseable {
  InputStream getInputStream();
  OutputStream getOutputStream();
  String getRemoteAddress();
  boolean isConnected();
  @Override void close();
  void setSoTimeout(int millis);
}
