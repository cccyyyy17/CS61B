package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>{
    @Override
    public void addFirst(T item) {
        if(size==Maxsize) resize(Maxsize*2);
        items[first]=item;
        first=(first-1+Maxsize)%Maxsize;
        size+=1;
    }

    @Override
    public void addLast(T item) {
        if(size==Maxsize) resize(Maxsize*2);
        items[last]=item;
        last=(last+1+Maxsize)%Maxsize;
        size+=1;
    }

    @Override
    public boolean isEmpty() {
        return size==0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        for(int i=first;i!=last;i=(i+1)%Maxsize){
           System.out.print(items[i]);
           System.out.print(" ");
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if(size==0) return null;
       first=(first+1+Maxsize)%Maxsize;
       size-=1;
       if(size>50&&Maxsize/size >=4) resize(size*2);
       return items[first];
    }

    @Override
    public T removeLast() {
       if(size==0) return null;
       last=(last-1+Maxsize)%Maxsize;
       size-=1;
       if(size>50&&Maxsize/size >=4  ) resize(size*2);
       return items[last];
    }

    @Override
    public T get(int index) {
        return items[(first+index+1+Maxsize)%Maxsize];
    }

    @Override
    public Iterator<T> iterator() {
        return new MyIterator();
    }
    public ArrayDeque(){
        Maxsize=8;
        items = (T[]) new Object[Maxsize];
        size = 0;
        first=0;
        last=1;

    }

    int size;
    T[] items;
    int first;
    int last;
    int Maxsize;

    public void resize(int capacity){
        T[] Newitems = (T[]) new Object[capacity];
        for(int i=first, j=0;i!=last;i=(i+1+Maxsize)%Maxsize,j++){
            Newitems[j] =items[i];
        }
        items=Newitems;
        last= size;
        Maxsize=capacity;
        first=Maxsize-1;
    }
    public class MyIterator implements  Iterator{
        private int wizPos;
        public  MyIterator(){
            wizPos = 0;
        }
        @Override
        public boolean hasNext() {
            return wizPos<size;
        }

        @Override
        public Object next() {
            T returnItem=get(wizPos);
            wizPos+=1;
            return returnItem;
        }
    }
}
