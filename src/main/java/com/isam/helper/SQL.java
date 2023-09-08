package com.isam.helper;

import java.util.*;
import java.sql.*;

import javax.sql.*;
import javax.naming.*;

public class SQL{
	private Connection conn = null;
	private Statement stmt = null;
	private String JNDIName = "";
	private PreparedStatement pstmt = null;
	private ResultSet rs=null;
	private CallableStatement cs=null;
	public SQL(){
		JNDIName = "jdbc/sqlISAM";
	}

	public SQL(String JNDIName){
		this.JNDIName = JNDIName;
	}

	private Connection getConnection() throws Exception{

		Context initContext = new InitialContext();
		Context envContext = (Context) initContext.lookup("java:comp/env");

		DataSource ds = (DataSource) envContext.lookup(JNDIName);

		if(ds == null){
			throw new Exception("Cannot get DataSource!");
		}else{
			return ds.getConnection();
		}
	}

	public void noCommit() throws SQLException{
		try{
			if(conn == null){
				conn = getConnection();
			}

			if(conn.isClosed()){
				conn = getConnection();
			}

			conn.setAutoCommit(false);
		}catch(Exception e){
			e.printStackTrace();
			close();
			throw new SQLException(e.toString());
		}
	}

	public void commit() throws SQLException{
		try{
			if(conn == null){
				conn = getConnection();
			}

			conn.commit();
			conn.setAutoCommit(true);
		}catch(Exception e){
			e.printStackTrace();
			close();
			throw new SQLException(e.toString());
		}
	}

	public void rollback() throws SQLException{
		try{
			if(conn == null){
				conn = getConnection();
			}

			conn.rollback();
			conn.setAutoCommit(true);
		}catch(Exception e){
			e.printStackTrace();
			close();
			throw new SQLException(e.toString());
		}
	}

	public boolean execute(String sqlcmd) throws SQLException{
		boolean result = false;

		try{
			if(conn == null){
				conn = getConnection();
				//conn.setAutoCommit(true);
			}

			if(conn.isClosed()){
				conn = getConnection();
			}

			stmt = conn.createStatement();
			result = stmt.execute(sqlcmd);
		}catch(Exception e){
			System.out.println("Error! SQL command:" + sqlcmd);
			e.printStackTrace();
			close();
			throw new SQLException(e.toString());
		}

		return result;
	}

	/**
	 *
	 * @param sqlcmd : a SQL SELECT command
	 * @return ResultSet : the query result
	 * @throws SQLException
	 */
	public ResultSet query(String sqlcmd) throws SQLException{
		ResultSet rs = null;

		try{
			if(conn == null || conn.isClosed()){
				conn = getConnection();
			}

			stmt = conn.createStatement();
			rs = stmt.executeQuery(sqlcmd);
		}catch(Exception e){
			System.out.println("Error! SQL command:" + sqlcmd);
			e.printStackTrace();
			close();
			throw new SQLException(e.toString());
		}

		return rs;
	}

	/**
	 *
	 * @param sqlcmd : a SQL INSERT, UPDATE, or DELETE command
	 * @throws SQLException
	 */
	public int write(String sqlcmd) throws SQLException{
		int rows = -1;

		try{
			if(conn == null){
				conn = getConnection();
			}

			if(conn.isClosed()){
				conn = getConnection();
			}

			stmt = conn.createStatement();
			rows = stmt.executeUpdate(sqlcmd);
		}catch(Exception e){
			System.out.println("Error! SQL command:" + sqlcmd);
			e.printStackTrace();
			close();
			throw new SQLException(e.toString());
		}

		return rows;
	}

	/**
	 *
	 * @param sqlcmd : a SQL INSERT, UPDATE, or DELETE command
	 * @throws SQLException
	 */
	public PreparedStatement prepare(String sqlcmd) throws SQLException{
		try{
			if(conn == null){
				conn = getConnection();
			}

			if(conn.isClosed()){
				conn = getConnection();
			}

			pstmt = conn.prepareStatement(sqlcmd);
			return pstmt;
		}catch(Exception e){
			System.out.println("Error! SQL command:" + sqlcmd);
			e.printStackTrace();
			close();
			throw new SQLException(e.toString());
		}
	}
	
	/**
	 *
	 * @param sqlcmd : a SQL INSERT, columnNames : column names of generated keys
	 * @return A PreparedStatement that returns generated keys
	 * @throws SQLException
	 */
	public PreparedStatement prepare(String sqlcmd, String columnNames[]) throws SQLException{
		try{
			if(conn == null){
				conn = getConnection();
			}

			if(conn.isClosed()){
				conn = getConnection();
			}

			pstmt = conn.prepareStatement(sqlcmd, columnNames);
			return pstmt;
		}catch(Exception e){
			System.out.println("Error! SQL command:" + sqlcmd);
			e.printStackTrace();
			close();
			throw new SQLException(e.toString());
		}
	}

	/**
	 *
	 * close the current connection
	 */
	private void closeConnection() throws SQLException{
		if(conn != null){
			try{
				if(!conn.isClosed()){
					conn.close();
				}
			}catch(SQLException e){
				e.printStackTrace();
			}
				conn = null;
		}
	}

	/**
	 *
	 * release all database resources
	 */
	public void close() throws SQLException{
		if(rs != null){
			try{
				rs.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
			rs = null;
		}
		if(stmt != null){
			try{
				//if(!stmt.isClosed()){
					stmt.close();
				//}
			}catch(SQLException e){
				;//e.printStackTrace();
			}
				stmt = null;
		}
		if(pstmt != null){
			try{
				//if(!pstmt.isClosed()){
					pstmt.close();
				//}
			}catch(SQLException e){
				;//e.printStackTrace();
			}
				pstmt = null;
		}
		if(cs != null){
			try{
				//if(!pstmt.isClosed()){
				cs.close();
				//}
			}catch(SQLException e){
				;//e.printStackTrace();
			}
			cs = null;
		}

		closeConnection();
	}

	protected void finalize() throws Throwable{
			close();
	}

	public static List<String> getRSColumns(ResultSet rs){
		List<String> result = new ArrayList<String>();

		try{
			ResultSetMetaData rsmd = rs.getMetaData();
			for(int i = 1;i <= rsmd.getColumnCount();i++){
				result.add(rsmd.getColumnName(i));
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		return result;
	}

	public static HashMap<String,String> RSToHashMap(List<String> columns, ResultSet rs){
		HashMap<String,String> result = new HashMap<String,String>();
		try{
			for(int i = 0;i < columns.size();i++){
				String column = (String) columns.get(i);
				result.put(column, DataUtil.trim(rs.getString(column)));
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		return result;
	}

	/**
	 *
	 * @param sqlcmd : a SQL INSERT, UPDATE, or DELETE command
	 * @throws SQLException
	 */
	public CallableStatement prepareCall(String sqlcmd) throws SQLException{
		try{
			if(conn == null){
				conn = getConnection();
			}

			if(conn.isClosed()){
				conn = getConnection();
			}

			cs = conn.prepareCall(sqlcmd);
			return cs;
		}catch(Exception e){
			System.out.println("Error! SQL command:" + sqlcmd);
			e.printStackTrace();
			close();
			throw new SQLException(e.toString());
		}
	}
}