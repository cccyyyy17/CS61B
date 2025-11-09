package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.TreeMap;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Yang
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File COMMIT_DIR = join(GITLET_DIR, "commit");
    public static final File BLOB_DIR = join(GITLET_DIR, "blob");
    public static TreeMap<String,Blob> stage = new TreeMap<>();
    public static Commit Head ;
    public static Commit Master;
    public static void gitinit() {
        if(!GITLET_DIR.exists()) {
            GITLET_DIR.mkdir();
            COMMIT_DIR.mkdir();
            BLOB_DIR.mkdir();

            Head = new Commit("initial commit",null,null,null);
            Head.saveCommit();
        }

    }

    public static void add(String filename){
        Blob b = new Blob(Utils.join(CWD,filename));
        b.saveBlob();
        stage.put(filename,b);
    }

    public static void commit(String message){
        Commit c = new Commit(message,Head,null,stage);
        c.saveCommit();

    }
    public static void operandsNumberError(){
        System.out.println("Not in an initialized Gitlet directory.");
        System.exit(0);
    }

    public static void initializeError(){
        System.out.println("Not in an initialized Gitlet directory.");
        System.exit(0);
    }

    public static void commendExistsError(){
        System.out.println("No command with that name exists.");
        System.exit(0);
    }
}
