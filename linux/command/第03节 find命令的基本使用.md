find 是 Linux 中强大的搜索命令，
不仅可以按照文件名搜索文件，还可以按照权限、大小、时间、inode 号等来搜索文件。

但是 find 命令是直接在硬盘中进行搜索的，如果指定的搜索范围过大，find命令就会消耗较大的系统资源，导致服务器压力过大。
所以，在使用 find 命令搜索时，不要指定过大的搜索范围。

find 命令的基本信息如下：
```text
命令名称：find。
英文原意：search for files in a directory hierarchy.
所在路径：/bin/find。
执行权限：所有用户。
功能描述：在目录中查找文件。
```

## 命令格式
```text
[root@localhost ~]# find 搜索路径 [选项] 搜索内容
```
find 是比较特殊的命令，它有两个参数：
- 第一个参数用来指定搜索路径；
- 第二个参数用来指定搜索内容。

而且find命令的选项比较复杂，我们一个一个举例来看。

## 按照文件名搜索
```text
[root@localhost ~]#find 搜索路径 [选项] 搜索内容
```
选项：
```text
-name: 按照文件名搜索；
-iname: 按照文件名搜索，不区分文件名大小；
-inum: 按照 inode 号搜索；
```

这是 find 最常用的用法，我们来试试：
```text
[root@localhost ~]# find / -name yum.conf
/etc/yum.conf
# 在目录下査找文件名是yum.conf的文件
```

但是 find 命令有一个小特性，就是搜索的文件名必须和你的搜索内容一致才能找到。
如果只包含搜索内容，则不会找到。我们做一个实验：
```text
[root@localhost ~]# touch yum.conf.bak
# 在/root/目录下建立一个文件yum.conf.bak
[root@localhost ~]# find / -name yum.conf
/etc/yum.conf
# 搜索只能找到yum.conf文件，而不能找到 yum.conf.bak 文件
```

find 能够找到的是只有和搜索内容 yum.conf 一致的 /etc/yum.conf 文件，
而 /root/yum.conf.bak 文件虽然含有搜索关键字，但是不会被找到。
这种特性我们总结为：find 命令是完全匹配的，必须和搜索关键字一模一样才会列出。

Linux 中的文件名是区分大小写的，也就是说，搜索小写文件，是找不到大写文件的。
如果想要大小通吃，就要使用 -iname 来搜索文件。
```text
[root@localhost ~]# touch CANGLS
[root@localhost ~]# touch cangls
# 建立大写和小写文件
[root@localhost ~]# find . -iname cangls
./CANGLS
./cangls
# 使用-iname，大小写文件通吃
```

每个文件都有 inode 号，如果我们知道 inode 号，则也可以按照 inode 号来搜索文件。
```text
[root@localhost ~]#ls -i install.log
262147 install.log
# 如果知道文件名，则可以用"ls -i"来査找inode号
[root@localhost ~]# find . -inum 262147
./install.log
# 如果知道inode号，则可以用find命令来査找文件
```

按照 inode 号搜索文件，也是区分硬链接文件的重要手段，因为硬链接文件的 inode 号是一致的。
```text
[root@localhost ~]# ln /root/install.log /tmp/
# 给install.log文件创建一个硬链接文件
[root@localhost ~]# ll -i /root/install.log /tmp/install.log
262147 -rw-r--r--.2 root root 24772 1 月 14 2014/root/install.log
262147 -rw-r--r--.2 root root 24772 1 月 14 2014/tmp/install.log
# 可以看到这两个硬链接文件的inode号是一致的
[root@localhost ~]# find / -inum 262147
/root/install.log
/tmp/install.log
# 如果硬链接不是我们自己建立的，则可以通过find命令搜索inode号，来确定硬链接文件
```

## 按照文件大小搜索
```text
[root@localhost ~]#find 搜索路径 [选项] 搜索内容
```
选项：
```text
-size[+-]大小：按照指定大小搜索文件
```
这里的"+"的意思是搜索比指定大小还要大的文件，"-" 的意思是搜索比指定大小还要小的文件。我们来试试：
```text
[root@localhost ~]# ll -h install.log
-rw-r--r--.1 root root 25K 1月 14 2014 install.log # 在当前目录下有一个大小是25KB的文件

[root@localhost ~]# find . -size 25k
./install.log
# 当前目录下，査找大小刚好是25KB的文件，可以找到

[root@localhost ~]# find . -size -25k
.
./.bashrc
./.viminfo
./.tcshrc
./.pearrc
./anaconda-ks.cfg
./test2
./.ssh
./.bash_history
./.lesshst
./.bash_profile
./yum.conf.bak
./.bashjogout
./install.log.syslog
./.cshrc
./cangls
# 搜索小于25KB的文件，可以找到很多文件
[root@localhost ~]# find . -size +25k
# 而当前目录下没有大于25KB的文件
```

其实 find 命令的 -size 选项是笔者个人觉得比较恶心的选项，为什么这样说？
find 命令可以按照 KB 来搜索，应该也可以按照 MB 来搜索吧。
```text
[root@localhost ~]# find.-size -25m
find:无效的-size类型"m"
# 为什么会报错呢？其实是因为如果按照MB来搜索，则必须是大写的M
```

这就是纠结点，千字节必须是小写的"k"，而兆字节必领是大写的"M"。
有些人会说："你别那么执着啊，你就不能不写单位，直接按照字节搜索啊？"很傻，很天真，不写单位，你们就以为会按照字节搜索吗？
我们来试试：
```text
[root@localhost ~]# ll anaconda-ks.cfg
-rw-------.1 root root 1207 1 月 14 2014 anaconda-ks.cfg
# anaconda-ks.cfg文件有1207字芳
[root@localhost ~]# find . -size 1207
# 但用find查找1207，是什么也找不到的
```

也就是说，find 命令的默认单位不是字节。
如果不写单位，那么 find 命令是按照 512 Byte 来进行査找的。
我们看看 find 命令的帮助。

```text
[root@localhost ~]# man find
-size n[cwbkMG]
File uses n units of space. The following suffixes can be used:
'b' for 512-byte blocks (this is the default if no suffix is used)
# 这是默认单位，如果单位为b或不写单位，则按照 512Byte搜索
'c' for bytes
# 搜索单位是c，按照字节搜索
'w' for two-byte words
# 搜索单位是w，按照双字节（中文）搜索
'k' for Kilobytes (units of 1024 bytes)
# 按照KB单位搜索，必须是小写的k
'M' for Megabytes (units of 1048576 bytes)
# 按照MB单位搜索，必须是大写的M
'G' for Gigabytes (units of 1073741824 bytes)
# 按照GB单位搜索，必须是大写的G
```

也就是说，如果想要按照字节搜索，则需要加搜索单位"c"。我们来试试：
```text
[root@localhost ~]# find . -size 1207c
./anaconda-ks.cfg
# 使用搜索单位c，才会按照字节搜索
```

## 按照修改时间搜索
Linux 中的文件有访问时间(atime)、数据修改时间(mtime)、状态修改时间(ctime)这三个时间，我们也可以按照时间来搜索文件。
```text
[root@localhost ~]# find搜索路径 [选项] 搜索内容
```
选项：
```text
-atime [+-]时间: 按照文件访问时间搜索
-mtime [+-]时间: 按照文改时间搜索
-ctime [+-]时间: 按照文件修改时间搜索
```

这三个时间的区别我们在 stat 命令中已经解释过了，这里用 mtime 数据修改时间来举例，重点说说 "[+-]"时间的含义。

每次笔者讲到这里，"-5"代表 5 天内修改的文件，而"+5"总有人说代表 5 天修改的文件。
要是笔者能知道 5 天系统中能建立什么文件，早就去买彩票了，那是未卜先知啊！
所以"-5"指的是 5 天内修改的文件，"5"指的是前 5~6 天那一天修改的文件，"+5"指的是 6 天前修改的文件。
我们来试试：
```text
[root@localhost ~]# find . -mtime -5
# 查找5天内修改的文件
```

大家可以在系统中把几个选项都试试，就可以明白各选项之间的差别了。

find 不仅可以按照 atmie、mtime、ctime 来査找文件的时间，也可以按照 amin、mmin 和 cmin 来査找文件的时间，
区别只是所有 time 选项的默认单位是天，而 min 选项的默认单位是分钟。

## 按照权限搜索
在 find 中，也可以按照文件的权限来进行搜索。权限也支持 [+/-] 选项。我们先看一下命令格式。
```text
[root@localhost ~]# find 搜索路径 [选项] 搜索内容
```
选项：
```text
-perm 权限模式：査找文件权限刚好等于"权限模式"的文件
-perm -权限模式：査找文件权限全部包含"权限模式"的文件
-perm +权限模式：査找文件权限包含"权限模式"的任意一个权限的文件
```

为了便于理解，我们要举几个例子。先建立几个测试文件。
```text
[root@localhost ~]# mkdir test
[root@localhost ~]# cd test/
[root@localhost test]# touch test1
[root@localhost test]# touch test2
[root@localhost test]# touch test3
[root@localhost test]# touch test4
# 建立测试目录，以及测试文件
[root@localhost test]# chmod 755 test1
[root@localhost test]# chmod 444 test2
[root@localhost test]# chmod 600 test3
[root@localhost test]# chmod 200 test4
# 设定实验权限。因为是实验权限，所以看起来比较别扭
[root@localhost test]# ll
总用量0
-rwxr-xr-x 1 root root 0 6月 17 11:05 test1 
-r--r--r-- 1 root root 0 6月 17 11:05 test2
-rw------- 1 root root 0 6月 17 11:05 test3
--w------- 1 root root 0 6月 17 11:05 test4
# 查看权限
```

【例 1】"-perm权限模式"。

这种搜索比较简单，代表査找的权限必须和指定的权限模式一模一样，才可以找到。
```text
[root@localhost test]# find . -perm 444
./test2
[root@localhost test]# find . -perm 200
./test4
# 按照指定权限搜索文件，文件的权限必须和搜索指定的权限一致，才能找到
```

【例 2】"-perm-权限模式"。

如果使用"-权限模式"，是代表的是文件的权限必须全部包含搜索命令指定的权限模式，才可以找到。
```text
[root@localhost test]#find . -perm -200
./test4 <-此文件权限为200
./test3 <-此文件权限为600
./test1 <-此文件权限为755
# 搜索文件的权限包含200的文件，不会找到test2文件，因为test2的权限为444，不包含200权限
```

因为 test4 的权限 200(-w-------)、test3 的权限 600(-rw-------)和 test1 的权限 755(-rwxr-xr-x) 
都包含 200(--w-------) 权限，所以可以找到；
而 test2 的权限是 444 (-r--r--r--)，不包含 200 (--w-------)权限，所以找不到，再试试：
```text
[root@localhost test]# find . -perm -444
.
./test2 <-此文件权限为444
./test1 <-此文件权限为755
# 搜索文件的权限包含444的文件
```

上述搜索会找到 test1 和 test2，因为 test1 的权限 755 (-rwxr-xr-x)和 test2 的权限 444 (-r--r--r--)
都完全包含 444 (-r--r--r--)权限，所以可以找到；
而 test3 的权限 600 (-rw-------)和 test4 的权限 200 (-w-------)不完全包含 444 (-r--r--r--) 权限，所以找不到。
也就是说，test3 和 test4 文件的所有者权限虽然包含 4 权限，
但是所属组权限和其他人权限都是 0，不包含 4 权限，所以找不到，这也是完全包含的意义。

【例 3】"-perm+权限模式"。

刚刚的"-perm-权限模式"是必须完全包含，才能找到；而"-perm+权限模式"是只要包含任意一个指定权限，就可以找到。我们来试试：

```text
[root@localhost test]# find . -perm +444
./test4 <-此文件权限为200
./test3 <-此文件权限为600
./testl <-此文件权限为755
# 搜索文件的权限包含200的文件，不会找到test2文件，因为test2的权限为444，不包含200权限。
```

因为 test4 的权限 200 (--w-------)、test3 的权限 600 (-rw-------)
和 test1 的权限 755 (-rwxr-xr-x)都包含 200(--w-------)权限，所以可以找到；
而 test2 的权限是 444 (-r--r--r--)，不包含 200 (--w-------)权限，所以找不到。

## 按照所有者和所属组搜索
```text
[root@localhost ~]# find 搜索路径 [选项] 搜索内容
```
选项：
```text
-uid 用户 ID:按照用户 ID 査找所有者是指定 ID 的文件
-gid 组 ID:按照用户组 ID 査找所属组是指定 ID 的文件
-user 用户名：按照用户名査找所有者是指定用户的文件
-group 组名：按照组名査找所属组是指定用户组的文件
-nouser：査找没有所有者的文件
```

这组选项比较简单，就是按照文件的所有者和所属组来进行文件的査找。
在 Linux 系统中，绝大多数文件都是使用 root 用户身份建立的，所以在默认情况下，绝大多数系统文件的所有者都是 root。
例如：
```text
[root@localhost ~]# find . -user root
# 在当前目录中査找所有者是 root 的文件
```

由于当前目录是 root 的家目录，所有文件的所有者都是 root 用户，所以这条搜索命令会找到当前目录下所有的文件。

按照所有者和所属组搜索时，"-nouser"选项比较常用，主要用于査找垃圾文件。
在 Linux 中，所有的文件都有所有者，只有一种情况例外，那就是外来文件。
比如光盘和 U 盘中的文件如果是由 Windows 复制的，在 Linux 中査看就是没有所有者的文件；
再比如手工源码包安装的文件，也有可能没有所有者。

除这种外来文件外，如果系统中发现了没有所有者的文件，一般是没有作用的垃圾文件（比如用户删除之后遗留的文件），这时需要用户手工处理。

搜索没有所有者的文件，可以执行以下命令：
```text
[root@localhost ~]# find / -nouser
```

## 按照文件类型搜索
```text
[root@localhost ~]# find 搜索路径 [选项] 搜索内容
```
选项:
```text
-type d：查找目录
-type f：查找普通文件
-type l：查找软链接文件
```

这个命令也很简单，主要按照文件类型进行搜索。
在一些特殊情况下，比如需要把普通文件和目录文件区分开，比如需要把普通文件和目录文件区分开，使用这个选项就很方便。

```text
[root@localhost ~]# find /etc -type d
# 查找/etc/目录下有哪些子目录
```

## 逻辑运算符
```text
[root@localhost ~]#find 搜索路径 [选项] 搜索内容
```
选项：
```text
-a：and逻辑与
-o：or逻辑或
-not：not逻辑非
```

### 1）-a: and逻辑与

find 命令也支持逻辑运算符选项，其中 -a 代表逻辑与运算，也就是 -a 的两个条件都成立，find 搜索的结果才成立。

举个例子：
```text
[root@localhost ~]# find . -size +2k -a -type f
# 在当前目录下搜索大于2KB，并且文件类型是普通文件的文件
```
在这个例子中，文件既要大于 2KB，又必须是普通文件，find 命令才可以找到。再举个例子：

```text
[root@localhost ~]# find . -mtime -3 -a -perm 644
# 在当前目录下搜索3天以内修改过，并且权限是644的文件
```

### 2）-o:or逻辑或
-o 选项代表逻辑或运算，也就是 -o 的两个条件只要其中一个成立，find 命令就可以找到结果。例如：

```text
[root@localhost ~]#find . -name cangls -o -name bols
./cangls
./bols
# 在当前目录下搜索文件名要么是cangls的文件，要么是bols的文件
```

-o 选项的两个条件只要成立一个，find 命令就可以找到结果，所以这个命令既可以找到 cangls 文件，也可以找到 bols 文件。

### 3）-not:not逻辑非
-not是逻辑非，也就是取反的意思。举个例子:
```text
[root@localhost ~]# find . -not -name cangls
# 在当前目录下搜索文件名不是cangls的文件
```

## 其他选项
### 1）-exec选项
这里我们主要讲解两个选项"-exec"和"-ok"，这两个选项的基本作用非常相似。我们先来看看 "exec"选项的格式。

```text
[root@localhost ~]# find 搜索路径 [选项] 搜索内容 -exec 命令2 {}\;
```

首先，请大家注意这里的"{}"和"\;"是标准格式，只要执行"-exec"选项，这两个符号必须完整输入。

其次，这个选项的作用其实是把 find 命令的结果交给由"-exec"调用的命令 2 来处理。"{}"就代表 find 命令的査找结果。

我们举个例子，刚刚在讲权限的时候，使用权限模式搜索只能看到文件名，例如：

```text
[root@localhost test]#find . -perm 444
./test2
```

如果要看文件的具体权限，还要用"ll"命令査看。用"-exec"选项则可以一条命令搞定：

```text
[root@localhost test]# find . -perm 444 -exec ls -l {}\；
-r--r--r-- 1 root root 0 6月 17 11:05 ./test2
# 使用"-exec"选项，把find命令的结果直接交给"ls -l"命令处理
```

"-exec"选项的作用是把 find 命令的结果放入"{}"中，再由命令 2 直接处理。
在这个例子中就是用"ls -l"命令直接处理，会使 find 命令更加方便。

### 2）-ok选项
"-ok"选项和"-exec"选项的作用基本一致，
区别在于：
"-exec"的命令会直接处理，而不询问；
"-ok"的命令 2 在处理前会先询问用户是否这样处理，在得到确认命令后，才会执行。

例如：
```text
[root@localhost test]# find . -perm 444 -ok rm -rf {}\;
<rm…./test2>?y  <-需要用户输入y,才会执行->
# 我们这次使用rm命令来删除find找到的结果，删除的动作最好确认一下
```
