# 项目概述

本项目包含多个控制器，用于处理分布式锁、规则引擎和线程本地化的相关操作。以下是各个控制器的详细说明和方法描述。

## 目录

- [LockController](#lockcontroller)
- [RuleController](#rulecontroller)
- [ThreadLocalController](#threadlocalcontroller)

## LockController

`LockController` 处理与分布式锁相关的操作，如秒杀库存锁定。

### 方法

- **flashSale**
  - **描述**: 利用分布式锁控制库存。在获得锁后减少库存，并更新数据库。
  - **路径**: `/lock/flashSale`
  - **返回值**: 根据是否成功获取锁及库存情况返回 "下单成功"、"库存不足" 或 "未获取锁"。

- **flashSaleV2**
  - **描述**: `flashSale` 方法的简化版，使用 RedissonClient 进行分布式锁操作。
  - **路径**: `/lock/flashSaleV2`
  - **返回值**: 根据是否成功获取锁及库存情况返回 "下单成功"、"库存不足" 或 "未获取锁"。

## RuleController

`RuleController` 使用 EasyRules 规则引擎处理规则相关的操作。

### 方法

- **simpleRule**
  - **描述**: 创建并执行一个简单的规则。根据条件触发规则执行。
  - **路径**: `/rule/simpleRule`
  - **返回值**: 返回布尔值，表示规则是否满足条件。

## ThreadLocalController

`ThreadLocalController` 处理线程本地化相关的操作，使用线程池进行异步处理。

### 方法

- **checkThreadLocal**
  - **描述**: 使用 ThreadLocal 测试方法。确保使用完 ThreadLocal 后回收，以避免数据紊乱。
  - **路径**: `/thread/checkThreadLocal`
  - **返回值**: 返回布尔值，表示线程本地化测试是否成功。

- **decode**
  - **描述**: 使用固定线程池进行异步任务处理，以避免 QPS 过大。
  - **路径**: `/thread/fixedThread`
  - **返回值**: 返回字符串 "ok"，表示任务已提交。

