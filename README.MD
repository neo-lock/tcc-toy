# tcc-toy
tcc-transaction sprint boot starter 

`可以通过邮件 wycoolcool@126.com 联系我`

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

## 演示

### tcc-toy-support 中mysql 表结构
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