package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void timeGetLast() {
        // TODO: YOUR CODE HERE
        AList<Integer> Ns=new AList<Integer>();
        SLList<Integer> test=new SLList<Integer>();
        AList<Double> times =new AList<>();
        AList<Integer> opCounts=new AList<>();
        int k=1000;

        for(int i=0;i<7;i++){
            Ns.addLast(k);
            opCounts.addLast(10000);
            for(int j=0;j<k;j++){
                test.addLast(j);
            }
            Stopwatch sw = new Stopwatch();
            for(int j=0;j<10000;j++){
                test.getLast();
            }
            double timeInSeconds = sw.elapsedTime();
            times.addLast(timeInSeconds);
            k=k*2;
        }
        printTimingTable(Ns,times,opCounts);

    }

}
