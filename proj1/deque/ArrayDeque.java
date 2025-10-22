package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    @Override
    public void addFirst(T item) {
        if (size == Maxsize) {
            resize(Maxsize * 2);
        }
        items[first] = item;
        first = (first - 1 + Maxsize) % Maxsize;
        size += 1;
    }

    @Override
    public boolean equals(Object o) {
        // 先进行类型检验
        if (o instanceof ArrayDeque otherArrayDeque) {
            if (size != otherArrayDeque.size()) {
                return false;
            }
            for (int i = 0; get(i) != null; i++) {
                if (otherArrayDeque.get(i) != this.get(i)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void addLast(T item) {
        if (size == Maxsize) {
            resize(Maxsize * 2);
        }
        items[last] = item;
        last = (last + 1 + Maxsize) % Maxsize;
        size += 1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        for (int i = first; i != last; i = (i + 1) % Maxsize) {
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
        first = (first + 1 + Maxsize) % Maxsize;
        size -= 1;
        T ReturnValue = items[first];
        if (size > 50 && Maxsize / size >= 4) {
            resize(size + 1);
        }
        return ReturnValue;
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        last = (last - 1 + Maxsize) % Maxsize;
        size -= 1;
        T ReturnValue = items[last];
        if (size > 50 && Maxsize / size >= 4) {
            resize(size + 1);
        }
        return ReturnValue;
    }

    @Override
    public T get(int index) {
        if (index == Maxsize) {
            return null;
        }
        return items[(first + index + 1 + Maxsize) % Maxsize];
    }

    public Iterator<T> iterator() {
        return new MyIterator();
    }

    public ArrayDeque() {
        Maxsize = 8;
        items = (T[]) new Object[Maxsize];
        size = 0;
        first = 0;
        last = 1;
    }

    private int size;
    private T[] items;
    private int first;
    private int last;
    private int Maxsize;

    private void resize(int capacity) {
        T[] Newitems = (T[]) new Object[capacity];
        for (int i = (first + 1) % Maxsize, j = 0; j != size; i = (i + 1 + Maxsize) % Maxsize, j++) {
            Newitems[j] = items[i];
        }
        items = Newitems;
        last = size;
        Maxsize = capacity;
        first = Maxsize - 1;
    }

    private class MyIterator implements Iterator<T> {
        private int wizPos;

        public MyIterator() {
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