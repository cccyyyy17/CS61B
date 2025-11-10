# Gitlet Design Document
## 任务清单
- [ ] Head指针没有实现持续化
-[x] 使用Utils.readContentAsString获取文件的内容
我该如何恢复文件？？使用 writeContents 方法将内容写回到文件
-[ ] 实现错误提示消息

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
- init()
- add()
- commit()
- rm():remove这次track，不会删除过去的Commit中的内容(即使sha1相同即内容相同)，即使内容相同，
过去的commit追踪，这次的commit不追踪(理解为另一个文件)
- checkout()
- log()

### Class Utils
#### 仓库代码

# 指针如何搞
完全没思路，回头再弄吧

 ***





## Algorithms

## Persistence

