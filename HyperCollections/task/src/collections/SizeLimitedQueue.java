package collections;

import java.util.*;

public class SizeLimitedQueue<E> {
    private final int maxSize;
    private final Queue<E> queue = new ArrayDeque<>();

    public SizeLimitedQueue(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("Maximum size must be positive");
        }
        this.maxSize = maxSize;
    }

    public void add(E element) {
        if (queue.size() == maxSize) {
            queue.poll();
        }
        queue.add(element);
    }

    public void clear() {
        queue.clear();
    }

    public boolean isAtFullCapacity() {
        return queue.size() == maxSize;
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public int maxSize() {
        return maxSize;
    }

    public E peek() {
        return queue.peek();
    }

    public E remove() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        return queue.remove();
    }

    public int size() {
        return queue.size();
    }

    public E[] toArray(E[] e) {
        return queue.toArray(e);
    }

    public Object[] toArray() {
        return queue.toArray();
    }

    @Override
    public String toString() {
        return queue.stream().toList().toString();
    }
}
