package org.lqz.module.dao.Impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.lqz.framework.util.JdbcUtil;
import org.lqz.module.services.Impl.AllTableServiceImpl;

/**
 * 说明：基础Dao数据库操作实现
 * 
 * @author Administrator
 * 
 */
public class BaseDaoImpl{

	public static JdbcUtil jdbcUtil;
	public static Connection conn;
	// 构造方法获取数据库连接
	public BaseDaoImpl() {
		jdbcUtil = new JdbcUtil();
		conn = jdbcUtil.getJdbcUtil().getConnection();
	}

	// 重写查询数据方法
	public static List select(String sql, int columnNum, Object[] paraArray)
			throws SQLException {
		List list = new ArrayList();
		PreparedStatement prs = conn.prepareStatement(sql);
		if (paraArray != null) {
			for (int i = 0, length = paraArray.length; i < length; i++) {
				prs.setObject(i + 1, paraArray[i]);
			}
		}
		ResultSet rs = prs.executeQuery();
		while (rs.next()) {
			Object[] array = new Object[columnNum];
			for (int i = 0; i < columnNum; i++) {
				array[i] = rs.getObject(i + 1);
			}
			list.add(array);
		}
		//jdbcUtil.getJdbcUtil().release(rs, pst, conn);
		return list;
	}
	// 返回一行数据，一个数组
	public static Object[] selectArray(String sql, int columnNum, Object[] paraArray)
			throws SQLException {
		PreparedStatement prs = conn.prepareStatement(sql);
		if (paraArray != null) {
			for (int i = 0, length = paraArray.length; i < length; i++) {
				prs.setObject(i + 1, paraArray[i]);
			}
		}
		ResultSet rs = prs.executeQuery();
		Object[] array=null;
		//while (rs.next())
		{
			array = new Object[columnNum];
			for (int i = 0; i < columnNum; i++) {
				array[i] = rs.getObject(i + 1);
			}
		}
		//jdbcUtil.getJdbcUtil().release(rs, pst, conn);
		return array;
	}

	// 返回一行数据，一个数组
	public static int selectMaxID(String sql)
			throws SQLException {
		PreparedStatement prs = conn.prepareStatement(sql);
		ResultSet rs = prs.executeQuery();
		int maxID= (int) rs.getObject(1);;
		//jdbcUtil.getJdbcUtil().release(rs, pst, conn);
		return maxID;
	}

	// 重写插入数据方法
	public static int insert(String sql, Object[] paraArray) throws SQLException {
		PreparedStatement prs = conn.prepareStatement(sql);
		for (int i = 0, length = paraArray.length; i < length; i++) {
			prs.setObject(i + 1, paraArray[i]);
		}
		// 指示受影响的行数（即更新计数）
		int i = prs.executeUpdate();
		//jdbcUtil.getJdbcUtil().release(rs, pst, conn);
		return i;
	}
	public static boolean validateTableNameExist(String tableName) {
		ResultSet rs = null;
		try {
			rs = conn.getMetaData().getTables(null, null, tableName, null);
			//jdbcUtil.getJdbcUtil().release(rs, pst, conn);//新添关闭数据库
			if (rs.next()) {
				return true;
			}else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}
    public static boolean validateTableListExist(ArrayList<String>  list) throws SQLException {
        for (int i=0;i<list.size();i++) {
            if (!validateTableNameExist(list.get(i)))
            return false;
        }
        return true;
    }

	// 重写更新数据方法
	public static int update(String sql, Object[] paraArray) throws SQLException {
		PreparedStatement prs = conn.prepareStatement(sql);
		for (int i = 0, length = paraArray.length; i < length; i++) {
			prs.setObject(i + 1, paraArray[i]);
		}
		int i = prs.executeUpdate();
		//jdbcUtil.getJdbcUtil().release(rs, pst, conn);
		return i;
	}

	// 重写删除数据方法
	public static int delete(String sql, Object[] paraArray) throws SQLException {
		PreparedStatement prs = conn.prepareStatement(sql);
		for (int i = 0, length = paraArray.length; i < length; i++) {
			prs.setObject(i + 1, paraArray[i]);
		}
		int i = prs.executeUpdate();
		//jdbcUtil.getJdbcUtil().release(rs, pst, conn);
		return i;
	}

	// 重写删除数据方法
	public static int deleteTable(String tableName) throws SQLException {
	    conn.close();
		new BaseDaoImpl();
		Statement stm = conn.createStatement();
		stm.executeUpdate("drop table if exists '"+tableName+"'");
		//jdbcUtil.getJdbcUtil().release(rs, stm, conn);
		return 1;
	}
}
