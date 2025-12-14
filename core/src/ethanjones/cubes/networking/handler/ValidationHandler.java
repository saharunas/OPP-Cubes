package ethanjones.cubes.networking.handler;

import ethanjones.cubes.core.logging.Log;
import ethanjones.cubes.networking.packet.Packet;
import ethanjones.cubes.networking.Networking;

public class ValidationHandler extends BasePacketHandler {

//    @Override
//    public boolean handle(Packet packet, Networking networking) {
//        if (packet == null) return false;
//        return super.handle(packet, networking);
//    }

    @Override
    public boolean handle(Packet packet, Networking networking) {
        Log.info("[CoR] ValidationHandler: " + packet.getClass().getSimpleName());
        return super.handle(packet, networking);
    }
}