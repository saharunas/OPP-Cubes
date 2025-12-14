package ethanjones.cubes.networking.handler;

import ethanjones.cubes.networking.packet.Packet;
import ethanjones.cubes.networking.Networking;
import ethanjones.cubes.core.logging.Log;

public class FallbackHandler extends BasePacketHandler {

//    @Override
//    public boolean handle(Packet packet, Networking networking) {
//        Log.warning("Unhandled packet: " + packet.getClass().getSimpleName());
//        return false;
//    }

    @Override
    public boolean handle(Packet packet, Networking networking) {
        Log.warning("[CoR] FallbackHandler: Unhandled " + packet.getClass().getSimpleName());
        return false;
    }
}