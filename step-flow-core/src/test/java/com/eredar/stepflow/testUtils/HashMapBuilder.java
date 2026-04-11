package com.eredar.stepflow.testUtils;


import java.util.HashMap;

public class HashMapBuilder<K, V> {

    private final HashMap<K, V> map;

    public HashMapBuilder(HashMap<K, V> map) {
        this.map = map;
    }

    public HashMapBuilder<K, V> builder() {
        return new HashMapBuilder<>(new HashMap<>());
    }

    public HashMapBuilder<K, V> put(K key, V value) {
        this.map.put(key, value);
        return this;
    }

    public HashMap<K, V> build() {
        return this.map;
    }
}
