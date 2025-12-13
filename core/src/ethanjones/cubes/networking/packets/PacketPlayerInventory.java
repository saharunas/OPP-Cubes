package ethanjones.cubes.networking.packets;

import ethanjones.cubes.entity.living.player.Player;
import ethanjones.cubes.networking.NetworkingManager;
import ethanjones.cubes.networking.packet.DataPacket;
import ethanjones.cubes.networking.packet.PacketDirection;
import ethanjones.cubes.networking.packet.PacketDirection.Direction;
import ethanjones.cubes.networking.server.ClientIdentifier;
import ethanjones.cubes.side.common.Cubes;
import ethanjones.cubes.side.common.Side;
import ethanjones.data.DataGroup;

@Direction(PacketDirection.OMNIDIRECTIONAL)
public class PacketPlayerInventory extends DataPacket {
  public DataGroup inv;

  @Override
  public void handlePacket() {
    Player player;
    if (Side.isServer()) {
      // Server receives inventory update from client
      ClientIdentifier client = Cubes.getServer().getClient(getSocketMonitor());
      player = client.getPlayer();
      player.getInventory().read(inv);
      
      // Broadcast to other clients so they can see hotbar changes
      PacketOtherPlayerInventory otherPacket = new PacketOtherPlayerInventory();
      otherPacket.playerUUID = player.uuid;
      otherPacket.inv = inv;
      NetworkingManager.sendPacketToOtherClients(otherPacket, client);
    } else {
      // Client receives own inventory update from server
      player = Cubes.getClient().player;
      player.getInventory().read(inv);
    }
  }

  @Override
  public DataGroup write() {
    return inv;
  }

  @Override
  public void read(DataGroup data) {
    this.inv = data;
  }

  @Override
  public boolean shouldCompress() {
    return true;
  }
}
