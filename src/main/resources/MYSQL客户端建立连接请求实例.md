## MYSQL客户端与服务端建立连接解析

### 一、 配置

Server端配置

- 3节点MariaDB+Galera集群
- Ubuntu18.04
- MariaDB10.1.47

Client端配置

- Win10 64位
- Navicat for mysql 11.1.13

### 二、 流程解析

1. TCP连接三次握手，只列举主要数据

   第一次 客户端 => 服务器

   ​	Sequence Number: 2358626209

   ​	[Next Sequence Number: 2358626210] 

   ​	Acknowledgment Number: 0														

   第二次 服务器 => 客户端

   ​	Sequence Number: 49776865

   ​	[Next Sequence Number: 49776866]

   ​	Acknowledgment Number: 2358626210

   第三次 客户端 => 服务器

   ​	Acknowledgment Number: 2358626210

   ​	[Next Sequence Number: 2358626210]

   ​	Acknowledgment Number: 49776866

2. TCP连接建立成功，MYSQL客户端与服务器进行通信

   第一次 服务器 => 客户端

   ​	Sequence Number: 49776866

   ​	[Next Sequence Number: 49776976]

   ​	Acknowledgment Number: 2358626210

   ​	MYSQL协议

   ​		Server Greeting服务器向客户端打招呼，传递服务器的基本信息，数据库版本，数据库级别的编码格式，此处是4字节的utf-8，编码排序模式是不区分大小写的utf8mb4_general_ci（case insensitive）。

   ​			Protocol: 10

   ​			Version: 5.5.5-10.1.47-MariaDB-0ubuntu0.18.04.1

   ​			Server Language: utf8mb4 COLLATE utf8mb4_general_ci (45)

   ​			Authentication Plugin: mysql_native_password

   第二次 客户端 => 服务器

   ​	Sequence Number: 2358626210

   ​	[Next Sequence Number: 2358626389]

   ​	Acknowledgment Number: 49776976

   ​	MYSQL协议

   ​		Login Request登录请求，传递客户端基本信息，例如操作系统以及位数，客户端版本，用户名，密码，以及客户端编码格式以及编码排序模式。

   ​			Charset: utf8 COLLATE utf8_general_ci (33)

   ​			Unused: 0000000000000000000000000000000000000000000000

   ​			Username: eddie

   ​			Password: 339e4c1c730caad7abf5e1758754da9bf77e8e3e

   ​			Client Auth Plugin: mysql_native_password

   ​			Connection Attributes

   ​				Connection Attributes length: 93

   ​				Connection Attribute - _os: Win32

   ​				Connection Attribute - _client_name: libmysql

   ​				Connection Attribute - _pid: 3904

   ​				Connection Attribute - _platform: x86

   ​				Connection Attribute - _client_version: 10.0.7

   ​	第三次 服务器 => 客户端

   ​		Sequence Number: 49776976

   ​		[Next Sequence Number: 49776987]

   ​		Acknowledgment Number: 2358626389

   ​		MYSQL协议 推测是登录成功返回的结果

   ​			Response Code: OK Packet (0x00)

   ​			Affected Rows: 0

   ​			Server Status: 0x0002

   ​			Warnings: 0

### 三、 SQL语句执行

​	主要分为两步通信

 1. 请求（Request Command Query）主要携带SQL语句

    以下仅展示MYSQL协议中部分关键字段

    MySQL Protocol

    ​	Packet Length: 38

    ​	Packet Number: 0

    ​	Request Command Query

    ​		Command: Query (3)

    ​		Statement: SELECT * FROM `student` LIMIT 0, 1000

 2. 响应结果（OK Packet），返回SQL语句执行结果

    MySQL Protocol

    ​	Catalog: def

    ​	Database: school

    ​	Table: student

    ​	Original table: student

    ​	Name: id

    ​	Original name: id

    ​	Charset number: binary COLLATE binary (63)

    MySQL Protocol

    ​	Catalog: def

    ​	Database: school

    ​	Table: student

    ​	Original table: student

    ​	Name: name

    ​	Original name: name

    ​	Charset number: utf8 COLLATE utf8_general_ci (33)

    MySQL Protocol

    ​	Catalog: def

    ​	Database: school

    ​	Table: student

    ​	Original table: student

    ​	Name: age

    ​	Original name: age

    ​	Charset number: binary COLLATE binary (63)

    ​	