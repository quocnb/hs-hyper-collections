package collections;

import java.util.*;

public class BiMap <K, V> {
    private final Map<K, V> map = new HashMap<>();

    public V put(K key, V value) {
        if (map.containsKey(key) || map.containsValue(value)) {
            throw new IllegalArgumentException();
        }
        return map.put(key, value);
    }

    public void putAll(Map<K, V> map) {
        for (K key : map.keySet()) {
            put(key, map.get(key));
        }
    }

    public Set<V> values() {
        return new HashSet<>(map.values());
    }

    public V forcePut(K key, V value) {
        List<K> removeKey = new ArrayList<>();
        for (K k : map.keySet()) {
            if (Objects.equals(k, key) || Objects.equals(value, map.get(k))) {
                removeKey.add(k);
            }
        }
        for (K k : removeKey) {
            map.remove(k);
        }
        return map.put(key, value);
    }

    public BiMap<V, K> inverse() {
        BiMap<V, K> inverse = new BiMap<>();
        for (K key: map.keySet()) {
            inverse.forcePut(map.get(key), key);
        }
        return inverse;
    }

    @Override
    public String toString() {
        return map.toString();
    }

}
