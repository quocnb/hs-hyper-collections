package collections;

import java.util.*;

public class Multiset <E> {
    private final List<E> list = new ArrayList<>();

    /**
     * Adds a single occurrence of the specified element to the current multiset
     * @param element E element
     */
    public void add(E element) {
        this.list.add(element);
    }

    /**
     * Adds a number of occurrences of an element to the current multiset
     * @param element E element
     * @param occurrences number of occurrences
     */
    public void add(E element, int occurrences) {
        if (occurrences <= 0) {
            return;
        }
        while (occurrences > 0) {
            list.add(element);
            occurrences -= 1;
        }
    }

    /**
     * Determines whether the current multiset contains the specified element
     * @param element E element
     * @return true if the list contains element, otherwise else
     */
    public boolean contains(E element) {
        return list.contains(element);
    }

    /**
     * Returns the number of occurrences of an element in the current multiset (the count of the element)
     * @param element E element
     * @return number of occurrences of element
     */
    public int count(E element) {
        int count = 0;
        for (E e : list) {
            if (Objects.equals(e, element)) {
                count += 1;
            }
        }
        return count;
    }

    /**
     * Returns the set of distinct elements contained in the current multiset
     * @return set of distinct elements
     */
    public Set<E> elementSet() {
        return new HashSet<>(list);
    }

    /**
     * Removes a single occurrence of the specified element from the current multiset, if present
     * @param element remove element
     */
    public void remove(E element) {
        list.remove(element);
    }

    /**
     * Removes a number of occurrences of the specified element from the current multiset
     * @param element remove element
     * @param occurrences a number of occurrences
     */
    public void remove(E element, int occurrences) {
        while (occurrences > 0) {
            list.remove(element);
            occurrences -= 1;
        }
    }

    /**
     * Adds or removes the necessary occurrences of an element so that the element attains the desired count.
     * If there are no occurences, multiset's elements should not change.
     * @param element check element
     * @param count desired count
     */
    public void setCount(E element, int count) {
        if (count < 0) {
            return;
        }
        int currentOccurrences = count(element);
        if (currentOccurrences == 0) {
            return;
        }
        if (currentOccurrences > count) {
            remove(element, currentOccurrences - count);
        } else if (currentOccurrences < count) {
            int lastIndex = list.lastIndexOf(element);
            while (currentOccurrences < count) {
                list.add(lastIndex, element);
                currentOccurrences += 1;
            }
        }
    }

    /**
     * Conditionally sets the count of an element to a new value, as described in setCount(E element, int count),
     * provided that the element has the expected current count. If there are no occurences, multiset's elements should not change.
     * @param element element
     * @param oldCount old count
     * @param newCount new count
     */
    public void setCount(E element, int oldCount, int newCount) {
        int curCount = count(element);
        if (curCount == 0) {
            return;
        }
        if (curCount == oldCount) {
            setCount(element, newCount);
        }
    }

    @Override
    public String toString() {
        return list.toString();
    }

    public static void main(String[] args) {
        Multiset<Character> multiset = new Multiset<>();
        multiset.add('a');
        multiset.add('b', 6);

        System.out.println(multiset); // [a, b, b, b, b, b, b]
        System.out.println(multiset.contains('c')); // false
        System.out.println(multiset.count('b')); // 6
        System.out.println(multiset.elementSet()); // ['a', 'b']

        multiset.remove('a');
        multiset.remove('b', 3);

        System.out.println(multiset); // [b, b, b]

        multiset.add('c');
        multiset.setCount('c', 2);
        multiset.setCount('b', 3, 4);

        System.out.println(multiset); // [b, b, b, b, c, c]
    }
}
