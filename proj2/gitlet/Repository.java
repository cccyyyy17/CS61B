package gitlet;

import com.sun.source.tree.Tree;

import java.util.*;

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
    @SuppressWarnings("unchecked")
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File COMMIT_DIR = join(GITLET_DIR, "commit");
    public static final File BLOB_DIR = join(GITLET_DIR, "blob");
    public static final File REFS_DIR = join(GITLET_DIR,"refs");

    public static final File STAGE = join(GITLET_DIR, "stage");
    public static final File REMOVALSTAGE = join(GITLET_DIR, "removal stage");
    public static final File HEAD = join(GITLET_DIR, "head");
    /*当前Head指向的分支名称，例如master*/
    public static final File CURBRANCHNAME = join(GITLET_DIR, "curbranch");
    public static TreeMap<String,String> stage ;
    public static TreeMap<String,String> removalStage ;
    public static void gitinit() throws IOException {
        if(!GITLET_DIR.exists()) {
            try{
                GITLET_DIR.mkdir();
                COMMIT_DIR.mkdir();
                BLOB_DIR.mkdir();
                REFS_DIR.mkdir();
                /*创建stage暂存区(一个TreeMap对象)并持久化保存(创建对应的文件)*/
                stage = new TreeMap<>();
                removalStage = new TreeMap<>();
                STAGE.createNewFile();
                REMOVALSTAGE.createNewFile();
                HEAD.createNewFile();
                CURBRANCHNAME.createNewFile();
            } catch (IOException e){
                throw new RuntimeException(e);
            }
            /* */
            Utils.writeObject(STAGE,stage);
            Utils.writeObject(REMOVALSTAGE,removalStage);
            Commit c = new Commit("initial commit",null,null,new TreeMap<>());
            String Head = c.getHash();
            c.saveCommit();
            Utils.writeObject(HEAD,Head);
            writeObject(CURBRANCHNAME,"master");
            updateCurrentBranch();
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
        String  Head = Utils.readObject(HEAD, String.class);
        Commit c = new Commit(message,Head,null,stage);
        Head = c.getHash();
        c.saveCommit();
        c.dump();
        stage.clear();
        Utils.writeObject(HEAD,Head);
        Utils.writeObject(STAGE,stage);
        updateCurrentBranch();
    }

    public static void rm(String fileName){
        /*根据文件名字来remove*/
        stage = Utils.readObject(STAGE,TreeMap.class);
        removalStage = Utils.readObject(REMOVALSTAGE,TreeMap.class);
        Blob b = new Blob(Utils.join(CWD,fileName));
        if (stage.containsKey(fileName)) {
            stage.remove(fileName);
        }
        /*读取当前Commit对象及其TreeMap对象，如果包含rm的File，就加入removalStage并删除源文件(Blob文件没有存入电脑)*/
        String Head = Utils.readObject(HEAD, String.class);
        File currentCommitFile = Utils.join(COMMIT_DIR,Head);
        Commit currentCommit = Utils.readObject(currentCommitFile,Commit.class);
        TreeMap<String,String> Date = currentCommit.getData();
        for (Map.Entry<String, String> entry : Date.entrySet()) {
            if (entry.getKey().equals(b.getHash())) {
                removalStage.put(entry.getKey(), entry.getValue());
                File deleteFile = Utils.join(CWD,fileName);
                Utils.restrictedDelete(deleteFile);
            }
        }
        Utils.writeObject(STAGE,stage);
        Utils.writeObject(REMOVALSTAGE,removalStage);

    }

    private static Commit commitHashToCommit(String hash){
        File currentCommitFile = Utils.join(COMMIT_DIR,hash);
        return Utils.readObject(currentCommitFile,Commit.class);
    }
    private static String blobHashToContent(String blobHash){
        File blobFile = join(BLOB_DIR,blobHash);
        Blob b = readObject(blobFile,Blob.class);
        return b.getContent();
    }
    private static void updateCurrentBranch(){
        String currentBranchName = readObject(CURBRANCHNAME,String.class);
        File currentBranch = join(REFS_DIR,currentBranchName);
        String writeContent = readObject(HEAD,String.class);
        writeObject(currentBranch,writeContent);
    }
    private static void deleteCommitData(String commitHash){
        Commit c = readObject(join(COMMIT_DIR,commitHash),Commit.class);
        TreeMap<String,String> data = c.getData();
        if(data == null) {return ;}
        for(Map.Entry<String,String> entry : data.entrySet()){
            File trackedFile = join(CWD,entry.getKey());
            restrictedDelete(trackedFile);
        }
    }
    private static void putCommitData(String commitHash){
        Commit c = readObject(join(COMMIT_DIR,commitHash),Commit.class);
        TreeMap<String,String> data = c.getData();
        if(data == null) {return ;}
        for(Map.Entry<String,String> entry : data.entrySet()){
            File trackedFile = join(CWD,entry.getKey());
            try{
                trackedFile.createNewFile();
                writeContents(trackedFile,blobHashToContent(entry.getValue()));
            }catch(IOException e){
                throw new RuntimeException(e);
            }
        }
    }

    public static void checkout(String s,String fileName)  {
      Commit head = commitHashToCommit(readObject(HEAD,String.class));
      TreeMap<String ,String> data = head.getData();
      if(data == null) {return ;}
      for(Map.Entry<String,String> entry : data.entrySet()){
          if(entry.getKey().equals(fileName)) {
              File file = join(CWD,fileName);
              if(!file.exists()) {  try{file.createNewFile();}catch (IOException e){throw new RuntimeException(e);}}
              writeContents(file,blobHashToContent(entry.getValue()));
          }
      }

    }

    public static void checkout(String commitId,String s,String fileName){
        Commit commit = commitHashToCommit(commitId);
        TreeMap<String ,String> data = commit.getData();
        if(data == null) {return ;}
        for(Map.Entry<String,String> entry : data.entrySet()){
            if(entry.getKey().equals(fileName)) {
                File file = join(CWD,fileName);
                if(!file.exists()) {  try{file.createNewFile();}catch (IOException e){throw new RuntimeException(e);}}
                writeContents(file,blobHashToContent(entry.getValue()));
            }
            }
        }

    public static void checkout(String branchName){
        /*选中当前分支就直接return*/
        if(branchName.equals(readObject(CURBRANCHNAME,String.class)) ){
            System.out.println("No need to checkout the current branch.");
            return ;
        }
        /*删除当前Commit所有data，恢复所有branch Commit的data*/
        stage = readObject(STAGE,TreeMap.class);
        stage.clear();
        writeObject(STAGE,stage);
        String headHash = readObject(HEAD,String.class);
        deleteCommitData(headHash);
        String branchHash = readObject(join(REFS_DIR,branchName),String.class);
        putCommitData(branchHash);
        writeObject(HEAD,branchHash);
    }




    public static void log() {
        String CommitFileName = Utils.readObject(HEAD, String.class);

        while (CommitFileName != null) {
            File currentCommitFile = Utils.join(COMMIT_DIR, CommitFileName);
            Commit currentCommit = Utils.readObject(currentCommitFile, Commit.class);

            System.out.println("===");
            System.out.println("commit " + currentCommit.getHash());
            System.out.println("Date: " + currentCommit.getFormattedDate());
            System.out.println(currentCommit.getMessage());
            System.out.println();

            CommitFileName = currentCommit.getParent();
        }
    }

    public static  void globalLog(){
        List<String> list = Utils.plainFilenamesIn(COMMIT_DIR);
        if(list != null){
            for(String s: list){
                File currentCommitFile = Utils.join(COMMIT_DIR,s);
                Commit currentCommit = Utils.readObject(currentCommitFile, Commit.class);
                System.out.println("===");
                System.out.println("commit " + currentCommit.getHash());
                System.out.println("Date: " + currentCommit.getFormattedDate());
                System.out.println(currentCommit.getMessage());
                System.out.println();
            }
        }
    }

    public static  void find(String message){
        List<String> list = Utils.plainFilenamesIn(COMMIT_DIR);
        if(list != null){
            for(String s: list){
                File currentCommitFile = Utils.join(COMMIT_DIR,s);
                Commit currentCommit = Utils.readObject(currentCommitFile, Commit.class);
                if(currentCommit.getMessage().equals(message)) {
                    System.out.println(currentCommit.getHash());
                }
            }
        }
    }

    public static void status(){
        System.out.println("=== Branches ===");
        System.out.println("=== Staged Files ===");
        System.out.println("=== Removed Files ===");
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println("=== Untracked Files ===");
    }

    public static void branch(String branchName){
        File branch = Utils.join(REFS_DIR,branchName);
        if(branch.exists()) {
            System.out.println("A branch with that name already exists.");
            return ;
        }
        /*创建指针文件*/
        try{
            branch.createNewFile();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
        /*把Head指针的内容写入branch指针*/
        String Head = Utils.readObject(HEAD, String.class);
        Utils.writeObject(branch,Head);
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
