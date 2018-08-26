package org.lqz.module.view;

import org.lqz.module.dao.Impl.BaseDaoImpl;
import org.lqz.module.services.Impl.AllTableServiceImpl;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import static org.lqz.module.view.IndexJFrame.creatHome;

public class MyMouseListener implements MouseListener {

    private IndexJFrame frame;
    private FileOperation fileOperation;

    public MyMouseListener(IndexJFrame f) {
        this.frame = f;
        fileOperation = new FileOperation(f);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        String btnName = ((JButton) e.getSource()).getText();
        if ("Import AgileBom".equals(btnName)) {//.equals(btnName.trim())
            //System.out.println("你点击了按钮...i_AB_Btn");
            if (fileOperation.open()) {
                fileOperation.importToSQL("AgileBom");
            }
        } else if ("Import EMC".equals(btnName)) {
            if (fileOperation.open()) {
                fileOperation.importToSQL("EMC");
            }
        } else if ("Import ModelList".equals(btnName)) {
            if (fileOperation.open()) {
                fileOperation.importToSQL("ModelList");
            }
        }else if ("Clear All Data".equals(btnName)) {
            Object[] options = {"Yes", "No"};
            int response = JOptionPane.showOptionDialog(this.frame, "Delete is not recoverable, are you sure to delete?",
                    "Tips", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (response == 0) {//Yes
                AllTableServiceImpl tableService = new AllTableServiceImpl();
                ArrayList<String> ModelName = new ArrayList<String>();
                try {
                    tableService.deleteTableByName("Agile_BOM");
                    tableService.deleteTableByName("EMC_MAKE_BUY");
                    tableService.deleteTableByName("Agile_EMC");
                    ModelName = tableService.TableNameSelectList();
                    for (int i = 0; i < ModelName.size(); i++) {
                        tableService.deleteTableByName(ModelName.get(i));
                    }
                }catch (Exception e1) {
                    e1.printStackTrace();
                }
                creatHome();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
//        JButton btn = (JButton) e.getComponent();
//        Border etchedBorder = new EtchedBorder(EtchedBorder.RAISED);// 设置边框凸显
//        btn.setBorder(etchedBorder);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
//         //TODO Auto-generated method stub
//        JButton btn = (JButton) e.getComponent();
//        Border etchedBorder = new EtchedBorder(EtchedBorder.LOWERED);// 设置边框凹显
//        btn.setBorder(etchedBorder);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        JButton btn = (JButton) e.getComponent();
        btn.setForeground(new Color(255, 255, 255));// 设置字体颜色
        btn.setBorderPainted(true);// 显示边框
        Border etchedBorder = new EtchedBorder(EtchedBorder.LOWERED);// 设置边框凹显
        btn.setBorder(etchedBorder);
        btn.setRolloverEnabled(true);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        JButton btn = (JButton) e.getComponent();
        btn.setForeground(Color.YELLOW);// 设置字体颜色
        btn.setBorderPainted(false);// 隐藏边框
    }

}
