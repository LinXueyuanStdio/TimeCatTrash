# 数据管理器

## 任务

隔离，使具体的 room bmob 等对业务透明
消除数据模型的歧义，统一数据模型，保持一致性

## 分析

数据源分为
1. 内存缓存
2. 本地数据库：ormlite，greendao，room，sql
3. 云端：Bmob
4. 文件夹：github，webdev，本地git
5. 文件：图片缓存、数据结构缓存、 preference

ormlite 和 android 自带的 sql 应该慢慢弃用了，全部转greendao
看room的表现，表现好就转room

webdev没做好

本地git 和 github 的同步可以手动上传和下载，所以我们只关注本地 git 项目文件夹

1 和 5 不用永久保证一致性，可以合并

所以以上数据源简化为

1. 内存缓存、图片缓存、数据结构缓存、 preference
2. 本地数据库：greendao
3. 云端：Bmob
4. 文件夹及文件：本地git

## 目标

### 快

缓存 <-> 本地数据库 <-同步-> 云端和本地git

### 场景

#### git ： 手动备份

本地git和本地数据库间的同步是手动同步

##### 本地git生命周期

新手首次创建git项目
手动同步：本地数据库 -> git项目
1. 读取本地数据库
2. 如果本地数据库有 block，转3，否则结束
3. 写入为git文件夹结构
手动同步：git项目 -> 本地数据库
1. git项目文件夹里无文件则结束，否则转2
2. 读取文件夹结构，构建block，一致性检验，写入本地数据库


老手克隆git项目
手动同步：本地数据库 -> git项目
1. 读取所有block，直接覆盖git对应的文件夹结构
手动同步：git项目 -> 本地数据库
1. git项目文件夹里无文件则结束，否则转2
2. 读取文件夹结构，构建block，按文件内容进行一致性检验，写入本地数据库

##### git文件夹结构

基于 blockObjectId 保证一致性
blockObjectId = userObjectId + Id

userObjectId
+--- README.md
+--- 自定义字符串 和main的结构完全一样 数据直接从main复制 是main的子集 冗余备份 用户不需要可直接自己删除
|    +--- blockObjectId
|    |    +--- README
|    |    +--- timecat.json 
|    |    +--- other assets .jpg .mp4 .mp3
|    \--- blockObjectId
+--- main 当前活动的目录
|    +--- blockObjectId
|    |    +--- README
|    |    +--- timecat.json 
|    |    +--- other assets .jpg .mp4 .mp3
|    +--- blockObjectId
|    +--- blockObjectId
|    \--- relation.json
\--- .gitignore

#### 直接云端

用户信息、用户权限等敏感、实时的场景

#### 缓存 <-> 本地数据库

要求：加载快 在查看视图显示

#### 本地数据库 <-同步-> 云端

##### 读

1. 读数据库

2. 读云端

##### 写

1. 直接写云端

2. 先写本地数据库，再写云端

3. 同时写数据库和云端

##### 策略

直接读写数据库，不管云端。设置脏字段和一致性字段，在后台自动同步到云端。

## 结论

DataManager 应该完成的功能
1. 直接读写云端（敏感、实时信息）
2. 直接读写数据库（价值数据）
3. 对数据库进行内存缓存和读取
4. 本地数据库 <-同步-> 云端
5. 本地数据库 <-同步-> git


