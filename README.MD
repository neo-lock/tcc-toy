# tcc-toy
tcc-transaction sprint boot starter 

## 简介
* 非侵入式开发
* 与feign集成
* 可以方便的集成自定义starter
* 集群支持


## tcc-toy-api
	接口定义
## tcc-toy-core
	代码实现
## tcc-toy-support
    任务回复和事务持久化的扩展
	
## 使用
* @ATccTransaction  表示本地事务
* @ATccRemoteParticipant 表示远程调用，添加在feign接口上

##### 主事务调用
~~~ java    
@ATccTransaction(confirmMethodName="confirmOrder",cancelMethodName="cancelOrder")
@Transactional
@Override
public void perparedPay(String orderNo, boolean paySuccess) throws Exception{}
~~~
##### feign接口调用
~~~ java
@ATccRemoteParticipant
@RequestMapping(value= {"/order/score"},method= {RequestMethod.PUT})
public void preparedAddScore(@RequestParam("buyerId") String buyerId, @RequestParam("totalPrice") int totalPrice, @RequestParam("orderNo") String orderNo);
~~~
##### 消费端Controller
~~~ java
@RequestMapping(value="/order/score",method=RequestMethod.PUT)
public void preparedAddScore(@RequestParam String buyerId,@RequestParam int totalPrice,@RequestParam String orderNo) throws Exception{}
~~~
##### 消费端服务
~~~ java
@ATccTransaction(cancelMethodName="cancelAddScore",confirmMethodName="confirmAddScore")
@Transactional
@Override
public void addScore(String accountId, int score, String orderNo, ScoreSource orderPayment) throws Exception{}
~~~

### `请注意参与事务的接口需要自己实现幂等`

## 注意
* 消费端在 confirm 和 cancel 有乐观锁的话，tcc-toy 会进行多次尝试，在try阶段则不会尝试
* 如果自定了全局异常捕获，当消费服务产生错误的时候，tcc-toy不能捕获错误，需要自己定义feign的错误解析器ErrorDecoder并且抛出异常
* TccTransactionRepository不能参与事务
* 只需要在进行事务恢复的服务中添加quartz表结构和配置
* 如果需要自定义任务，可以扩展 com.lockdown.tcctoy.support.SchedulerFactoryBeanListener

## 演示

### tcc-toy-support 中 事务控制 mysql 表结构
    CREATE TABLE `${your table name}` (
      `tid` varchar(45) NOT NULL,
      `branchQualifier` varchar(45) NOT NULL DEFAULT '',
      `transactionType` varchar(25) NOT NULL,
      `transactionStatus` varchar(25) NOT NULL,
      `version` int(11) NOT NULL DEFAULT '1',
      `invokeCount` int(11) NOT NULL DEFAULT '0',
      `createTime` timestamp NULL DEFAULT NULL,
      `updateTime` timestamp NULL DEFAULT NULL,
      `content` blob NOT NULL,
      `domain` varchar(45) NOT NULL,
      PRIMARY KEY (`tid`,`branchQualifier`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8
### demo-tcc-order 业务订单 mysql 表结构
    CREATE TABLE `t_order` (
      `id` int(11) NOT NULL AUTO_INCREMENT,
      `orderNo` varchar(45) NOT NULL,
      `buyerId` varchar(45) NOT NULL,
      `productId` int(11) NOT NULL,
      `amount` int(11) NOT NULL DEFAULT '0',
      `totalPrice` int(11) NOT NULL DEFAULT '0',
      `prepareStatus` varchar(45) DEFAULT NULL,
      `status` varchar(45) NOT NULL,
      `createTime` timestamp NULL DEFAULT NULL,
      `updateTime` varchar(45) DEFAULT NULL,
      PRIMARY KEY (`id`),
      UNIQUE KEY `orderNo_UNIQUE` (`orderNo`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8
    
    ============= 如果需要在这里控制事务恢复，需要添加Quartz的表=============

### demo-tcc-score 积分 mysql 表结构
    CREATE TABLE `t_score` (
      `accountId` varchar(45) NOT NULL,
      `score` int(11) NOT NULL DEFAULT '0',
      `updateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
      `version` int(11) NOT NULL DEFAULT '0',
      PRIMARY KEY (`accountId`),
      UNIQUE KEY `accountId_UNIQUE` (`accountId`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8
    
    CREATE TABLE `t_score_log` (
      `id` int(11) NOT NULL AUTO_INCREMENT,
      `accountId` varchar(45) NOT NULL,
      `score` int(11) NOT NULL,
      `orderNo` varchar(45) NOT NULL,
      `scoreSource` varchar(45) NOT NULL,
      `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
      `valid` tinyint(1) NOT NULL DEFAULT '0',
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB CHARSET=utf8
### demo-tcc-products 商品 mysql 表结构
    CREATE TABLE `t_products` (
      `id` int(11) NOT NULL AUTO_INCREMENT,
      `amount` int(11) NOT NULL DEFAULT '0',
      `version` int(11) NOT NULL DEFAULT '0',
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB  DEFAULT CHARSET=utf8
    
    CREATE TABLE `t_products_delivery_log` (
      `pid` int(11) NOT NULL,
      `orderNo` varchar(45) NOT NULL,
      `amount` int(11) NOT NULL,
      `buyerId` varchar(45) NOT NULL,
      `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
      `valid` tinyint(1) NOT NULL DEFAULT '0',
      PRIMARY KEY (`pid`,`orderNo`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8

`有疑问可以通过邮件 wycoolcool@126.com 联系我`