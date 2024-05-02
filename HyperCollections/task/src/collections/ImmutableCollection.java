package collections;

import java.util.*;
import java.util.function.Consumer;

public final class ImmutableCollection <E> implements Iterable<E> {

    List<E> items = new ArrayList<>();

    private ImmutableCollection() { }

    public static <T> ImmutableCollection<T> of() {
        return new ImmutableCollection<T>();
    }

    public static <E> ImmutableCollection<E> of(E... elements) {
        ImmutableCollection<E> collection = new ImmutableCollection<>();
        for (E element : elements) {
            if (element == null) {
                throw  new NullPointerException();
            }
            collection.items.add(element);
        }
        return collection;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int size() {
        return items.size();
    }

    public boolean contains(E element) {
        return items.contains(element);
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        Iterable.super.forEach(action);
    }

    @Override
    public Spliterator<E> spliterator() {
        return Iterable.super.spliterator();
    }
}
