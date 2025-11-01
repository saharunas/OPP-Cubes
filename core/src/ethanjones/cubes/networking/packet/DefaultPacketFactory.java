package ethanjones.cubes.networking.packet;

import ethanjones.cubes.networking.packet.PacketDirection;
import ethanjones.cubes.networking.socket.SocketMonitor;

public class DefaultPacketFactory extends PacketFactory {

  public DefaultPacketFactory(SocketMonitor socketMonitor) {
    super(socketMonitor);
  }

  @Override
  protected Packet newPacket(Class<? extends Packet> packetClass) throws Exception {
    // Optional: enforce receive-direction here
    PacketDirection.checkPacketReceive(packetClass, socketMonitor.getSide());

    Packet p = packetClass.newInstance(); // uses existing no-arg constructors
    p.setSocketMonitor(socketMonitor);
    return p;
  }
}
