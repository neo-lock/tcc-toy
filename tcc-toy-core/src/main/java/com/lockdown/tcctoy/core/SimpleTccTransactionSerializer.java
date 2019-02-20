package com.lockdown.tcctoy.core;

import com.alibaba.fastjson.JSON;
import com.lockdown.tcctoy.api.TccTransaction;
import com.lockdown.tcctoy.api.support.TccTransactionSerializer;

public class SimpleTccTransactionSerializer implements TccTransactionSerializer{
	
	@Override
	public TccTransaction parseBytes (byte[] content) {
		return JSON.parseObject(content,TccLocalTransaction.class);
	}
	
	@Override
	public byte[] serializeTransaction(TccTransaction transaction) {
		return JSON.toJSONBytes(transaction);
	}
}
