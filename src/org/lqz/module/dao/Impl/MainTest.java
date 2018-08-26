package org.lqz.module.dao.Impl;

import org.lqz.framework.util.JdbcUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MainTest {
     Vector<Integer> levelVector=new Vector<Integer>();
    static JdbcUtil jdbcUtil;
    static Connection conn;
    static PreparedStatement pst;
    ResultSet rs;

//    // 构造方法获取数据库连接
//    public static void BaseDaoImpl() {
//        jdbcUtil = new JdbcUtil();
//        conn = jdbcUtil.getJdbcUtil().getConnection();
//    }


    public static void main(String[] args) throws SQLException {
        jdbcUtil = new JdbcUtil();
        conn = jdbcUtil.getJdbcUtil().getConnection();

        String sql="update Agile_BOM set Description='EMC ISG DEPLOYMENT---100' where id='1'";
        pst = conn.prepareStatement(sql);
        int i = pst.executeUpdate();

        sql="update Agile_BOM set Description='EMC ISG DEPLOYMENT' where id='1'";
        pst = conn.prepareStatement(sql);
        i = pst.executeUpdate();

        sql="update Agile_BOM set Description='EMC ISG DEPLOYMENT-2' where id='1'";
        pst = conn.prepareStatement(sql);
        i = pst.executeUpdate();

        jdbcUtil.getJdbcUtil().release(null, pst, conn);


        jdbcUtil = new JdbcUtil();
        conn = jdbcUtil.getJdbcUtil().getConnection();
        sql="update Agile_BOM set Description='EMC ISG DEPLOYMENT-3' where id='1'";
        pst = conn.prepareStatement(sql);
        i = pst.executeUpdate();

        sql="update Agile_BOM set Description='EMC ISG DEPLOYMENT-4' where id='1'";
        pst = conn.prepareStatement(sql);
        i = pst.executeUpdate();
        //conn.close();
        //jdbcUtil.getJdbcUtil().release(null, null, conn);
    }
}
