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
import java.util.UUID;

public class AddEMCJFrame extends JFrame implements MouseListener {

    // 定义全局组件
    JPanel backgroundPanel, labelPanel, contentPanel, buttonPanel;
    JLabel label_Level, label_Number,  label_Description, label_Version;
    JTextField  Level, Number, Description, Version;
    JButton button_add;

    // 获得屏幕的大小
    final static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
    final static int height = Toolkit.getDefaultToolkit().getScreenSize().height;

    // 父面板对象
    EMCDBJPanel parentPanel;
    public AddEMCJFrame(EMCDBJPanel parentPanel) {
        this.parentPanel = parentPanel;
        initBackgroundPanel();
        this.add(backgroundPanel);
        this.setTitle("添加元组");
        this.setSize(600, 450);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    // 初始化背景面板
    public void initBackgroundPanel() {
        backgroundPanel = new JPanel(new BorderLayout());

        initContentPanel();
        initButtonPanel();
        initLabelPanel();

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
        contentPanel = new JPanel(new GridLayout(4,2));
        label_Level = new JLabel("Level", JLabel.CENTER);
        label_Number = new JLabel("Number", JLabel.CENTER);
        label_Description = new JLabel("Description", JLabel.CENTER);
        label_Version = new JLabel("Part_Type", JLabel.CENTER);

        Level = new JTextField("");
        Number =new JTextField("");
        Description = new JTextField("");
        Version = new JTextField("");

        contentPanel.add(label_Level);
        contentPanel.add(Level);
        contentPanel.add(label_Number);
        contentPanel.add(Number);
        contentPanel.add(label_Description);
        contentPanel.add(Description);
        contentPanel.add(label_Version);
        contentPanel.add(Version);
    }

    // 初始化按钮面板
    public void initButtonPanel() {
        buttonPanel = new JPanel();
        button_add = new JButton("保存");
        button_add.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
        button_add.setForeground(Color.white);
        button_add.setFont(MyFont.Static);
        button_add.addMouseListener(this);
        buttonPanel.add(button_add);
    }

    // 鼠标点击事件
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == button_add) {
            String Level_String = Level.getText().trim();  String Number_String = Number.getText().trim();
            String Description_String = Description.getText().trim();  String Version_String = Version.getText().trim();
            if (Level_String.isEmpty()) {
                JOptionPane.showMessageDialog(null, "请输入Level");
            } else if (Number_String.isEmpty()) {
                JOptionPane.showMessageDialog(null, "请输入Number");
            } else if (Description_String.isEmpty()) {
                JOptionPane.showMessageDialog(null, "请输入Description");
            } else if (Version_String.isEmpty()) {
                JOptionPane.showMessageDialog(null, "请输入Version");
            } else {
                int result = 0;
                Object[] params = {Level_String, Number_String, Description_String,Version_String};//12
                AllTableServiceImpl goodsService = new AllTableServiceImpl();
                try {
                    result = goodsService.EMCaddById(params);
                } catch (Exception e1) {
                    e1.printStackTrace();
                } finally {
                    AgileEMCJPanel.hasAgileEMCTable = false;//重新加载表格
                }
                if (result > 0) {
                    JOptionPane.showMessageDialog(null, "元组添加成功");
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
