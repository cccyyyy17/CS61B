package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V>{
    private class BSTNode{
        K key;
        V value;
        BSTNode left;
        BSTNode right;
    BSTNode(K k,V v,BSTNode l,BSTNode r){
            key=k;
            value=v;
            left=l;
            right=r; }

    }
    private BSTNode root = null;
    private int size = 0;
    @Override
    public void clear() {
        root=null;
        size=0;
    }

    @Override
    public boolean containsKey(K key) {
        BSTNode n=root;
        while(n!=null){
            if(n.key.compareTo(key)==0) return true;
            else if(n.key.compareTo(key)<0) n=n.right;
            else if(n.key.compareTo(key)>0) n=n.left;
        }
        return false;
    }

    @Override
    public V get(K key) {
        BSTNode n=root;
        while(n!=null){
            if(n.key.compareTo(key)==0) return n.value;
            else if(n.key.compareTo(key)<0) n=n.right;
            else if(n.key.compareTo(key)>0) n=n.left;
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        root=insert(root,key,value);
        size++;
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException("can return a set");
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException("This map is read-only");
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException("This map is read-only");
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException("can not to iterate");
    }
    private void printInOrder(BSTNode node){
        if(node == null) return;
        if(node.left!=null) printInOrder(node.left);
        System.out.println(node.value);
        if(node.right!=null) printInOrder(node.right);
    }
    private void printInOrder(){
        printInOrder(root);
    }
    private BSTNode insert(BSTNode n,K key,V value){
        if(n == null)  {return  new  BSTNode(key,value,null,null);}
        else if(key.compareTo(n.key)<0) {n.left  = insert(n.left,key,value);}
        else if(key.compareTo(n.key)>0) {n.right = insert(n.right,key,value);}
        return n;
    }
}
