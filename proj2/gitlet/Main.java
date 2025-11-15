package gitlet;



import static gitlet.Repository.GITLET_DIR;
import static gitlet.Utils.*;



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
                    message("Incorrect operands.");return ;
                }
                Repository.gitinit();
                break;
            case "add":
                if (args.length > 2) {
                    message("Incorrect operands.");return ;
                }
                if(!GITLET_DIR.exists()) {
                    message("Not in an initialized Gitlet directory.");return ;
                }
                Repository.add(args[1]);
                break;
            case "commit":
                if (args.length > 2) {
                    message("Incorrect operands.");return ;
                }
                if(!GITLET_DIR.exists()) {
                    message("Not in an initialized Gitlet directory.");return ;
                }
                Repository.commit(args[1]);
                break;
            case "rm":
                if (args.length > 2) {
                    message("Incorrect operands.");return ;
                }
                if(!GITLET_DIR.exists()) {
                    message("Not in an initialized Gitlet directory.");return ;
                }
                Repository.rm(args[1]);
                break;
            case "checkout":
                if (args.length > 4) {
                    message("Incorrect operands.");return ;
                }
                if(!GITLET_DIR.exists()) {
                    message("Not in an initialized Gitlet directory.");return ;
                }
                if(args.length == 4) {
                    Repository.checkout(args[1],args[2],args[3]);
                }
                else if(args.length == 3) {
                    Repository.checkout(args[1],args[2]);
                }
                else if(args.length == 2) {
                    Repository.checkout(args[1]);
                }
                break;
            case "log":
                if (args.length > 1) {
                    message("Incorrect operands.");return ;
                }
                if(!GITLET_DIR.exists()) {
                    message("Not in an initialized Gitlet directory.");return ;
                }
                Repository.log();
                break;
            case "global-log":
                if (args.length > 1) {
                    message("Incorrect operands.");return ;
                }
                if(!GITLET_DIR.exists()) {
                    message("Not in an initialized Gitlet directory.");return ;
                }
                Repository.globalLog();
                break;
            case "find":
                if (args.length > 2) {
                    message("Incorrect operands.");return ;
                }
                if(!GITLET_DIR.exists()) {
                    message("Not in an initialized Gitlet directory.");return ;
                }
                Repository.find(args[1]);
                break;
            case "status":
                if (args.length > 1) {
                    message("Incorrect operands.");return ;
                }
                if(!GITLET_DIR.exists()) {
                    message("Not in an initialized Gitlet directory.");return ;
                }
                Repository.status();
                break;
            case "branch":
                if (args.length > 2) {
                    message("Incorrect operands.");return ;
                }
                if(!GITLET_DIR.exists()) {
                    message("Not in an initialized Gitlet directory.");return ;
                }
                Repository.branch(args[1]);
                break;
            case "rm-branch":
                if (args.length > 2) {
                    message("Incorrect operands.");return ;
                }
                if(!GITLET_DIR.exists()) {
                    message("Not in an initialized Gitlet directory.");return ;
                }
                Repository.rmBranch(args[1]);
                break;
            case "reset":
                if (args.length > 2) {
                    message("Incorrect operands.");return ;
                }
                if(!GITLET_DIR.exists()) {
                    message("Not in an initialized Gitlet directory.");return ;
                }
                Repository.reset(args[1]);
                break;
            case "merge":
                if (args.length > 2) {
                    message("Incorrect operands.");return ;
                }
                if(!GITLET_DIR.exists()) {
                    message("Not in an initialized Gitlet directory.");return ;
                }
                Repository.merge(args[1]);
                break;
            default:
                message("No command with that name exists.");
                return ;

        }
    }

}
