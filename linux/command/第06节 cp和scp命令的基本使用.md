# Linux cp 命令

cp（英文全拼：copy file）命令主要用于复制文件或目录。

## 语法
```text
cp [options] source dest
或
cp [选项] 源文件 目标文件
```
其中，source（源文件）表示要复制的文件或目录的路径，dest（目标文件）表示复制后的文件或目录的路径。

选项说明：
```text
-a：此选项通常在复制目录时使用，它保留链接、文件属性，并复制目录下的所有内容。其作用等于 dpR 参数组合。
-d：复制时保留链接。这里所说的链接相当于 Windows 系统中的快捷方式。
-r 或 --recursive：用于复制目录及其所有的子目录和文件，如果要复制目录，需要使用该选项。
-i 或 --interactive：在复制前提示确认，如果目标文件已存在，则会询问是否覆盖，回答 y 时目标文件将被覆盖。。
-u 或 --update：仅复制源文件中更新时间较新的文件。
-v 或 --verbose：显示详细的复制过程。
-p 或 --preserve：保留源文件的权限、所有者和时间戳信息。
-f 或 --force：强制复制，即使目标文件已存在也会覆盖，而且不给出提示。
-l：不复制文件，只是生成链接文件。
```

## 实例
将文件 file.txt 复制到目录 /path/to/destination/ 中：
```text
cp file.txt /path/to/destination/
```

使用指令 cp 将当前目录 test/ 下的所有文件复制到新目录 newtest 下，输入如下命令：
```text
cp –r test/ newtest  
```
        
注意：用户使用该指令复制目录时，必须使用参数 -r 或者 -R 。

复制文件，并在目标文件已存在时进行确认：
```text
cp -i file.txt /path/to/destination/
```

以上只是 cp 命令的一些常见用法，你可以通过运行 man cp 命令查看更多选项和用法。

---
# Linux scp命令

scp 命令用于 Linux 之间复制文件和目录。

scp 是 secure copy 的缩写, scp 是 linux 系统下基于 ssh 登陆进行安全的远程文件拷贝命令。

scp 是加密的，rcp 是不加密的，scp 是 rcp 的加强版。

## 语法
```text
scp [-1246BCpqrv] [-c cipher] [-F ssh_config] [-i identity_file]
[-l limit] [-o ssh_option] [-P port] [-S program]
[[user@]host1:]file1 [...] [[user@]host2:]file2
```
简易写法:
```text
scp [可选参数] file_source file_target
```

参数说明：
```text
-1： 强制scp命令使用协议ssh1
-2： 强制scp命令使用协议ssh2
-4： 强制scp命令只使用IPv4寻址
-6： 强制scp命令只使用IPv6寻址
-B： 使用批处理模式（传输过程中不询问传输口令或短语）
-C： 允许压缩。（将-C标志传递给ssh，从而打开压缩功能）
-p：保留原文件的修改时间，访问时间和访问权限。
-q： 不显示传输进度条。
-r： 递归复制整个目录。
-v：详细方式显示输出。scp和ssh(1)会显示出整个过程的调试信息。这些信息用于调试连接，验证和配置问题。
-c cipher： 以cipher将数据传输进行加密，这个选项将直接传递给ssh。
-F ssh_config： 指定一个替代的ssh配置文件，此参数直接传递给ssh。
-i identity_file： 从指定文件中读取传输时使用的密钥文件，此参数直接传递给ssh。
-l limit： 限定用户所能使用的带宽，以Kbit/s为单位。
-o ssh_option： 如果习惯于使用ssh_config(5)中的参数传递方式，
-P port：注意是大写的P, port是指定数据传输用到的端口号
-S program： 指定加密传输时所使用的程序。此程序必须能够理解ssh(1)的选项。
```

## 实例
### 1、从本地复制到远程
命令格式：
```text
scp local_file remote_username@remote_ip:remote_folder
或者
scp local_file remote_username@remote_ip:remote_file
或者
scp local_file remote_ip:remote_folder
或者
scp local_file remote_ip:remote_file
第1,2个指定了用户名，命令执行后需要再输入密码，第1个仅指定了远程的目录，文件名字不变，第2个指定了文件名；
第3,4个没有指定用户名，命令执行后需要输入用户名和密码，第3个仅指定了远程的目录，文件名字不变，第4个指定了文件名；
```

应用实例：
```text
scp /home/space/music/1.mp3 root@www.runoob.com:/home/root/others/music
scp /home/space/music/1.mp3 root@www.runoob.com:/home/root/others/music/001.mp3
scp /home/space/music/1.mp3 www.runoob.com:/home/root/others/music
scp /home/space/music/1.mp3 www.runoob.com:/home/root/others/music/001.mp3
```

复制目录命令格式：
```text
scp -r local_folder remote_username@remote_ip:remote_folder
或者
scp -r local_folder remote_ip:remote_folder
第1个指定了用户名，命令执行后需要再输入密码；
第2个没有指定用户名，命令执行后需要输入用户名和密码；
```

应用实例：
```text
scp -r /home/space/music/ root@www.runoob.com:/home/root/others/
scp -r /home/space/music/ www.runoob.com:/home/root/others/
```

上面命令将本地 music 目录复制到远程 others 目录下。

### 2、从远程复制到本地
从远程复制到本地，只要将从本地复制到远程的命令的后2个参数调换顺序即可，如下实例

应用实例：
```text
scp root@www.runoob.com:/home/root/others/music /home/space/music/1.mp3
scp -r www.runoob.com:/home/root/others/ /home/space/music/
```

说明：

1.如果远程服务器防火墙有为scp命令设置了指定的端口，我们需要使用 -P 参数来设置命令的端口号，命令格式如下：
```text
# scp 命令使用端口号 4588
scp -P 4588 remote@www.runoob.com:/usr/local/sin.sh /home/administrator
```
2.使用scp命令要确保使用的用户具有可读取远程服务器相应文件的权限，否则scp命令是无法起作用的。