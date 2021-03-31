Mariadb + Galera集群配置安装流程

1. 软件源更新，所需组件安装

   ```shell
   apt update
   apt install mariadb-server galera-3 -y
   ```

2. 启动mariadb服务

   ```shell
   systemctl enable mariadb
   systemctl restart mariadb
   ```

3. 配置mysql

   ```sh
   mysql_secure_installation
   mysql -u root -p
   ```

4. mysql用户、权限配置

   ```sql
   #只读
   CREATE USER ‘guo’@'%' IDENTIFIED BY '1003';
   GRANT SELECT ON *.* TO 'guo'@'%' IDENTIFIED BY '1003';
   #读写
   CREATE USER 'eddie'@'%' IDENTIFIED BY '1003';
   GRANT ALL PRIVILEGES ON *.* TO 'eddie'@'%' IDENTIFIED BY '1003';
   ```

5. 修改配置文件（多个节点注意区分）

   ```shell
   vim /etc/mysql/mariadb.conf.d/50-server.cnf
   #基于实际配置、需求修改mysql常规配置项
   ...
   #Galera配置项
   [mariadb-10.1]
   wsrep_on = ON
   #确保将二进制日志格式设置为使用行级复制，而不是语句级复制。
   #请勿更改此值，因为它会影响性能和一致性。二进制日志只能使用行级复制。
   binlog_format = ROW
   #对于mysql只能使用innodb引擎
   default_storage_engine = innodb
   #确保用于生成自动增量值的InnoDB锁定模式设置为交错锁定模式，该模式由2值指定。
   #不要更改此值。其他模式可能导致INSERT具有AUTO_INCREMENT列的表失败。
   innodb_autoinc_lock_mode = 2
   innodb_file_per_table
   innodb_log_buffer_size = 8M
   #InnoDB缓冲池 建议为物理内存的3/4或4/5
   innodb_buffer_pool_size = 256M
   #开启二进制日志
   log-bin = mysql-bin
   wsrep_provider = /usr/lib/galera/libgalera_smm.so
   #首先启动的节点以下配置项需改成 wsrep_cluster_address = "gcomm://" 作为单节点集群启动，不向任何节点同步数据
   wsrep_cluster_address = "gcomm://100.8.0.88,100.8.0.89,100.8.0.90"
   wsrep_cluster_name = 'cluster_eddie'
   wsrep_node_address = '100.8.0.88'
   wsrep_node_name = 'MariaDB-Node-01'
   #wsrep_node_name = 'MariaDB-Node-02'
   #wsrep_node_name = 'MariaDB-Node-03'
   wsrep_sst_method = rsync
   #同步数据时使用的账号密码，多节点统一
   wsrep_sst_auth = eddie:1003
   #禁止DNS查询
   skip-name-resolve
   ```

6. 修改hosts

   ```shell
   vim /etc/hosts
   MariaDB-Node-01  100.8.0.88
   MariaDB-Node-02  100.8.0.89
   MariaDB-Node-03  100.8.0.90
   ```

7. 启动集群

   ```shell
   #第一个节点启动（单节点集群）
   galera_new_cluster
   #其余节点启动（自动读取配置文件加入集群）
   systemctl restart mariadb
   ```

8. 相关指令

   ```sql
   #查看集群状态
   SHOW STATUS LIKE "wsrep_cluster%"；
   +--------------------------+--------------------------------------+
   | Variable_name            | Value                                |
   +--------------------------+--------------------------------------+
   | wsrep_cluster_conf_id    | 5                                    |
   | wsrep_cluster_size       | 3                                    |
   | wsrep_cluster_state_uuid | 39cc944f-8ba8-11eb-8937-125aea8623aa |
   | wsrep_cluster_status     | Primary                              |
   +--------------------------+--------------------------------------+
   #一般集群是单数个节点，在主节点down之后会推举出来一个新的主节点，如果是偶数个节点就会出现脑裂问题
   #手动设定主节点
   SET GLOBAL wsrep_provider_options='pc.bootstrap=1';
   ```

   [详细参数]: https://galeracluster.com/library/documentation/galera-status-variables.html

9. 相关排错

- 关机后重启集群失败

  ```shell
  2021-03-24  1:25:39 139960160324736 [ERROR] WSREP: gcs/src/gcs_core.cpp:gcs_core_open():208: Failed to open backend connection: -110 (Connection timed out)
  2021-03-24  1:25:39 139960160324736 [ERROR] WSREP: gcs/src/gcs.cpp:gcs_open():1404: Failed to open channel 'cluster_eddie' at 'gcomm://100.8.0.88,100.8.0.89,100.8.0.90': -110 (Connection timed out)
  2021-03-24  1:25:39 139960160324736 [ERROR] WSREP: gcs connect failed: Connection timed out
  2021-03-24  1:25:39 139960160324736 [ERROR] WSREP: wsrep::connect(gcomm://100.8.0.88,100.8.0.89,100.8.0.90) failed: 7
  2021-03-24  1:25:39 139960160324736 [ERROR] Aborting
  ```

  说明当前节点启动时尝试连接集群中其他节点失败，导致无法启动，修改当前节点配置，设置为空集群，不向任何节点同步数据

  ```shell
  #wsrep_cluster_address="gcomm://100.8.0.88,100.8.0.89,100.8.0.90"
  wsrep_cluster_address="gcomm://"
  ```

  如果运气不好，关机时当前节点不是最后一个退出集群的节点，会出现如下

  ```shell
  tail -f /var/log/mysql/error.log
  2021-03-24  1:32:22 139893234891904 [Note] WSREP: GCache history reset: old(39cc944f-8ba8-11eb-8937-125aea8623aa:0) -> new(39cc944f-8ba8-11eb-8937-125aea8623aa:5)
  2021-03-24  1:32:22 139893234891904 [Note] WSREP: Assign initial position for certification: 5, protocol version: -1
  2021-03-24  1:32:22 139893234891904 [Note] WSREP: wsrep_sst_grab()
  2021-03-24  1:32:22 139893234891904 [Note] WSREP: Start replication
  2021-03-24  1:32:22 139893234891904 [Note] WSREP: Setting initial position to 39cc944f-8ba8-11eb-8937-125aea8623aa:5
  2021-03-24  1:32:22 139893234891904 [ERROR] WSREP: It may not be safe to bootstrap the cluster from this node. It was not the last one to leave the cluster and may not contain all the updates. To force cluster bootstrap with this node, edit the grastate.dat file manually and set safe_to_bootstrap to 1 .
  2021-03-24  1:32:22 139893234891904 [ERROR] WSREP: wsrep::connect(gcomm://) failed: 7
  2021-03-24  1:32:22 139893234891904 [ERROR] Aborting
  ```

  所以要找到最后退出集群的节点（节点少还可以，节点多了就比较麻烦了，所以最好设置主从节点），也可以通过设置权重来强制以某节点作为原始数据进行同步。

  ```shell
  root@ubuntu:~# cat /var/lib/mysql/grastate.dat
  GALERA saved state
  version: 2.1
  #uuid可以判断多节点是否在一个集群下
  uuid:    39cc944f-8ba8-11eb-8937-125aea8623aa 
  seqno:   -1
  #权重 默认是0
  #safe_to_bootstrap: 0 
  safe_to_bootstrap: 1
  ```

- 额外配置项

  ```shell
  #会将冲突信息写入错误日志中，例如两个节点同时写同一行数据
  wsrep_log_conflicts = ON
  #复制过程中的错误信息写在日志中
  wsrep_provider_options = "cert.log_conflicts=ON"
  #显示debug 信息在日志中
  wsrep_debug = ON 
  ```

10. 概念以及原理

    DBMS: 数据库管理系统
    
    wsrep API：为Galera Replication Plugin提供接口、回调函数等功能，连接、访问数据库
    
    Galera Replication Plugin：提供核心复制功能的插件，主要包括三层
    
    - Certification Layer: 准备写集（write-set），进行验证，保证其他节点可以正常使用，提交事务
    - Replication Layer: 分配全局事务ID（GTID）并进行排序，管理复制协议。
    - Group Communication Framework:通信框架，与集群中的各节点进行通信。
    
    Group Communication Plugins：通信组件，实现虚拟同步。所谓虚拟同步，简单说是指一个事务在一个节点上执行成功后，保证它在其它节点也一定会被成功执行，但并不能保证实时同步。为了解决实时性问题，Galera集群实现了自己的运行时可配置的时态流控。
    
    复制插件工作原理及流程：
    
    基于验证的复制使用组通信和事务排序技术实现同步复制。它通过广播并发事务之间建立的全局总序来协调事务提交。简单说就是事务必须以相同的顺序应用于所有实例。事务在本节点乐观执行，然后在提交时运行一个验证过程以保证全局数据一致性。所谓乐观执行是指，事务在一个节点提交时，被认为与其它节点上的事务没有冲突，首先在本地执行，然后再发送到所有节点做冲突检测，无冲突时在所有节点提交，否则在所有节点回滚。
    
    详细请移步
    
    []: https://blog.csdn.net/wzy0623/article/details/102522268
    
    

