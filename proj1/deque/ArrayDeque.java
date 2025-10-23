package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    @Override
    public void addFirst(T item) {
        if (size == maxSize) {
            resize(maxSize * 2);
        }
        items[first] = item;
        first = (first - 1 + maxSize) % maxSize;
        size += 1;
    }

    @Override
    public boolean equals(Object o) {
        // 先进行类型检验
        if (o instanceof Deque) {
            Deque<T> otherArrayDeque = ( Deque<T>) o;
            if (size != otherArrayDeque.size()) {
                return false;
            }
            for (int i = 0; get(i) != null; i++) {
                if (!this.get(i).equals(otherArrayDeque.get(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void addLast(T item) {
        if (size == maxSize) {
            resize(maxSize * 2);
        }
        items[last] = item;
        last = (last + 1 + maxSize) % maxSize;
        size += 1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        for (int i = first; i != last; i = (i + 1) % maxSize) {
            System.out.print(items[i]);
            System.out.print(" ");
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        first = (first + 1 + maxSize) % maxSize;
        size -= 1;
        T returnValue = items[first];
        if (maxSize > 50 && maxSize / size >= 4) {
            resize(size + 1);
        }
        return returnValue;
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        last = (last - 1 + maxSize) % maxSize;
        size -= 1;
        T returnValue = items[last];
        if (maxSize > 50 && maxSize / size >= 4) {
            resize(size + 1);
        }
        return returnValue;
    }

    @Override
    public T get(int index) {
        if (index == maxSize) {
            return null;
        }
        return items[(first + index + 1 + maxSize) % maxSize];
    }

    public Iterator<T> iterator() {
        return new MyIterator();
    }

    public ArrayDeque() {
        maxSize = 8;
        items = (T[]) new Object[maxSize];
        size = 0;
        first = 0;
        last = 1;
    }

    private int size;
    private T[] items;
    private int first;
    private int last;
    private int maxSize;

    private void resize(int capacity) {
        T[] newItems = (T[]) new Object[capacity];
        for (int i = (first + 1) % maxSize, j = 0;
             j != size; i = (i + 1 + maxSize) % maxSize, j++) {
            newItems[j] = items[i];
        }
        items = newItems;
        last = size;
        maxSize = capacity;
        first = maxSize - 1;
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
}
