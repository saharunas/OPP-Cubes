package ethanjones.cubes.core.util.iterator;

import ethanjones.cubes.networking.packet.Packet;
import ethanjones.cubes.networking.packet.PriorityPacketQueue;

import java.util.Iterator;

/**
 * ConcreteCollection #2 (PriorityBlockingQueue via {@link PriorityPacketQueue}).
 */
public class PriorityPacketQueueCollection implements IterableCollection<Inspectable> {

    private final PriorityPacketQueue packetQueue;

    public PriorityPacketQueueCollection(PriorityPacketQueue packetQueue) {
        this.packetQueue = packetQueue;
    }

    @Override
    public CubesIterator<Inspectable> createIterator() {
        return new PriorityPacketQueueIterator(packetQueue);
    }

    /** ConcreteIterator for {@link PriorityPacketQueueCollection}. */
    private static final class PriorityPacketQueueIterator implements CubesIterator<Inspectable> {

        private final Iterator<Packet> iterator;

        private PriorityPacketQueueIterator(PriorityPacketQueue packetQueue) {
            // PriorityPacketQueue.iterableView() iterates the underlying PriorityBlockingQueue
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