package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>{

    public class Node{
         T item;
         Node prev;
         Node next;
        public Node(Node pre,T t,Node n){
            this.prev=pre;
            item=t;
            next=n;
        }
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
    public LinkedListDeque(){
        size =0;
        first=new Node(null,null,null);
        last=new Node(first,null,null);
        first.next=last;
    }

    int size;
    Node first;
    Node last;



    @Override
    public void addFirst(T item) {
        Node node =new Node(first,item,first.next);
        first.next.prev=node;
        first.next=node;
        size=size+1;
    }

    @Override
    public void addLast(T item) {
        Node node=new Node(last.prev,item,last);
        last.prev.next=node;
        last.prev=node;
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
        if(isEmpty()) return;
        Node p=first.next;
        while(p!=last) {
            System.out.print(p.item);
            System.out.print(" ");
            p=p.next;
        }
        System.out.println();

    }

    @Override
    public T removeFirst() {
        if(size==0 ) return null;
        Node n= first.next;
        first.next=n.next;
        n.next.prev=first;
        size-=1;
        return n.item;
    }

    @Override
    public T removeLast() {
       if(size==0) return null;
       Node n=last.prev;
       last.prev=n.prev;
       n.prev.next=last;
       size-=1;
       return n.item;
    }

    @Override
    public T get(int index) {
        Node n=first.next;
        for(int i=0;i<size && n!=null;i++){
            if( i==index) return n.item;
            else n=n.next;
        }
        return null;
    }

    @Override
    public Iterator<T> iterator() {
        return new MyIterator();
    }

    public Node MygetRecursive (int index){
        if(index<0) return null;
        else if(index==0) return first.next;
        else return MygetRecursive(index-1).next;
    }
    public T getRecursive(int index){
       return MygetRecursive(index).item;
    }

}
