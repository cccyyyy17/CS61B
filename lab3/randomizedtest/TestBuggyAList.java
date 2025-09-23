package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
    public static void main(String[] args){
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> bug=new BuggyAList<>();
        int N = 500;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 3);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                bug.addLast(randVal);
                System.out.println("addLast(" + randVal + ")");
            } else if (operationNumber == 1&& bug.size()!=0) {
                L.getLast();
                bug.getLast();
                System.out.println("addLast(" + L.getLast() + ")");

            }
            else if(operationNumber==2 && bug.size()!=0){
                L.removeLast();
                bug.removeLast();
                System.out.println("addLast(" +  L.removeLast() + ")");
            }
        }
    }
}
