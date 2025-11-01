package ethanjones.cubes.networking.packets;

import ethanjones.cubes.core.util.VectorUtil;
import ethanjones.cubes.entity.living.player.Player;
import ethanjones.cubes.networking.packet.Packet;
import ethanjones.cubes.networking.packet.PacketDirection;
import ethanjones.cubes.networking.packet.PacketDirection.Direction;

import com.badlogic.gdx.math.Vector3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

@Direction(PacketDirection.TO_CLIENT)
public class PacketOtherPlayerConnected extends Packet {
  //TODO PacketPlayerDisconnected ???
  public String username;
  public UUID uuid;
  public Vector3 angle;
  public Vector3 position;
  public String skinColor; // Skin color for visual customization

  @Override
  public void write(DataOutputStream dataOutputStream) throws IOException {
    dataOutputStream.writeUTF(username);
    dataOutputStream.writeLong(uuid.getMostSignificantBits());
    dataOutputStream.writeLong(uuid.getLeastSignificantBits());
    VectorUtil.stream(angle, dataOutputStream);
    VectorUtil.stream(position, dataOutputStream);
    dataOutputStream.writeUTF(skinColor != null ? skinColor : "default");
  }

  @Override
  public void read(DataInputStream dataInputStream) throws IOException {
    username = dataInputStream.readUTF();
    uuid = new UUID(dataInputStream.readLong(), dataInputStream.readLong());
    angle = VectorUtil.stream(dataInputStream);
    position = VectorUtil.stream(dataInputStream);
    skinColor = dataInputStream.readUTF();
  }

  @Override
  public void handlePacket() {
    Player player = new Player(username, uuid);
    player.position.set(position);
    player.angle.set(angle);
    player.setSkinColor(skinColor != null ? skinColor : "default");
    player.addToWorld();
  }

  @Override
  public String toString() {
    return super.toString() + " " + uuid.toString() + " " + username;
  }
}
