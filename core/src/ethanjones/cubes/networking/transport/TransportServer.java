package ethanjones.cubes.networking.transport;

public interface TransportServer extends AutoCloseable {
  TransportSocket accept();
  int getPort();
  @Override void close();
}
