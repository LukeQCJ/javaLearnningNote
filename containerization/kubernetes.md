# kubernetes学习

---
## 一、kubernetes简介

### 1、简介
> kubernetes是开源的、生产级的、自动化的部署、扩展和管理 的**容器编排**系统。
>> Google于2014年开源给 ***CNCF*** (Cloud Native Computing Foundation)。
> 
>> kubernetes希腊语，意思为 “驾驶员”。（可以理解为 容器 就是一条条海上的船，k8s就是 驾驶员 来引导容器的方向）

### 2、容器
> 容器技术是指 一系列隔离运行的**进程**，提供了一种轻量级 操作系统层面的**虚拟化技术**。相对于传统VM虚拟机，具有启动快、性能损耗小、更轻量等优点。
>> 基于Linux系统的NameSpace实现资源隔离、基于CGroups实现资源限制。
> 
>> 虚拟机和容器对比：
![vmVScontainer.png](img/vmVScontainer.png)

> ***docker***是目前使用最广、最成熟的容器技术。
>> docker的3大核心：
>> * 镜像
>> * 容器
>> * 仓库
> 
>> 目前K8S默认使用的是docker引擎。
> 
> 容器化应用的优势：
>> 容器解决了应用 打包、部署、运行的问题，“一次构建、随处运行”(Build, Ship, and Run Any App, Anywhere) 
> 
> 容器化系统面临的挑战：
>> * 跨机器部署
>> * 资源调度
>> * 负载均衡
>> * 自动伸缩
>> * 容错处理
>> * 服务发现
>> 
### 3、容器编排 
> 容器编排指的是 以 **容器** 为基本对象，进行 **容器调度**、**资源管理**、**服务管理**，协同各个容器 来共同实现应用的功能。
  
### 4、kubernetes发展史
> 基于Google内部的“Borg”和“Omega”系统，于2014年10月正式开源，现在由云原生计算基金会CNCF 负责管理。并且，受到众多厂商(Google、RedHat、CoreOS、IBM、MicroSoft、HUAWEI)的积极支持，社区十分活跃。
> 
> kubernetes是Google开源的生产级的容器编排系统，是Google多年大规模容器管理技术Borg的开源版本。
>> 提供的功能特性如下：
>>> * 基于容器的应用部署、维护和滚动升级
>>> * 负载均衡和服务发现
>>> * 跨机器和跨地区的集群调度
>>> * 自动伸缩
>>> * 无状态服务和有状态服务
>>> * 广泛的Volume支持
>>> * 插件机制保证扩展性
> 
> kubernetes发展非常迅速，已经成为容器编排领域的领导者。

---

## 二、kubernetes架构与核心组件详解

### 1、kubernetes架构
> kubernetes cluster是一个master-slave的架构
> ![k8sArchitecture.png](img/k8sArchitecture.png)
>>  * master
>>  * worker node (Minion)

### 2、master节点
> k8s的master节点的内部组件
> ![masterInnerComponents.png](img/masterInnerComponents.png)
>> * API Server
>> * Scheduler
>> * Controller Manager
>> * DashBoard(addons)

### 3、worker 节点
> worker 节点的核心组件：
> 
> ![slaveInnerArchitecture.png](img/slaveInnerArchitecture.png)
> * kubelet: 处理master节点下发到本节点的任务，管理pod和其中的容器，并定期向master节点汇报资源使用情况；
> * kube-proxy: 运行在work节点上的agent，实现service的抽象，为一组pod抽象的服务(service)提供统一接口并提供负责均衡功能；
> * container runtime: docker、rocket

### 4、etcd
> etcd是一个由CoreOS开发并开源的、基于Raft协议的分布式的一致性K\V存储。
> 
> ![etcdInK8s.png](img/etcdInK8s.png)
> 
> 在k8s架构中，用作分布式K\V存储系统，用于保存集群所有的网络配置信息和对象的状态信息。

### 5、kubernetes整体架构
>![k8sArchitectureDetails.png](img/k8sArchitectureDetails.png)

### 6、kubernetes调用流程
> ![k8sFlowProcessDiagram.png](img/k8sFlowProcessDiagram.png)
> 1. 用户通过“kubectl”命令进行操作，例如部署新的应用;
> 2. API Server接收到请求，并将其存储到etcd;
> 3. Watcher和Controllers检测到资源状态的变化，并进行操作;
> 4. Replica Watcher/Controller检测到新的app，创建新的pod达到期望的实例个数;
> 5. Scheduler将新的pod分配到kubelet;
> 6. kubelet检测到pods，并通过容器运行时部署它们;
> 7. kube-proxy管理pod的网络，包括服务发现、负载均衡;
>

---
## 三、kubernetes安装
> 待补充

---

## 四、kubernetes基础

### 1、资源对象
> 在kubernetes中，所有资源实体都可以表示为资源对象。
> 通过Yaml格式文件来描述资源，称为资源清单(Manifest)。
> * 资源实例化后就称为对象
> * 通过API或kubectl来管理kubernetes资源对象

### 2、资源类型
> kubernetes中的主要资源类型如下:
> 
> ![k8sResources.png](img/k8sResources.png)

> kubernetes的对象模型(即Yaml描述文件的格式)
> 
> ![k8sResourceSpecYml.png](img/k8sResourceSpecYml.png)
> * apiVersion: 创建该对象使用的kubernetes API的版本
> * kind: 想要创建的对象的类型
> * metadata: 帮助识别该对象的唯一性的数据，包括name字符串、UID和namespace
> * spec: 用于描述资源的容器配置信息
> 
### 3、创建资源对象时常用命令
> ![k8sResourceCreateCommand.png](img/k8sResourceCreateCommand.png)
> 
> 其他命令:
> 
> kubectl logs pod名称: 查看pod日志
> 
> kubectl exec -it pod名称 sh: 进入pod中

---

## 五、kubernetes核心概念

### 1、pods（豌豆荚）
> pod是一组紧密管理的容器集合，一个容器就像一粒豌豆，pod就是包裹着多个豌豆的壳。
> 
> 同一个pod中，各个容器共享PID、IPC、Network和UTS namespace。
> 
> pod是kubernetes中调度的基本单位。pod会被调度到worker节点上去，因此pod是一个非持久的实体。
> 
> ![k8sPods.png](img/k8sPods.png)
> 
> pod的生命周期：
> 
> ![k8sPodLifecycle.png](img/k8sPodLifecycle.png)
> 
### 2、Labels和Selectors
> * labels: 用来 **标记** 资源
>   * 标记任务对象
>   * 具体表示形式为: key: value
> * Selectors: 根据标记的资源来 **选择** 某些标记的资源
>   * 根据label来选择对象
>   * 支持2种方式：
>     * equality-based: key=value
>     * set-based: key in (value1,value2,...)
> 
### 3、controller
> Controller是在集群上管理和运行容器的对象，Controller是实际存在的，Pod是虚拟的。
> 
> Pod是通过Controller实现应用的运维，比如弹性伸缩，滚动升级等.
> Pod和Controller之间是通过label标签来建立关系，同时Controller又被称为控制器工作负载。
>
> Controller使用pod模板来创建实际需要的pod，并保证其按照某种期望的状态运行(如副本数量等)。
> 
> Controller可以创建和管理多个pod，提供副本管理、滚动升级和集群级别的自愈能力。
> 
> kubernetes中内置了多种controller:
> 
> ![k8sControllers.png](img/k8sControllers.png)
> 
### 4、ReplicaSet
> ReplicaSet 可以确保pod以指定的副本数运行，即如果有容器异常退出，会自动创建新的pod来替代。
> 只支持equality-based selector。
> 
> * ReplicationController (rc)
>   * 功能和ReplicaSet相似，但仅支持equality-based selector。
> * ReplicaSet (rs)
>   * 是ReplicationController的升级，支持set-based selector。
>   * 支持自动扩容和收缩。如：kubectl scale --replicas=5 -f pod-rs.yml
>   * 不支持服务的滚动部署。
>   * 可单独使用，但是更多时候还是使用Deployment来管理。
> 
### 5、Deployment
> deployment提供了一种声明式的方法来通过ReplicaSet管理pod。
> 
> ![k8sDeployment.png](img/k8sDeployment.png)
> 
> * 支持pod的RollingUpdate，并自动管理其背后的ReplicaSet。
> * 支持rollback到之前的revision。
> 
> ![k8sDeploymentArchitecture.png](img/k8sDeploymentArchitecture.png)