package hashmap;

import javax.xml.crypto.dsig.keyinfo.KeyValue;
import java.security.Key;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    @Override
    public void clear() {
        size=0;
        KeySet=null;
        buckets = createTable(MaxSize);
    }

    @Override
    public boolean containsKey(K key) {
        int keyvalue=MapKey(MaxSize,key.hashCode());
        for(Node n :buckets[keyvalue]){
            if(n.key.equals( key)) return true;
        }
        return false;
    }

    @Override
    public V get(K key) {
        int keyvalue=MapKey(MaxSize,key.hashCode());
        for(Node n :buckets[keyvalue]){
            if(n.key.equals(key) ) {return n.value;}
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        int hashvalue = key.hashCode();
        hashvalue = MapKey(MaxSize,hashvalue);
        if(!containsKey(key)) {
            buckets[hashvalue].add(createNode(key,value));
            size++;
            KeySet.add(key);
        }
        else {
            for(Node n :buckets[hashvalue]){
                if(n.key.equals( key)) {
                    n.value=value;
                }
            }
        }
        if((double) size /MaxSize>=maxLoad) { resize();}
    }
    private void resize(){
        MaxSize=MaxSize*2;
        Collection<Node>[] oldbuckets = buckets;
        buckets = createTable(MaxSize);
        for(int i = 0;i < MaxSize/2; i++){
            for(Node n :oldbuckets[i]){
                int hashvalue = n.key.hashCode();
                hashvalue = MapKey(MaxSize,hashvalue);
                buckets[hashvalue].add(createNode(n.key,n.value));
            }
        }
    }
    @Override
    public Set<K> keySet() {
        if(KeySet.isEmpty())  return Set.of();
        else return KeySet;

    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException("Read Only");
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException("Read Only");
    }

    @Override
    public Iterator<K> iterator() {
        return KeySet.iterator();
    }

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }
    private double maxLoad;
    private int MaxSize;
    private HashSet<K> KeySet= new HashSet<>();
    private int size;
    private Collection<Node>[] buckets;

    private int MapKey(int Maxsize,int keyhash){
        while(keyhash<0) keyhash+=Maxsize;
        while(keyhash>=Maxsize) keyhash=keyhash%Maxsize;
        return keyhash;
    }

    public MyHashMap() {
        size = 0;
        MaxSize=16;
        buckets = createTable(16);
        this.maxLoad=0.75;
    }

    public MyHashMap(int initialSize) {
        size = 0;
        MaxSize=initialSize;
        buckets = createTable(initialSize);
        this.maxLoad=0.75;
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        MaxSize = initialSize;
        buckets = createTable(initialSize);
        this.maxLoad = maxLoad;
        size = 0;
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        Node n=new Node(key,value);
        return n;
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        Collection<Node>[] newbuckets= new Collection[tableSize];
        for(int i=0;i<tableSize;i++){
            newbuckets[i] = createBucket();
        }
        return newbuckets;
    }


}
