本篇介绍了 Dockerfile 典型的基本结构和它支持的众多指令，
并具体讲解通过这些指令来编写定制镜像的 Dockerfile，以及如何生成镜像。

## 1.FROM指定基础镜像
FROM 指令用于指定其后构建新镜像所使用的基础镜像。如果本地不存在，则默认会去Docker Hub下载指定镜像。
FROM指令必是Dockerfile文件中的首条命令，启动构建流程后，Docker将基于该镜像构建新镜像，FROM后的命令也会基于这个基础镜像。

```text
FROM 语法格式为：
FROM 或
FROM :或
FROM :
```

通过FROM指定的镜像，可以是任何有效的基础镜像。
FROM有以下限制：
- FROM必须是Dockerfile中第一条非注释命令。
- 在一个Dockerfile文件中创建多个镜像时，FROM可以多次出现。只需在每个新命令FROM之前，记录提交上次的镜像ID。
- tag或digest是可选的，如果不使用这两个值时，会使用latest版本的基础镜像。

## 2.RUN执行命令
在镜像的构建过程中执行特定的命令，并生成一个中间镜像。格式：

```text
#shell格式
RUN
#exec格式
RUN ["executable", "param1", "param2"]
```

- RUN命令将在当前image中执行任意合法命令并提交执行结果。命令执行提交后，就会自动执行Dockerfile中的下一个指令。
- 层级RUN指令和生成提交是符合Docker核心理念的做法。它允许像版本控制那样，在任意一个点，对image镜像进行定制化构建。
- RUN指令创建的中间镜像会被缓存，并会在下次构建中使用。
  如果不想使用这些缓存镜像，可以在构建时指定--no-cache参数，如：docker build --no-cache。

## 3.CMD启动容器
CMD 用于指定在容器启动时所要执行的命令。CMD 有以下三种格式：

```text
CMD ["executable","param1","param2"]
CMD ["param1","param2"]
CMD command param1 param2
```

省略可执行文件的exec格式，这种写法使CMD中的参数当做ENTRYPOINT的默认参数，
此时ENTRYPOINT也应该是exec格式，具体与ENTRYPOINT的组合使用，参考ENTRYPOINT。

注意:与 RUN 指令的区别：RUN 在【构建的时候执行】，并生成一个新的镜像，CMD 在【容器运行的时候执行】，在构建时不进行任何操作。

## 4.LABEL添加元数据
LABEL 用于为镜像添加元数据，元数以键值对的形式指定：

```text
LABEL = = = ...
```

使用 LABEL 指定元数据时，一条 LABEL 可以指定一条或多条元数据，指定多条元数据时不同元数据之间通过空格分隔。
推荐将所有的元数据通过一条 LABEL 指令指定，以免生成过多的中间镜像。
如，通过 LABEL 指定一些元数据：

```text
LABEL version="1.0" description="这是一个Web服务器" by="IT笔录"
```

指定后可以通过 docker inspect 查看：

```text
docker inspect /test
"Labels": {
  "version": "1.0",
  "description": "这是一个Web服务器",
  "by": "IT笔录"
},
```

## 5.EXPOSE设置监听端口
为构建的镜像设置监听端口，是容器在运行时监听。格式：

```text
EXPOSE [...]
#例如：EXPOSE 22 80 8443
```

注意：该指令知识起到声明作用，并不会自动完成端口映射。
在启动容器时需要使用-P，Docker主机会自动分配一个宿主机的临时端口转发到指定的端口；
使用-p（注意大小写），则可以具体指定哪个宿主机的本地端口有会映射过来。

## 6.ENV设置环境变量
指定环境变量，在镜像生成过程中会被后续 RUN 指令使用，在镜像启动的容器中也会存在。

```text
ENV  
ENV = =...
```

7.COPY复制
格式如下：

```text
COPY <源路径>... <目标路径>
COPY ["<源路径1>",... "<目标路径>"]
```

复制本地主机的源地址（为 Dockerfile 所在目录的相对路径、文件或目录）下的内容到镜像的目的地址下。
目标路径不存在时，会自动创建。
当使用本地目录为源目录时，推荐使用COPY。

## 8.ADD复制
该命令将复制指定的源路径下的内容到容器中的目的路径下。ADD 指令和 COPY 的格式和性质基本是一致的。
但是在COPY基础上增加了一些功能。
例如，源路径可以是一个 URL，这种情况下，Docker引擎会试图去下载这个链接的文件到目标路径去。

在构建镜像时，复制上下文中的文件到镜像内，格式：

```text
ADD <源路径>... <目标路径>
ADD ["<源路径>",... "<目标路径>"]
```

## 9.ENTRYPOINT
ENTRYPOINT 用于给容器配置一个可执行程序。也就是说，每次使用镜像创建容器时，通过 ENTRYPOINT 指定的程序都会被设置成默认程序。
ENTRYPOINT 有以下两种形式：

```text
ENTRYPOINT ["executable", "param1", "param2"]
ENTRYPOINT command param1 param2
```

ENTRYPOINT 与 CMD 非常类似，
不同的是通过docker run执行的命令不会覆盖 ENTRYPOINT，而docker run命令中指定的任何参数，都会被当做参数再次传递给 ENTRYPOINT。
Dockerfile 中只允许有一个 ENTRYPOINT 命令，多指定时会覆盖前面的设置，而只执行最后的 ENTRYPOINT 指令。

docker run运行容器时指定的参数都会被传递给 ENTRYPOINT ，且会覆盖 CMD 命令指定的参数。
如，执行docker run <image> -d时，-d 参数将被传递给入口点。

也可以通过docker run --entrypoint重写 ENTRYPOINT 入口点。如：可以像下面这样指定一个容器执行程序：

```text
ENTRYPOINT ["/usr/bin/nginx"]
```

完整构建代码：

```text
FROM ubuntu:16.04
MAINTAINER MoeWah "admin@mao.sh"
RUN apt-get update
RUN apt-get install -y nginx
RUN echo 'Hello World, 我是个容器' > /var/www/html/index.html
ENTRYPOINT ["/usr/sbin/nginx"]
EXPOSE 80
```

使用 docker build 构建镜像，并将镜像指定为 moewah/test：

```text
docker build -t="moewah/test" .
```

构建完成后，使用 moewah/test 启动一个容器：

```text
docker run -i -t  moewah/test -g "daemon off;"
```

在运行容器时，我们使用了-g "daemon off;"，这个参数将会被传递给 ENTRYPOINT，
最终在容器中执行的命令为/usr/sbin/nginx -g "daemon off;"。

## 10.VOLUME 定义匿名卷
VOLUME 用于创建挂载点，即向基于所构建镜像创始的容器添加卷：

```text
VOLUME ["/data"]
```

一个卷可以存在于一个或多个容器的指定目录，该目录可以绕过联合文件系统，并具有以下功能：

- 卷可以容器间共享和重用
- 容器并不一定要和其它容器共享卷
- 修改卷后会立即生效
- 对卷的修改不会对镜像产生影响
- 卷会一直存在，直到没有任何容器在使用它

## 11.WORKDIR指定工作目录
WORKDIR 用于在容器内设置一个工作目录：

```text
WORKDIR /path/to/workdir
```

通过 WORKDIR 设置工作目录后，Dockerfile 中其后的命令 RUN、CMD、ENTRYPOINT、ADD、COPY 等命令都会在该目录下执行。 
如，使用 WORKDIR 设置工作目录：

```text
WORKDIR /aWORKDIR bWORKDIR cRUN pwd
```

在以上示例中，pwd 最终将会在/a/b/c目录中执行。在使用 docker run 运行容器时，可以通过-w参数覆盖构建时所设置的工作目录。

## 12.USER指定当前用户
指定运行容器时的用户名或 UID，后续的 RUN 等指令也会使用指定的用户身份。语法格式为：

```text
USER daemon
```

使用 USER 指定用户时，可以使用用户名、UID 或 GID，或者两者的组合。以下都是合法的指定：

```text
USER user
USER user:group
USER uid
USER uid:gid
USER user:gid
USER uid:group
```

## 13.ARG
指定一些镜像内使用的参数（例如版本号信息等），这些参数在执行docker build命令时才以--build-arg=格式传入。语法格式为：

```text
ARG [=]
docker build --build-arg site=moewah.com -t moewah/test .
```

## 14.ONBUILD
配置当前所创建的镜像作为其他镜像的基础镜像时，所执行的创建操作指令。语法格式为：

```text
ONBUILD [INSTRUCTION]
```

例如，Dockerfile 使用如下的内容创建了镜像image-A：

```text
[...]
ONBUILD ADD . /app/src
ONBUILD RUN /usr/local/bin/python-build --dir /app/src
[...]
```

如果基于 image-A 创建新的镜像时，新的 Dockerfile 中使用FROM image-A指定基础镜像，会自动执行 ONBUILD 指令的内容，
等价于在后面添加了两条指令：

```text
FROM image-A
ADD . /app/src
RUN /usr/local/bin/python-build --dir /app/src
```

使用 ONBUILD 指令的镜像，推荐在标签中注明，例如ruby:1.9-onbuild。

## 15.STOPSIGNAL
STOPSIGNAL 用于设置停止容器所要发送的系统调用信号：

```text
STOPSIGNAL signal
```

所使用的信号必须是内核系统调用表中的合法的值，如：SIGKILL。

## 16.SHELL
指定其他命令使用 shell 时的默认 shell 类型。

```text
SHELL ["executable", "parameters"]
```

默认值为["/bin/sh","-c"]。

## 17.Dockerfile示例

构建 Nginx 运行环境：
```text
# 指定基础镜像
FROM sameersbn/ubuntu:14.04.20161014
# 维护者信息
MAINTAINER moewah "admin@mao.sh"
# 设置环境
ENV RTMP_VERSION=1.1.10 \
NPS_VERSION=1.11.33.4 \
LIBAV_VERSION=11.8 \
NGINX_VERSION=1.10.1 \
NGINX_USER=www-data \
NGINX_SITECONF_DIR=/etc/nginx/sites-enabled \
NGINX_LOG_DIR=/var/log/nginx \
NGINX_TEMP_DIR=/var/lib/nginx \
NGINX_SETUP_DIR=/var/cache/nginx

# 设置构建时变量，镜像建立完成后就失效
ARG BUILD_LIBAV=false
ARG WITH_DEBUG=false
ARG WITH_PAGESPEED=true
ARG WITH_RTMP=true

# 复制本地文件到容器目录中
COPY setup/ ${NGINX_SETUP_DIR}/
RUN bash ${NGINX_SETUP_DIR}/install.sh

# 复制本地配置文件到容器目录中
COPY nginx.conf /etc/nginx/nginx.conf
COPY entrypoint.sh /sbin/entrypoint.sh

# 运行指令
RUN chmod 755 /sbin/entrypoint.sh

# 允许指定的端口
EXPOSE 80/tcp 443/tcp 1935/tcp

# 指定网站目录挂载点
VOLUME ["${NGINX_SITECONF_DIR}"]

ENTRYPOINT ["/sbin/entrypoint.sh"]
CMD ["/usr/sbin/nginx"]
```

构建 Tomcat 环境：
```text
# 指定基于的基础镜像
FROM ubuntu:13.10
# 维护者信息
MAINTAINER moewah "admin@mao.sh"

# 镜像的指令操作
# 获取APT更新的资源列表
RUN echo "deb http://archive.ubuntu.com/ubuntu precise main universe"> /etc/apt/sources.list
# 更新软件
RUN apt-get update

# Install curl
RUN apt-get -y install curl

# Install JDK 7
RUN cd /tmp && curl -L 'http://download.oracle.com/otn-pub/java/jdk/7u65-b17/jdk-7u65-linux-x64.tar.gz' -H 'Cookie: oraclelicense=accept-securebackup-cookie; gpw_e24=Dockerfile' | tar -xz
RUN mkdir -p /usr/lib/jvm
RUN mv /tmp/jdk1.7.0_65/ /usr/lib/jvm/java-7-oracle/

# Set Oracle JDK 7 as default Java
RUN update-alternatives --install /usr/bin/java java /usr/lib/jvm/java-7-oracle/bin/java 300
RUN update-alternatives --install /usr/bin/javac javac /usr/lib/jvm/java-7-oracle/bin/javac 300
# 设置系统环境
ENV JAVA_HOME /usr/lib/jvm/java-7-oracle/

# Install tomcat7
RUN cd /tmp && curl -L 'http://archive.apache.org/dist/tomcat/tomcat-7/v7.0.8/bin/apache-tomcat-7.0.8.tar.gz' | tar -xz
RUN mv /tmp/apache-tomcat-7.0.8/ /opt/tomcat7/

ENV CATALINA_HOME /opt/tomcat7
ENV PATH $PATH:$CATALINA_HOME/bin
# 复件tomcat7.sh到容器中的目录
ADD tomcat7.sh /etc/init.d/tomcat7
RUN chmod 755 /etc/init.d/tomcat7

# Expose ports.  指定暴露的端口
EXPOSE 8080

# Define default command.
ENTRYPOINT service tomcat7 start && tail -f /opt/tomcat7/logs/catalina.out
```


tomcat7.sh命令文件
```text
export JAVA_HOME=/usr/lib/jvm/java-7-oracle/
export TOMCAT_HOME=/opt/tomcat7

case $1 in
start)
sh $TOMCAT_HOME/bin/startup.sh
;;
stop)
sh $TOMCAT_HOME/bin/shutdown.sh
;;
restart)
sh $TOMCAT_HOME/bin/shutdown.sh
sh $TOMCAT_HOME/bin/startup.sh
;;
esac
exit 0
```

## 写在最后：原则和建议
首先，要尽量吃透每个指令的含义和执行效果，自己多编写一些简单的例子进行测试，弄清楚了在撰写正式的 Dockerfile。
此外，Docker Hub 官方仓库中提供了大量的优秀镜像和对应的 Dockerfile，可以通过阅读它们来学习如何撰写高效的 Dockerfile。

* 容器轻量化。从镜像中产生的容器应该尽量轻量化，能在足够短的时间内停止、销毁、重新生成并替换原来的容器。
* 使用.gitignore。在大部分情况下，Dockerfile 会和构建所需的文件放在同一个目录中，为了提高构建的性能，
  应该使用.gitignore 来过滤掉不需要的文件和目录。
* 为了减少镜像的大小，减少依赖，仅安装需要的软件包。
* 一个容器只做一件事。解耦复杂的应用，分成多个容器，而不是所有东西都放在一个容器内运行。
  如一个 Python Web 应用，可能需要 Server、DB、Cache、MQ、Log 等几个容器。一个更加极端的说法：One process per container。
* 减少镜像的图层。不要多个 Label、ENV 等标签。
* 对续行的参数按照字母表排序，特别是使用apt-get install -y安装包的时候。
* 使用构建缓存。如果不想使用缓存，可以在构建的时候使用参数--no-cache=true来强制重新生成中间镜像。