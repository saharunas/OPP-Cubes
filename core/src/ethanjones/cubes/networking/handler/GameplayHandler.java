package ethanjones.cubes.networking.handler;

import ethanjones.cubes.core.logging.Log;
import ethanjones.cubes.networking.packet.Packet;
import ethanjones.cubes.networking.Networking;

public class GameplayHandler extends BasePacketHandler {

//    @Override
//    public boolean handle(Packet packet, Networking networking) {
//
//        if (packet.getClass().getPackage().getName()
//                .contains("networking.packets")) {
//
//            packet.handlePacket();
//            return true;
//        }
//
//        return super.handle(packet, networking);
//    }

    @Override
    public boolean handle(Packet packet, Networking networking) {
        Log.info("[CoR] GameplayHandler: " + packet.getClass().getSimpleName());

        if (packet.getClass().getPackage().getName().contains("networking.packets")) {
            packet.handlePacket();
            return true;
        }
        return super.handle(packet, networking);
    }
}