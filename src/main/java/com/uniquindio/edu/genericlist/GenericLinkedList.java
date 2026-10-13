package com.uniquindio.edu.genericlist;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class GenericLinkedList<T> implements CustomList<T> {

    private int size;
    private GenericNode<T> firstNode;

    public GenericLinkedList() {
        this.size = 0;
        this.firstNode = null;
    }

    //Metodos de la interface CustomList

    @Override
    public void add(T data) {
        addLast(data);
    }

    @Override
    public T getElement(int index) {
        return getValueNode(index);
    }

    @Override
    public int size() {
        return size;
    }


    public void addFirst(T data){
        GenericNode<T> newNode = new GenericNode<>(data);
        if (isEmpty()){
            firstNode = newNode;
        } else {
            newNode.setNext(firstNode);
            firstNode = newNode;
        }
        size++;
    }

    public void addLast(T data){
        GenericNode<T> newNode = new GenericNode<>(data);
        if (isEmpty()){
            firstNode = newNode;
        } else {
            GenericNode<T> current = firstNode;
            while (current.getNext() != null) {
                current = current.getNext();
            }
            current.setNext(newNode);
        }
        size++;
    }

    public void add(T data, int index){
        if (index < 0 || index > size){
            throw new IndexOutOfBoundsException("Índice fuera del rango: " + index);
        }
        if (index == 0){
            addFirst(data);
            return;
        } else if (index == size){
            addLast(data);
            return;
        } else {
            GenericNode<T> newNode = new GenericNode<>(data);
            GenericNode<T> current = firstNode;
            for (int i = 0; i < index - 1; i++){
                current = current.getNext();
            }
            newNode.setNext(current.getNext());
            current.setNext(newNode);
            size++;
        }
    }

    public T getValueNode(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + index);
        }

        GenericNode<T> current = firstNode;
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }

        return current.getData();
    }

    public int getIndexNode(T node) {
        if (size == 0) {
            return -1;
        }

        GenericNode<T> current = firstNode;
        for (int i = 0; i < size; i++) {
            if (current.getData().equals(node)) {
                return i;
            }
            current = current.getNext();
        }

        return -1;
    }

    public void printListReverse(){
        printListReverse(firstNode);
        System.out.println();
    }

    private void printListReverse(GenericNode<T> node){
        if (node != null){
            printListReverse(node.getNext());
            System.out.print(node.getData() + " -> ");
        }
    }

    public void printList() {
        GenericNode<T> current = firstNode;
        while (current != null) {
            System.out.print(" -> " + current.getData());
            current = current.getNext();
        }
        System.out.println();
    }

    public boolean validIndex(int index){
        return index >= 0 && index < size;
    }

    @Override
    public boolean isEmpty() {
        return firstNode == null;
    }

    public void removeFirst(){
        if (size == 0){
            throw new IllegalStateException("No se puede remover, lista vacía");
        }
        firstNode = firstNode.getNext();
        size--;
    }

    public void removeLast(){
        if (size == 0) return;
        if (size == 1){
            removeFirst();
            return;
        }

        GenericNode<T> current = firstNode;
        for (int i = 0; i < size - 2; i++){
            current = current.getNext();
        }

        current.setNext(null);
        size--;
    }

    //Retorna true/false si fué removido
    @Override
    public boolean remove(T data){
        if (size == 0 || data == null){
            return false;
        }

        if (firstNode.getData().equals(data)) {
            firstNode = firstNode.getNext();
            size--;
            return true;
        }

        GenericNode<T> previous = firstNode;
        GenericNode<T> current = firstNode.getNext();

        while (current != null) {
            if (current.getData().equals(data)) {
                previous.setNext(current.getNext());
                size--;
                return true;
            }
            previous = current;
            current = current.getNext();
        }

        return false;
    }

    //Limpia nuestra lista
    public void clear(){
        size = 0;
        firstNode = null;
    }

    //Iterator permitiendo recorrer nuestra lista con un for-each
    @Override
    public Iterator<T> iterator() {
        return new GenericListIterator();
    }

    //Clase privada dentro de GenericLinkedList para cumplir con el encapsulamiento
    private class GenericListIterator implements Iterator<T>{
        private GenericNode<T> actualNode = firstNode;

        @Override
        public boolean hasNext() {
            return actualNode != null;
        }

        @Override
        public T next() {
            if(!hasNext()){
                throw new NoSuchElementException("No hay elementos en la lista");
            }

            T value = actualNode.getData();
            actualNode = actualNode.getNext();
            return value;
        }
    }
}
