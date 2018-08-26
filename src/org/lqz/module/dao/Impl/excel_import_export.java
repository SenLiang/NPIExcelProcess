package org.lqz.module.dao.Impl;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.lqz.framework.util.JdbcUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.lqz.module.services.Impl.AllTableServiceImpl;
import org.lqz.module.view.AgileEMCJPanel;
import org.lqz.module.view.FileOperation;
import org.lqz.module.view.Progress;
import org.lqz.module.view.Progress.*;

import static org.lqz.module.view.IndexJFrame.creatHome;

public class excel_import_export {
    public static int stRows = 0;
    public static int stColumn = 0;
    public static int exitWriteFlag = 0;
    public final static String DATE_OUTPUT_PATTERNS = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    public final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
            DATE_OUTPUT_PATTERNS);


    public static ArrayList<ArrayList<String>> ReadDataFromExcel(String filePath, String readFileName) throws FileNotFoundException {
        ArrayList<String> innerAList = null;
        ArrayList<ArrayList<String>> outerAlist = new ArrayList<ArrayList<String>>();
        Workbook wb = null;
        try {
            wb = FileOperation.initWorkBook(filePath);
            Sheet sheet = wb.getSheetAt(0);
            stRows = sheet.getPhysicalNumberOfRows();
            //stColumn=sheet.getRow(0).getLastCellNum();
//            System.out.println("stRows-------------"+stRows);
//            System.out.println("stColumn-------------"+stColumn);
            // stColumn=3;
            if (readFileName == "AgileBom") {
                for (int i = 0; i < stRows; i++) {
                    innerAList = new ArrayList<String>();
                    //System.out.print(i+1+"---");
                    Cell Levelcell = sheet.getRow(i).getCell(0);
                    Cell Numbercell = sheet.getRow(i).getCell(1);
                    Cell Descriptioncell = sheet.getRow(i).getCell(3);
                    innerAList.add(getCellValue(Levelcell));
                    innerAList.add(getCellValue(Numbercell));
                    innerAList.add(getCellValue(Descriptioncell));
                    //System.out.println("Levelcell-" +getCellValue(Levelcell)+" Numbercell-" +getCellValue(Numbercell)+" Descriptioncell-" +getCellValue(Descriptioncell));
                    outerAlist.add(innerAList);
                }
            } else if (readFileName == "EMC") {
                for (int i = 0; i < stRows; i++) {
                    innerAList = new ArrayList<String>();
                    //System.out.print(i+1+"---");
                    Cell Levelcell = sheet.getRow(i).getCell(9);
                    Cell Data_material = sheet.getRow(i).getCell(10);
                    Cell Material_Number_Desc = sheet.getRow(i).getCell(14);
                    Cell New_Version = sheet.getRow(i).getCell(17);
                    innerAList.add(getCellValue(Levelcell));
                    innerAList.add(getCellValue(Data_material));
                    innerAList.add(getCellValue(Material_Number_Desc));
                    innerAList.add(getCellValue(New_Version));
//                    System.out.println("Levelcell-" +getCellValue(Levelcell)+" data_material-" + getCellValue(Data_material)+
//                            " Material_Number_Desc-" +getCellValue(Material_Number_Desc)+ " New_Version-" +getCellValue(New_Version));
                    outerAlist.add(innerAList);
                }
            } else if (readFileName == "ImportConfig") {
                for (int i = 0; i < stRows; i++) {
                    innerAList = new ArrayList<String>();
                    //System.out.print(i+1+"---");
                    Cell Numbercell = sheet.getRow(i).getCell(0);
                    Cell Quantitycell = sheet.getRow(i).getCell(1);
                    innerAList.add(getCellValue(Numbercell));
                    innerAList.add(getCellValue(Quantitycell));
                    //System.out.println("Numbercell-" +getCellValue(Numbercell)+" Quantitycell-" +getCellValue(Quantitycell));
                    outerAlist.add(innerAList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return outerAlist;
    }

//    public static void main(String[] args) {
//        new BaseDaoImpl();
//        String filePath = "C:/Users/sen_liang/Desktop/Code-history/NPIExcelProcess-8-21/src/ImportExcel.xlsx";
//        File excelFile = new File(filePath);
//        ArrayList<ArrayList<String>> readExcel = null;
//        try {
//            readExcel = ReadDataFromExcel(filePath, "ImportConfig");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        ImportDataToSQLite(readExcel);
//    }

    public static void WriteDataToSQLite(ArrayList<ArrayList<String>> al, String readFileName) {
        Statement stm = null;
        PreparedStatement prs = null;
        try {
            BaseDaoImpl.conn.close();
            new BaseDaoImpl();
            stm = BaseDaoImpl.conn.createStatement();
            if (readFileName == "AgileBom") {
                stm.executeUpdate("drop table if exists Agile_BOM");
                stm.executeUpdate("create table Agile_BOM(" +
                        "id integer primary key autoincrement, " +//0
                        "Level vachar(10), Number vachar(30), Description vachar(50) );"); //+  //9,10,11,12
                prs = BaseDaoImpl.conn.prepareStatement("insert into Agile_BOM values (?,?,?,?);");
            } else if (readFileName == "EMC") {
                stm.executeUpdate("drop table if exists EMC_MAKE_BUY");//判断是否有表tables的存在。有则删除
                stm.executeUpdate("create table EMC_MAKE_BUY(" +
                        "id integer primary key autoincrement," +//0
                        "Level vachar(5) ," + "Data_material vachar(30) ," +
                        "Material_Number_Desc vachar(50) ," + "New_Version vachar(10) );"); //+  //9,10,11,12
                prs = BaseDaoImpl.conn.prepareStatement("insert into EMC_MAKE_BUY values (?,?,?,?,?);");
            }
            BaseDaoImpl.conn.setAutoCommit(false);
            //begin from 1
            for (int i = 1; i < al.size(); i++) {
                Progress.current = i;
                if (exitWriteFlag == 1) break;
                //System.out.print(i + "    ");
                for (int j = 0; j < al.get(i).size(); j++) {//
                    //System.out.print(j + "   " + al.get(i).get(j) + "   ");
                    prs.setString(j + 2, al.get(i).get(j));
                } //for(inner);
                //System.out.println();
                prs.addBatch();
//                BaseDaoImpl.conn.setAutoCommit(false);
//                prs.executeBatch();
//                BaseDaoImpl.conn.setAutoCommit(true);
            } //for(outside);
            prs.executeBatch();
            BaseDaoImpl.conn.setAutoCommit(true);
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } finally {
            creatHome();
            AgileEMCJPanel.hasAgileEMCTable = false;//重新加载表格
        }
    }

    public static void ImportDataToSQLite(ArrayList<ArrayList<String>> al) {
        Statement stm = null;
        PreparedStatement prs = null;
        try {
            BaseDaoImpl.conn.close();
            new BaseDaoImpl();
            stm = BaseDaoImpl.conn.createStatement();
            stm.executeUpdate("drop table if exists Query_Config");
            stm.executeUpdate("create table Query_Config(" +
                    "id integer primary key autoincrement, " +//0
                    "Number vachar(30), Quantity vachar(10) );"); //+  //9,10,11,12
            prs = BaseDaoImpl.conn.prepareStatement("insert into Query_Config values (?,?,?);");
            BaseDaoImpl.conn.setAutoCommit(false);
            //begin from 1
            for (int i = 1; i < al.size(); i++) {
                //System.out.print(i + "    ");
                for (int j = 0; j < al.get(i).size(); j++) {//
                    //System.out.print("   " + al.get(i).get(j) + "   ");
                    prs.setString(j + 2, al.get(i).get(j));
                } //for(inner);
                //System.out.println();
                prs.addBatch();
            } //for(outside);
            prs.executeBatch();
            BaseDaoImpl.conn.setAutoCommit(true);
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        }
    }

    public static String getCellValue(Cell cell) {
        String ret = "";
        if (cell == null) return ret;
        CellType type = cell.getCellTypeEnum();
        switch (type) {
            case BLANK:
                ret = "";
                break;
            case BOOLEAN:
                ret = String.valueOf(cell.getBooleanCellValue());
                break;
            case ERROR:
                ret = null;
                break;
            case FORMULA:
                Workbook wb = cell.getSheet().getWorkbook();
                CreationHelper crateHelper = wb.getCreationHelper();
                FormulaEvaluator evaluator = crateHelper.createFormulaEvaluator();
                ret = getCellValue(evaluator.evaluateInCell(cell));
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    Date theDate = cell.getDateCellValue();
                    ret = simpleDateFormat.format(theDate);
                } else {
                    ret = NumberToTextConverter.toText(cell.getNumericCellValue());
                }
                break;
            case STRING:
                ret = cell.getRichStringCellValue().getString();
                break;
            default:
                ret = "";
        }
        return ret; // 有必要自行trim
    }
}
