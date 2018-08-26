package org.lqz.module.view;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.tree.*;
import org.lqz.framework.util.Tools;
import org.lqz.module.entity.AgileBOM;
import static org.lqz.module.dao.Impl.BaseDaoImpl.*;
import static org.lqz.module.view.ShowIncludeNTreeJPanel.expandSpecifyNode;

public class ShowExcludeNTreeJPanel implements ActionListener, MouseListener, TreeSelectionListener {
    // 定义全局组件
    JPanel backgroundPanel, topPanel, toolPanel, searchPanel, treePanel;
    JTable table;
    JLabel label_warehouse;
    JButton GButton,bt_export;
    JButton ExpandAllButton;
    JTextField detailText;
    JScrollPane treeScroll, consoleScroll;
    static JTree tree = new JTree();
    static boolean treeFlag = false;
    JSplitPane showTreeDetail;
    JTextPane consoleText;
    JPopupMenu jPopMenu;
    JMenuItem copy, clear;
    Clipboard clipboard;
    IndexJFrame jframe;
    public static Vector<Integer> levelVector = new Vector<Integer>();
    public static HashMap<Integer, AgileBOM> idAgileBOM = new HashMap<Integer, AgileBOM>();
    final String tips = "Please input a PathNumber.";
    //static int pathNumberID;


    public ShowExcludeNTreeJPanel(IndexJFrame jframe) {
        this.jframe = jframe;
        backgroundPanel = new JPanel(new BorderLayout());
        backgroundPanel.setName("ShowExcludeNTreeJPanel");
        initTopPanel();
            if (validateTableNameExist("Agile_EMC"))
                initTreePanel();
            else {
                Object[] options = {"OK"};
                int response = JOptionPane.showOptionDialog(backgroundPanel, "Input the table Agile BOM & EMC MAKE BUY correctly!",
                        "Warning", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            }

    }
//    public void setJframe(IndexJFrame jframe) {
//        this.jframe = jframe;
//    }
    // 初始化顶部面板
    public void initTopPanel() {
        topPanel = new JPanel(new BorderLayout());
        initSearchPanel();
        backgroundPanel.add(topPanel, "North");
    }

    // 初始化搜素条件面板
    public void initSearchPanel() {

        searchPanel = new JPanel();
        JPanel input = new JPanel(new FlowLayout(FlowLayout.CENTER));
        GButton = new JButton("Go");
        GButton.addActionListener(this);

        ExpandAllButton = new JButton("ExpandAll");
        ExpandAllButton.addActionListener(this);


        detailText = new JTextField();
        detailText.setPreferredSize(new Dimension(600, 40));
        detailText.setText(tips);
        detailText.setFont(new Font("标楷体", Font.TRUETYPE_FONT | Font.ITALIC, 18));

        Icon icon_export  = new ImageIcon(IndexJFrame.class.getResource("/image/excel.png"));
        //Icon icon_export = new ImageIcon("image/excel.png");
        bt_export = new JButton(icon_export);
        bt_export.setToolTipText("导出Excel");
        bt_export.addActionListener(this);


        input.add(detailText);
        input.add(GButton);
        input.add(ExpandAllButton);

        label_warehouse = new JLabel("PathNumber");
        searchPanel.add(label_warehouse);
        searchPanel.add(input);

        topPanel.add(searchPanel, "Center");
        topPanel.add(bt_export, "East");
        detailText.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (tips.equalsIgnoreCase(detailText.getText())) {
                    detailText.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if ("".equals(detailText.getText())) {
                    detailText.setText(tips);
                }
            }
        });
    }


    // 初始化数据表格面板
    public void initTreePanel() {
        if (treeFlag == false)//保存上次查询结果，避免重置
            getTree();
        else {
            TreeModel treeModel = new DefaultTreeModel(root);
            tree.setModel(treeModel);
            tree.addTreeSelectionListener(this);
            NodeRenderer renderer = new NodeRenderer("");
            tree.setCellRenderer(renderer);
        }

        treeScroll = new JScrollPane(tree);
        Tools.setJspStyle(treeScroll);
        treePanel = new JPanel(new BorderLayout());
        treePanel.setOpaque(false);
        treePanel.add(treeScroll);

        JPanel cp = new JPanel(new BorderLayout());
        //cp.setPreferredSize(new Dimension(300, 150));
        consoleText = new JTextPane();
        consoleScroll = new JScrollPane(consoleText);
        //consoleText.setText("Node information...");
        insertDocument(consoleText, "Node information...\n", Color.GREEN, 2);
        consoleText.setFont(new Font("标楷体", Font.TRUETYPE_FONT, 15));
        consoleScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        consoleScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        cp.add(consoleScroll);


        clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        copy = new JMenuItem("Copy(C)");
        clear = new JMenuItem("Clear");
        jPopMenu = new JPopupMenu();
        jPopMenu.add(copy);
        jPopMenu.add(clear);
        copy.addActionListener(this);
        clear.addActionListener(this);
        consoleText.add(jPopMenu);
        consoleText.addMouseListener(this);


        showTreeDetail = new JSplitPane();
        showTreeDetail.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        showTreeDetail.setLeftComponent(treePanel);
        showTreeDetail.setRightComponent(cp);
        showTreeDetail.setDividerSize(5);
        showTreeDetail.setDividerLocation(1600);
        backgroundPanel.add(showTreeDetail, "Center");
    }

    public static DefaultMutableTreeNode root;
    public static DefaultMutableTreeNode tipsNode;

    public void getTree() {
        StringBuilder sqlBuilder = new StringBuilder(
                "select * from Agile_EMC WHERE Level= '" + 0 + "' ");
        String sql = sqlBuilder.toString();
        Object[] row = null;
        try {
            row = selectArray(sql, 6, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        AgileBOM AB = AgileBOM.CreatRowBOMQuery(row);
        root = new DefaultMutableTreeNode(AB);
        tipsNode = new DefaultMutableTreeNode("Please input a PathNumber for more nodes");
        root.add(tipsNode);
        TreeModel treeModel = new DefaultTreeModel(root);
        //tree = new JTree(top);
        tree.setModel(treeModel);
        tree.addTreeSelectionListener(this);
    }

    //public static Vector<Integer> levelVector=new Vector<Integer>();
    //public static HashMap<Integer, AgileBOM> idAgileBOM = new HashMap<Integer, AgileBOM>();
    // 更新数据表格
    public void refreshTreePanel(String pathNumber) {
        root.removeAllChildren();
        BOMQueryMaxID();
        List<Object[]> PathNumberList = pathNumberBOMQuery(pathNumber);//只取第一个满足pathNumber的组
        if (!PathNumberList.isEmpty()) {
            for (Object[] PathNumberArray : PathNumberList) {
                //pathNumberID = (int) PathNumberArray[0];
                //System.out.println("pathNumberID---"+pathNumberID);
                int L1ID = BOMQueryL1Node(PathNumberArray);
                int nextL1ID = BOMQueryNextL1Node(PathNumberArray, L1ID);
                //System.out.println("L1ID---"+L1ID+"  maxID---"+maxID+"  nextL1ID---"+nextL1ID);
                ExcludeNANode getTreeNode = null;
                DefaultMutableTreeNode top = null;
                try {
                    levelVector.clear();
                    idAgileBOM.clear();
                    if (nextL1ID == maxID) {
                        MarkID(L1ID, nextL1ID + 1);//标注levelVector, idAgileBOM
                        getTreeNode = new ExcludeNANode(L1ID, nextL1ID, levelVector, idAgileBOM);
                        top = getTreeNode.ExtraverseFolder(levelVector.get(0), L1ID, nextL1ID);
                    } else {
                        MarkID(L1ID, nextL1ID);
                        getTreeNode = new ExcludeNANode(L1ID, nextL1ID - 1, levelVector, idAgileBOM);
                        top = getTreeNode.ExtraverseFolder(levelVector.get(0), L1ID, nextL1ID - 1);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (top != null)
                    root.add(top);
                else {
                    top = new DefaultMutableTreeNode("The version corresponding to the node is null or N/A, and is not displayed");
                    root.add(top);
                }
            }
        }
        TreeModel treeModel = new DefaultTreeModel(root);
        tree.setModel(treeModel);
        // tree.expandPath(pathNumberTreePath);//pathNumberTreePath
        //tree.addTreeSelectionListener(this);
        NodeRenderer renderer = new NodeRenderer(pathNumber);
        tree.setCellRenderer(renderer);
        backgroundPanel.validate();
        treeFlag = true;
    }


    //    public static Vector<Integer> levelVector=new Vector<Integer>();
//    public static HashMap<Integer, AgileBOM> idAgileBOM = new HashMap<Integer, AgileBOM>();
    public static void MarkID(int l1ID, int nextL1ID) throws SQLException {
        StringBuilder sqlBuilder = new StringBuilder(
                //"SELECT * FROM Agile_BOM WHERE id between " + l1ID + " and " + (nextL1ID - 1));
                "SELECT * FROM Agile_EMC WHERE id between " + l1ID + " and " + (nextL1ID - 1));
        String sql = sqlBuilder.toString();
        List<Object[]> list = select(sql, 6, null);

        if (!list.isEmpty()) {
            for (Object[] object : list) {
                int id = Integer.valueOf((Integer) object[0]);
                int level = Integer.valueOf((String) object[1]);
                //System.out.println("id---"+id+"  level---"+level);
                levelVector.add(level);
                AgileBOM AB = AgileBOM.CreatRowBOMQuery(object);
                idAgileBOM.put(id, AB);
            }
        }
    }


    public static List<Object[]> pathNumberBOMQuery(String number) {// conditionParams[] = { item_category.getKey(), item_warehouse.getKey() };
        StringBuilder sqlBuilder = new StringBuilder(
                "select id, Level from Agile_BOM WHERE Number= '" + number + "' ");
        String sql = sqlBuilder.toString();
        List<Object[]> list = null;
        try {
            list = select(sql, 2, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static int BOMQueryL1Node(Object[] PathNumberList) {
        //System.out.println("---" + PathNumberList[0] + "---" + PathNumberList[1] + "---");
        int level = Integer.parseInt((String) PathNumberList[1]);
        int id = 0;
        if (level == 0) {// 0-层
            id = (int) PathNumberList[0] + 1;
            return id;
        }
        if (level == 1) {// 0-层
            id = (int) PathNumberList[0];
        } else {//   id,   Level,   Description
            int idQuery = (Integer) PathNumberList[0] - 1;
            StringBuilder sqlBuilder = new StringBuilder(
                    "select id, Level from Agile_BOM WHERE id= '" + idQuery + "' ");
            String sql = sqlBuilder.toString();
            Object[] array = null;
            try {
                array = selectArray(sql, 2, null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            id = BOMQueryL1Node(array);
        }
        return id;
    }

    public static int maxID = 0;

    public static int BOMQueryMaxID() {
        StringBuilder sqlBuilder = new StringBuilder(
                "SELECT max(id) FROM Agile_BOM");
        String sql = sqlBuilder.toString();
        try {
            maxID = selectMaxID(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maxID;
    }


    public static int BOMQueryNextL1Node(Object[] PathNumberList, int L1ID) {
        //System.out.println("---" + PathNumberList[0] + "---" + PathNumberList[1] + "---");
        int level = Integer.parseInt((String) PathNumberList[1]);
        int id = (int) PathNumberList[0];
        if (id == maxID) {//
            id = maxID;
        } else if (level == 0) {
            int idQuery = (Integer) PathNumberList[0] + 2;
            StringBuilder sqlBuilder = new StringBuilder(
                    "select id, Level from Agile_BOM WHERE id= '" + idQuery + "' ");
            String sql = sqlBuilder.toString();
            Object[] array = null;
            try {
                array = selectArray(sql, 2, null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            id = BOMQueryNextL1Node(array, L1ID);
        } else if (level == 1 && id != L1ID) {
            return id;
        } else {//   level=1,level=0
            int idQuery = (Integer) PathNumberList[0] + 1;
            StringBuilder sqlBuilder = new StringBuilder(
                    "select id, Level from Agile_BOM WHERE id= '" + idQuery + "' ");
            String sql = sqlBuilder.toString();
            Object[] array = null;
            try {
                array = selectArray(sql, 2, null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            id = BOMQueryNextL1Node(array, L1ID);
        }
        return id;
    }

    public void expandAllNode(JTree tree, TreePath parent) {
        // 展开树的所有节点的方法
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration e = node.children(); e.hasMoreElements(); ) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAllNode(tree, path);
            }
        }
            tree.expandPath(parent);
    }


    public static Vector<Vector> ExTreeNodeVector=new Vector<Vector>();
    public static void exportExTree(JTree tree, TreePath parent) {
        // 展开树的所有节点的方法
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) parent.getLastPathComponent();
        AgileBOM user;
        if (node.getUserObject() instanceof AgileBOM)
            user = (AgileBOM) node.getUserObject();
        else
            return;
        //System.out.println("Level-----"+user.getLevel());
        Vector temp = new Vector<String>();
        for (int i=0;i<user.getLevel();i++)
            temp.add("");
        temp.add(user.getLevel());
        temp.add(user.getNumber());
        temp.add(user.getDescription());
        temp.add(user.getVersion());
        ExTreeNodeVector.add(temp);
        if (node.getChildCount() >= 0) {
            for (Enumeration e = node.children(); e.hasMoreElements(); ) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                exportExTree(tree, path);
            }
        }
    }
    // 下拉框改变事件
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == GButton) {
            String inputNumber = detailText.getText().trim();
            //System.out.println("你输入的是  " + inputNumber);
            if ((inputNumber.equals(tips)) || (inputNumber.equals("")))
                insertDocument(consoleText, "\nSorry, please input a PathNumber.", Color.red, 1);
            else {
                refreshTreePanel(detailText.getText().trim());
                TreeNode root = (TreeNode) tree.getModel().getRoot();
                expandSpecifyNode(inputNumber, tree, new TreePath(root));
            }
        }
        if (e.getSource() == ExpandAllButton) {
            //System.out.println("你输入的是  " + ExpandAllButton);
            // 展开所有结点
            TreeNode root = (TreeNode) tree.getModel().getRoot();
            expandAllNode(tree, new TreePath(root));
        } else if (e.getSource() == copy) {
            String temp = consoleText.getSelectedText();
            StringSelection content = new StringSelection(temp);
            clipboard.setContents(content, null);
        } else if (e.getSource() == clear) {
            consoleText.setText("Node details...\n");
        }else if (e.getSource() ==bt_export) {
            TreeNode root = (TreeNode) tree.getModel().getRoot();
            ExTreeNodeVector.clear();
            exportExTree(tree, new TreePath(root));
            FileOperation fileOperation = new FileOperation(this.jframe);
            boolean isSaveSuccess = fileOperation.SaveAs("ExcludeTree");
            if (isSaveSuccess)
                JOptionPane.showOptionDialog(this.backgroundPanel, "Export this table successfully!",
                        "Tips", JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE, null, null, null);
        }
    }

    // 鼠标点击事件
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            jPopMenu.show(consoleText, e.getX(), e.getY());
        }
    }

    // 鼠标划入事件
    @Override
    public void mouseEntered(MouseEvent e) {
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
    public void valueChanged(TreeSelectionEvent e) {
        //System.out.println("ADD-------------------------");
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
                .getLastSelectedPathComponent();
        if (node == null)
            return;
        Object object = node.getUserObject();
        TreeModel treeModel = tree.getModel();
        if (object instanceof AgileBOM) {
            AgileBOM nodeAgileBOM = (AgileBOM) object;
            insertDocument(consoleText, "\nThe details of this node are as follows\n" +
                    nodeAgileBOM.AgileBOMtoString(), Color.BLACK, 1);
            insertDocument(consoleText, "\n=================================================\n", Color.BLUE, 1);
        }
    }

    public static void insertDocument(JTextPane JTP, String str, Color textColor, int setFont)// 根据传入的颜色及文字，将文字插入控制台
    {
        SimpleAttributeSet set = new SimpleAttributeSet();
        StyleConstants.setForeground(set, textColor);// 设置文字颜色
        StyleConstants.setFontSize(set, 18);// 设置字体大小
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

    class NodeRenderer extends DefaultTreeCellRenderer {
        String pathNumber;

        public NodeRenderer(String pathNumber) {
            this.pathNumber = pathNumber;
        }

        public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                      boolean selected, boolean expanded, boolean leaf, int row,
                                                      boolean hasFocus) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            super.getTreeCellRendererComponent(tree, value, selected, expanded,
                    leaf, row, hasFocus);
            AgileBOM user = null;
            if (node.getUserObject() instanceof AgileBOM)
                user = (AgileBOM) node.getUserObject();

            if (user != null && user.getNumber().equals(pathNumber)) {
                //System.out.println("Find user..."+pathNumber);
                this.setForeground(Color.red);
            }
            setTextSelectionColor(Color.BLUE);
            setBackgroundSelectionColor(Color.GREEN);
            setBackgroundNonSelectionColor(Color.WHITE);
            return this;
        }
    }


}
