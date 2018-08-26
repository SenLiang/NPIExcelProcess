package org.lqz.module.dao.Impl;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.lqz.module.services.Impl.AllTableServiceImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Vector;

import static org.lqz.module.view.GenerateConfigTableJPanel.ModelQueryVector;
import static org.lqz.module.view.ImGeTableJPanel.ImGeQueryVector;
import static org.lqz.module.view.NodeDetailJPanel.BOMQueryVector;
import static org.lqz.module.view.NodeDetailJPanel.FullBOMQueryVector;
import static org.lqz.module.view.ShowExcludeNTreeJPanel.ExTreeNodeVector;
import static org.lqz.module.view.ShowIncludeNTreeJPanel.TreeNodeVector;

public class WriteExcel {
    public String file_pathname = "";
    private Workbook workbook = null;
    public List<String> titlerow = null;

    //创建对象时自动创建工作簿
    public WriteExcel() throws Exception {
        workbook = new XSSFWorkbook();
    }

    public WriteExcel(String file_pathname) throws Exception {
        this.file_pathname = file_pathname;
        workbook = new XSSFWorkbook();
    }

    //创建无数据的工作表，并返回表对象
    public Sheet CreateSheet() throws Exception {
        Sheet sheet = this.workbook.createSheet();
        return sheet;
    }

    //串讲有数据的工作表，并返回表对象
    public Sheet CreateSheet(Vector<Vector> sheetdata) throws Exception {
        Sheet sheet = this.workbook.createSheet();
//        if(this.titlerow != null)
//            sheetdata.add(0, this.titlerow);
        for (int row = 0; row < sheetdata.size(); row++) {
            Row rows = sheet.createRow(row);
            for (int col = 0; col < sheetdata.get(row).size(); col++) {
                rows.createCell(col).setCellValue(String.valueOf(sheetdata.get(row).get(col)));
            }
        }
        return sheet;
    }

    public void SaveToFile(String filename) throws Exception {
        this.file_pathname = filename;
        this.SaveToFile();
    }

    public void SaveToFile() throws Exception {
        File xfile = new File(this.file_pathname);
        if (!xfile.exists())
            xfile.createNewFile();

        FileOutputStream xstream = new FileOutputStream(xfile);
        this.workbook.write(xstream);
        this.workbook.close();
        xstream.flush();
        xstream.close();
    }

    public static void SaveToExcel(String exportName, String filepath) {
        WriteExcel writetoexcel = null;
        try {
            writetoexcel = new WriteExcel(filepath);
            if (exportName.equals("AgileBom")) {
                AllTableServiceImpl goodsService = new AllTableServiceImpl();
                Vector<Vector> vector = goodsService.AgileBomSelectList();
                writetoexcel.CreateSheet(vector);
                writetoexcel.SaveToFile();
            }else if (exportName.equals("EMC")){
                AllTableServiceImpl goodsService = new AllTableServiceImpl();
                Vector<Vector> vector = goodsService.EMCSelectList();
                writetoexcel.CreateSheet(vector);
                writetoexcel.SaveToFile();
           }else if (exportName.equals("ConfigBOM")){
                Vector<Vector> vector=(Vector<Vector>)ModelQueryVector.clone(); ;
                Vector temp = new Vector<String>();
                temp.add("Quantity");temp.add("Level");
                temp.add("Level_1_Component");
                temp.add("Description");temp.add("EMC");
                vector.insertElementAt(temp,0);
                writetoexcel.CreateSheet(vector);
                writetoexcel.SaveToFile();
            }else if (exportName.equals("AgileEMC")){
                AllTableServiceImpl goodsService = new AllTableServiceImpl();
                Vector<Vector> vector = goodsService.AgileEMCSelectList();
                writetoexcel.CreateSheet(vector);
                writetoexcel.SaveToFile();
            }else if (exportName.equals("BOMQuery")){
                Vector<Vector> vector= (Vector<Vector>) BOMQueryVector.clone();
                Vector temp = new Vector<String>();
                temp.add("Node");temp.add("Level");
                temp.add("Description");temp.add("EMC");
                vector.insertElementAt(temp,0);
                writetoexcel.CreateSheet(vector);
                writetoexcel.SaveToFile();
            }else if (exportName.equals("FullBOMQuery")){
                Vector<Vector> vector=FullBOMQueryVector;
                Vector temp = new Vector<String>();
                temp.add("Level");temp.add("P/N");
                temp.add("Description");temp.add("EMC");
                vector.insertElementAt(temp,0);
                writetoexcel.CreateSheet(vector);
                writetoexcel.SaveToFile();
            }else if (exportName.equals("ImportConfig")){
                Vector<Vector> vector=ImGeQueryVector;
                Vector temp = new Vector<String>();
                temp.add("Quantity");temp.add("Level");
                temp.add("Level_1_Component");
                temp.add("Description");temp.add("EMC");
                vector.insertElementAt(temp,0);
                writetoexcel.CreateSheet(vector);
                writetoexcel.SaveToFile();
            }else if (exportName.equals("IncludeTree")){
                Vector<Vector> vector=TreeNodeVector;
                writetoexcel.CreateSheet(vector);
                writetoexcel.SaveToFile();
            }else if (exportName.equals("ExcludeTree")){
                Vector<Vector> vector=ExTreeNodeVector;
                writetoexcel.CreateSheet(vector);
                writetoexcel.SaveToFile();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    public static void main(String[] args) {
//        try {
//            WriteExcel writetoexcel = new WriteExcel("C:/ideaWorkRoom/NPIExcelProcess/src/Testcase/test.xls");
//            AllTableServiceImpl goodsService = new AllTableServiceImpl();
//            Vector<Vector> vector = new Vector<Vector>();
//            vector = goodsService.AgileBomSelectList();
//            writetoexcel.CreateSheet(vector);
//            writetoexcel.SaveToFile();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
}
