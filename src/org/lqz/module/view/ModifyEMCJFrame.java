package org.lqz.module.view;

import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;
import org.lqz.framework.util.Item;
import org.lqz.framework.util.MyFont;
import org.lqz.module.dao.Impl.BaseDaoImpl;
import org.lqz.module.services.Impl.AllTableServiceImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;

public class ModifyEMCJFrame extends JFrame implements MouseListener {

    // 定义全局组件
    JPanel backgroundPanel, labelPanel, contentPanel, buttonPanel;
    JLabel label_id, label_Level, label_Data_material, label_Material_Number_Desc, label_New_Version;
    JTextField id, Level, Data_material, Material_Number_Desc, New_Version;
    JButton button_modify;

    // 获得屏幕的大小
    final static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
    final static int height = Toolkit.getDefaultToolkit().getScreenSize().height;

    // 表格对象
    JTable table;
    int selectedRow;
    EMCDBJPanel parentPanel;

    public ModifyEMCJFrame(EMCDBJPanel parentPanel, JTable table, int selectedRow) {
        this.table = table;
        this.selectedRow = selectedRow;
        this.parentPanel = parentPanel;

        initBackgroundPanel();

        this.add(backgroundPanel);

        this.setTitle("修改元组");
        this.setSize(600, 450);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    // 初始化背景面板
    public void initBackgroundPanel() {
        backgroundPanel = new JPanel(new BorderLayout());

        initLabelPanel();
        initContentPanel();
        initButtonPanel();

        backgroundPanel.add(labelPanel, "North");
        backgroundPanel.add(contentPanel, "Center");
        backgroundPanel.add(buttonPanel, "South");
    }

    // 初始化label面板
    public void initLabelPanel() {

        labelPanel = new JPanel();
        JLabel title = new JLabel("EMC信息");
        title.setFont(MyFont.Static);
        labelPanel.add(title);
    }

    // 初始化商品信息面板
    public void initContentPanel() {
        contentPanel = new JPanel(new GridLayout(5, 2));
        //label_id = new JLabel("id", JLabel.CENTER);
        label_Level = new JLabel("Level", JLabel.CENTER);
        label_Data_material = new JLabel("Data_material", JLabel.CENTER);
        label_Material_Number_Desc = new JLabel("Material_Number_Desc", JLabel.CENTER);
        label_New_Version = new JLabel("New_Version", JLabel.CENTER);

        //id = new JTextField(String.valueOf(table.getValueAt(selectedRow, 0)));
        Level = new JTextField((String) table.getValueAt(selectedRow, 1));
        Data_material = new JTextField((String) table.getValueAt(selectedRow, 2));
        Material_Number_Desc = new JTextField((String) table.getValueAt(selectedRow, 3));
        New_Version = new JTextField((String) table.getValueAt(selectedRow, 4));
//        contentPanel.add(label_id);
//        contentPanel.add(id);
        contentPanel.add(label_Level);
        contentPanel.add(Level);
        contentPanel.add(label_Data_material);
        contentPanel.add(Data_material);
        contentPanel.add(label_Material_Number_Desc);
        contentPanel.add(Material_Number_Desc);
        contentPanel.add(label_New_Version);
        contentPanel.add(New_Version);
    }

    // 初始化按钮面板
    public void initButtonPanel() {
        buttonPanel = new JPanel();
        button_modify = new JButton("保存修改");
        button_modify.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
        button_modify.setForeground(Color.white);
        button_modify.setFont(MyFont.Static);
        button_modify.addMouseListener(this);
        buttonPanel.add(button_modify);
    }

    // 鼠标点击事件
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == button_modify) {
            //String id_String = id.getText().trim();
            String Level_String = Level.getText().trim();
            String Data_material_String = Data_material.getText().trim();
            String Material_Number_Desc_String = Material_Number_Desc.getText().trim();
            String New_Version_String = New_Version.getText().trim();
//            if (id_String .isEmpty()) {
//                JOptionPane.showMessageDialog(null, "请输入id");
//            } else
            if (Level_String.isEmpty()) {
                JOptionPane.showMessageDialog(null, "请输入Level");
            } else if (Data_material_String.isEmpty()) {
                JOptionPane.showMessageDialog(null, "请输入Number");
            } else if (Material_Number_Desc_String.isEmpty()) {
                JOptionPane.showMessageDialog(null, "请输入Part_Class");
            } else if (New_Version_String.isEmpty()) {
                JOptionPane.showMessageDialog(null, "请输入Description");
            } else {
                int result = 0;
                //int id_int = Integer.valueOf(id_String);
                String selecteid = String.valueOf(table.getValueAt(selectedRow, 0));
                Object[] params = {Level_String, Data_material_String, Material_Number_Desc_String, New_Version_String, selecteid};//5
                AllTableServiceImpl goodsService = new AllTableServiceImpl();
                try {
                    result = goodsService.EMCupdateById(params);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                if (result > 0) {
                    JOptionPane.showMessageDialog(null, "商品修改成功");
                    this.setVisible(false);
                    parentPanel.refreshTablePanel();
                }
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

}
