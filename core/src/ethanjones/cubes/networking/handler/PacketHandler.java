package ethanjones.cubes.networking.handler;

import ethanjones.cubes.networking.packet.Packet;
import ethanjones.cubes.networking.Networking;

public interface PacketHandler {
    void setNext(PacketHandler next);
    boolean handle(Packet packet, Networking networking);
}