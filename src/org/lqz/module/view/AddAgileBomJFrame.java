package org.lqz.module.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.UUID;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;
import org.lqz.framework.util.Item;
import org.lqz.framework.util.MyFont;
import org.lqz.module.services.Impl.AllTableServiceImpl;

public class AddAgileBomJFrame extends JFrame implements MouseListener {

    // 定义全局组件
    JPanel backgroundPanel, labelPanel, contentPanel, buttonPanel;
    JLabel  label_id ,
            label_Level, label_Number, label_Part_Class , label_Description,
            label_Part_Type, label_PPlatform_Affected, label_Design_Type, label_Rev,
            label_warehouse, label_category;
    JTextField  id ,
            Level, Number, Part_Class ,Description,
            Part_Type, PPlatform_Affected, Design_Type, Rev;
    JComboBox warehouse, category;
    JButton button_add;

    // 获得屏幕的大小
    final static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
    final static int height = Toolkit.getDefaultToolkit().getScreenSize().height;

    // 父面板对象
    AgileBOMDBJPanel parentPanel;

    public AddAgileBomJFrame(AgileBOMDBJPanel parentPanel) {
        this.parentPanel = parentPanel;

        initBackgroundPanel();

        this.add(backgroundPanel);

        this.setTitle("添加元组");
        this.setSize(800, 550);
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
        contentPanel = new JPanel(new GridLayout(11, 2));


        label_id = new JLabel("id", JLabel.CENTER);
        label_Level = new JLabel("Level", JLabel.CENTER);
        label_Number = new JLabel("Number", JLabel.CENTER);
        label_Part_Class = new JLabel("Part_Class", JLabel.CENTER);
        label_Description = new JLabel("Description", JLabel.CENTER);
        label_Part_Type = new JLabel("Part_Type", JLabel.CENTER);
        label_PPlatform_Affected= new JLabel("PPlatform_Affected", JLabel.CENTER);
        label_Design_Type = new JLabel("Design_Type", JLabel.CENTER);
        label_Rev = new JLabel("Rev", JLabel.CENTER);
        label_warehouse = new JLabel("所属分类", JLabel.CENTER);
        label_category = new JLabel("所属层级", JLabel.CENTER);

        id = new JTextField("");
        Level = new JTextField("");
        Number =new JTextField("");
        Part_Class = new JTextField("");
        Description = new JTextField("");
        Part_Type =new JTextField("");
        PPlatform_Affected = new JTextField("");
        Design_Type =new JTextField("");
        Rev = new JTextField("");

        // 商品种类下拉框
        category = new JComboBox();
        category.addItem(new Item("Level1", "Level1"));
        category.addItem(new Item("Level2", "Level3"));
        category.addItem(new Item("Level3", "Level3"));
        category.addItem(new Item("Level4", "Level4"));
        category.addItem(new Item("Level5", "Level5"));
        // 设置所选商品种类为默认种类
        category.setSelectedIndex(0);

        // 仓库下拉框
        warehouse = new JComboBox();
        warehouse.addItem(new Item("Buy", "Buy"));
        warehouse.addItem(new Item("Make", "Make"));
        warehouse.setSelectedIndex(0);

        contentPanel.add(label_id);
        contentPanel.add(id);
        contentPanel.add(label_Level);
        contentPanel.add(Level);
        contentPanel.add(label_Number);
        contentPanel.add(Number);
        contentPanel.add(label_Part_Class);
        contentPanel.add(Part_Class);
        contentPanel.add(label_Part_Class);
        contentPanel.add(Part_Class);
        contentPanel.add(label_Description);
        contentPanel.add(Description);
        contentPanel.add(label_Part_Type);
        contentPanel.add(Part_Type);
        contentPanel.add(label_PPlatform_Affected);
        contentPanel.add(PPlatform_Affected);
        contentPanel.add(label_Design_Type);
        contentPanel.add(Design_Type);
        contentPanel.add(label_Rev);
        contentPanel.add(Rev);


        contentPanel.add(label_category);
        contentPanel.add(category);

        contentPanel.add(label_warehouse);
        contentPanel.add(warehouse);
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

            String id_String = id.getText().trim();
            String Level_String = Level.getText().trim();  String Number_String = Number.getText().trim();
            String Part_Class_String = Part_Class.getText().trim();  String Description_String = Description.getText().trim();
            String Part_Type_String = Part_Type.getText().trim();  String PPlatform_Affected_String = PPlatform_Affected.getText().trim();
            String Design_Type_String = Design_Type.getText().trim();  String Rev_String = Rev.getText().trim();

            if (id_String .isEmpty()) {
                JOptionPane.showMessageDialog(null, "请输入id");
            } else if (Level_String.isEmpty()) {
                JOptionPane.showMessageDialog(null, "请输入Level");
            } else if (Number_String.isEmpty()) {
                JOptionPane.showMessageDialog(null, "请输入Number");
            } else if (Part_Class_String.isEmpty()) {
                JOptionPane.showMessageDialog(null, "请输入Part_Class");
            }  else if (Description_String.isEmpty()) {
                JOptionPane.showMessageDialog(null, "请输入Description");
            } else if (Part_Type_String.isEmpty()) {
                JOptionPane.showMessageDialog(null, "请输入Part_Type");
            }else if (PPlatform_Affected_String.isEmpty()) {
                JOptionPane.showMessageDialog(null, "请输入PPlatform_Affected");
            }  else if (Design_Type_String.isEmpty()) {
                JOptionPane.showMessageDialog(null, "请输入Design_Type");
            } else if (Rev_String.isEmpty()) {
                JOptionPane.showMessageDialog(null, "请输入Rev");
            }else {
                int result = 0;
                Object[] params = {
                        Level_String, Number_String, Part_Class_String, Description_String,
                        Part_Type_String, PPlatform_Affected_String, Design_Type_String, Rev_String};//12
                AllTableServiceImpl goodsService = new AllTableServiceImpl();
                try {
                    result = goodsService.AgileBominsertById(params);
                } catch (Exception e1) {
                    e1.printStackTrace();
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
