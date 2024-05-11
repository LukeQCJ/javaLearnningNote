# NameNode启动问题：Failed to load an FSImage file!
在启动进程中没有看到namenode进程，就去hadoop配置的namenode节点看日志，在hadoop安装目录的logs里面。

错误日志内容：
```text
2022-01-23 13:35:53,807 FATAL org.apache.hadoop.hdfs.server.namenode.NameNode: Failed to start namenode.
java.io.IOException: Failed to load an FSImage file!
```

解决方法：删除大小为0的映射文件
```text
[root@master lib]# find / -name dfs
/usr/local/hadoop/tmp/dfs
[root@master lib]# cd /usr/local/hadoop/tmp/dfs
[root@master dfs]# ll
total 0
drwx------ 3 root root 38 Jan 22 12:08 data
drwxr-xr-x 3 root root 20 Jan 23 13:35 name
drwxr-xr-x 3 root root 38 Jan 23 13:36 namesecondary
[root@master dfs]# cd name
[root@master name]# ll
total 12
drwxr-xr-x 2 root root 8192 Jan 22 12:51 current
[root@master name]# cd current/

[root@master current]# pwd
/usr/local/hadoop/tmp/dfs/name/current
```
找到为0字节的FSImage file，删除掉：
```text
-rw-r--r-- 1 root root   67018 5月   1 23:20 fsimage_0000000000000065099
-rw-r--r-- 1 root root      62 5月   1 23:20 fsimage_0000000000000065099.md5
-rw-r--r-- 1 root root       0 5月  11 22:30 fsimage_0000000000000065398
-rw-r--r-- 1 root root      62 5月  11 22:30 fsimage_0000000000000065398.md5
```
删除掉 fsimage_0000000000000065398和fsimage_0000000000000065398.md5文件。

重新启动hadoop集群，就OK了。