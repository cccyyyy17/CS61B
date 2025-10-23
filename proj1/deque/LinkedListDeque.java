package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {

    private class Node {
        T item;
        Node prev;
        Node next;
         Node(Node pre, T t, Node n) {
            this.prev = pre;
            item = t;
            next = n;
        }
    }

    private class MyIterator implements Iterator<T> {
        private int wizPos;
         MyIterator() {
            wizPos = 0;
        }
        @Override
        public boolean hasNext() {
            return wizPos < size;
        }

        @Override
        public T next() {
            T returnItem = get(wizPos);
            wizPos += 1;
            return returnItem;
        }
    }

    public LinkedListDeque(){
        size = 0;
        first = new Node(null, null, null);
        last = new Node(first, null, null);
        first.next = last;
    }

    private int size;
    private Node first;
    private Node last;

    @Override
    public void addFirst(T item) {
        Node node = new Node(first, item, first.next);
        first.next.prev = node;
        first.next = node;
        size = size + 1;
    }

    @Override
    public boolean equals(Object o) {
        // 先进行类型检验
        if (o instanceof Deque) {
            LinkedListDeque<T> otherLinkListDeque = (LinkedListDeque<T>) o;
            if (size != otherLinkListDeque.size()) {
                return false;
            }
            for (int i = 0; i<size; i++) {
                if (!this.get(i).equals(otherLinkListDeque.get(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void addLast(T item) {
        Node node = new Node(last.prev, item, last);
        last.prev.next = node;
        last.prev = node;
        size += 1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        if (isEmpty()) {
            return;
        }
        Node p = first.next;
        while (p != last) {
            System.out.print(p.item);
            System.out.print(" ");
            p = p.next;
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        Node n = first.next;
        first.next = n.next;
        n.next.prev = first;
        size -= 1;
        return n.item;
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        Node n = last.prev;
        last.prev = n.prev;
        n.prev.next = last;
        size -= 1;
        return n.item;
    }

    @Override
    public T get(int index) {
        if (index == size) {
            return null;
        }
        Node n = first.next;
        for (int i = 0; i < size && n != null; i++) {
            if (i == index) {
                return n.item;
            } else {
                n = n.next;
            }
        }
        return null;
    }

    @Override
    public Iterator<T> iterator() {
        return new MyIterator();
    }

    private Node myGetRecursive(int index) {
        if (index < 0) {
            return null;
        } else if (index == 0) {
            return first.next;
        } else {
            return myGetRecursive(index - 1).next;
        }
    }

    public T getRecursive(int index) {
        return myGetRecursive(index).item;
    }
}
