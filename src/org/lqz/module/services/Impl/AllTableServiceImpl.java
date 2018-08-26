package org.lqz.module.services.Impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.lqz.module.dao.Impl.BaseDaoImpl;
import org.lqz.module.view.AgileEMCJPanel;
import static org.lqz.module.dao.Impl.BaseDaoImpl.*;

public class AllTableServiceImpl {

    //条件查询商品
    public Vector<Vector> AgileBomSelectByCondition() throws Exception {
        Vector<Vector> rows = new Vector<Vector>();
        //BaseDaoImpl dao = new BaseDaoImpl();
        StringBuilder sqlBuilder = new StringBuilder(
                "select id, Level,Number, Description from Agile_BOM ");
        String sql = sqlBuilder.toString();
        //System.out.println("AgileBomSelectByCondition-sql-----"+sql);
        List<Object[]> list = select(sql, 4, null);
        if (!list.isEmpty()) {
            for (Object[] object : list) {
                Vector temp = new Vector<String>();
                for (int i = 0; i < object.length; i++) {
                    temp.add(object[i]);
                }
                rows.add(temp);
            }
        }
        return rows;
    }
    //条件查询商品
    public Vector<Vector> AgileBomSelectList() throws Exception {
        Vector<Vector> rows = new Vector<Vector>();
        StringBuilder sqlBuilder = new StringBuilder(
                "select id, Level,Number, Description from Agile_BOM ");
        String sql = sqlBuilder.toString();
        //System.out.println("AgileBomSelectByCondition-sql-----"+sql);
        List<Object[]> list = select(sql, 4, null);
        Vector columnName= new Vector<String>();
        columnName.add("id");columnName.add("Level");
        columnName.add("Number");columnName.add("Description");
        rows.add(columnName);
        if (!list.isEmpty()) {
            for (Object[] object : list) {
                Vector temp = new Vector<String>();
                for (int i = 0; i < object.length; i++) {
                    temp.add(object[i]);
                }
                rows.add(temp);
            }
        }
        return rows;
    }

    //条件查询商品
    public Vector<Vector> EMCSelectList() throws Exception {
        Vector<Vector> rows = new Vector<Vector>();
        StringBuilder sqlBuilder = new StringBuilder(
                "select id, Level, Data_material," +
                        "Material_Number_Desc, New_Version from EMC_MAKE_BUY ");
        String sql = sqlBuilder.toString();
        //System.out.println("AgileBomSelectByCondition-sql-----"+sql);
        List<Object[]> list = select(sql, 5, null);
        Vector columnName= new Vector<String>();
        columnName.add("id");columnName.add("Level");
        columnName.add("Data material");columnName.add("Material Number_Desc");
        columnName.add("New Version");
        rows.add(columnName);
        if (!list.isEmpty()) {
            for (Object[] object : list) {
                Vector temp = new Vector<String>();
                for (int i = 0; i < object.length; i++) {
                    temp.add(object[i]);
                }
                rows.add(temp);
            }
        }
        return rows;
    }

    public Vector<Vector> EMCSelectByCondition() throws Exception {
        Vector<Vector> rows = new Vector<Vector>();
        StringBuilder sqlBuilder = new StringBuilder(
                "select id, Level, Data_material," +
                        "Material_Number_Desc, New_Version from EMC_MAKE_BUY ");
        String sql = sqlBuilder.toString();
        List<Object[]> list = select(sql, 5, null);
        if (!list.isEmpty()) {
            for (Object[] object : list) {
                Vector temp = new Vector<String>();
                for (int i = 0; i < object.length; i++) {
                    temp.add(object[i]);
                }
                rows.add(temp);
            }
        }
        return rows;
    }
    public Vector<Vector> AgileEMCSelectByCondition() throws Exception {
        Vector<Vector> rows = new Vector<Vector>();
        StringBuilder sqlBuilder = new StringBuilder(
                "select  * from Agile_EMC;");
        String sql = sqlBuilder.toString();
        //System.out.println("AgileEMCSelectByCondition-sql-----" + sql);
        List<Object[]> list = select(sql, 6, null);
        if (!list.isEmpty()) {
            for (Object[] object : list) {
                Vector temp = new Vector<String>();
                for (int i = 0; i < object.length; i++) {
                    temp.add(object[i]);
                }
                rows.add(temp);
            }
        }
        return rows;
    }
    //条件查询商品
    public Vector<Vector> AgileEMCSelectList() throws Exception {
        Vector<Vector> rows = new Vector<Vector>();
        StringBuilder sqlBuilder = new StringBuilder(
                "select  * from Agile_EMC;");
        String sql = sqlBuilder.toString();
        List<Object[]> list = select(sql, 6, null);

        Vector columnName= new Vector<String>();
        columnName.add("id");columnName.add("Level");
        columnName.add("Number");columnName.add("Agile_Description");
        columnName.add("EMC_Description");columnName.add("New_Version");
        rows.add(columnName);
        if (!list.isEmpty()) {
            for (Object[] object : list) {
                Vector temp = new Vector<String>();
                for (int i = 0; i < object.length; i++) {
                    temp.add(object[i]);
                }
                rows.add(temp);
            }
        }
        return rows;
    }
    //条件查询商品
    public Vector<Vector> QueryConfigSelectList() throws Exception {
        Vector<Vector> rows = new Vector<Vector>();
        StringBuilder sqlBuilder = new StringBuilder(
                "select  * from Query_Config;");
        String sql = sqlBuilder.toString();
        List<Object[]> list = select(sql, 3, null);
        if (!list.isEmpty()) {
            for (Object[] object : list) {
                Vector temp = new Vector<String>();
                for (int i = 0; i < object.length; i++) {
                    temp.add(object[i]);
                }
                rows.add(temp);
            }
        }
        return rows;
    }



    //条件查询商品
    public ArrayList<String> TableNameSelectList() throws Exception {
        ArrayList<String> nameAList = new ArrayList<String>();
        StringBuilder sqlBuilder = new StringBuilder(
                "select name from TableName;");
        String sql = sqlBuilder.toString();
        List<Object[]> list = select(sql, 1, null);
        if (!list.isEmpty()) {
            for (Object[] object : list) {
                nameAList.add(object[0].toString()) ;
            }
        }
        return nameAList;
    }

    public static void AgileEMCTable() {
        //System.out.println("Creat  AgileEMCTable-------------------------");
        //应当先判断表是否存在，再创建
        try {
            BaseDaoImpl.conn.close();
            new BaseDaoImpl();

            Statement stm = BaseDaoImpl.conn.createStatement();
            stm.executeUpdate("drop table if exists Agile_EMC");//判断是否有表tables的存在。有则删除
            stm.executeUpdate("create table Agile_EMC as " +
                    "select  DISTINCT  Agile_BOM.id as id, Agile_BOM.Level as Level, Number, Description, EMC_MAKE_BUY.Material_Number_Desc as Material_Number_Desc, New_Version \n" +
                    "                    from Agile_BOM left join EMC_MAKE_BUY on Agile_BOM.Number=EMC_MAKE_BUY.Data_material;"); //+  //9,10,11,12
            BaseDaoImpl.conn.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //jdbcUtil.getJdbcUtil().release(null, stm, conn);
        AgileEMCJPanel.hasAgileEMCTable = true;
    }


    //逻辑删除Agile_BOM元组
    public int AgileBomdeleteById(Object[] paraArray) throws Exception {
        int result = 0;
        result = update("delete FROM Agile_BOM  where id=?", paraArray);
        return result;
    }


    //通过id修改AgileBom元组
    public int AgileBomupdateById(Object[] paraArray) throws Exception {
        int result = 0;
        result = update("update Agile_BOM set  Level=?,Number=?,Description=? where id=?",
                paraArray);
        return result;
    }

    //通过id修改AgileBom元组
    //  "id","Level","Data_material" ,"Material_Number_Desc","New_Version" };
    public int EMCupdateById(Object[] paraArray) throws Exception {
        int result = 0;
        result = update("update EMC_MAKE_BUY set " +
                        "Level=?, Data_material=?, Material_Number_Desc=?, New_Version=? where id=?",
                paraArray);
        return result;
    }
    public int EMCaddById(Object[] paraArray) throws Exception {
        int result = 0;
        result = insert(
                "insert into EMC_MAKE_BUY(" +
                        "Level,Data_material, Material_Number_Desc, New_Version) values(?,?,?,?)",
                paraArray);
        return result;
    }

    //逻辑删除EMC元组
    public int EMCdeleteById(Object[] paraArray) throws Exception {
        int result = 0;
        result = update("delete FROM EMC_MAKE_BUY where id=?", paraArray);
        return result;
    }

    //逻辑删除EMC元组
    public int deleteTableByName(String name) throws Exception {
        int result = 0;
        result = deleteTable(name);
        return result;
    }

    //插入AgileBom元组
    public int AgileBominsertById(Object[] paraArray) throws Exception {
        //BaseDaoImpl dao = new BaseDaoImpl();
        int result = 0;

//		Object[] params = { id,
//				Level_String, Number_String, Part_Class_String, Description_String,
//				Part_Type_String, PPlatform_Affected_String, Design_Type_String, Rev_String};//12

        result = insert(
                "insert into Agile_BOM(" +
                        "Level,Number,Part_Class, Description," +
                        "Part_Type, PPlatform_Affected,Design_Type,Rev,del_flag)  " +
                        "values(?,?,?,?,?,?,?,?,'0')",
                paraArray);
        return result;
    }



}
