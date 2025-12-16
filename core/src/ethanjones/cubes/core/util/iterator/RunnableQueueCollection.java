package ethanjones.cubes.core.util.iterator;

import ethanjones.cubes.core.util.RunnableQueue;

import java.util.Iterator;

/**
 * ConcreteCollection #3 (ConcurrentLinkedQueue via {@link RunnableQueue}).
 */
public class RunnableQueueCollection implements IterableCollection<Inspectable> {

    private final RunnableQueue runnableQueue;

    public RunnableQueueCollection(RunnableQueue runnableQueue) {
        this.runnableQueue = runnableQueue;
    }

    @Override
    public CubesIterator<Inspectable> createIterator() {
        return new RunnableQueueIterator(runnableQueue);
    }

    /** ConcreteIterator for {@link RunnableQueueCollection}. */
    private static final class RunnableQueueIterator implements CubesIterator<Inspectable> {

        private final Iterator<Runnable> iterator;

        private RunnableQueueIterator(RunnableQueue runnableQueue) {
            // RunnableQueue.iterableView() iterates a safe snapshot of the underlying ConcurrentLinkedQueues
            this.iterator = runnableQueue == null ? null : runnableQueue.iterableView().iterator();
        }

        @Override
        public boolean hasMore() {
            return iterator != null && iterator.hasNext();
        }

        @Override
        public Inspectable getNext() {
            if (iterator == null) return new RunnableInspectable(null);
            return new RunnableInspectable(iterator.next());
        }
    }
}