package org.lqz.module.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;
import org.lqz.framework.util.Item;
import org.lqz.framework.util.MyFont;
import org.lqz.module.services.Impl.AllTableServiceImpl;

public class ModifyAgileBomJFrame extends JFrame implements MouseListener {

    // 定义全局组件
    JPanel backgroundPanel, labelPanel, contentPanel, buttonPanel;
    JLabel  label_id , label_Level, label_Number,  label_Description;
    JTextField  id,Level, Number,Description;
    JButton button_modify;

    // 表格对象
    JTable table;
    int selectedRow;
    AgileBOMDBJPanel parentPanel;

    public ModifyAgileBomJFrame(AgileBOMDBJPanel parentPanel, JTable table, int selectedRow) {
        this.table = table;
        this.selectedRow = selectedRow;
        this.parentPanel = parentPanel;

        initBackgroundPanel();
        this.add(backgroundPanel);
        this.setTitle("修改元组");
        this.setSize(600, 360);
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

        JLabel title = new JLabel("AgileBom信息");
        title.setFont(MyFont.Static);

        labelPanel.add(title);
    }

    // 初始化商品信息面板
    public void initContentPanel() {
        contentPanel = new JPanel(new GridLayout(4, 2));

        //String params[] = {  "id", "Level","Number","Description"};
       // label_id = new JLabel("id", JLabel.CENTER);
        label_Level = new JLabel("Level", JLabel.CENTER);
        label_Number = new JLabel("Number", JLabel.CENTER);
        label_Description = new JLabel("Description", JLabel.CENTER);
       // id = new JTextField(String.valueOf(table.getValueAt(selectedRow, 0)));
        Level = new JTextField((String) table.getValueAt(selectedRow, 1));
        Number = new JTextField((String) table.getValueAt(selectedRow, 2));
        Description = new JTextField((String) table.getValueAt(selectedRow, 3));

//        contentPanel.add(label_id);
//        contentPanel.add(id);
        contentPanel.add(label_Level);
        contentPanel.add(Level);
        contentPanel.add(label_Number);
        contentPanel.add(Number);
        contentPanel.add(label_Description);
        contentPanel.add(Description);
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
            String Number_String = Number.getText().trim();
            String Description_String = Description.getText().trim();
//            if (id_String .isEmpty()) {
//                JOptionPane.showMessageDialog(null, "请输入id");
//            } else
                if (Level_String.isEmpty()) {
                JOptionPane.showMessageDialog(null, "请输入Level");
            } else if (Number_String.isEmpty()) {
                JOptionPane.showMessageDialog(null, "请输入Number");
            } else if (Description_String.isEmpty()) {
                JOptionPane.showMessageDialog(null, "请输入Description");
            } else {
                int result = 0;
                //int id_int = Integer.valueOf(id_String);
                String selecteid = String.valueOf(table.getValueAt(selectedRow, 0)) ;

                Object[] params = {  Level_String, Number_String, Description_String,selecteid};//5
                AllTableServiceImpl agileService = new AllTableServiceImpl();
                try {

                    result = agileService.AgileBomupdateById(params);
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
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
