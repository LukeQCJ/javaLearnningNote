# 四、Zookeeper客户端（zkCli）的使用

操作命令：
- create [-s] [-e] path data [acl]：创建ZNode，-s 指定是顺序节点，-e 指定是临时节点。
- ls path [watch]：列出Path下所有子ZNode。
- get path [watch]：获取Path对应的ZNode的数据和属性。
- ls2 path [watch]：查看Path下所有子ZNode以及子ZNode的属性。
- set path data [version]：更新节点，version 数据版本。
- delete path [version]：删除节点，如果要删除的节点有子ZNode则无法删除，version 数据版本。
- rmr path：删除节点，如果有子ZNode则递归删除。
- setquota -n | -b val path：修改ZNode配额，-n 设置子节点最大个数，-b 设置节点数据最大长度。
- history：列出历史记录。


## 1、多节点类型创建

创建持久节点：
```text
create /xxx vvv
```

创建持久序号节点：
```text
create -s /xxx vvv
```

创建临时节点：
```text
create -e /xxx vvv
```

创建临时序号节点：
```text
create -e -s /xxx vvv
```

创建容器节点
```text
create -c /xxx vvv
```

## 2、查询节点

普通查询：
```text
# 本级查询
ls /xxx

# 递归查询
ls -R /xxx
```

查询节点的内容：
```text
get /xxx
```

查询节点信息：
```text
get -s /xxx
```

注：节点信息
```text
data：数据
cZxid：创建节点的事务ID
mZxid：修改节点的事务ID
pZxid：添加和删除子节点的事务ID
ctime：节点创建的时间
mtime：节点最近修改的时间
dataVersion：节点内数据的版本，每更新一次数据，版本会+1
aclVersion：此节点的权限版本
ephemeralOwner：如果当前节点是临时节点，该是是当前节点所有者的session id。如果节点不是临时节点，则该值为零
dataLength：节点内数据的长度
numChildren：该节点的子节点个数
```

## 3、删除节点

普通删除：
```text
# 删除节点，当有子节点，删除失败，要用下面的命令
delete /xxx

# 删除节点以及子节点
deleteall /xxx

rmr /app1 递归删除
```

乐观锁删除：
```text
# 只有指定删除的数据版本 == 当前节点的数据版本号，才能够删除成功，因为每对节点进行一次数据操作，节点的dataVersion就会+1。
# 这样删除，通过乐观锁保证并发下数据操作的唯一性
delete -v dataVersion
```

## 4、权限设置
注册当前会话的账号和密码：
```text
addauth digest xiaowang:123456
```

创建节点并设置权限（指定该节点的用户，以及用户所拥有的权限s）：
```text
create /test-node abcd auth:xiaowang:123456:cdwra
```

在另一个会话中必须先使用账号密码，才能拥有操作节点的权限。
