package ethanjones.cubes.networking.packets;

import ethanjones.cubes.entity.living.player.Player;
import ethanjones.cubes.networking.packet.DataPacket;
import ethanjones.cubes.networking.packet.PacketDirection;
import ethanjones.cubes.networking.packet.PacketDirection.Direction;
import ethanjones.cubes.side.common.Cubes;
import ethanjones.data.DataGroup;

import java.util.UUID;

/**
 * Packet to synchronize other players' inventory changes (especially hotbar selection)
 * Similar to PacketOtherPlayerMovement pattern
 */
@Direction(PacketDirection.TO_CLIENT)
public class PacketOtherPlayerInventory extends DataPacket {
  
  public UUID playerUUID;
  public DataGroup inv;
  
  @Override
  public void handlePacket() {
    // Find the other player by UUID
    Player player = (Player) Cubes.getClient().world.getEntity(playerUUID);
    if (player != null) {
      player.getInventory().read(inv);
    }
  }
  
  @Override
  public DataGroup write() {
    DataGroup data = new DataGroup();
    data.put("uuid_most", playerUUID.getMostSignificantBits());
    data.put("uuid_least", playerUUID.getLeastSignificantBits());
    data.put("inv", inv);
    return data;
  }
  
  @Override
  public void read(DataGroup data) {
    long mostSig = data.getLong("uuid_most");
    long leastSig = data.getLong("uuid_least");
    this.playerUUID = new UUID(mostSig, leastSig);
    this.inv = data.getGroup("inv");
  }
  
  @Override
  public boolean shouldCompress() {
    return true;
  }
}
