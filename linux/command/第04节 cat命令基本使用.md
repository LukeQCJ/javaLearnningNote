cat（英文全拼：concatenate）命令用于连接文件并打印到标准输出设备上，它的主要作用是用于查看和连接文件。

## 使用权限
所有使用者

## 语法格式
```text
cat [选项] [文件]
```

参数说明：
```text
-n：显示行号，会在输出的每一行前加上行号。
-b：显示行号，但只对非空行进行编号。
-s：压缩连续的空行，只显示一个空行。
-E：在每一行的末尾显示 $ 符号。
-T：将 Tab 字符显示为 ^I。
-v：显示一些非打印字符。
```

使用说明：
```text
显示文件内容：cat filename 会将指定文件的内容输出到终端上。
连接文件：cat file1 file2 > combined_file 可以将 file1 和 file2 的内容连接起来，并将结果输出到 combined_file 中。
创建文件：可以使用 cat 命令来创建文件，例如 cat > filename，然后你可以输入文本，按 Ctrl+D 来保存并退出。
在终端显示文件：可以将 cat 与管道（|）结合使用，用来显示其他命令的输出，例如 ls -l | cat 会将 ls -l 的输出通过 cat 打印到终端上。
```

## 实例
查看文件内容：显示文件 filename 的内容。
```text
cat filename
```

创建文件：将标准输入重定向到文件 filename，覆盖该文件的内容。
```text
cat > filename
```

追加内容到文件：将标准输入追加到文件 filename 的末尾。
```text
cat >> filename
```

连接文件：将 file1 和 file2 的内容合并到 file3 中。
```text
cat file1 file2 > file3
```

显示多个文件的内容：同时显示 file1 和 file2 的内容。
```text
cat file1 file2
```

使用管道：将 cat 命令的输出作为另一个命令的输入。
```text
cat filename | command
```

查看文件的最后几行：显示文件 filename 的最后 10 行。
```text
cat filename | tail -n 10
```

使用 -n 选项显示行号：显示文件 filename 的内容，并在每行的前面加上行号。
```text
cat -n filename
```

使用 -b 选项仅显示非空行的行号：
```text
cat -b filename
```

使用 -s 选项合并空行：显示文件 filename 的内容，并合并连续的空行。
```text
cat -s filename
```

使用 -t 选项显示制表符：显示文件 filename 的内容，并用 ^I 表示制表符。
```text
cat -t filename
```

使用 -e 选项显示行结束符：显示文件 filename 的内容，并用 $ 表示行结束。
```text
cat -e filename
```

把 textfile1 的文档内容加上行号后输入 textfile2 这个文档里：
```text
cat -n textfile1 > textfile2
```

把 textfile1 和 textfile2 的文档内容加上行号（空白行不加）之后将内容附加到 textfile3 文档里：
```text
cat -b textfile1 textfile2 >> textfile3
```

清空 /etc/test.txt 文档内容：
```text
cat /dev/null > /etc/test.txt
```

cat 也可以用来制作镜像文件。例如要制作软盘的镜像文件，将软盘放好后输入：
```text
cat /dev/fd0 > OUTFILE
```

相反的，如果想把 image file 写到软盘，输入：
```text
cat IMG_FILE > /dev/fd0
```
注：
```text
1. OUTFILE 指输出的镜像文件名。
2. IMG_FILE 指镜像文件。
3. 若从镜像文件写回 device 时，device 容量需与相当。
4. 通常用制作开机磁片。
```