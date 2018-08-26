package org.lqz.module.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.lqz.framework.util.BaseTableModule;
import org.lqz.framework.util.Tools;
import org.lqz.module.dao.Impl.BaseDaoImpl;
import org.lqz.module.services.Impl.AllTableServiceImpl;

import static org.lqz.module.dao.Impl.BaseDaoImpl.validateTableNameExist;

public class AgileBOMDBJPanel implements MouseListener {

    // 定义全局组件
    JPanel backgroundPanel, topPanel, toolPanel, exportPanel, tablePanel;
    BaseTableModule baseTableModule;
    JTable table;
    JScrollPane jScrollPane;
    JLabel tool_modify, tool_delete, tool_export;
    IndexJFrame jframe;

    public AgileBOMDBJPanel() {

        backgroundPanel = new JPanel(new BorderLayout());
        backgroundPanel.setName("AgileBOMDBJPanel");
        initTopPanel();
        if (validateTableNameExist("Agile_BOM"))
            initTablePanel();
        else {
            Object[] options = {"OK"};
            int response = JOptionPane.showOptionDialog(backgroundPanel, "Input the table Agile BOM firstly!",
                    "Warning", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
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
        Icon icon_modify = new ImageIcon(IndexJFrame.class.getResource("/image/modify.png"));
        //Icon icon_modify = new ImageIcon("image/modify.png");
        tool_modify = new JLabel(icon_modify);
//		tool_modify.setText("修改商品");
        tool_modify.setToolTipText("修改元组");
        tool_modify.addMouseListener(this);

        Icon icon_delete = new ImageIcon(IndexJFrame.class.getResource("/image/delete.png"));
        //Icon icon_delete = new ImageIcon("image/delete.png");
        tool_delete = new JLabel(icon_delete);
        tool_delete.setToolTipText("删除元组");
        tool_delete.addMouseListener(this);

        Icon icon_export = new ImageIcon(IndexJFrame.class.getResource("/image/excel.png"));
        //Icon icon_export = new ImageIcon("image/excel.png");
        tool_export = new JLabel(icon_export);
        tool_export.setToolTipText("导出Excel");
        tool_export.addMouseListener(this);

        //toolPanel.add(tool_add);
        toolPanel.add(tool_modify);
        toolPanel.add(tool_delete);

        exportPanel = new JPanel();
        exportPanel.setPreferredSize(new Dimension(120, 40));
        exportPanel.add(tool_export, BorderLayout.EAST);

        topPanel.add(toolPanel, "West");
        topPanel.add(exportPanel, "East");
    }


    // 初始化数据表格面板
    public void initTablePanel() {

        String conditionParams[] = {"全部", "全部"};
        String params[] = {"id", "Level", "Number", "Description"};
        //"LCycle_Phase", "Rev_Date","Rev_Incorp_Date","Effect_Date"};
        AllTableServiceImpl goodsService = new AllTableServiceImpl();
        Vector<Vector> vector = new Vector<Vector>();
        try {
            vector = goodsService.AgileBomSelectByCondition();
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
        backgroundPanel.add(tablePanel, "Center");
    }

    // 更新数据表格
    public void refreshTablePanel() {
        backgroundPanel.remove(tablePanel);
        String params[] = {"id", "Level", "Number", "Description"};
        AllTableServiceImpl goodsService = new AllTableServiceImpl();
        Vector<Vector> vector = new Vector<Vector>();
        try {
            vector = goodsService.AgileBomSelectByCondition();
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
        backgroundPanel.add(tablePanel, "Center");
        backgroundPanel.validate();
    }

    // 鼠标点击事件
    @Override
    public void mouseClicked(MouseEvent e) {

        if (e.getSource() == tool_modify) {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(null, "请选择元组");
            } else {
                new ModifyAgileBomJFrame(this, table, row);
            }

        } else if (e.getSource() == tool_delete) {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(null, "请选择元组");
            } else {
                String id = String.valueOf(table.getValueAt(row, 0));
                int result = JOptionPane.showConfirmDialog(null, "是否确定删除？", "用户提示", JOptionPane.YES_NO_OPTION);
                if (result == 0) {
                    String[] params = {id};
                    AllTableServiceImpl agileService = new AllTableServiceImpl();
                    try {

                        int tempresult = agileService.AgileBomdeleteById(params);
                        if (tempresult > 0) {
                            JOptionPane.showMessageDialog(null, "商品删除成功！");
                            refreshTablePanel();
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        } else if (e.getSource() == tool_export) {
            FileOperation fileOperation = new FileOperation(jframe);
            boolean isSaveSuccess = fileOperation.SaveAs("AgileBom");
            if (isSaveSuccess)
                JOptionPane.showOptionDialog(this.backgroundPanel, "Export this table successfully!",
                        "Tips", JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE, null, null, null);
        }
    }

    // 鼠标划入事件
    @Override
    public void mouseEntered(MouseEvent e) {
        if (e.getSource() == tool_modify) {
            tool_modify.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        } else if (e.getSource() == tool_delete) {
            tool_delete.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        } else if (e.getSource() == tool_export) {
            tool_export.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
    }

}
