package deque;

import deque.ArrayDeque;

import java.util.Arrays;
import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    Comparator<T> MyComparator ;
    public MaxArrayDeque(Comparator<T> c){
        MyComparator=c;
    }
    public T max(){
        if(size()==0)  return null;
        T max=get(0);
        for(int i=(first+1)%Maxsize;i!=last;i=(i+1)%Maxsize){
            if(MyComparator.compare(items[i],max)>0) max=items[i];
        }
        return max;
    }
    public T max(Comparator<T> c){
        if(size()==0)  return null;
        T max=get(0);
        for(int i=(first+1)%Maxsize;i!=last;i=(i+1)%Maxsize){
            if(c.compare(items[i],max)>0) max=items[i];
        }
        return max;
    }






}
