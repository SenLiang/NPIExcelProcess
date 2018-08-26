package org.lqz.module.view;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;

import org.lqz.framework.util.ImagePanel;
import org.lqz.framework.util.MyFont;
import org.lqz.framework.util.WindowOpacity;
import org.lqz.module.dao.Impl.BaseDaoImpl;
import org.lqz.module.entity.User;
import org.lqz.module.services.Impl.AllTableServiceImpl;

import static org.lqz.module.dao.Impl.BaseDaoImpl.validateTableListExist;
import static org.lqz.module.dao.Impl.BaseDaoImpl.validateTableNameExist;


public class IndexJFrame extends JFrame implements MouseListener, ActionListener {

    // 定义用户对象
    private User user;
    // 定义辅助变量
    int sign_home = 0;
    int sign_baseData = 0;
    int sign_showTree = 0;
    int sign_searchNodeDetail = 0;
    int sign_model_list = 0;
    int sign_config_bom = 0;
    int sign_userManager = 0;


    // 获得屏幕的大小
    final static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
    final static int height = Toolkit.getDefaultToolkit().getScreenSize().height;
    // 定义全局组件
    JPanel backgroundPanel;
    JPanel topPanel;
    JPanel topMenu;
    static JPanel centerPanel;
    JTabbedPane jTabbedPane;
    JLabel home, baseData, showTree, searchNodeDetail, model_list, config_bom, userManager;
    public JButton i_AB_Btn, i_BM_Btn, i_ML_Btn, saveBtn;


    public IndexJFrame() {
        //窗口淡入淡出
        new WindowOpacity(this);

        // 设置tab面板缩进
        UIManager.put("TabbedPane.tabAreaInsets", new javax.swing.plaf.InsetsUIResource(0, 0, 0, 0));


        Image logoIcon = new ImageIcon(IndexJFrame.class.getResource("/image/dell.png")).getImage();
        //Image imgae = ImageIO.read(new File("image/logo.png"));
        this.setIconImage(logoIcon);

        initBackgroundPanel();

        this.setTitle("  NPI Excel Processing System");
        this.setSize((int) (width * 0.8f), (int) (height * 0.8f));
        this.setVisible(true);
        this.setLocationRelativeTo(null);    // 此窗口将置于屏幕的中央。
        //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            // 按下窗口关闭钮事件处理
            public void windowClosing(WindowEvent e) {
                Object[] options = {"Exit", "Cancel"};
                int response = JOptionPane.showOptionDialog(null, "Do you want to exit NPI Excel Processing System?",
                        "Confirm Exit", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (response == 0) {
                    System.exit(0);
                }
            }
        });
    }

    // 初始化背景面板
    public void initBackgroundPanel() {

        backgroundPanel = new JPanel(new BorderLayout());
        initTop();
        initCenterPanel();

        backgroundPanel.add(topPanel, "North");
        backgroundPanel.add(centerPanel, "Center");

        this.add(backgroundPanel);
    }

    // 初始化顶部顶部面板
    public void initTop() {
        initTopMenu();
        topPanel = new JPanel(new BorderLayout());
        topPanel.setPreferredSize(new Dimension(width, 40));
        topPanel.add(topMenu, "West");
    }

    // 初始化顶部菜单
    public void initTopMenu() {

        topMenu = new JPanel(new FlowLayout());
        //topMenu.setPreferredSize(new Dimension(900, 40));
        topMenu.setOpaque(false);

        String[] nameStrings = {"Home", "Agile_BOM_DB", "Agile_BOM_Tree", "Node_Detail", "Model_List", "Config_BOM", "Help"};
        home = CreateMenuLabel(home, nameStrings[0], "home", topMenu);
        home.setName("home");

        baseData = CreateMenuLabel(baseData, nameStrings[1], "baseData", topMenu);
        baseData.setName("baseData");

        showTree = CreateMenuLabel(showTree, nameStrings[2], "showTree", topMenu);
        showTree.setName("showTree");

        searchNodeDetail = CreateMenuLabel(searchNodeDetail, nameStrings[3], "Node_Detail", topMenu);
        searchNodeDetail.setName("Node_Detail");

        model_list = CreateMenuLabel(model_list, nameStrings[4], "Model_List", topMenu);
        model_list.setName("Model_List");

        config_bom = CreateMenuLabel(model_list, nameStrings[5], "Config_BOM", topMenu);
        config_bom.setName("Config_BOM");

        userManager = CreateMenuLabel(userManager, nameStrings[6], "Help", topMenu);
        userManager.setName("userManager");
    }

    // 创建顶部菜单Label
    public JLabel CreateMenuLabel(JLabel jlabel, String text, String name, JPanel jpanel) {
        //添加一横线
        JLabel line = new JLabel("<html>&nbsp;<font color='#D2D2D2'>|</font>&nbsp;</html>");
        Icon icon = new ImageIcon(IndexJFrame.class.getResource("/image/" + name + ".png"));
        //Icon icon = new ImageIcon("image/" + name + ".png");
        jlabel = new JLabel(icon);
        jlabel.setText("<html><font color='black'>" + text + "</font>&nbsp;</html>");
        jlabel.addMouseListener(this);
        jlabel.setFont(MyFont.Static);
        jpanel.add(jlabel);
        if (!"Help".equals(name)) {
            jpanel.add(line);
        }
        return jlabel;
    }



    // 初始化中心面板
    public void initCenterPanel() {
        centerPanel = new JPanel(new BorderLayout());
        // "首页"两字变为蓝色
        home.setText("<html><font color='#336699' style='font-weight:bold'>" + "Home" + "</font>&nbsp;</html>");
        creatSecondHome();
        creatHome();
        centerPanel.setOpaque(false);// 设置控件透明
    }

    // 初始化辅助变量
    public void initSign() {
        sign_home = 0;
        sign_baseData = 0;
        sign_showTree = 0;
        sign_searchNodeDetail = 0;
        sign_model_list = 0;
        sign_config_bom = 0;
        sign_userManager = 0;
    }

    // 创建首页面板
    public static void creatHome() {
        centerPanel.removeAll();
        if (centerBackground.getComponentCount() > 1)
            centerBackground.remove(dbPanel);

        centerBackground.add(dbStateJPanel(), BorderLayout.EAST);
        centerPanel.add(centerBackground, "Center");
        centerPanel.updateUI();
    }

    // 创建首页面板
    public static ImagePanel centerBackground;

    public void creatSecondHome() {
        Random rand = new Random();
        int ImageID = rand.nextInt(10) + 1;
        Image img = (new ImageIcon(IndexJFrame.class.getResource("/image/background/indexbackground" + ImageID + ".png"))).getImage();
        //Image bgimg = ImageIO.read(new File("image/indexbackground.png"));
        centerBackground = new ImagePanel(img);
        centerBackground.setLayout(new BorderLayout());
        //i_AB_Btn, i_BM_Btn,o_AB_Btn, o_BM_Btn,saveBtn;
        JMenuBar menuBar = new JMenuBar();
        menuBar.setOpaque(false);
        //menuBar.setLayout(new FlowLayout(40, 4, 0));
        menuBar.setLayout(new GridLayout(1, 4));
        // 增加文件选择按钮 C:\ideaWorkRoom\SalesManagerment\image\Icons
        ImageIcon imAgileIcon = new ImageIcon(IndexJFrame.class.getResource("/image/Icons/imAgile.png"));
        i_AB_Btn = createBtn("Import AgileBom", "/image/Icons/imAgile.png");
        menuBar.add(i_AB_Btn);
        // 增加编辑按钮
        i_BM_Btn = createBtn("Import EMC", "/image/Icons/imemc.png");
        menuBar.add(i_BM_Btn);
        // 增加编辑按钮
        i_ML_Btn = createBtn("Import ModelList", "/image/Icons/immodelist.png");
        menuBar.add(i_ML_Btn);

        // 增加保存文件按钮
        saveBtn = createBtn("Clear All Data", "/image/Icons/importSave.png");
        menuBar.add(saveBtn);
        //menuBar.setPreferredSize(new Dimension(1018, 100));//18,20
        centerBackground.add(menuBar, BorderLayout.NORTH);
    }

    // 创建首页面板
    public static JPanel dbPanel;

    public static JPanel dbStateJPanel() {
        ArrayList<String>  ModelName = new ArrayList<String>();
        AllTableServiceImpl goodsService = new AllTableServiceImpl();
        try {
            ModelName = goodsService.TableNameSelectList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        dbPanel = new JPanel(new GridLayout(ModelName.size()+2, 2));
        String AB_Name = null, EMC_Name = null, ML_Name = null;

            if (validateTableNameExist("Agile_BOM"))
                AB_Name = "/image/Icons/Yes.png";
            else
                AB_Name = "/image/Icons/No.png";
            if (validateTableNameExist("EMC_MAKE_BUY"))
                EMC_Name = "/image/Icons/Yes.png";
            else
                EMC_Name = "/image/Icons/No.png";

        JLabel label_AB = new JLabel(" Agile Bom");
        label_AB.setForeground(Color.WHITE);
        label_AB.setFont(new Font("粗体", Font.PLAIN, 20));
        JLabel label_AB_State = new JLabel();
        Icon ABicon = new ImageIcon(IndexJFrame.class.getResource(AB_Name));
        label_AB_State.setIcon(ABicon);

        JLabel label_EMC = new JLabel("EMC Make/Buy");
        label_EMC.setForeground(Color.WHITE);
        label_EMC.setFont(new Font("粗体", Font.PLAIN, 20));
        JLabel label_EMC_State = new JLabel();
        Icon EMCicon = new ImageIcon(IndexJFrame.class.getResource(EMC_Name));
        label_EMC_State.setIcon(EMCicon);
        dbPanel.add(label_AB);
        dbPanel.add(label_AB_State);
        dbPanel.add(label_EMC);
        dbPanel.add(label_EMC_State);
        for (int i = 0; i < ModelName.size(); i++) {
            JLabel label_ML = new JLabel(ModelName.get(i));
            label_ML.setForeground(Color.WHITE);
            label_ML.setFont(new Font("粗体", Font.PLAIN, 20));
            JLabel label_ML_State = new JLabel();

                if (validateTableNameExist(ModelName.get(i)))
                    ML_Name = "/image/Icons/Yes.png";
                else
                    ML_Name = "/image/Icons/No.png";

            Icon MLicon = new ImageIcon(IndexJFrame.class.getResource(ML_Name));
            label_ML_State.setIcon(MLicon);
            dbPanel.add(label_ML);
            dbPanel.add(label_ML_State);
        }
        dbPanel.setOpaque(false);
        return dbPanel;
    }


    /**
     * 创建工具栏按钮
     *
     * @param text 按钮名称
     * @param icon 按钮图标所在路径
     * @return 返回添加样式和监听器后的按钮
     * @author lqj
     */
    public JButton createBtn(String text, String icon) {
        JButton btn = new JButton(text, new ImageIcon(IndexJFrame.class.getResource(icon)));
        btn.setUI(new BasicButtonUI());// 恢复基本视觉效果
        btn.setPreferredSize(new Dimension(250, 80));// 设置按钮大小
        btn.setForeground(Color.YELLOW);
        btn.setContentAreaFilled(false);// 设置按钮透明
        btn.setFont(new Font("粗体", Font.PLAIN, 20));// 按钮文本样式
        btn.setMargin(new Insets(0, 0, 0, 0));// 按钮内容与边框距离
        btn.addMouseListener(new MyMouseListener(this));
        return btn;
    }

    // 创建基础数据面板
    public void creatBaseDataTab() {
        centerPanel.removeAll();
        // 设置tab标题位置
        jTabbedPane = new JTabbedPane(JTabbedPane.TOP);
        // 设置tab布局
        jTabbedPane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
        jTabbedPane.setFont(MyFont.Static);

        AgileBOMDBJPanel ABJPanel = new AgileBOMDBJPanel();
        ABJPanel.setJframe(this);
        jTabbedPane.addTab("Agile_BOM", ABJPanel.backgroundPanel);
        jTabbedPane.setForegroundAt(0, Color.ORANGE);

        EMCDBJPanel EMCJPanel = new EMCDBJPanel();
        EMCJPanel.setJframe(this);
        jTabbedPane.addTab("EMC_MAKE_BUY", EMCJPanel.backgroundPanel);

         AgileEMCJPanel AEJPanel = new AgileEMCJPanel();
         AEJPanel.setJframe(this);
         jTabbedPane.addTab("AgileEMC", AEJPanel.backgroundPanel);

        //jTabbedPane.addMouseListener(new MyMouseListener(this));
        jTabbedPane.addChangeListener(new MyStateListener(this));
        centerPanel.add(jTabbedPane, "Center");
    }

    // 创建数据树状展示面板
    public void creatshowTreeTab() {
        centerPanel.removeAll();
        // 设置tab标题位置
        jTabbedPane = new JTabbedPane(JTabbedPane.TOP);
        // 设置tab布局
        jTabbedPane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
        jTabbedPane.setFont(MyFont.Static);

        jTabbedPane.addTab("Include N/A", new ShowIncludeNTreeJPanel(this).backgroundPanel);
        jTabbedPane.setForegroundAt(0, Color.RED);

        jTabbedPane.addTab("Exclude N/A", new ShowExcludeNTreeJPanel(this).backgroundPanel);
        jTabbedPane.addTab("Full Tree", new ShowFullTreeJPanel().backgroundPanel);

        jTabbedPane.addChangeListener(new MyStateListener(this));

        centerPanel.add(jTabbedPane, "Center");
    }

    // 创建节点细节展示面板
    public void creatNodeDetailTab() {
        centerPanel.removeAll();
        // 设置tab标题位置
        jTabbedPane = new JTabbedPane(JTabbedPane.TOP);
        // 设置tab布局
        jTabbedPane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
        jTabbedPane.setFont(MyFont.Static);

        NodeDetailJPanel NDJPanel = new NodeDetailJPanel();
        NDJPanel.setJframe(this);
        jTabbedPane.addTab("Node_Details", NDJPanel.backgroundPanel);
        centerPanel.add(jTabbedPane, "Center");
    }

    // 创建模型管理面板
    public void creatModelListTab() {
        centerPanel.removeAll();
        // 设置tab标题位置
        jTabbedPane = new JTabbedPane(JTabbedPane.TOP);
        // 设置tab布局
        jTabbedPane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
        jTabbedPane.setFont(MyFont.Static);
        AllTableServiceImpl goodsService = new AllTableServiceImpl();
        ArrayList<String>  list = new ArrayList<String>();
        try {
            list = goodsService.TableNameSelectList();
            if (validateTableListExist(list)) {
                for (int i=0;i<list.size();i++)
                    jTabbedPane.addTab(list.get(i), new ModelListJPanel(list.get(i)).backgroundPanel);
                //jTabbedPane.addChangeListener(new MyStateListener(this));
            } else {
                Object[] options = {"OK"};
                int response = JOptionPane.showOptionDialog(backgroundPanel, "Input every table of ModelList correctly!",
                        "Warning", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        centerPanel.add(jTabbedPane, "Center");
    }

    // 创建节点细节展示面板
    public void creatConfigBomTab() {
        centerPanel.removeAll();
        // 设置tab标题位置
        jTabbedPane = new JTabbedPane(JTabbedPane.TOP);
        // 设置tab布局
        jTabbedPane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
        jTabbedPane.setFont(MyFont.Static);

        jTabbedPane.addTab("Config_BOM", new getConfigBOMJPanel().backgroundPanel);
        jTabbedPane.setForegroundAt(0, Color.BLUE);

        GenerateConfigTableJPanel ConfigTableJPanel = new GenerateConfigTableJPanel();
        ConfigTableJPanel.setJframe(this);
        jTabbedPane.addTab("Generate_Table", ConfigTableJPanel.backgroundPanel);

        ImGeTableJPanel ImGeJPanel = new ImGeTableJPanel();
        ImGeJPanel.setJframe(this);
        jTabbedPane.addTab("Import & Generate", ImGeJPanel.backgroundPanel);

        jTabbedPane.setForegroundAt(0, Color.BLUE);
        jTabbedPane.addChangeListener(new MyStateListener(this));
        centerPanel.add(jTabbedPane, "Center");
    }

    // 创建用户管理面板
    public void creatUserManagerTab() {

        centerPanel.removeAll();
        // 设置tab标题位置
        jTabbedPane = new JTabbedPane(JTabbedPane.TOP);
        // 设置tab布局
        jTabbedPane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
        jTabbedPane.setFont(MyFont.Static);

        jTabbedPane.addTab("Help", new UserHelpJPanel(user, this).backgroundPanel);
        centerPanel.add(jTabbedPane, "Center");
    }

    // 鼠标点击事件
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == home) {
            initSign();
            sign_home = 1;
            creatHome();
            home.setText("<html><font color='#336699' style='font-weight:bold'>" + "Home" + "</font>&nbsp;</html>");
            baseData.setText("<html><font color='black'>" + "Agile_BOM_DB" + "</font>&nbsp;</html>");
            showTree.setText("<html><font color='black'>" + "Agile_BOM_Tree" + "</font>&nbsp;</html>");
            searchNodeDetail.setText("<html><font color='black'>" + "Node_Detail" + "</font>&nbsp;</html>");
            model_list.setText("<html><font color='black'>" + "Model_List" + "</font>&nbsp;</html>");
            config_bom.setText("<html><font color='black'>" + "Config_BOM" + "</font>&nbsp;</html>");
            userManager.setText("<html><font color='black'>" + "Help" + "</font>&nbsp;</html>");
        } else if (e.getSource() == baseData) {
            initSign();
            sign_baseData = 1;
            creatBaseDataTab();
            home.setText("<html><font color='black'>" + "Home" + "</font>&nbsp;</html>");
            baseData.setText("<html><font color='#336699' style='font-weight:bold'>" + "Agile_BOM_DB" + "</font>&nbsp;</html>");
            showTree.setText("<html><font color='black'>" + "Agile_BOM_Tree" + "</font>&nbsp;</html>");
            searchNodeDetail.setText("<html><font color='black'>" + "Node_Detail" + "</font>&nbsp;</html>");
            model_list.setText("<html><font color='black'>" + "Model_List" + "</font>&nbsp;</html>");
            config_bom.setText("<html><font color='black'>" + "Config_BOM" + "</font>&nbsp;</html>");
            userManager.setText("<html><font color='black'>" + "Help" + "</font>&nbsp;</html>");
        } else if (e.getSource() == showTree) {
            initSign();
            sign_showTree = 1;
            //System.out.println("show tree---------------");
            creatshowTreeTab();
            home.setText("<html><font color='black'>" + "Home" + "</font>&nbsp;</html>");
            baseData.setText("<html><font color='black'>" + "Agile_BOM_DB" + "</font>&nbsp;</html>");
            showTree.setText("<html><font color='#336699' style='font-weight:bold'>" + "Agile_BOM_Tree" + "</font>&nbsp;</html>");
            searchNodeDetail.setText("<html><font color='black'>" + "Node_Detail" + "</font>&nbsp;</html>");
            model_list.setText("<html><font color='black'>" + "Model_List" + "</font>&nbsp;</html>");
            config_bom.setText("<html><font color='black'>" + "Config_BOM" + "</font>&nbsp;</html>");
            userManager.setText("<html><font color='black'>" + "Help" + "</font>&nbsp;</html>");
        } else if (e.getSource() == searchNodeDetail) {
            initSign();
            sign_searchNodeDetail = 1;
            creatNodeDetailTab();
            //System.out.println("searchBy_PN---------------");
            home.setText("<html><font color='black'>" + "Home" + "</font>&nbsp;</html>");
            baseData.setText("<html><font color='black'>" + "Agile_BOM_DB" + "</font>&nbsp;</html>");
            showTree.setText("<html><font color='black'>" + "Agile_BOM_Tree" + "</font>&nbsp;</html>");
            searchNodeDetail.setText("<html><font color='#336699' style='font-weight:bold'>" + "Node_Detail" + "</font>&nbsp;</html>");
            model_list.setText("<html><font color='black'>" + "Model_List" + "</font>&nbsp;</html>");
            config_bom.setText("<html><font color='black'>" + "Config_BOM" + "</font>&nbsp;</html>");
            userManager.setText("<html><font color='black'>" + "Help" + "</font>&nbsp;</html>");
        } else if (e.getSource() == model_list) {
            initSign();
            sign_model_list = 1;
            creatModelListTab();
            home.setText("<html><font color='black'>" + "Home" + "</font>&nbsp;</html>");
            baseData.setText("<html><font color='black'>" + "Agile_BOM_DB" + "</font>&nbsp;</html>");
            showTree.setText("<html><font color='black'>" + "Agile_BOM_Tree" + "</font>&nbsp;</html>");
            model_list.setText("<html><font color='#336699' style='font-weight:bold'>" + "Model_List" + "</font>&nbsp;</html>");
            config_bom.setText("<html><font color='black'>" + "Config_BOM" + "</font>&nbsp;</html>");
            searchNodeDetail.setText("<html><font color='black'>" + "Node_Detail" + "</font>&nbsp;</html>");
            userManager.setText("<html><font color='black'>" + "Help" + "</font>&nbsp;</html>");
        } else if (e.getSource() == config_bom) {
            initSign();
            sign_config_bom = 1;
            creatConfigBomTab();
            home.setText("<html><font color='black'>" + "Home" + "</font>&nbsp;</html>");
            baseData.setText("<html><font color='black'>" + "Agile_BOM_DB" + "</font>&nbsp;</html>");
            showTree.setText("<html><font color='black'>" + "Agile_BOM_Tree" + "</font>&nbsp;</html>");
            model_list.setText("<html><font color='black'>" + "Model_List" + "</font>&nbsp;</html>");
            config_bom.setText("<html><font color='#336699' style='font-weight:bold'>" + "Config_BOM" + "</font>&nbsp;</html>");
            searchNodeDetail.setText("<html><font color='black'>" + "Node_Detail" + "</font>&nbsp;</html>");
            userManager.setText("<html><font color='black'>" + "Help" + "</font>&nbsp;</html>");
        } else if (e.getSource() == userManager) {
            initSign();
            sign_userManager = 1;
            creatUserManagerTab();
            home.setText("<html><font color='black'>" + "Home" + "</font>&nbsp;</html>");
            baseData.setText("<html><font color='black'>" + "Agile_BOM_DB" + "</font>&nbsp;</html>");
            showTree.setText("<html><font color='black'>" + "Agile_BOM_Tree" + "</font>&nbsp;</html>");
            userManager.setText("<html><font color='#336699' style='font-weight:bold'>" + "Help" + "</font>&nbsp;</html>");
            searchNodeDetail.setText("<html><font color='black'>" + "Node_Detail" + "</font>&nbsp;</html>");
            model_list.setText("<html><font color='black'>" + "Model_List" + "</font>&nbsp;</html>");
            config_bom.setText("<html><font color='black'>" + "Config_BOM" + "</font>&nbsp;</html>");
        } else {
            System.out.println("ok");
        }
    }

    // 鼠标划入事件
    @Override
    public void mouseEntered(MouseEvent e) {
        if (e.getSource() == home) {
            // 鼠标改变形状
            home.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            home.setText("<html><font color='#336699' style='font-weight:bold'>" + "Home" + "</font>&nbsp;</html>");
        } else if (e.getSource() == baseData) {
            baseData.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            baseData.setText("<html><font color='#336699' style='font-weight:bold'>" + "Agile_BOM_DB" + "</font>&nbsp;</html>");
        } else if (e.getSource() == showTree) {
            showTree.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            showTree.setText("<html><font color='#336699' style='font-weight:bold'>" + "Agile_BOM_Tree" + "</font>&nbsp;</html>");
        } else if (e.getSource() == searchNodeDetail) {
            searchNodeDetail.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            searchNodeDetail.setText("<html><font color='#336699' style='font-weight:bold'>" + "Node_Detail" + "</font>&nbsp;</html>");
        } else if (e.getSource() == model_list) {
            model_list.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            model_list.setText("<html><font color='#336699' style='font-weight:bold'>" + "Model_List" + "</font>&nbsp;</html>");
        } else if (e.getSource() == config_bom) {
            config_bom.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            config_bom.setText("<html><font color='#336699' style='font-weight:bold'>" + "Config_BOM" + "</font>&nbsp;</html>");
        } else if (e.getSource() == userManager) {
            userManager.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            userManager.setText("<html><font color='#336699' style='font-weight:bold'>" + "Help" + "</font>&nbsp;</html>");
        }

    }

    // 鼠标划出事件
    @Override
    public void mouseExited(MouseEvent e) {
        if (e.getSource() == home) {
            if (sign_home == 0) {
                home.setText("<html><font color='black'>" + "Home" + "</font>&nbsp;</html>");
            }
        } else if (e.getSource() == baseData) {
            if (sign_baseData == 0) {
                baseData.setText("<html><font color='black'>" + "Agile_BOM_DB" + "</font>&nbsp;</html>");
            }
        } else if (e.getSource() == showTree) {
            if (sign_showTree == 0) {
                showTree.setText("<html><font color='black'>" + "Agile_BOM_Tree" + "</font>&nbsp;</html>");
            }
        } else if (e.getSource() == searchNodeDetail) {
            if (sign_searchNodeDetail == 0) {
                searchNodeDetail.setText("<html><font color='black'>" + "Node_Detail" + "</font>&nbsp;</html>");
            }
        } else if (e.getSource() == model_list) {
            if (sign_model_list == 0) {
                model_list.setText("<html><font color='black'>" + "Model_List" + "</font>&nbsp;</html>");
            }
        } else if (e.getSource() == config_bom) {
            if (sign_config_bom == 0) {
                config_bom.setText("<html><font color='black'>" + "Config_BOM" + "</font>&nbsp;</html>");
            }
        } else if (e.getSource() == userManager) {
            if (sign_userManager == 0) {
                userManager.setText("<html><font color='black'>" + "Help" + "</font>&nbsp;</html>");
            }
        }

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

    }

}
