package ethanjones.cubes.networking.packets;

import ethanjones.cubes.entity.living.player.Player;
import ethanjones.cubes.networking.packet.Packet;
import ethanjones.cubes.networking.packet.PacketDirection;
import ethanjones.cubes.networking.packet.PacketDirection.Direction;
import ethanjones.cubes.side.common.Cubes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

@Direction(PacketDirection.TO_CLIENT)
public class PacketPlayerSkinColor extends Packet {
  public UUID playerUUID;
  public String skinColor;

  @Override
  public void write(DataOutputStream dataOutputStream) throws IOException {
    dataOutputStream.writeLong(playerUUID.getMostSignificantBits());
    dataOutputStream.writeLong(playerUUID.getLeastSignificantBits());
    dataOutputStream.writeUTF(skinColor != null ? skinColor : "default");
  }

  @Override
  public void read(DataInputStream dataInputStream) throws IOException {
    playerUUID = new UUID(dataInputStream.readLong(), dataInputStream.readLong());
    skinColor = dataInputStream.readUTF();
  }

  @Override
  public void handlePacket() {
    // Find the player in the world and update their skin color
    Player player = (Player) Cubes.getClient().world.getEntity(playerUUID);
    if (player != null) {
      player.setSkinColor(skinColor);
    }
  }
}
