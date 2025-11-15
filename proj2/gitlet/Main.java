package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

import static gitlet.Repository.GITLET_DIR;
import static gitlet.Utils.*;

import static gitlet.Repository.CWD;


/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Yang
 */

public class Main {
    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args)  {
        if (args.length == 0) {
            message("Please enter a command.");
            return;
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                if (args.length > 1) {
                    throw error("Incorrect operands.");
                }
                Repository.gitinit();
                break;
            case "add":
                if (args.length > 2) {
                    throw error("Incorrect operands.");
                }
                if(!GITLET_DIR.exists()){
                    throw error("Not in an initialized Gitlet directory.");
                }
                Repository.add( args[1]);
                break;
            case "commit":
                if (args.length > 2) {
                    throw error("Incorrect operands.");
                }
                if(!GITLET_DIR.exists()){
                    throw error("Not in an initialized Gitlet directory.");
                }
                Repository.commit(args[1]);
                break;
            case "rm":
                if (args.length > 2) {
                    throw error("Incorrect operands.");
                }
                if(!GITLET_DIR.exists()){
                    throw error("Not in an initialized Gitlet directory.");
                }
                Repository.rm(args[1]);
                break;
            case "checkout":
                if (args.length > 4) {
                    throw error("Incorrect operands.");
                }
                if(!GITLET_DIR.exists()){
                    throw error("Not in an initialized Gitlet directory.");
                }
                if(args.length == 4) {
                    Repository.checkout(args[1],null,args[3]);
                }
                else if(args.length == 3){
                    Repository.checkout(null,args[2]);
                }
                else if(args.length == 2){
                    Repository.checkout(args[1]);
                }
                break;
            case "log":
                if (args.length > 1) {
                    throw error("Incorrect operands.");
                }
                if(!GITLET_DIR.exists()){
                    throw error("Not in an initialized Gitlet directory.");
                }
                Repository.log();
                break;
            case "global-log":
                if (args.length > 1) {
                    throw error("Incorrect operands.");
                }
                if(!GITLET_DIR.exists()){
                    throw error("Not in an initialized Gitlet directory.");
                }
                Repository.globalLog();
                break;
            case "find":
                if (args.length > 2) {
                    throw error("Incorrect operands.");
                }
                if(!GITLET_DIR.exists()){
                    throw error("Not in an initialized Gitlet directory.");
                }
                Repository.find(args[1]);
                break;
            case "status":
                if (args.length > 1) {
                    throw error("Incorrect operands.");
                }
                if(!GITLET_DIR.exists()){
                    throw error("Not in an initialized Gitlet directory.");
                }
                Repository.status();
                break;
            case "branch":
                if (args.length > 2) {
                    throw error("Incorrect operands.");
                }
                if(!GITLET_DIR.exists()){
                    throw error("Not in an initialized Gitlet directory.");
                }
                Repository.branch(args[1]);
                break;
            case "rm-branch":
                if (args.length > 2) {
                    throw error("Incorrect operands.");
                }
                if(!GITLET_DIR.exists()){
                    throw error("Not in an initialized Gitlet directory.");
                }
                Repository.rmBranch(args[1]);
                break;
            case "reset":
                if (args.length > 2) {
                    throw error("Incorrect operands.");
                }
                if(!GITLET_DIR.exists()){
                    throw error("Not in an initialized Gitlet directory.");
                }
                Repository.reset(args[1]);
                break;
            case "merge":
                if (args.length > 2) {
                    throw error("Incorrect operands.");
                }
                if(!GITLET_DIR.exists()){
                    throw error("Not in an initialized Gitlet directory.");
                }
                Repository.merge(args[1]);
                break;
            default:
              throw error("No command with that name exists.");

        }
    }

}
