package ethanjones.cubes.core.util.iterator;

import ethanjones.cubes.networking.packet.Packet;

public class PacketInspectable implements Inspectable {

    private final Packet packet;

    public PacketInspectable(Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return packet;
    }

    @Override
    public String inspect() {
        if (packet == null) return "<null packet>";
        // Packet.toString() already returns the simple class name
        return "Packet: " + packet.toString();
    }
}