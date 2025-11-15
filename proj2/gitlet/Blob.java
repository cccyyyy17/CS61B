package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import static gitlet.Repository.BLOB_DIR;
import static gitlet.Utils.writeObject;

public class Blob implements Serializable, Dumpable { // 文件内容
    private String hash;     // 内容的SHA-1哈希值
    private String content;

    public Blob(File f) {
        content = Utils.readContentsAsString(f);
        this.hash = Utils.sha1(content);  // 计算哈希值
    }

    public void saveBlob() {
        File outFile = Utils.join(BLOB_DIR, hash);
        if (!outFile.exists()) {
            try {
                outFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        writeObject(outFile, this);
    }

    public String getContent() {
        return content;
    }
    public String getHash() {
        return hash;
    }
    @Override
    public void dump() {
        System.out.printf("content:%s%n", content);
    }
}
