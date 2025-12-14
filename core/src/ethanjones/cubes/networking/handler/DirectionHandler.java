package ethanjones.cubes.networking.handler;

import ethanjones.cubes.core.logging.Log;
import ethanjones.cubes.networking.packet.Packet;
import ethanjones.cubes.networking.packet.PacketDirection;
import ethanjones.cubes.networking.Networking;
import ethanjones.cubes.side.common.Side;

public class DirectionHandler extends BasePacketHandler {

    private final Side side;

    public DirectionHandler(Side side) {
        this.side = side;
    }

//    @Override
//    public boolean handle(Packet packet, Networking networking) {
//        PacketDirection.checkPacketReceive(packet.getClass(), side);
//        return super.handle(packet, networking);
//    }

    @Override
    public boolean handle(Packet packet, Networking networking) {
        PacketDirection.checkPacketReceive(packet.getClass(), side);
        return super.handle(packet, networking);
    }
}