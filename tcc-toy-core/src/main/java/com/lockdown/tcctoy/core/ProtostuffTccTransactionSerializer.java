package com.lockdown.tcctoy.core;

import com.lockdown.tcctoy.api.TccTransaction;
import com.lockdown.tcctoy.api.support.TccTransactionSerializer;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

public class ProtostuffTccTransactionSerializer implements TccTransactionSerializer {

	@Override
	public TccTransaction parseBytes(byte[] content) {
		Schema<TccLocalTransaction> schema = RuntimeSchema.getSchema(TccLocalTransaction.class);
		TccLocalTransaction instance = new TccLocalTransaction();
		ProtostuffIOUtil.mergeFrom(content, instance, schema);
		return instance;
	}

	@Override
	public byte[] serializeTransaction(TccTransaction transaction) {
		Schema<TccLocalTransaction> schema = RuntimeSchema.getSchema(TccLocalTransaction.class);
		LinkedBuffer buffer = LinkedBuffer.allocate(1024 * 1024);
		byte[] result = null;
		try {
			result = ProtostuffIOUtil.toByteArray((TccLocalTransaction)transaction, schema, buffer);
		}finally {
			buffer.clear();
		}
		return result;
	}

}
