package ethanjones.cubes.networking.server;

import ethanjones.cubes.core.logging.Log;
import ethanjones.cubes.core.system.Branding;
import ethanjones.cubes.core.system.Executor;
import ethanjones.cubes.networking.transport.TransportSocket;
import ethanjones.cubes.side.common.Cubes;
import ethanjones.cubes.side.common.Side;

import com.badlogic.gdx.net.NetJavaSocketImpl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.concurrent.Callable;

import static ethanjones.cubes.networking.client.ClientConnectionInitializer.extractJavaSocket;

public class ServerConnectionInitializer {

  public static final int TIMEOUT = 5000;

  private static class Checker implements Callable<Object> {

    private final TransportSocket socket;

    private Checker(TransportSocket socket) {
      this.socket = socket;
    }

    @Override
    public Object call() {
      try {
        initialConnect(socket);
      } catch (SocketTimeoutException e) {
        Log.debug(new IOException("Client did not respond in time", e));
        try { socket.close(); } catch (Exception ignored) {}
      } catch (IOException e) {
        Log.debug(e);
        try { socket.close(); } catch (Exception ignored) {}
      } catch (Exception e) {
        Log.debug(e);
        try { socket.close(); } catch (Exception ignored) {}
      }
      return null;
    }
  }

  public static void check(TransportSocket socket) {
    Executor.execute(new Checker(socket));
  }

  private static void initialConnect(TransportSocket socket) throws Exception {
    // require first byte within TIMEOUT
    socket.setSoTimeout(TIMEOUT);

    DataInputStream in  = new DataInputStream(socket.getInputStream());
    DataOutputStream out = new DataOutputStream(socket.getOutputStream());

    byte code = in.readByte(); // may throw SocketTimeoutException

    // send server version
    out.writeInt(Branding.VERSION_MAJOR);
    out.writeInt(Branding.VERSION_MINOR);
    out.writeInt(Branding.VERSION_POINT);
    out.writeInt(Branding.VERSION_BUILD);
    out.writeUTF(Branding.VERSION_HASH);

    switch (code) {
      case 0:
        connect(socket, out, in);
        return;
      case 1:
        ping(socket, out, in);
        return;
      default:
        throw new IOException("Unrecognised connection code " + code);
    }
  }

  private static void connect(TransportSocket socket,
                              DataOutputStream out,
                              DataInputStream in) throws Exception {
    // normal operation: no read timeout
    socket.setSoTimeout(0);
    // hand off to networking
    ((ServerNetworking) Side.getNetworking()).accepted(socket);
  }

  private static void ping(TransportSocket socket,
                           DataOutputStream out,
                           DataInputStream in) throws Exception {
    Log.debug(socket.getRemoteAddress() + " pinged the server");

    List<ClientIdentifier> clients = Cubes.getServer().getAllClients();
    out.writeInt(clients.size());
    for (ClientIdentifier client : clients) {
      out.writeUTF(client.getPlayer().username);
    }
    out.flush();

    // Close after responding to ping (matches your previous behavior)
    socket.close();
  }
  
}
