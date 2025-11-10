package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Yang
 */

public class Main {
    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            return;
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                if (args.length > 1) {
                    Repository.operandsNumberError();
                }
                Repository.gitinit();
                break;
            case "add":
                if (!Repository.GITLET_DIR.exists()) {
                   Repository.initializeError();
                }
                Repository.add( args[1]);
                break;
            case "commit":
                Repository.commit(args[1]);
            case "rm":
                Repository.rm(args[1]);
            case "checkout":
                Repository.checkout();
            case "log":
                Repository.log();
            default:
               Repository.commendExistsError();

        }
    }

}
