package ethanjones.cubes.core.util.iterator;

public interface CubesIterator<T> {

    boolean hasMore();

    T getNext();
}