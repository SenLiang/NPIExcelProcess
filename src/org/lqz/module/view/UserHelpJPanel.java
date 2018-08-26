package org.lqz.module.view;

import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;
import org.lqz.framework.util.ImagePanel;
import org.lqz.framework.util.MyFont;
import org.lqz.main.Entrance;
import org.lqz.module.entity.User;
import org.lqz.module.services.Impl.UserServiceImpl;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * 用户管理功能面板
 */
public class UserHelpJPanel{

    // 定义全局组件
    ImagePanel backgroundPanel;
    JPanel contentPanel,textPanel;

    // 定义大小变量
    int width;
    int height;

    // 定义用户对象
    User user = null;
    JFrame jframe = null;

    public UserHelpJPanel(User user, JFrame jframe) {
        this.user = user;
        this.jframe = jframe;

        Image userIcon = new ImageIcon(IndexJFrame.class.getResource("/image/userbackground.jpg")).getImage();
        //Image bgimg = ImageIO.read(new File("image/userbackground.jpg"));
        backgroundPanel = new ImagePanel(userIcon);
        // 获取背景面板大小
        this.width = backgroundPanel.getWidth();
        this.height = backgroundPanel.getHeight();
        initContentPanel();
    }

    public void initContentPanel() {
        backgroundPanel.removeAll();
        JTextPane ctext = new JTextPane();// 控制台窗格
        ctext.setEditable(false);
        ctext.setOpaque(false);
        insertDocument(ctext, "NPI Excel Processing System\n\n", Color.BLUE, 3,25);
        insertDocument(ctext, "Version:    1.0.0  (Beta Version)\n\n" +
                "Developer:    LiangSen & XuBob\n\n" +
                "E-Mail:    Sen_Liang@Dell.com\n\n" +
                "               Bob_Xu@Dell.com\n" +
                "Useage:\n" +
                "•\tImport Excel：Agile Bom、ECM Buy/Make file、EMC Isilon Model list\n" +
                "•\tQuery Dell Modlist with qty(L3)\n" +
                "•\tQuery buy part list with qty(L3->L6 Buy) for material checking purpose\n" +
                "•\tQuery Buy/Make part list and relationship with qty(L3->L5 Make+ L3->L6 buy) for assembly build purpose\n" +
                "•\tBuy/Make(Query Bom)\n" +
                "•\tBom up (related make part/mods/models)\n" +
                "•\tBom down (buy/make child and relationship of buy/make child of child…)\n\n", Color.BLACK, 2,20);
        insertDocument(ctext, "Note：this software is only available for internal use in NPI, " +
                "any comments or suggestions, please email the developer\n", Color.red, 4,18);
        backgroundPanel.add(ctext, BorderLayout.CENTER);
    }
    public static void insertDocument(JTextPane JTP, String str, Color textColor, int setFont,int FrontSize)// 根据传入的颜色及文字，将文字插入控制台
    {
        SimpleAttributeSet set = new SimpleAttributeSet();
        StyleConstants.setForeground(set, textColor);// 设置文字颜色
        StyleConstants.setFontSize(set, FrontSize);// 设置字体大小
        //StyleConstants.setAlignment(set, StyleConstants.ALIGN_CENTER);居中对齐
        switch (setFont) {
            case 1://正常输出
                StyleConstants.setFontFamily(set, "新宋体");
            case 2://提示，警告，异常
                StyleConstants.setFontFamily(set, "标楷体");
            case 3://错误提示
                StyleConstants.setFontFamily(set, "华文行楷");
            default:
                StyleConstants.setFontFamily(set, "微软雅黑");
        }
        Document doc = JTP.getDocument();
        try {
            doc.insertString(doc.getLength(), str, set);// 插入文字
        } catch (BadLocationException e) {
        }
    }

}
