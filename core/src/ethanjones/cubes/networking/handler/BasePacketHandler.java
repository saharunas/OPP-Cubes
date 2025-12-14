package ethanjones.cubes.networking.handler;

import ethanjones.cubes.networking.packet.Packet;
import ethanjones.cubes.networking.Networking;

public abstract class BasePacketHandler implements PacketHandler {

    protected PacketHandler next;

    @Override
    public void setNext(PacketHandler next) {
        this.next = next;
    }

    @Override
    public boolean handle(Packet packet, Networking networking) {
        if (next != null) {
            return next.handle(packet, networking);
        }
        return false;
    }
}