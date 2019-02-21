package com.lockdown.tcctoy.support;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import com.lockdown.tcctoy.api.TccTransaction;
import com.lockdown.tcctoy.api.TccTransactionType;
import com.lockdown.tcctoy.api.TransactionId;
import com.lockdown.tcctoy.api.exception.TccIOException;
import com.lockdown.tcctoy.api.support.TccTransactionRepository;
import com.lockdown.tcctoy.api.support.TccTransactionSerializer;

public class JdbcTransactionRepository implements TccTransactionRepository {

	
	private DataSource datasource;
	
	private TccTransactionSerializer serializer;
	
	private String tableName;
	
	private String domain;
	
	public JdbcTransactionRepository(TccTransactionSerializer serializer,DataSource datasource,String tableName) {
		this.datasource = datasource;
		this.serializer = serializer;
		this.tableName = tableName;
	}
	

	
	public void setSerializer(TccTransactionSerializer serializer) {
		this.serializer = serializer;
	}


	public void setDatasource(DataSource datasource) {
		this.datasource = datasource;
	}

	private void closeResultSet(ResultSet rs) {
		if(null!=rs) {
			try {
				rs.close();
			} catch (SQLException e) {
				throw new TccIOException(e);
			}
		}
	}
	
	private void closeStatement(Statement st) {
		if(null!=st) {
			try {
				st.close();
			} catch (SQLException e) {
				throw new TccIOException(e);
			}
		}
	}
	
	private void closeConnection(Connection c) {
		if(null!=c) {
			try {
				c.close();
			} catch (SQLException e) {
				throw new TccIOException(e);
			}
		}
	}
	
	@Override
	public int create(TccTransaction transaction) {
		
		
		Connection connection = null;
		PreparedStatement ps = null;
		
		try {
			connection = datasource.getConnection();
			
			StringBuilder sb = new StringBuilder(" INSERT INTO "+tableName+" (tid,branchQualifier,transactionType,transactionStatus,version,invokeCount,createTime,updateTime,content,domain)"
					+ "	VALUES (?,?,?,?,?,?,?,?,?,?)");
			ps = connection.prepareStatement(sb.toString());
			ps.setString(1, transaction.transactionId().getTid());
			ps.setString(2, transaction.transactionId().getBranchQualifier());
			ps.setString(3, transaction.getTransactionType().name());
			ps.setString(4, transaction.getTransactionStatus().name());
			ps.setInt(5, transaction.getVerion());
			ps.setInt(6, transaction.getInvokeCount());
			ps.setTimestamp(7,new java.sql.Timestamp(transaction.getCreateTime().getTime()));
			ps.setTimestamp(8, new java.sql.Timestamp(transaction.getUpdateTime().getTime()));
			ps.setBytes(9, serializer.serializeTransaction(transaction));
			ps.setString(10, domain);
			return ps.executeUpdate();
			
		}catch(SQLException ex) {
			throw new TccIOException(ex);
		}finally {
			closeStatement(ps);
			closeConnection(connection);
		}
	}


	@Override
	public int update(TccTransaction transaction) {
		
		transaction.updateVersion();
		Connection connection = null;
		PreparedStatement ps = null;
		
		try {
			connection = datasource.getConnection();
			StringBuilder sb = new StringBuilder(" UPDATE "+tableName+" SET transactionStatus = ?,version = ? ,invokeCount =  ? ,updateTime = ?,content = ? WHERE tid = ? AND branchQualifier = ? AND version = ? AND domain = ? ");
			ps = connection.prepareStatement(sb.toString());
			ps.setString(1, transaction.getTransactionStatus().name());
			ps.setInt(2, transaction.getVerion());
			ps.setInt(3, transaction.getInvokeCount());
			ps.setTimestamp(4, new java.sql.Timestamp(transaction.getUpdateTime().getTime()));
			ps.setBytes(5, serializer.serializeTransaction(transaction));
			ps.setString(6, transaction.transactionId().getTid());
			ps.setString(7, transaction.transactionId().getBranchQualifier());
			ps.setInt(8, transaction.getVerion()-1);
			ps.setString(9, domain);
			return ps.executeUpdate();
			
		}catch(SQLException ex) {
			throw new TccIOException(ex);
		}finally {
			closeStatement(ps);
			closeConnection(connection);
		}
	}


	@Override
	public int delete(TccTransaction transaction) {
		Connection connection = null;
		PreparedStatement ps = null;
		
		try {
			connection = datasource.getConnection();
			
			StringBuilder sb = new StringBuilder(" DELETE FROM "+tableName+" WHERE tid = ? AND branchQualifier = ? AND domain = ? ");
			ps = connection.prepareStatement(sb.toString());
			
			ps.setString(1, transaction.transactionId().getTid());
			ps.setString(2, transaction.transactionId().getBranchQualifier());
			ps.setString(3, domain);
			
			return ps.executeUpdate();
			
		}catch(SQLException ex) {
			throw new TccIOException(ex);
		}finally {
			closeStatement(ps);
			closeConnection(connection);
		}
	}


	
	@Override
	public TccTransaction findTransaction(TransactionId id) {
		
		TccTransaction tcc = null;
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			connection = datasource.getConnection();
			
			StringBuilder sb = new StringBuilder(" SELECT content FROM "+tableName+" WHERE tid = ? AND branchQualifier = ? AND domain = ?" );
			ps = connection.prepareStatement(sb.toString());
			
			ps.setString(1, id.getTid());
			ps.setString(2, id.getBranchQualifier());
			ps.setString(3, domain);
	
			rs = ps.executeQuery();
			
			if(rs.next()) {
				byte[] content = rs.getBytes(1);
				tcc = serializer.parseBytes(content);
			}
			
			return tcc;
		}catch(SQLException ex) {
			throw new TccIOException(ex);
		}finally {
			closeResultSet(rs);
			closeStatement(ps);
			closeConnection(connection);
		}
		
	}
	
	@Override
	public List<TccTransaction> loadRetryTransaction(Date date, int limitRetry) {
		return loadRetryTransaction(date,new Date(),limitRetry);
	}
	
	@Override
	public List<TccTransaction> loadRetryTransaction(Date startTime, Date endTime, int limitRetry) {
		List<TccTransaction> result = new ArrayList<TccTransaction>();
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = datasource.getConnection();
			
			StringBuilder sb = new StringBuilder(" SELECT content FROM "+tableName+" WHERE transactionType = ? AND domain = ? AND (updateTime BETWEEN ? AND ?)  AND invokeCount <= ? " );
			ps = connection.prepareStatement(sb.toString());
			ps.setString(1, TccTransactionType.ROOT.name());
			ps.setString(2, domain);
			ps.setTimestamp(3, new java.sql.Timestamp(startTime.getTime()));
			ps.setTimestamp(4, new java.sql.Timestamp(endTime.getTime()));
			ps.setInt(5, limitRetry);
	
			rs = ps.executeQuery();
			
			while(rs.next()) {
				byte[] content = rs.getBytes(1);
				TccTransaction tcc = serializer.parseBytes(content);
				result.add(tcc);
			}
			
			return result;
		}catch(SQLException ex) {
			throw new TccIOException(ex);
		}finally {
			closeResultSet(rs);
			closeStatement(ps);
			closeConnection(connection);
		}
	}
	
	@Override
	public int validateVersion(TccTransaction transaction) {
		transaction.updateVersion();
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = datasource.getConnection();
			
			StringBuilder sb = new StringBuilder(" UPDATE "+tableName+" SET version = ?,updateTime = ?,content = ? WHERE tid = ? AND branchQualifier = ? AND version = ? AND domain = ? ");
			ps = connection.prepareStatement(sb.toString());
			
			ps.setInt(1, transaction.getVerion());
			ps.setTimestamp(2, new java.sql.Timestamp(transaction.getUpdateTime().getTime()));
			ps.setBytes(3, serializer.serializeTransaction(transaction));
			ps.setString(4, transaction.transactionId().getTid());
			ps.setString(5, transaction.transactionId().getBranchQualifier());
			ps.setInt(6, transaction.getVerion()-1);
			ps.setString(7, domain);
			return ps.executeUpdate();
			
		}catch(SQLException ex) {
			throw new TccIOException(ex);
		}finally {
			closeStatement(ps);
			closeConnection(connection);
		}
	}

	@Override
	public int updateTransactionVersion(TccTransaction transaction) {
		transaction.updateVersion();
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = datasource.getConnection();
			
			StringBuilder sb = new StringBuilder(" UPDATE "+tableName+" SET transactionStatus = ?,version = ? ,invokeCount =  ? ,updateTime = ?,content = ? WHERE tid = ? AND branchQualifier = ? AND version = ? AND domain = ? ");
			ps = connection.prepareStatement(sb.toString());
			ps.setString(1, transaction.getTransactionStatus().name());
			ps.setInt(2, transaction.getVerion());
			ps.setInt(3, transaction.getInvokeCount());
			ps.setTimestamp(4, new java.sql.Timestamp(transaction.getUpdateTime().getTime()));
			ps.setBytes(5, serializer.serializeTransaction(transaction));
			ps.setString(6, transaction.transactionId().getTid());
			ps.setString(7, transaction.transactionId().getBranchQualifier());
			ps.setInt(8, transaction.getVerion()-1);
			ps.setString(9, domain);
			return ps.executeUpdate();
			
		}catch(SQLException ex) {
			throw new TccIOException(ex);
		}finally {
			closeStatement(ps);
			closeConnection(connection);
		}
	}
	
	
	@Override
	public int hasBranch(TccTransaction transaction) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int result = 0;
		try {
			connection = datasource.getConnection();
			
			StringBuilder sb = new StringBuilder(" SELECT count(*) FROM "+tableName+" WHERE tid = ? AND branchQualifier != ? ");
			ps = connection.prepareStatement(sb.toString());
			ps.setString(1, transaction.transactionId().getTid());
			ps.setString(2, TccTransactionType.ROOT.name());
			
			rs = ps.executeQuery();
			
			if(rs.next()) {
				result = rs.getInt(1);
			}
			
			return result;
			
		}catch(SQLException ex) {
			throw new TccIOException(ex);
		}finally {
			closeResultSet(rs);
			closeStatement(ps);
			closeConnection(connection);
		}
		
	}

	

	@Override
	public void setDomain(String domain) {
		this.domain = domain;
	}








	






	


	
	

}
