package gitlet;

import com.sun.source.tree.Tree;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

import static gitlet.Repository.*;
import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable,Dumpable{
    /** The message of this Commit. */
    private String message;
    /** Time record. */
    private Date timestamp;
    /** Parent reference.  */
    private String parent;
    /** Second Parent reference.  */
    private String parent2;

    private TreeMap<String,String> Data;
    public Commit(String m,String p1,String p2,TreeMap<String,String> data){
        message = m;
        parent = p1;
        parent2 = p2;
        Data = data;
        if(p1 == null) timestamp = new Date(0L);
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
        StringBuilder content = new StringBuilder(this.message + this.timestamp + this.parent);
        if(Data != null){
            for(String s : Data.values()){
                content.append(s);
            }
        }

        return sha1(content.toString());
    }

    public String getMessage(){
        return message;
    }

    public String getFormattedDate() {
        return String.format(Locale.ENGLISH,
                "%ta %tb %td %tT %tY %tz",
                timestamp, timestamp, timestamp, timestamp, timestamp, timestamp);
    }
    public String getParent(){
        return parent;
    }
    public TreeMap<String,String> getData(){
        return Data;
    }
    @Override
    public void dump() {
        System.out.println("Hash:"+getHash());
        System.out.printf("message: %s%n",message);
        System.out.printf("parent: %s%n",parent);
        System.out.println("content:");
        for (Map.Entry<String, String> entry : Data.entrySet()) {
            File blobFile = join(BLOB_DIR,entry.getValue());
            Blob blob = readObject(blobFile, Blob.class);
            System.out.println(entry.getKey() + " -> " + entry.getValue() + " -> " + blob.getContent());
        }
    }
}
