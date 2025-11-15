package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.ArrayList;

import static gitlet.Utils.*;
import static java.lang.System.exit;

/** Represents a gitlet repository.
 *  does at a high level.
 *
 *  @author Yang
 */
public class Repository {
    @SuppressWarnings("unchecked")
    /**
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
    public static final File REFS_DIR = join(GITLET_DIR, "refs");

    public static final File STAGE = join(GITLET_DIR, "stage");
    public static final File REMOVALSTAGE = join(GITLET_DIR, "removal stage");
    public static final File HEAD = join(GITLET_DIR, "head");
    /*当前Head指向的分支名称，例如master*/
    public static final File CURBRANCHNAME = join(GITLET_DIR, "curbranch");
    private static TreeMap<String, String> stage = new TreeMap<>();
    private static TreeMap<String, String> removalStage = new TreeMap<>();

    public static void gitinit() {
        if (GITLET_DIR.exists()) {
            message("A Gitlet version-control system "
                    + "already exists in the current directory.");
            exit(0);
        }
        try {
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        /* */
        Utils.writeObject(STAGE, stage);
        Utils.writeObject(REMOVALSTAGE, removalStage);
        Commit c = new Commit("initial commit", null, null, new TreeMap<>());
        String head = c.getHash();
        c.saveCommit();
        Utils.writeObject(HEAD, head);
        writeObject(CURBRANCHNAME, "master");
        updateCurrentBranch();
    }

    public static void add(String fileName) {
        File blobFile = Utils.join(CWD, fileName);
        if (!blobFile.exists()) {
            message("File does not exist.");
            exit(0);
        }
        removalStage = Utils.readObject(REMOVALSTAGE, TreeMap.class);
        if (removalStage.containsKey(fileName)) {
            removalStage.remove(fileName);
        }
        writeObject(REMOVALSTAGE, removalStage);
        Blob b = new Blob(blobFile);
        b.saveBlob();
        String head = Utils.readObject(HEAD, String.class);
        Commit curCommit = readObject(join(COMMIT_DIR, head), Commit.class);
        TreeMap<String, String> data = curCommit.getData();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            if (entry.getKey().equals(fileName) && entry.getValue().equals(b.getHash())) {
                return;
            }
        }
        stage = Utils.readObject(STAGE, TreeMap.class);
        stage.put(fileName, b.getHash());
        Utils.writeObject(STAGE, stage);
    }

    private static void addStageToCommit(Commit c) {
        stage = Utils.readObject(STAGE, TreeMap.class);
        TreeMap<String, String> data = c.getData();
        for (Map.Entry<String, String> entry : stage.entrySet()) {
            data.put(entry.getKey(), entry.getValue());
        }
    }

    private static void removalStageToCommit(Commit c) {
        removalStage = Utils.readObject(REMOVALSTAGE, TreeMap.class);
        TreeMap<String, String> data = c.getData();
        for (Map.Entry<String, String> entry : removalStage.entrySet()) {
            if (data.containsKey(entry.getKey())) {
                data.remove(entry.getKey());
            }
        }
    }

    public static void commit(String message) {
        stage = Utils.readObject(STAGE, TreeMap.class);
        removalStage = Utils.readObject(REMOVALSTAGE, TreeMap.class);
        if (stage.isEmpty() && removalStage.isEmpty()) {
            message("No changes added to the commit.");
            exit(0);
        }
        if (message == null || message.isEmpty()) {
            message("Please enter a commit message.");
            exit(0);
        }
        String head = Utils.readObject(HEAD, String.class);
        Commit headCommit = readObject(join(COMMIT_DIR, head), Commit.class);
        Commit c = new Commit(message, head, null, headCommit.getData());
        addStageToCommit(c);
        removalStageToCommit(c);
        head = c.getHash();
        c.saveCommit();
        stage.clear();
        removalStage.clear();
        Utils.writeObject(HEAD, head);
        Utils.writeObject(STAGE, stage);
        Utils.writeObject(REMOVALSTAGE, removalStage);
        updateCurrentBranch();
    }

    public static void rm(String fileName) {
        /*根据文件名字来remove*/
        int signal = 1;
        stage = Utils.readObject(STAGE, TreeMap.class);
        removalStage = Utils.readObject(REMOVALSTAGE, TreeMap.class);
        if (stage.containsKey(fileName)) {
            stage.remove(fileName);
            signal = 0;
        }
        /*读取当前Commit对象及其TreeMap对象，如果包含rm的File，就加入removalStage并删除源文件(Blob文件没有存入电脑)*/
        String head = Utils.readObject(HEAD, String.class);
        File currentCommitFile = Utils.join(COMMIT_DIR, head);
        Commit currentCommit = Utils.readObject(currentCommitFile, Commit.class);
        TreeMap<String, String> date = currentCommit.getData();
        for (Map.Entry<String, String> entry : date.entrySet()) {
            if (entry.getKey().equals(fileName)) {
                removalStage.put(entry.getKey(), entry.getValue());
                File deleteFile = Utils.join(CWD, fileName);
                Utils.restrictedDelete(deleteFile);
                signal = 0;
            }
        }
        Utils.writeObject(STAGE, stage);
        Utils.writeObject(REMOVALSTAGE, removalStage);
        if (signal == 1) {
            message("No reason to remove the file.");
        }
    }

    private static Commit commitHashToCommit(String hash) {
        File currentCommitFile = Utils.join(COMMIT_DIR, hash);
        if (!currentCommitFile.exists()) {
            message("No commit with that id exists.");
            exit(0);
        }
        return Utils.readObject(currentCommitFile, Commit.class);
    }

    private static String blobHashToContent(String blobHash) {
        File blobFile = join(BLOB_DIR, blobHash);
        Blob b = readObject(blobFile, Blob.class);
        return b.getContent();
    }

    private static void updateCurrentBranch() {
        String currentBranchName = readObject(CURBRANCHNAME, String.class);
        File currentBranch = join(REFS_DIR, currentBranchName);
        String writeContent = readObject(HEAD, String.class);
        writeObject(currentBranch, writeContent);
    }

    private static void deleteCommitData(String commitHash) {
        Commit c = readObject(join(COMMIT_DIR, commitHash), Commit.class);
        TreeMap<String, String> data = c.getData();
        if (data == null) {
            return;
        }
        for (Map.Entry<String, String> entry : data.entrySet()) {
            File trackedFile = join(CWD, entry.getKey());
            restrictedDelete(trackedFile);
        }
    }

    private static void putCommitData(String commitHash) {
        Commit c = readObject(join(COMMIT_DIR, commitHash), Commit.class);
        TreeMap<String, String> data = c.getData();
        if (data == null) {
            return;
        }
        for (Map.Entry<String, String> entry : data.entrySet()) {
            File trackedFile = join(CWD, entry.getKey());
            try {
                trackedFile.createNewFile();
                writeContents(trackedFile, blobHashToContent(entry.getValue()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void untrackedFileErrorWithoutDelete(String currentHash, String targetHash) {
        /*清除current文件，恢复target文件*/
        Commit target = readObject(join(COMMIT_DIR, targetHash), Commit.class);
        TreeMap<String, String> targetData = target.getData();

        Commit head = readObject(join(COMMIT_DIR, currentHash), Commit.class);
        TreeMap<String, String> headData = head.getData();
        /*会被覆盖的文件报的错误*/
        for (String targetFileName : targetData.keySet()) {
            File f = join(CWD, targetFileName);
            if (f.exists() && !headData.containsKey(targetFileName)) {
                message("There is an untracked file in the way; "
                        + "delete it, or add and commit it first.");
                exit(0);
            }
        }
    }

    private static void untrackedFileError(String currentHash, String targetHash) {
        /*清除current文件，恢复target文件*/
        Commit target = readObject(join(COMMIT_DIR, targetHash), Commit.class);
        TreeMap<String, String> targetData = target.getData();

        Commit head = readObject(join(COMMIT_DIR, currentHash), Commit.class);
        TreeMap<String, String> headData = head.getData();
        /*会被覆盖的文件报的错误*/
        stage = Utils.readObject(STAGE, TreeMap.class);
        for (String targetFileName : targetData.keySet()) {
            File f = join(CWD, targetFileName);
            if (f.exists() && !headData.containsKey(targetFileName)
                    && !stage.containsKey(targetFileName)) {
                message("There is an untracked file in the way; "
                        + "delete it, or add and commit it first.");
                exit(0);
            }
        }
        deleteCommitData(currentHash);
        putCommitData(targetHash);
        writeObject(HEAD, targetHash);
        stage = readObject(STAGE, TreeMap.class);
        stage.clear();
        writeObject(STAGE, stage);
        removalStage = Utils.readObject(REMOVALSTAGE, TreeMap.class);
        removalStage.clear();
        Utils.writeObject(REMOVALSTAGE, removalStage);
        /*更新CURBRANCHNAME存放的内容(每次更新改变Head指针的指向分支都要更新)*/
    }

    private static String seekSplitPoint(String headHash, String branchHash, String branchName) {
        if (headHash == null || branchHash == null) {
            return null;
        }

        Set<String> visitedHead = new HashSet<>();
        Set<String> visitedBranch = new HashSet<>();
        List<String> headList = new ArrayList<>();
        List<String> branchList = new ArrayList<>();

        initializeLists(headHash, branchHash, headList, branchList);

        return findCommonAncestor(headList, branchList,
                visitedHead, visitedBranch, headHash, branchHash, branchName);
    }

    private static void initializeLists(String headHash, String branchHash,
                                        List<String> headList, List<String> branchList) {
        headList.add(headHash);
        branchList.add(branchHash);
    }

    private static String findCommonAncestor(List<String> headList, List<String> branchList,
                            Set<String> visitedHead, Set<String> visitedBranch,
                            String headHash, String branchHash, String branchName) {
        int headIndex = 0;
        int branchIndex = 0;

        while (headIndex < headList.size() || branchIndex < branchList.size()) {
            String commonAncestor = processHeadSide(headList, visitedHead, visitedBranch,
                    headHash, branchHash, branchName, headIndex);
            if (commonAncestor != null) {
                return commonAncestor;
            }
            headIndex++;

            commonAncestor = processBranchSide(branchList, visitedHead, visitedBranch,
                    headHash, branchHash, branchName, branchIndex);
            if (commonAncestor != null) {
                return commonAncestor;
            }
            branchIndex++;
        }
        return null;
    }

    private static String processHeadSide(List<String> headList, Set<String> visitedHead,
                                          Set<String> visitedBranch, String headHash,
                                          String branchHash, String branchName, int index) {
        if (index >= headList.size()) {
            return null;
        }

        String currentHead = headList.get(index);
        if (currentHead == null || visitedHead.contains(currentHead)) {
            return null;
        }

        visitedHead.add(currentHead);

        if (visitedBranch.contains(currentHead)) {
            return handleFoundCommonAncestor(currentHead, headHash, branchHash, branchName, true);
        }

        addParentCommits(currentHead, headList);
        return null;
    }

    private static String processBranchSide(List<String> branchList, Set<String> visitedHead,
                                            Set<String> visitedBranch, String headHash,
                                            String branchHash, String branchName, int index) {
        if (index >= branchList.size()) {
            return null;
        }

        String currentBranch = branchList.get(index);
        if (currentBranch == null || visitedBranch.contains(currentBranch)) {
            return null;
        }

        visitedBranch.add(currentBranch);

        if (visitedHead.contains(currentBranch)) {
            return handleFoundCommonAncestor(currentBranch,
                  headHash, branchHash, branchName, false);
        }

        addParentCommits(currentBranch, branchList);
        return null;
    }

    private static String handleFoundCommonAncestor(String commonHash, String headHash,
                                                    String branchHash, String branchName,
                                                    boolean isFromHead) {
        if (commonHash.equals(branchHash)) {
            message("Given branch is an ancestor of the current branch.");
            exit(0);
        }
        if (commonHash.equals(headHash)) {
            message("Current branch fast-forwarded.");
            checkout(branchName);
            exit(0);
        }
        return commonHash;
    }

    private static void addParentCommits(String commitHash, List<String> commitList) {
        File commitFile = join(COMMIT_DIR, commitHash);
        if (commitFile.exists()) {
            Commit commit = readObject(commitFile, Commit.class);
            if (commit != null) {
                if (commit.getParent() != null) {
                    commitList.add(commit.getParent());
                }
                if (commit.getSecondParent() != null) {
                    commitList.add(commit.getSecondParent());
                }
            }
        }
    }

    private static void dealConflict(String headDataHash, String branchDataHash, String fileName) {
        System.out.println("Encountered a merge conflict.");
        StringBuilder conflictContent = new StringBuilder();
        conflictContent.append("<<<<<<< HEAD\n");
        if (headDataHash != null) {
            Blob headData = readObject(join(BLOB_DIR, headDataHash), Blob.class);
            if (headData.getContent() != null) {
                conflictContent.append(headData.getContent());
            }
        }
        conflictContent.append("=======\n");
        if (branchDataHash != null) {
            Blob branchData = readObject(join(BLOB_DIR, branchDataHash), Blob.class);
            if (branchData.getContent() != null) {
                conflictContent.append(branchData.getContent());
            }
        }
        conflictContent.append(">>>>>>>\n");
        File f = join(CWD, fileName);
        try {
            f.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        writeContents(f, conflictContent.toString());
        Blob b = new Blob(f);
        b.saveBlob();
        stage.put(fileName, b.getHash());
    }

    public static void checkout(String s, String fileName) {
        if (!Objects.equals(s, "--")) {
            message("Incorrect operands.");
            exit(0);
        }
        int signal = 1;
        Commit head = commitHashToCommit(readObject(HEAD, String.class));
        TreeMap<String, String> data = head.getData();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            if (entry.getKey().equals(fileName)) {
                File file = join(CWD, fileName);
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                writeContents(file, blobHashToContent(entry.getValue()));
                signal = 0;
            }
        }
        if (signal == 1) {
            message("File does not exist in that commit.");
            exit(0);
        }
    }

    private static String findFullCommitId(String abbreviatedId) {
        List<String> commitList = plainFilenamesIn(COMMIT_DIR);
        int length = abbreviatedId.length();
        for (String s : commitList) {
            if (s.substring(0, length).equals(abbreviatedId)) {
                return s;
            }
        }
        return null;
    }

    public static void checkout(String commitId, String s, String fileName) {
        if (!Objects.equals(s, "--")) {
            message("Incorrect operands.");
            exit(0);
        }
        int signal = 1;
        /*错误消息实现在commitHashToCommit中*/
        String commitFullId = commitId;
        if (commitId.length() < 30) {
            commitFullId = findFullCommitId(commitId);
        }
        Commit commit = commitHashToCommit(commitFullId);
        TreeMap<String, String> data = commit.getData();
        if (data == null) {
            return;
        }
        for (Map.Entry<String, String> entry : data.entrySet()) {
            if (entry.getKey().equals(fileName)) {
                File file = join(CWD, fileName);
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                writeContents(file, blobHashToContent(entry.getValue()));
                signal = 0;
            }
        }
        if (signal == 1) {
            message("File does not exist in that commit.");
            exit(0);
        }
    }

    public static void checkout(String branchName) {
        /*选中当前分支就直接return*/
        if (branchName.equals(readObject(CURBRANCHNAME, String.class))) {
            System.out.println("No need to checkout the current branch.");
            return;
        }
        /*删除当前Commit所有data，恢复所有branch Commit的data*/
        File branchFile = join(REFS_DIR, branchName);
        if (!branchFile.exists()) {
            message("No such branch exists.");
            exit(0);
        }
        String branchHash = readObject(branchFile, String.class);
        String headHash = readObject(HEAD, String.class);
        untrackedFileError(headHash, branchHash);

        /*更新CURBRANCHNAME存放的内容(每次更新改变Head指针的指向分支都要更新)*/
        writeObject(CURBRANCHNAME, branchName);
    }

    public static void log() {
        String commitFileName = Utils.readObject(HEAD, String.class);

        while (commitFileName != null) {
            File currentCommitFile = Utils.join(COMMIT_DIR, commitFileName);
            Commit currentCommit = Utils.readObject(currentCommitFile, Commit.class);

            System.out.println("===");
            System.out.println("commit " + currentCommit.getHash());
            System.out.println("Date: " + currentCommit.getFormattedDate());
            System.out.println(currentCommit.getMessage());
            System.out.println();

            commitFileName = currentCommit.getParent();
        }
    }

    public static void globalLog() {
        List<String> list = Utils.plainFilenamesIn(COMMIT_DIR);
        if (list != null) {
            for (String s : list) {
                File currentCommitFile = Utils.join(COMMIT_DIR, s);
                Commit currentCommit = Utils.readObject(currentCommitFile, Commit.class);
                System.out.println("===");
                System.out.println("commit " + currentCommit.getHash());
                System.out.println("Date: " + currentCommit.getFormattedDate());
                System.out.println(currentCommit.getMessage());
                System.out.println();
            }
        }
    }

    public static void find(String message) {
        int signal = 1;
        List<String> list = Utils.plainFilenamesIn(COMMIT_DIR);
        if (list != null) {
            for (String s : list) {
                File currentCommitFile = Utils.join(COMMIT_DIR, s);
                Commit currentCommit = Utils.readObject(currentCommitFile, Commit.class);
                if (currentCommit.getMessage().equals(message)) {
                    System.out.println(currentCommit.getHash());
                    signal = 0;
                }
            }
        }
        if (signal == 1) {
            message("Found no commit with that message.");
        }
    }

    public static void status() {
        System.out.println("=== Branches ===");
        List<String> dir = plainFilenamesIn(REFS_DIR);
        String currentBranch = readObject(CURBRANCHNAME, String.class);
        System.out.println("*" + currentBranch);
        if (dir != null) {
            for (String s : dir) {
                if (!s.equals(currentBranch)) {
                    System.out.println(s);
                }
            }
        }
        System.out.println();

        System.out.println("=== Staged Files ===");
        stage = Utils.readObject(STAGE, TreeMap.class);
        for (String key : stage.keySet()) {
            System.out.println(key);
        }
        System.out.println();

        System.out.println("=== Removed Files ===");
        removalStage = Utils.readObject(REMOVALSTAGE, TreeMap.class);
        for (String key : removalStage.keySet()) {
            System.out.println(key);
        }
        System.out.println();

        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();
        System.out.println("=== Untracked Files ===");
    }

    public static void branch(String branchName) {
        File branch = Utils.join(REFS_DIR, branchName);
        if (branch.exists()) {
            System.out.println("A branch with that name already exists.");
            return;
        }
        /*创建指针文件*/
        try {
            branch.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        /*把Head指针的内容写入branch指针*/
        String head = Utils.readObject(HEAD, String.class);
        Utils.writeObject(branch, head);
    }

    public static void rmBranch(String branchName) {
        File branchFile = join(REFS_DIR, branchName);
        if (!branchFile.exists()) {
            message("A branch with that name does not exist.");
            exit(0);
        }
        String currentBranchName = readObject(CURBRANCHNAME, String.class);
        if (currentBranchName.equals(branchName)) {
            message("Cannot remove the current branch.");
            exit(0);
        }
        branchFile.delete();
    }

    public static void reset(String commitId) {
        File commit = join(COMMIT_DIR, commitId);
        if (!commit.exists()) {
            message("No commit with that id exists.");
            exit(0);
        }
        String headHash = readObject(HEAD, String.class);
        untrackedFileError(headHash, commitId);
        updateCurrentBranch();
    }

    public static void merge(String branchName) {
        checkPreconditions(branchName);

        String headHash = readObject(HEAD, String.class);
        File branchFile = join(REFS_DIR, branchName);
        String branchHash = readObject(branchFile, String.class);
        String splitPoint = seekSplitPoint(headHash, branchHash, branchName);

        untrackedFileErrorWithoutDelete(headHash, branchHash);
        performFileMerging(branchName, headHash, branchHash, splitPoint);
        createMergeCommit(branchName, branchHash);
    }

    private static void checkPreconditions(String branchName) {
        stage = Utils.readObject(STAGE, TreeMap.class);
        removalStage = Utils.readObject(REMOVALSTAGE, TreeMap.class);
        if (!stage.isEmpty() || !removalStage.isEmpty()) {
            message("You have uncommitted changes.");
            exit(0);
        }

        File branchFile = join(REFS_DIR, branchName);
        if (!branchFile.exists()) {
            message("A branch with that name does not exist.");
            exit(0);
        }

        String headHash = readObject(HEAD, String.class);
        String branchHash = readObject(branchFile, String.class);
        if (branchHash.equals(headHash)) {
            message("Cannot merge a branch with itself.");
            exit(0);
        }
    }

    private static void performFileMerging(String branchName, String headHash,
                                           String branchHash, String splitPoint) {
        Commit sp = readObject(join(COMMIT_DIR, splitPoint), Commit.class);
        Commit head = readObject(join(COMMIT_DIR, headHash), Commit.class);
        Commit branch = readObject(join(COMMIT_DIR, branchHash), Commit.class);

        TreeMap<String, String> spData = sp.getData();
        TreeMap<String, String> headData = head.getData();
        TreeMap<String, String> branchData = branch.getData();

        mergeBranchFiles(headHash, branchHash, spData, headData, branchData);
        mergeHeadOnlyFiles(headHash, branchHash, spData, headData, branchData);
    }

    private static void mergeBranchFiles(String headHash, String branchHash,
                                         TreeMap<String, String> spData,
                                         TreeMap<String, String> headData,
                                         TreeMap<String, String> branchData) {
        for (Map.Entry<String, String> entry : branchData.entrySet()) {
            String headFileHash = headData.get(entry.getKey());
            String branchFileHash = entry.getValue();
            String blobFileName = entry.getKey();

            if (headData.containsKey(blobFileName)) {
                handleBothBranchesHaveFile(blobFileName, headFileHash, branchFileHash,
                        headHash, branchHash, spData);
            } else {
                handleOnlyBranchHasFile(blobFileName, branchFileHash, headHash, branchHash, spData);
            }
        }
    }

    private static void handleBothBranchesHaveFile(String fileName, String headHashValue,
                     String branchHashValue, String headHash,
                     String branchHash, TreeMap<String, String> spData) {
        if (headHashValue.equals(branchHashValue)) {
            stage.put(fileName, branchHashValue);
            return;
        }

        if (!spData.containsKey(fileName)) {
            dealConflict(headHashValue, branchHashValue, fileName);
        } else {
            String spFileHash = spData.get(fileName);
            if (headHashValue.equals(spFileHash)) {
                checkout(branchHash, "--", fileName);
                stage.put(fileName, branchHashValue);
            } else if (branchHashValue.equals(spFileHash)) {
                checkout(headHash, "--", fileName);
                stage.put(fileName, headHashValue);
            } else {
                dealConflict(headHashValue, branchHashValue, fileName);
            }
        }
    }

    private static void handleOnlyBranchHasFile(String fileName, String branchHashValue,
                                                String headHash, String branchHash,
                                                TreeMap<String, String> spData) {
        if (!spData.containsKey(fileName)) {
            checkout(branchHash, "--", fileName);
            stage.put(fileName, branchHashValue);
        } else if (spData.get(fileName).equals(branchHashValue)) {
            File deleteFile = join(CWD, fileName);
            if (deleteFile.exists()) {
                restrictedDelete(deleteFile);
            }
        } else {
            dealConflict(null, branchHashValue, fileName);
        }
    }

    private static void mergeHeadOnlyFiles(String headHash, String branchHash,
                                           TreeMap<String, String> spData,
                                           TreeMap<String, String> headData,
                                           TreeMap<String, String> branchData) {
        for (Map.Entry<String, String> entry : headData.entrySet()) {
            String blobFileName = entry.getKey();
            if (!branchData.containsKey(blobFileName)) {
                handleOnlyHeadHasFile(blobFileName, entry.getValue(), headHash, branchHash, spData);
            }
        }
    }

    private static void handleOnlyHeadHasFile(String fileName, String headHashValue,
                                              String headHash, String branchHash,
                                              TreeMap<String, String> spData) {
        if (!spData.containsKey(fileName)) {
            checkout(headHash, "--", fileName);
            stage.put(fileName, headHashValue);
        } else if (spData.get(fileName).equals(headHashValue)) {
            File deleteFile = join(CWD, fileName);
            if (deleteFile.exists()) {
                restrictedDelete(deleteFile);
            }
        } else {
            dealConflict(headHashValue, null, fileName);
        }
    }

    private static void createMergeCommit(String branchName, String branchHash) {
        String commitMessage = "Merged " + branchName + " into "
                + readObject(CURBRANCHNAME, String.class) + ".";

        removalStage = Utils.readObject(REMOVALSTAGE, TreeMap.class);
        if (stage.isEmpty()) {
            message("No changes added to the commit.");
            exit(0);
        }

        String headPoint = Utils.readObject(HEAD, String.class);
        Commit c = new Commit(commitMessage, headPoint, branchHash, stage);
        removalStageToCommit(c);
        writeObject(STAGE, stage);
        headPoint = c.getHash();
        c.saveCommit();
        stage.clear();
        Utils.writeObject(HEAD, headPoint);
        Utils.writeObject(STAGE, stage);
        updateCurrentBranch();
    }
}
