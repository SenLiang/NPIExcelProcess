package org.lqz.module.view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.lqz.module.dao.Impl.excel_import_export.exitWriteFlag;


public class Progress {
    Timer timer;
    // 任务的当前完成量
    public static  int current=0;
    // 总任务量
    public static int amount=0;
    public void setAmount(int amount) {
        this.amount=amount;
    }
    public void setCurrent(int current) {
        this.current=current;
    }
    public int getAmount() {
        return this.amount;
    }
    public int getCurrent() {
        return this.current;
    }

    public String getPercent() {
        return String.format("%.2f", 100.0 * current / amount) + "%";
    }
    public void init(IndexJFrame parentJFrame,String readFileName) {
        // 创建进度对话框
        final ProgressMonitor dialog = new ProgressMonitor(parentJFrame,
                "Executing load "+readFileName+", please wait  patiently or cancel the task...", "Have completed: 0.00%", 0,
                getAmount());
        // 创建一个计时器
        timer = new Timer(100, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 以任务的当前完成量设置进度对话框的完成比例
                dialog.setProgress(getCurrent());
                dialog.setNote("Have completed: " + current+"/" +amount+"  "+getPercent());
                //System.out.println("getAmount--------" + getAmount()+"---getCurrent-------" + getCurrent());
                if (getAmount() == getCurrent()) {
                   // System.out.println("Game is over!");
                    Object[] options = {"OK"};
                    int response = JOptionPane.showOptionDialog(parentJFrame, "Import this table "+readFileName+" successfully!",
                            "Tips", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                    // 停止计时器
                    timer.stop();
                    // 中断任务的执行线程
                    parentJFrame.setEnabled(true);
                }

                // 如果用户单击了进度对话框的”取消“按钮
                if (dialog.isCanceled()) {
                    // 停止计时器
                    timer.stop();
                    // 中断任务的执行线程
                    // 系统退出
                   // System.exit(0);
                    exitWriteFlag=1;
                    parentJFrame.setEnabled(true);
                }
            }
        });
        timer.start();
    }
}