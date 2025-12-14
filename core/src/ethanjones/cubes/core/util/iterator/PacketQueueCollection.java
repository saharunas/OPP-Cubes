package ethanjones.cubes.core.util.iterator;

import ethanjones.cubes.networking.packet.Packet;
import ethanjones.cubes.networking.packet.PacketQueue;

import java.util.Iterator;

public class PacketQueueCollection implements IterableCollection<Inspectable> {

    private final PacketQueue packetQueue;

    public PacketQueueCollection(PacketQueue packetQueue) {
        this.packetQueue = packetQueue;
    }

    @Override
    public CubesIterator<Inspectable> createIterator() {
        return new PacketQueueIterator(packetQueue);
    }

    private static final class PacketQueueIterator implements CubesIterator<Inspectable> {

        private final Iterator<Packet> iterator;

        private PacketQueueIterator(PacketQueue packetQueue) {
            // PacketQueue.iterableView() iterates the underlying LinkedBlockingQueue
            this.iterator = packetQueue == null ? null : packetQueue.iterableView().iterator();
        }

        @Override
        public boolean hasMore() {
            return iterator != null && iterator.hasNext();
        }

        @Override
        public Inspectable getNext() {
            if (iterator == null) return new PacketInspectable(null);
            return new PacketInspectable(iterator.next());
        }
    }
}