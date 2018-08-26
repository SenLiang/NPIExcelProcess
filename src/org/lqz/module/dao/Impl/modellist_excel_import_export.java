package org.lqz.module.dao.Impl;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.lqz.framework.util.JdbcUtil;
import org.lqz.module.view.AgileEMCJPanel;
import org.lqz.module.view.FileOperation;
import org.lqz.module.view.Progress;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import static org.lqz.module.view.IndexJFrame.creatHome;

public class modellist_excel_import_export {
    public static int AmountRows = 0;
    public static int exitWriteFlag = 0;
    public final static String DATE_OUTPUT_PATTERNS = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    public final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
            DATE_OUTPUT_PATTERNS);
    public static ArrayList<String> sheetNameAList =null;

    public static ArrayList<ArrayList<ArrayList<String>>> ReadModelFromExcel(String filePath) throws FileNotFoundException {
        ArrayList<String> innerAList = null;
        ArrayList<ArrayList<String>> outerAlist=null;
        ArrayList<ArrayList<ArrayList<String>>> exAlist = new ArrayList<ArrayList<ArrayList<String>>>();
        Workbook wb = null;
        try {
            wb = FileOperation.initWorkBook(filePath);
            int SheetNumber = wb.getNumberOfSheets();
            sheetNameAList = new ArrayList<String>();
            AmountRows = 0;
            //System.out.println("SheetNumber-----------------" + SheetNumber);
            for (int Sheeti = 0; Sheeti < SheetNumber; Sheeti++) {
                outerAlist = new ArrayList<ArrayList<String>>();
                Sheet sheet = wb.getSheetAt(Sheeti);
                sheetNameAList.add(sheet.getSheetName());
                int stRows = sheet.getPhysicalNumberOfRows();
                AmountRows+=stRows;
                //stColumn=sheet.getRow(0).getLastCellNum();
//            System.out.println("stRows-------------"+stRows);
//            System.out.println("stColumn-------------"+stColumn);
                // stColumn=3;
                for (int i = 0; i < stRows; i++) {
                    innerAList = new ArrayList<String>();
                    //System.out.print(i+1+"---");
                    Cell Modelcell = sheet.getRow(i).getCell(0);
                    Cell L1Componentcell = sheet.getRow(i).getCell(8);
                    Cell ComponentDesccell = sheet.getRow(i).getCell(9);
                    Cell MakeBuyccell = sheet.getRow(i).getCell(10);
                    Cell Quantitycell = sheet.getRow(i).getCell(13);
                    innerAList.add(getCellValue(Modelcell));
                    innerAList.add(getCellValue(L1Componentcell));
                    innerAList.add(getCellValue(ComponentDesccell));
                    innerAList.add(getCellValue(MakeBuyccell));
                    innerAList.add(getCellValue(Quantitycell));
//                    System.out.println("Modelcell-" + getCellValue(Modelcell) + " L1Componentcell-" + getCellValue(L1Componentcell) +
//                            " ComponentDesccell-" + getCellValue(ComponentDesccell)
//                            + " MakeBuyccell-" + getCellValue(MakeBuyccell) + " Quantitycell-" + getCellValue(Quantitycell));
                    outerAlist.add(innerAList);
                }
                exAlist.add(outerAlist);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        insertTableName(sheetNameAList);
        return exAlist;
    }



//    public static void main(String[] args) {
//        String filePath = "C:/Users/sen_liang/Desktop/Code-history/NPIExcelProcess-8-21/src/ImportExcel.xlsx";
//        File excelFile = new File(filePath);
//        ArrayList<ArrayList<String>> readExcel = null;
//        try {
//            readExcel = ImportDataFromExcel(filePath);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        WriteModelToSQLite(readExcel, sheetNameAList);
//    }

    public static void insertTableName(ArrayList<String> sheetNameAList) {
        Statement stm = null;
        PreparedStatement prs = null;
        try {
            BaseDaoImpl.conn.close();
            new BaseDaoImpl();
            stm = BaseDaoImpl.conn.createStatement();
            stm.executeUpdate("drop table if exists TableName");//判断是否有表tables的存在。有则删除
            stm.executeUpdate("create table TableName (id integer primary key autoincrement, name vachar(30));");
            BaseDaoImpl.conn.setAutoCommit(false);
            for (int sheetNumber = 0; sheetNumber < sheetNameAList.size(); sheetNumber++) {
                prs = BaseDaoImpl.conn.prepareStatement("insert into TableName (name) values ('"+sheetNameAList.get(sheetNumber)+"');");
                int i = prs.executeUpdate();// 指示受影响的行数（即更新计数）
            }//for(outside);
            BaseDaoImpl.conn.setAutoCommit(true);
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } finally {
//            creatHome();
//            AgileEMCJPanel.hasAgileEMCTable=false;//重新加载表格
        }
    }
//    public static void main(String[] args) {
//        new BaseDaoImpl();
//        ArrayList<String> sheetNameAList=new ArrayList<String>();
//        sheetNameAList.add("Infinity Chassis Model"); sheetNameAList.add("Compute Node Model");
//        sheetNameAList.add("FrontEnd IO Model"); sheetNameAList.add("BackEnd IO Model");
//        sheetNameAList.add("SSD Drive Model"); sheetNameAList.add("Filler Model");
//        insertTableName(sheetNameAList);
//    }

    public static void WriteModelToSQLite(ArrayList<ArrayList<ArrayList<String>>> alSheet, ArrayList<String> sheetNameAList) {
        Statement stm = null;
        PreparedStatement prs = null;
        try {
            BaseDaoImpl.conn.close();
            new BaseDaoImpl();
            stm = BaseDaoImpl.conn.createStatement();
            int ExeCount=0;
            for (int sheetNumber = 0; sheetNumber < sheetNameAList.size(); sheetNumber++) {
                String sheetName = sheetNameAList.get(sheetNumber);
                //System.out.println("sheetName--------------"+sheetName);
                ArrayList<ArrayList<String>> al = alSheet.get(sheetNumber);
                stm.executeUpdate("drop table if exists  '" + sheetName+"'");//判断是否有表tables的存在。有则删除
                stm.executeUpdate("create table '" + sheetName + "'(" +
                        "id integer primary key autoincrement, " +//0
                        "Model vachar(20), L1Comp vachar(20), CompDesc vachar(30)," +
                        "MakeBuy vachar(10), Quantity integer(10) );"); //+  //9,10,11,12
                //  "KC_POSITION vachar(3) ," + "KC_MARK vachar(3) );");
                prs = BaseDaoImpl.conn.prepareStatement("insert into '" + sheetName + "' values (?,?,?,?,?,?);");
                BaseDaoImpl.conn.setAutoCommit(false);
                //begin from 1
                for (int i = 1; i < al.size(); i++) {
                    Progress.current = ExeCount+i;
                    if (exitWriteFlag == 1) break;
                    //System.out.print(i + "    ");
                    for (int j = 0; j < al.get(i).size(); j++) {//
                        prs.setString(j + 2, al.get(i).get(j));
                    } //for(inner);
                   //System.out.println();
                    prs.addBatch();
                }
                prs.executeBatch();
                BaseDaoImpl.conn.setAutoCommit(true);
                if (exitWriteFlag == 1) break;
                ExeCount= ExeCount+al.size();
            }//for(outside);
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } finally {
                //conn.close();
                //jdbcUtil.getJdbcUtil().release(null, stm, conn);
                //jdbcUtil.getJdbcUtil().release(null, prs, conn);
                 creatHome();
            AgileEMCJPanel.hasAgileEMCTable=false;//重新加载表格
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
