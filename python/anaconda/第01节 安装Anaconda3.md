# 开始安装Anaconda3

1） 获取安装包

本文推荐更稳定的离线安装，该方法需要下载Anaconda的安装包。

在 Anaconda3官网 或者 [清华大学镜像站(建议)](https://mirrors.tuna.tsinghua.edu.cn/anaconda/archive/)。

下载Anaconda3 for Linux对应版操作系统的最新版本的安装包本文使用的版本是 Anaconda3-2021.05-Linux-x86_64.sh

您也可以使用wget命令获取安装文件
```text
wget https://repo.anaconda.com/archive/Anaconda3-2021.05-Linux-x86_64.sh
```

2） 打开终端，使用CD命令转到Anaconda的下载位置（或在资源管理器中安装包所在的目录下右键打开终端）。

3） 使用bash命令安装Anaconda
```text
bash Anaconda3-2021.05-Linux-x86_64.sh
```

注意：请在命令中将Anaconda3-2021.05-Linux-x86_64.sh替换为您下载的文件名。

4） 在看到以下提示时按回车键继续

5） 在看到要输入内容后直接输入yes同意许可协议以继续您的安装

6） 选择您的安装位：
按回车键选择默认位置安装，本文建议选择默认位置安装即可，默认安装到当前用户目录下的Anaconda3文件夹中。

7） 此处询问您是否需要程序自动为您配置环境变量。
输入yes自动配置环境变量，输入no稍后自行配置。虽然不建议自动配置，本文选择自动配置。

8） 如果选择自动配置环境变量则到此anaconda已安装成功，您需要重新启动终端来激活conda相关命令。

# 环境变量配置
如果您选择了no并且需要配置环境变量，或因为其他原因需要手动配置环境变量请看本条，选择yes并且配置成功的请跳过本节直达下一节测试conda是否安装成功。

```text
方法一 直接使用export配置变量
export PATH="[你安装的路径]/anaconda3/bin:/$PATH"

# 请将[你安装的路径]替换为实际安装了路径，举例：
export PATH="/home/lineeks/anaconda3/bin:/$PATH"
```

```text
方法二 使用vim编辑器
vim ~/.bashrc

# 在最后一行加上 （按o进入编辑模式并在下一行插入文本）
export PATH="[你安装的路径]/anaconda3/bin:/$PATH"

# 插入完成后按两下Esc退出编辑模式，输入以下命令回车保存并退出
:qw!
```

# 测试安装是否成功
重新打开终端并输入以下命令来启动Anaconda：
```text
conda list
```

如果Anaconda成功安装，您将看到已安装的包列表。

安装成功后建议更新Anaconda3，使用以下命令更新Anaconda：
```text
conda update -y conda
```

# anaconda常用命令

conda list：查看安装包列表
conda env list：查看环境列表
conda create -n my_env python==3.8：创建虚拟环境，并指定python版本为3.8
conda activate my_env：激活my_env环境
conda deactivate：退出当前环境



