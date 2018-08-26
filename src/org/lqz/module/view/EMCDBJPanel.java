package org.lqz.module.view;

import org.lqz.framework.util.BaseTableModule;
import org.lqz.framework.util.Item;
import org.lqz.framework.util.MyFont;
import org.lqz.framework.util.Tools;
import org.lqz.module.dao.Impl.BaseDaoImpl;
import org.lqz.module.services.Impl.AllTableServiceImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static org.lqz.framework.util.Tools.fitTableColumns;
import static org.lqz.module.dao.Impl.BaseDaoImpl.select;
import static org.lqz.module.dao.Impl.BaseDaoImpl.validateTableNameExist;
import static org.lqz.module.view.ShowIncludeNTreeJPanel.expandSpecifyNode;

public class EMCDBJPanel implements MouseListener, ActionListener {

    // 定义全局组件
    JPanel backgroundPanel, topPanel, toolPanel, exportPanel, tablePanel;
    BaseTableModule baseTableModule;
    JTable table;
    JScrollPane jScrollPane;
    JLabel tool_add,tool_modify, tool_delete, tool_export, label_number;
    JTextArea ResultText;
    IndexJFrame jframe;
    JButton QButton;
    JTextField NumberText;
    final String tips = "Please input a PathNumber.";

    public EMCDBJPanel() {

        backgroundPanel = new JPanel(new BorderLayout());
        backgroundPanel.setName("EMCDBJPanel");
        initTopPanel();
        if (validateTableNameExist("EMC_MAKE_BUY"))
            initTablePanel();
        else {
            Object[] options = {"OK"};
            int response = JOptionPane.showOptionDialog(backgroundPanel, "Input the table EMC MAKE BUY firstly!",
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
        Icon icon_add = new ImageIcon(IndexJFrame.class.getResource("/image/add.png"));
        // Icon icon_modify = new ImageIcon("image/modify.png");
        tool_add = new JLabel(icon_add);
        tool_add.setToolTipText("增加元组");
        tool_add.addMouseListener(this);

        Icon icon_modify = new ImageIcon(IndexJFrame.class.getResource("/image/modify.png"));
        // Icon icon_modify = new ImageIcon("image/modify.png");
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
        toolPanel.add(tool_add);
        toolPanel.add(tool_modify);
        toolPanel.add(tool_delete);

        exportPanel = new JPanel();
        exportPanel.setPreferredSize(new Dimension(120, 40));
        exportPanel.add(tool_export, BorderLayout.EAST);

        JPanel input = new JPanel(new FlowLayout(FlowLayout.CENTER));
        QButton = new JButton("Go");
        QButton.addActionListener(this);

        NumberText = new JTextField();
        NumberText.setPreferredSize(new Dimension(500, 40));
        NumberText.setText(tips);
        NumberText.setFont(new Font("标楷体", Font.TRUETYPE_FONT | Font.ITALIC, 18));


        label_number = new JLabel("PathNumber");
        input.add(label_number);
        input.add(NumberText);
        input.add(QButton);
        //input.add(ResultText);

        topPanel.add(input, "Center");
        NumberText.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (tips.equalsIgnoreCase(NumberText.getText())) {
                    NumberText.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if ("".equals(NumberText.getText())) {
                    NumberText.setText(tips);
                }
            }
        });
        topPanel.add(toolPanel, "West");
        topPanel.add(exportPanel, "East");
    }

    // 初始化数据表格面板
    public void initTablePanel() {
        String conditionParams[] = {"全部", "全部"};
        String params[] = {"id", "Level", "Data_material", "Material_Number_Desc", "New_Version"};
        AllTableServiceImpl goodsService = new AllTableServiceImpl();
        Vector<Vector> vector = new Vector<Vector>();
        try {
            vector = goodsService.EMCSelectByCondition();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //打印出所有元组
//        for (Vector x: vector){
//            for (Object s: x)
//                System.out.print("    "+s);
//          System.out.println();
//        }

        baseTableModule = new BaseTableModule(params, vector);
        table = new JTable(baseTableModule);

        Tools.setTableStyle(table);
        //       DefaultTableColumnModel dcm = (DefaultTableColumnModel) table.getColumnModel();// 获取列模型
//        dcm.getColumn(0).setMinWidth(0); // 将第一列的最小宽度、最大宽度都设置为0
//        dcm.getColumn(0).setMaxWidth(0);

        jScrollPane = new JScrollPane(table);
        Tools.setJspStyle(jScrollPane);
        tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        tablePanel.add(jScrollPane);
        backgroundPanel.add(tablePanel, "Center");

        ResultText = new JTextArea();
        //ResultText.setPreferredSize(new Dimension(500, 30));
        ResultText.setFont(new Font("标楷体", Font.TRUETYPE_FONT | Font.ITALIC, 18));
        ResultText.setLineWrap(true);
        ResultText.setForeground(Color.BLUE);
        backgroundPanel.add(ResultText, "South");
    }


    // 更新数据表格
    public void refreshTablePanel() {
        backgroundPanel.remove(tablePanel);
        String params[] = {"id", "Level", "Data_material",
                "Material_Number_Desc", "New_Version"};
        AllTableServiceImpl emcService = new AllTableServiceImpl();
        Vector<Vector> vector = new Vector<Vector>();
        try {
            vector = emcService.EMCSelectByCondition();
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
                new ModifyEMCJFrame(this, table, row);
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
                    AllTableServiceImpl goodsService = new AllTableServiceImpl();
                    try {
                        int tempresult = goodsService.EMCdeleteById(params);
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
            boolean isSaveSuccess = fileOperation.SaveAs("EMC");
            if (isSaveSuccess)
                JOptionPane.showOptionDialog(this.backgroundPanel, "Export this table successfully!",
                        "Tips", JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE, null, null, null);
        }else if (e.getSource() == tool_add) {
            new AddEMCJFrame(this);
        }
    }

    // 鼠标划入事件
    @Override
    public void mouseEntered(MouseEvent e) {
        if (e.getSource() == tool_modify) {
            tool_modify.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        } else if (e.getSource() == tool_delete) {
            tool_delete.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        } else if (e.getSource() == tool_add) {
            tool_delete.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == QButton) {
            String inputNumber = NumberText.getText().trim();
            ResultText.setText("");
            List<Integer> IdList = null;
            if ((inputNumber.equals(tips)) || (inputNumber.equals("")))
                ResultText.setText("       Sorry, please input a PathNumber.");
            else {
                IdList = MarkId(inputNumber);
            }
            //setOneRowBackgroundColor(table,12,Color.red);
            //tablePanel.updateUI();
            setTableStyle(table, IdList);
        }
    }

    public List<Integer> MarkId(String number) {
        StringBuilder sqlBuilder = new StringBuilder(
                "select id from EMC_MAKE_BUY WHERE EMC_MAKE_BUY.Data_material='" + number + "'");
        String sql = sqlBuilder.toString();
        List<Object[]> list = null;
        try {
            list = select(sql, 1, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String result = "";
        List IDList = new ArrayList();
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size() - 1; i++) {
                result = result + list.get(i)[0] + ", ";
                IDList.add(list.get(i)[0]);
            }
            result = result + list.get(list.size() - 1)[0];
            IDList.add(list.get(list.size() - 1)[0]);
            ResultText.setText("        ID:  " + result);
        }
        return IDList;
    }


    /**
     * 设置表格的某一行的背景色
     *
     * @param table
     */
    public static void setOneRowBackgroundColor(JTable table, int rowIndex,
                                                Color color) {
        try {
            DefaultTableCellRenderer tcr = new DefaultTableCellRenderer() {

                public Component getTableCellRendererComponent(JTable table,
                                                               Object value, boolean isSelected, boolean hasFocus,
                                                               int row, int column) {
                    if (row == rowIndex) {
                        setBackground(color);
                        setForeground(Color.BLACK);
                    } else if (row > rowIndex) {
                        setBackground(Color.WHITE);
                        setForeground(Color.BLACK);
                    } else {
                        setBackground(Color.WHITE);
                        setForeground(Color.BLACK);
                    }

                    return super.getTableCellRendererComponent(table, value,
                            isSelected, hasFocus, row, column);
                }
            };

            int columnCount = table.getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                table.getColumn(table.getColumnName(i)).setCellRenderer(tcr);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    // 设置Table样式
    @SuppressWarnings("static-access")
    public static void setTableStyle(JTable jtb, List<Integer> idlist) {
        // 设置选中项背景
        jtb.setSelectionBackground(new Color(51, 153, 255));
        // 设置行高
        jtb.setRowHeight(40);
        // 设置表格可排序
        jtb.setAutoCreateRowSorter(true);
        // 设置表格表头内容居中
        DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) jtb
                .getTableHeader().getDefaultRenderer();
        renderer.setHorizontalAlignment(renderer.CENTER);
        // 设置表格单元格内容居中
        DefaultTableCellRenderer r = new DefaultTableCellRenderer() {

            public Component getTableCellRendererComponent(JTable table,
                                                           Object value, boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                if (idlist.contains(row + 1)) {
                    setBackground(Color.red);
                    setForeground(Color.BLACK);
                } else {
                    setBackground(Color.WHITE);
                    setForeground(Color.BLACK);
                }

                return super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);
            }
        };

        r.setHorizontalAlignment(JLabel.CENTER);
        jtb.setDefaultRenderer(Object.class, r);

        jtb.setFont(MyFont.Static);
        jtb.setCellSelectionEnabled(true);//设置表格可复制
        fitTableColumns(jtb);
    }

}

