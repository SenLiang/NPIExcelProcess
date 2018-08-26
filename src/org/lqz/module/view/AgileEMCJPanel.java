package org.lqz.module.view;

import org.lqz.framework.util.BaseTableModule;
import org.lqz.framework.util.Item;
import org.lqz.framework.util.Tools;
import org.lqz.module.dao.Impl.BaseDaoImpl;
import org.lqz.module.services.Impl.AllTableServiceImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import static org.lqz.module.dao.Impl.BaseDaoImpl.validateTableNameExist;
import static org.lqz.module.services.Impl.AllTableServiceImpl.AgileEMCTable;

public class AgileEMCJPanel {

    // 定义全局组件
    JPanel backgroundPanel, topPanel, toolPanel, btAEPanel, tablePanel;
    BaseTableModule baseTableModule;
    JTable table;
    JScrollPane jScrollPane;
    JButton ExportAEButton;
    IndexJFrame jframe;
    public static boolean hasAgileEMCTable = true;//应为false

    public AgileEMCJPanel() {
        backgroundPanel = new JPanel(new BorderLayout());
        backgroundPanel.setName("AgileEMCJPanel");
        initTopPanel();
            if (!validateTableNameExist("Agile_BOM") || !validateTableNameExist("EMC_MAKE_BUY")) {
                Object[] options = {"OK"};
                int response = JOptionPane.showOptionDialog(backgroundPanel, "Input the table Agile BOM & EMC MAKE BUY correctly!",
                        "Warning", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            } else {
                initTablePanel();
            }
    }

    public void setJframe(IndexJFrame jframe) {
        this.jframe = jframe;
    }

    // 初始化顶部面板
    public void initTopPanel() {
        topPanel = new JPanel(new BorderLayout());
        initToolPanel();
        backgroundPanel.add(topPanel, "North");
    }

    // 初始化工具面板
    public void initToolPanel() {
        toolPanel = new JPanel();
        topPanel.add(toolPanel, "West");
    }

    // 初始化数据表格面板
    public void initTablePanel() {
        String params[] = {"id", "Level", "Number", "Agile_Description", "EMC_Description", "New_Version"};
        Vector<Vector> vector = new Vector<Vector>();
        try {
            if (hasAgileEMCTable == false || !validateTableNameExist("Agile_EMC"))
                AgileEMCTable();//线创建字表，后查询,只创建一次
            AllTableServiceImpl goodsService = new AllTableServiceImpl();
            vector = goodsService.AgileEMCSelectByCondition();
        } catch (Exception e) {
            e.printStackTrace();
        }
        baseTableModule = new BaseTableModule(params, vector);
        table = new JTable(baseTableModule);
        Tools.setTableStyle(table);
        jScrollPane = new JScrollPane(table);
        Tools.setJspStyle(jScrollPane);
        tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);

        tablePanel.add(jScrollPane);
        btAEPanel = new JPanel();
        btAEPanel.setPreferredSize(new Dimension(200, 30));
        ExportAEButton = new JButton("Export This Table");
        ExportAEButton.setPreferredSize(new Dimension(200, 30));// 设置按钮大小

        ExportAEButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                FileOperation fileOperation = new FileOperation(jframe);
                boolean isSaveSuccess = fileOperation.SaveAs("AgileEMC");
                if (isSaveSuccess)
                    JOptionPane.showOptionDialog(backgroundPanel, "Export this table successfully!",
                            "Tips", JOptionPane.DEFAULT_OPTION,
                            JOptionPane.INFORMATION_MESSAGE, null, null, null);
            }
        });
        btAEPanel.add(ExportAEButton, BorderLayout.CENTER);
        backgroundPanel.add(btAEPanel, BorderLayout.SOUTH);
        backgroundPanel.add(tablePanel, "Center");
    }

}
