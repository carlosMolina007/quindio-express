package com.uniquindio.edu.genericlist;

public interface CustomList<T> extends Iterable<T>{

    void add(T element);
    boolean remove(T element);
    T getElement(int index);
    int size();
    boolean isEmpty();


}
