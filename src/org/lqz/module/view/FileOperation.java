package org.lqz.module.view;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.lqz.module.dao.Impl.excel_import_export;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.util.ArrayList;
import java.util.Vector;

import org.lqz.module.dao.Impl.modellist_excel_import_export;
import org.lqz.module.view.Progress;

import static org.lqz.module.dao.Impl.WriteExcel.SaveToExcel;
import static org.lqz.module.dao.Impl.excel_import_export.ImportDataToSQLite;
import static org.lqz.module.dao.Impl.excel_import_export.ReadDataFromExcel;
import static org.lqz.module.dao.Impl.excel_import_export.WriteDataToSQLite;
import static org.lqz.module.dao.Impl.modellist_excel_import_export.*;


public class FileOperation {
    JFileChooser fileChooser = new JFileChooser();
    IndexJFrame ParentJFrame;
    String src;
    String fileName;

    public FileOperation(IndexJFrame ParentJFrame) {
        this.ParentJFrame = ParentJFrame;
        src = "";
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new MyFileFilter("Excel Data Table(.xls)", ".xls"));//添加文件过滤器
        fileChooser.addChoosableFileFilter(new MyFileFilter("Excel Data Table(.xlsx)", ".xlsx"));//添加文件过滤器
        FileSystemView fsv = FileSystemView.getFileSystemView();
        fileChooser.setCurrentDirectory(fsv.getHomeDirectory());//设置默认路径为桌面路径

    }

    public boolean open() {
        if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(ParentJFrame)) {
            File file = fileChooser.getSelectedFile();
            if (file.getName() == null) return false;
            src = file.toString();//获取文件路径
            fileName = file.getName();//获取文件名
            setStyle(file);//设置编辑区的标题
            return true;
        }
        return false;
    }

    private static final String SUFFIX_2003 = ".xls";
    private static final String SUFFIX_2007 = ".xlsx";

    public static Workbook initWorkBook(String mFilePath) throws IOException {
        File file = new File(mFilePath);
        InputStream is = new FileInputStream(file);
        Workbook workbook = null;
        if (mFilePath.endsWith(SUFFIX_2003)) {
            workbook = new HSSFWorkbook(is);
        } else if (mFilePath.endsWith(SUFFIX_2007)) {
            workbook = new XSSFWorkbook(is);
        }
        return workbook;
    }

    public boolean importToSQL(String readFileName) {
        //由此进行分类
        String filePath = src;//"Testcase/Agile_BOM.xls";
        if (readFileName.equals("AgileBom") || readFileName.equals("EMC")) {
            ArrayList<ArrayList<String>> readExcel = null;
            try {
                readExcel = ReadDataFromExcel(filePath, readFileName);
            } catch (FileNotFoundException e) {
                System.out.println("Sorry, read error！");
                e.printStackTrace();
            }
            ParentJFrame.setEnabled(false);
            ProgressThreat r = new ProgressThreat(this.ParentJFrame, readExcel, readFileName);
            Thread t = new Thread(r);
            t.start();
        } else if (readFileName.equals("ModelList")) {
            ArrayList<ArrayList<ArrayList<String>>> MutiReadExcel = null;
            try {
                MutiReadExcel = ReadModelFromExcel(filePath);
            } catch (FileNotFoundException e) {
                System.out.println("Sorry, read error！");
                e.printStackTrace();
            }
            ParentJFrame.setEnabled(false);
            ProgressThreat r = new ProgressThreat(this.ParentJFrame, readFileName, MutiReadExcel);
            Thread t = new Thread(r);
            t.start();
        }else if (readFileName.equals("ImportConfig")) {
            ArrayList<ArrayList<String>> readExcel = null;
            try {
                readExcel = ReadDataFromExcel(filePath, readFileName);
            } catch (FileNotFoundException e) {
                System.out.println("Sorry, read error！");
                e.printStackTrace();
            }
            ImportDataToSQLite(readExcel);
            return true;
        }
        return false;
    }

    public boolean SaveAs(String exportName) {
        if (JFileChooser.APPROVE_OPTION == fileChooser.showSaveDialog(ParentJFrame)) {
            File newFile;
            File file = fileChooser.getSelectedFile();
            if (file.getName() == null) return false;
            BufferedWriter br;
            MyFileFilter filter = (MyFileFilter) fileChooser.getFileFilter();
            String ends = filter.getEnds();
            if (file.toString().indexOf(ends) != -1) {
                newFile = file;
            } else {
                newFile = new File(file.getAbsolutePath() + ends);
            }
            src = newFile.toString();
            fileName = newFile.getName();
            if (newFile.renameTo(newFile) || (file.toString().indexOf(ends) == -1)) {
                //System.out.println("The file is not opening");
                if (newFile.exists())//已存在同名文件，弹出提示对话框
                {
                    int result = JOptionPane.showConfirmDialog(ParentJFrame, "A file with the same name already exists. " +
                                    "\n                  Rename it?",
                            "Warning", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (result == 0) {//Yes
                        SaveAs(exportName);
                    } else if (result == 1) {//No
                        newFile.delete();
                        SaveToExcel(exportName,src);
                        return true;
                    }
                } else {
                    SaveToExcel(exportName,src);
                    return true;
                }
            } else {
                // System.out.println("The file is opening");
                Object[] options = {"OK"};
                int response = JOptionPane.showOptionDialog(ParentJFrame, "Sorry. close the file before you write.",
                        "Tips", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (response == 0) //Yes
                    SaveAs(exportName);
            }
        }
        return false;
    }

    public void setStyle(File file) {
        String name = file.getName();
        ParentJFrame.setTitle("NPI Excel Processing System---" + file.getAbsolutePath());
    }

    public String getPath() {
        String name = getFileName();
        int t = src.lastIndexOf(name, src.length() - 1);
        String path = src.substring(0, t);
        return path;
    }

    public String getFileName() {
        if (fileName == null) return "";
        int point = fileName.indexOf(".");
        String name = fileName.substring(0, point);
        return name;
    }


    class MyFileFilter extends FileFilter {
        String ends; // 文件后缀
        String description; // 文件描述
        public MyFileFilter(String description, String ends) {
            this.ends = ends; // 设置文件后缀
            this.description = description; // 设置文件描述文字
        }

        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) return true;
            String fileName = f.getName();
            if (fileName.toUpperCase().endsWith(this.ends.toUpperCase())) return true;//判断文件名后缀
            return false;
        }

        @Override
        public String getDescription() {
            return this.description;
        }

        public String getEnds() {
            return this.ends;
        }

    }
}
