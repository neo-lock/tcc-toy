# tcc-toy
tcc-transaction sprint boot starter 

## 简介
* 非侵入式开发
* 与feign集成
* 简单

## tcc-toy-api
	接口定义
## tcc-toy-core
	代码实现
## tcc-toy-support
	任务回复和事务持久化的扩展
## 使用
* @ATccTransaction  表示本地事务
* @ATccRemoteParticipant 表示远程调用，添加在feign接口上

#### @ATccTransaction
··· java
    
    @ATccTransaction(confirmMethodName="confirmOrder",cancelMethodName="cancelOrder")
    @Transactional
    @Override
    public void perparedPay(String orderNo, boolean paySuccess) throws Exception{}
   
···
	
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