package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

import static gitlet.Repository.CWD;

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
                break;
            case "rm":
                Repository.rm(args[1]);
                break;
            case "checkout":
                if(args.length == 4) {
                    Repository.checkout(args[1],args[3]);
                }
                else if(args.length == 3){
                    Repository.checkout(args[2]);
                }
                else if(args.length == 2){
                    Repository.checkout(args[1]);
                }
                break;
            case "log":
                Repository.log();
                break;
            case "global-log":
                Repository.globalLog();
                break;
            case "find":
                Repository.find(args[1]);
                break;
            case "status":
                Repository.status();
                break;
            default:
               Repository.commendExistsError();

        }
    }

}
