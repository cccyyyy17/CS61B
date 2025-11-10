package gitlet;

import com.sun.source.tree.Tree;
import edu.princeton.cs.algs4.ST;
import jdk.jshell.execution.Util;

import java.io.File;
import java.io.IOException;
import java.util.Map;
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
    public static final File STAGE = join(GITLET_DIR, "stage");
    public static final File REMOVALSTAGE = join(GITLET_DIR, "removal stage");
    public static final File HEAD = join(GITLET_DIR, "head");
    public static TreeMap<String,String> stage ;
    public static TreeMap<String,String> removalStage ;
    public static String Head ;
    public static String Master;
    public static void gitinit() throws IOException {
        if(!GITLET_DIR.exists()) {
            try{
                GITLET_DIR.mkdir();
                COMMIT_DIR.mkdir();
                BLOB_DIR.mkdir();
                /*创建stage暂存区(一个TreeMap对象)并持久化保存(创建对应的文件)*/
                stage = new TreeMap<>();
                removalStage = new TreeMap<>();
                STAGE.createNewFile();
                REMOVALSTAGE.createNewFile();
                HEAD.createNewFile();
            } catch (IOException e){
                throw new RuntimeException(e);
            }
            /* */
            Utils.writeObject(STAGE,stage);
            Utils.writeObject(REMOVALSTAGE,removalStage);
            Commit c = new Commit("initial commit",null,null,null);
            Head = c.getHash();
            c.saveCommit();
            Utils.writeObject(HEAD,Head);
        }

    }

    public static void add(String fileName){
        Blob b = new Blob(Utils.join(CWD,fileName));
        b.saveBlob();
        b.dump();
        stage = Utils.readObject(STAGE,TreeMap.class);
        stage.put(fileName,b.getHash());
        Utils.writeObject(STAGE,stage);
    }

    public static void commit(String message){
        stage = Utils.readObject(STAGE,TreeMap.class);
        Commit c = new Commit(message,Head,null,stage);
        c.saveCommit();
        c.dump();
        stage.clear();
        Utils.writeObject(STAGE,stage);
    }

    public static void rm(String fileName){
        stage = Utils.readObject(STAGE,TreeMap.class);
        removalStage = Utils.readObject(REMOVALSTAGE,TreeMap.class);
        Blob b = new Blob(Utils.join(CWD,fileName));
        if (stage.containsValue(b.getHash())) {
            stage.remove(b.getHash());
        }
        /*读取当前Commit对象及其TreeMap对象，如果包含rm的FIle，就加入removalStage并删除源文件(Blob文件没有存入电脑)*/
        Head = Utils.readObject(HEAD, String.class);
        File currentCommitFile = Utils.join(COMMIT_DIR,Head);
        Commit currentCommit = Utils.readObject(currentCommitFile,Commit.class);
        TreeMap<String,String> Date = currentCommit.getData();
        for (Map.Entry<String, String> entry : Date.entrySet()) {
            if (entry.getKey().equals(b.getHash())) {
                removalStage.put(entry.getKey(), entry.getValue());
            }
        }
        File deleteFile = Utils.join(CWD,fileName);
        Utils.restrictedDelete(deleteFile);

    }

    public static void checkout(){

    }

    public static void log(){

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
