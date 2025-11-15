# Gitlet Design Document
## 任务清单
- [x] 冲突函数里的println
- [] merge调用了dump方法

**Name**:Yang

## Classes and Data Structures

### Class Commit
- String message:提交信息
+ Date timestamp:时间表示 = new Date()
* String parent:直接父节点 hashcode值作为标识ID
- String parent2:分支父节点，用于merge操作
+ getHash():通过每个函数的toString()函数返回字符串(空指针返回null)计算hash值
* 实现Dumpable接口，输出message(调试用)
* 
### Class Blob
- String filepath 
* String hash ：根据content内容计算出的值，唯一标识Blob
- String content：使用Utils.readContentAsString获取文件的内容
- 我该如何恢复文件？
* 实现Dumpable接口，输出content(调试用)

### Class Repository
+ String Head ：Head指针
- TreeMap<String,String> stage = new TreeMap<>():
使用Util.writeContent(覆盖写方法)维护一个stage文件存放TreeMap
模拟暂存区的效果
- 同样方式维护一个removalStage变量
- CURBRANCHNAME ：当前分支名称，初始化时候存入master，在checkout时候考虑修改内容
- init()
- add()
- commit()
- rm():remove这次track，不会删除过去的Commit中的内容(即使sha1相同即内容相同)，即使内容相同，
过去的commit追踪，这次的commit不追踪(理解为另一个文件)
- checkout()
- log()

### 常用代码
- 读取Head所指内容的hash值`Head = Utils.readObject(HEAD, String.class);`
* 读当前分支名称`String currentBranch = readObject(CURBRANCHNAME,String.class)`
- 暂存区`stage = Utils.readObject(STAGE,TreeMap.class);`
+ 暂存移除区`removalStage = Utils.readObject(REMOVALSTAGE,TreeMap.class);`
### 取Commit类代码
+ 读取Head指针对应的文件`File currentCommitFile = Utils.join(COMMIT_DIR,Head);`
- 读取Head所指的Commit对象`Commit currentCommit = Utils.readObject(currentCommitFile,Commit.class);`
* 把修改了内容的指针写回去`Utils.writeObject(HEAD,Head);
                      Utils.writeObject(STAGE,stage);
- 读与写一般成对出现

# merge操作
1. different content ：内容寻址，所谓的修改过文件的定义
2. 任何自split point增加的文件都保存
3. 任何自split point的文件，一个分支没改变(内容orHash值没改变)，另一个分支删除了，保持删除
以文件名Name来判断是否为同一文件，以文件内容Content来判断是否修改
- Name 都有 
  1. content同 -- > 保留该文件 ,暂存
  2. content不同 
      - sp没有 -- 冲突
      - sp有，其中一个和sp相同，保留不同的(head相同留branch，branch相同留head)
      - sp有，都不相同 --> 冲突 
- Name 一个有一个无(branch有)，我还得写head有branch没有的(记)
  1. sp没有 -->保留有的，此时是branch
  2. sp也有 
      - 和sp内容相同，移除 
      - 和sp也不同 -->冲突
- Name 都没有 -->保持现状(遍历时不会经过)(不用遍历)

removalStage = Utils.readObject(REMOVALSTAGE,TreeMap.class);
removalStage.clear();
Utils.writeObject(REMOVALSTAGE,removalStage);


## Algorithms

## Persistence

