package gitlet;

import com.sun.source.tree.Tree;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Date; // TODO: You'll likely use this in this class
import java.util.Map;
import java.util.TreeMap;

import static gitlet.Repository.BLOB_DIR;
import static gitlet.Repository.COMMIT_DIR;
import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable{
    /** The message of this Commit. */
    private String message;
    /** Time record. */
    private Date timestamp;
    /** Parent reference.  */
    private Commit parent;
    /** Second Parent reference.  */
    private Commit parent2;
    /*How to define blob reference */
    private TreeMap<String,Blob> Data;
    public Commit(String m,Commit p1,Commit p2,TreeMap<String,Blob> data){
        message = m;
        parent = p1;
        parent2 = p2;
        Data = data;
        if(p1==null) timestamp = new Date(0);
        else timestamp = new Date();
    }
    public  void saveCommit(){
        File outFile = Utils.join(COMMIT_DIR, getHash());
        if (!outFile.exists()) {
            try {
                outFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        writeObject(outFile, this);
    }

    public static Commit fromFile(File file){
        return readObject(file,Commit.class);
    }
    public String getHash() {
        return sha1(this.message+this.timestamp+ this.parent);
    }
}
