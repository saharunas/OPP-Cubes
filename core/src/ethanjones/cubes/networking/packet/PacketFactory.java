package ethanjones.cubes.networking.packet;

import ethanjones.cubes.networking.socket.SocketMonitor;
import ethanjones.cubes.side.common.Side;

public abstract class PacketFactory {

  protected final SocketMonitor socketMonitor;

  protected PacketFactory(SocketMonitor socketMonitor) {
    this.socketMonitor = socketMonitor;
  }

  /** Factory Method — create from dynamic ID assigned by PacketIDDatabase. */
  public final Packet createFromId(int id) throws Exception {
    Class<? extends Packet> c = socketMonitor.getPacketIDDatabase().get(id);
    return newPacket(c);
  }

  /** Factory Method — create from class name (pre-ID handshake path). */
  public final Packet createFromClassName(String className) throws Exception {
    @SuppressWarnings("unchecked")
    Class<? extends Packet> c =
        (Class<? extends Packet>) Class.forName(className).asSubclass(Packet.class);

    // mirror existing behavior: server sends back ID mapping once it sees a class
    if (socketMonitor.getSide() == Side.Server) {
      socketMonitor.getPacketIDDatabase().sendID(c, socketMonitor);
    }
    return newPacket(c);
  }

  /** The single overridable construction hook. */
  protected abstract Packet newPacket(Class<? extends Packet> packetClass) throws Exception;
}
