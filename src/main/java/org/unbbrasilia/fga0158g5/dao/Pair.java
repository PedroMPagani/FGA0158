package org.unbbrasilia.fga0158g5.dao;

public class Pair<T, K> {

    private final T key;
    private final K value;

    public Pair(T key, K value){
        this.key= key;
        this.value = value;
    }

    public K getValue() {
        return value;
    }

    public T getKey() {
        return key;
    }

}