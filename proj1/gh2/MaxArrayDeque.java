package gh2;

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
        for(int i=0;get(i)!=null;i++){
            if(MyComparator.compare(get(i),max)>0) max=get(i);
        }
        return max;
    }
    public T max(Comparator<T> c){
        if(size()==0)  return null;
        T max=get(0);
        for(int i=0;get(i)!=null;i++){
            if(c.compare(get(i),max)>0) max=get(i);
        }
        return max;
    }






}
