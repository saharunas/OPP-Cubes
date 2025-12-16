package ethanjones.cubes.core.util.iterator;

public interface IterableCollection<T> {
    CubesIterator<T> createIterator();
}
