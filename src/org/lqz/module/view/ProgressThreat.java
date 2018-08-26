package org.lqz.module.view;

import org.lqz.module.dao.Impl.excel_import_export;
import org.lqz.module.dao.Impl.modellist_excel_import_export;

import java.util.ArrayList;

import static org.lqz.module.dao.Impl.excel_import_export.WriteDataToSQLite;
import static org.lqz.module.dao.Impl.excel_import_export.exitWriteFlag;
import static org.lqz.module.dao.Impl.modellist_excel_import_export.WriteModelToSQLite;

public class ProgressThreat implements Runnable{
    IndexJFrame ParentJFrame;
    ArrayList<ArrayList<String>> readExcel;
    ArrayList<ArrayList<ArrayList<String>>> mutiReadExcel;
    String readFileName;

    public ProgressThreat(IndexJFrame ParentJFrame, ArrayList<ArrayList<String>> readExcel,String readFileName) {
        this.ParentJFrame=ParentJFrame;
        this.readExcel=readExcel;
        this.readFileName=readFileName;
    }

    public ProgressThreat(IndexJFrame parentJFrame,String readFileName,ArrayList<ArrayList<ArrayList<String>>> mutiReadExcel ) {
        this.ParentJFrame=parentJFrame;
        this.mutiReadExcel=mutiReadExcel;
        this.readFileName=readFileName;
    }

    @Override
    public void run() {
        if (readFileName=="AgileBom"||readFileName=="EMC"){
        Progress.amount = excel_import_export.stRows-1;
        exitWriteFlag=0;
        new Progress().init(ParentJFrame,readFileName);
        WriteDataToSQLite(readExcel,readFileName);
        }else if (readFileName=="ModelList"){
            Progress.amount = modellist_excel_import_export.AmountRows-1;
            exitWriteFlag=0;
            new Progress().init(ParentJFrame,readFileName);
            WriteModelToSQLite(mutiReadExcel,modellist_excel_import_export.sheetNameAList);
        }
    }
}
