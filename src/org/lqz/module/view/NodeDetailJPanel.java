package org.lqz.module.view;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import org.lqz.framework.util.BaseTableModule;
import org.lqz.framework.util.Item;
import org.lqz.framework.util.Tools;
import org.lqz.module.dao.Impl.BaseDaoImpl;
import org.lqz.module.entity.AgileBOM;

import static org.lqz.module.dao.Impl.BaseDaoImpl.select;
import static org.lqz.module.dao.Impl.BaseDaoImpl.validateTableNameExist;
import static org.lqz.module.view.ShowIncludeNTreeJPanel.BOMQueryMaxID;
import static org.lqz.module.view.ShowIncludeNTreeJPanel.BOMQueryNextL1Node;
import static org.lqz.module.view.ShowIncludeNTreeJPanel.maxID;

public class NodeDetailJPanel implements ActionListener {
    // 定义全局组件
    static JPanel backgroundPanel, topPanel, toolPanel, searchPanel, showBOMPanel;
    JComboBox select_category;

    JLabel label_category, label_warehouse;
    JButton GButton,bt_export;
    JTextField detailText;
    IndexJFrame jframe;
    static JScrollPane jScrollPane;
    final String tips = "Please input a PathNumber.";
    public static Vector<Vector> BOMQueryVector = new Vector<Vector>();
    public static Vector<Vector> FullBOMQueryVector = new Vector<Vector>();

    public NodeDetailJPanel() {

        backgroundPanel = new JPanel(new BorderLayout());
        initTopPanel();
            if (validateTableNameExist("EMC_MAKE_BUY")&&validateTableNameExist("Agile_BOM"))
                initDetailShowPanel();
            else{
                Object[] options = {"OK"};
                int response=JOptionPane.showOptionDialog(backgroundPanel, "Input the table Agile BOM & EMC MAKE BUY correctly!",
                        "Warning",JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            }

    }
    public void setJframe(IndexJFrame jframe) {
        this.jframe=jframe;
    }
    // 初始化顶部面板
    public void initTopPanel() {

        topPanel = new JPanel(new BorderLayout());
        initSearchPanel();
        backgroundPanel.add(topPanel, "North");
    }

    // 初始化搜素条件面板
    public void initSearchPanel() {

        searchPanel = new JPanel();
        // 商品种类下拉框
        select_category = new JComboBox();
        select_category.addItem(new Item("BOM查询", "BOM查询"));
        select_category.addItem(new Item("Full BOM查询", "Full BOM查询"));
        select_category.addActionListener(this);

        JPanel input = new JPanel(new FlowLayout(FlowLayout.CENTER));
        GButton = new JButton("Go");
        GButton.addActionListener(this);
        Icon icon_export  = new ImageIcon(IndexJFrame.class.getResource("/image/excel.png"));
        //Icon icon_export = new ImageIcon("image/excel.png");
        bt_export = new JButton(icon_export);
        bt_export.setToolTipText("导出Excel");
        bt_export.addActionListener(this);

        detailText = new JTextField();
        detailText.setPreferredSize(new Dimension(600, 40));
        detailText.setText(tips);
        detailText.setFont(new Font("标楷体", Font.TRUETYPE_FONT | Font.ITALIC, 18));
        input.add(detailText);
        input.add(GButton);
        //input.add(bt_export);

        // 标签
        label_category = new JLabel(" 查询类型 ");
        label_warehouse = new JLabel("PathNumber");

        searchPanel.add(label_category);
        searchPanel.add(select_category);

        searchPanel.add(label_warehouse);
        searchPanel.add(input);

        topPanel.add(searchPanel, "Center");
        topPanel.add(bt_export,BorderLayout.EAST);
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
    public void initDetailShowPanel() {
        searchBOMTable(null);
    }

    public void searchBOMTable(String sql) {

        String params[] = {"Node", "Level", "Description", "EMC"};
        if (sql != null) {
            BOMQueryVector.clear();
            pathNumberBOMQuery(sql);
        }

        BaseTableModule baseTableModule = new BaseTableModule(params, BOMQueryVector);
        JTable table = new MyTable(baseTableModule);
        Tools.setTableStyle(table);
        jScrollPane = new JScrollPane(table);
        Tools.setJspStyle(jScrollPane);

        showBOMPanel = new JPanel(new BorderLayout());
        showBOMPanel.setOpaque(false);
        showBOMPanel.add(jScrollPane);
        backgroundPanel.add(showBOMPanel, "Center");
        backgroundPanel.updateUI();
    }

    class MyTable extends JTable {

        public MyTable(BaseTableModule baseTableModule) {
            super(baseTableModule);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (getFillsViewportHeight()) {
                paintEmptyRows(g);
            }
        }

        /**
         * Paints the backgrounds of the implied empty rows when the table model is
         * insufficient to fill all the visible area available to us. We don't
         * involve cell renderers, because we have no data.
         */
        protected void paintEmptyRows(Graphics g) {
            final int rowCount = getRowCount();
            final Rectangle clip = g.getClipBounds();
            if (rowCount * rowHeight < clip.height) {
                for (int i = rowCount; i <= clip.height / rowHeight; ++i) {
                    g.setColor(colorForRow(i));
                    g.fillRect(clip.x, i * rowHeight, clip.width, rowHeight);
                }
            }
        }

        /**
         * Returns the appropriate background color for the given row.
         */
        protected Color colorForRow(int row) {
            return (row % 4 == 0) ? Color.PINK : Color.WHITE;
        }

        /**
         * Shades alternate rows in different colors.
         */
        @Override
        public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
            Component c = super.prepareRenderer(renderer, row, column);
            if (isCellSelected(row, column) == false) {
                c.setBackground(colorForRow(row));
                c.setForeground(UIManager.getColor("Table.foreground"));
            } else {
                c.setBackground(UIManager.getColor("Table.selectionBackground"));
                c.setForeground(UIManager.getColor("Table.selectionForeground"));
            }
            return c;
        }
    }

    public void pathNumberBOMQuery(String number) {// conditionParams[] = { item_category.getKey(), item_warehouse.getKey() };
        StringBuilder sqlBuilder = new StringBuilder(
                //"select id, Level, Description from Agile_BOM WHERE Number= '" + number + "' ");
                "select  id, Level, Description,  New_Version  from Agile_EMC WHERE Number='" + number + "' ");

        String sql = sqlBuilder.toString();
        List<Object[]> list = null;
        try {
            list = select(sql, 4, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (!list.isEmpty()) {
            for (Object[] object : list) {
                Vector temp = new Vector<String>();
                temp.add("P/N     " + number);//6 space
                for (int i = 1; i < object.length; i++) {
                    temp.add(object[i]);
                }
                BOMQueryVector.add(temp);
                BOMQueryL1Node(object);
                BOMQueryParentNode(Integer.valueOf((String) object[1]), object);
                BOMQueryChildNode(object);
            }
        }

    }

    // object[0]   id,   Level,   Description
    public void BOMQueryL1Node(Object[] PathNumberList) {// conditionParams[] = { item_category.getKey(), item_warehouse.getKey() };
        //System.out.println("---" + PathNumberList[0] + "---" + PathNumberList[1] + "---");
        int level = Integer.parseInt((String) PathNumberList[1]);
        if (level == 0) {// 0-层
            //System.out.println("level0------------" + level);
            int id = (int) PathNumberList[0] + 1;
            StringBuilder sqlBuilder = new StringBuilder(
                    "select id, Level from Agile_BOM WHERE id= '" + id + "' ");
            String sql = sqlBuilder.toString();
            List<Object[]> list = null;
            try {
                list = select(sql, 2, null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Object[] array = list.get(0);
            BOMQueryL1Node(array);

        } else if (level == 1) {//1-层
            //System.out.println("level1------------" + level);
            StringBuilder sqlBuilder = new StringBuilder(
                    //"select Number, Level, Description from Agile_BOM WHERE id= '" + PathNumberList[0] + "' ");
                    "select  Number, Level, Description, New_Version  from Agile_EMC WHERE id= '" + PathNumberList[0] + "' ");
            String sql = sqlBuilder.toString();
            List<Object[]> list = null;
            try {
                list = select(sql, 4, null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (!list.isEmpty()) {
                for (Object[] object1 : list) {
                    Vector temp = new Vector<String>();
                    temp.add("L1     " + object1[0]);
                    for (int i = 1; i < object1.length; i++) {
                        temp.add(object1[i]);
                    }
                    BOMQueryVector.add(temp);
                }
            }
        } else {//   id,   Level,   Description
            // System.out.println("level>1------------" + level);
            int id = (Integer) PathNumberList[0] - 1;
            StringBuilder sqlBuilder = new StringBuilder(
                    "select id, Level from Agile_BOM WHERE id= '" + id + "' ");
            String sql = sqlBuilder.toString();
            List<Object[]> list = null;
            try {
                list = select(sql, 2, null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Object[] array = list.get(0);
            BOMQueryL1Node(array);
        }
    }

    public void BOMQueryParentNode(int NodeLevel, Object[] PathNumberList) {// conditionParams[] = { item_category.getKey(), item_warehouse.getKey() };
        //System.out.println("---" + PathNumberList[0] + "---" + PathNumberList[1] + "---");
        int level = Integer.parseInt((String) PathNumberList[1]);
        int id = (Integer) PathNumberList[0] - 1;
        if (level == 0) {
            Vector temp = new Vector<String>();
            temp.add("Parent     " + null);
            temp.add("null");
            temp.add("null");
            temp.add("null");
            BOMQueryVector.add(temp);
        } else if (level == 1) {

            StringBuilder sqlBuilder = new StringBuilder(
                    "select  Number, Level, Description, New_Version  from Agile_EMC WHERE Level= '0' ");
            String sql = sqlBuilder.toString();
            List<Object[]> list = null;
            try {
                list = select(sql, 4, null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (!list.isEmpty()) {
                for (Object[] object1 : list) {
                    Vector temp = new Vector<String>();
                    temp.add("Parent     " + object1[0]);
                    for (int i = 1; i < object1.length; i++) {
                        temp.add(object1[i]);
                    }
                    BOMQueryVector.add(temp);
                }
            }
        } else if (level == (NodeLevel - 1)) {
            StringBuilder sqlBuilder = new StringBuilder(
                    "select  Number, Level, Description, New_Version  from Agile_EMC where id= '" + PathNumberList[0] + "' ");
            String sql = sqlBuilder.toString();
            List<Object[]> list = null;
            try {
                list = select(sql, 4, null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (!list.isEmpty()) {
                for (Object[] object1 : list) {
                    Vector temp = new Vector<String>();
                    temp.add("Parent     " + object1[0]);
                    for (int i = 1; i < object1.length; i++) {
                        temp.add(object1[i]);
                    }
                    BOMQueryVector.add(temp);
                }
            }
        } else {//>1-层  id,   Level,   Description
            // System.out.println("level>1------------" + level);
            StringBuilder sqlBuilder = new StringBuilder(
                    "select id, Level from Agile_BOM WHERE id= '" + id + "' ");
            String sql = sqlBuilder.toString();
            List<Object[]> list = null;
            try {
                list = select(sql, 2, null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Object[] array = list.get(0);//直接父节点只有一个
            BOMQueryParentNode(NodeLevel, array);
        }
    }

    //id,   Level,   Description
    public void BOMQueryChildNode(Object[] PathNumberList) {
        int level = Integer.parseInt((String) PathNumberList[1]);
        int id = (Integer) PathNumberList[0] + 1;
        StringBuilder sqlBuilder = new StringBuilder(
                "select  Number, Level, Description, New_Version  from Agile_EMC WHERE id= '" + id + "' ");
        String sql = sqlBuilder.toString();
        List<Object[]> list = null;
        try {
            list = select(sql, 4, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int nextNodeLevel = 0;
        if (!list.isEmpty()) {
            Object[] array = list.get(0);
            nextNodeLevel = Integer.parseInt((String) array[1]);
        }
        if (nextNodeLevel == (level + 1)) {
            for (Object[] object1 : list) {
                Vector temp = new Vector<String>();
                temp.add("Child     " + object1[0]);
                for (int i = 1; i < object1.length; i++) {
                    temp.add(object1[i]);
                }
                BOMQueryVector.add(temp);
            }
        } else {
            Vector temp = new Vector<String>();
            temp.add("Child     " + null);
            temp.add("null");
            temp.add("null");
            temp.add("null");
            BOMQueryVector.add(temp);
        }

    }


    // 初始化数据表格面板
    public void searchFullBOMTable(String pathNumber) {
        String params[] = {"Level", "P/N", "Description", "EMC"};
        if (!pathNumber.equals(""))
            FullBOMQueryVector.clear();
        BOMQueryMaxID();
        List<Object[]> PathNumberList = ShowIncludeNTreeJPanel.pathNumberBOMQuery(pathNumber);//只取第一个满足pathNumber的组
        if (!PathNumberList.isEmpty()) {
            for (Object[] PathNumberArray : PathNumberList) {
                //pathNumberID = (int) PathNumberArray[0];
                //System.out.println("pathNumberID---"+pathNumberID);
                int L1ID = ShowIncludeNTreeJPanel.BOMQueryL1Node(PathNumberArray);
                int nextL1ID = BOMQueryNextL1Node(PathNumberArray, L1ID);
                //System.out.println("maxID---"+maxID+"    L1ID---"+L1ID+"   nextL1ID ---"+nextL1ID );
                try {
                    fillFullBOMForm(L1ID, nextL1ID);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        BaseTableModule baseTableModule = new BaseTableModule(params, FullBOMQueryVector);
        JTable table = new JTable(baseTableModule);
        Tools.setFullTableStyle(table);
        jScrollPane = new JScrollPane(table);
        Tools.setJspStyle(jScrollPane);

        showBOMPanel = new JPanel(new BorderLayout());
        showBOMPanel.setOpaque(false);
        showBOMPanel.add(jScrollPane);
        backgroundPanel.add(showBOMPanel, "Center");
        backgroundPanel.updateUI();
    }


    // 下拉框改变事件
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == select_category) {
            String warehouse_id = ((Item) select_category.getSelectedItem()).getKey();
            if (warehouse_id == "BOM查询") {
                backgroundPanel.remove(showBOMPanel);
                searchBOMTable("");
            } else if (warehouse_id == "Full BOM查询") {
                backgroundPanel.remove(showBOMPanel);
                searchFullBOMTable("");
            }
        } else if (e.getSource() == GButton) {
            backgroundPanel.remove(showBOMPanel);
            String pathNumebr = detailText.getText().trim();
            //System.out.println("pathNumebr---" + pathNumebr);
            if (pathNumebr != tips) {
                String warehouse_id = ((Item) select_category.getSelectedItem()).getKey();
                if (warehouse_id == "BOM查询")
                    searchBOMTable(pathNumebr);
                else if (warehouse_id == "Full BOM查询")
                    searchFullBOMTable(pathNumebr);
            }
        } else if (e.getSource() == bt_export) {
            String pathNumebr = detailText.getText().trim();
            //System.out.println("pathNumebr---" + pathNumebr);
            if (pathNumebr != tips) {
                String warehouse_id = ((Item) select_category.getSelectedItem()).getKey();
                FileOperation fileOperation=new FileOperation(jframe);
                boolean isSaveSuccess=false;
                if (warehouse_id == "BOM查询")
                   isSaveSuccess=fileOperation.SaveAs("BOMQuery");
                else if (warehouse_id == "Full BOM查询")
                    isSaveSuccess=fileOperation.SaveAs("FullBOMQuery");

                if (isSaveSuccess)
                    JOptionPane.showOptionDialog(this.backgroundPanel, "Export this table successfully!",
                            "Tips", JOptionPane.DEFAULT_OPTION,
                            JOptionPane.INFORMATION_MESSAGE, null, null, null);
            }
        }
//                if (warehouse_id == "BOM查询")
//                    searchBOMTable(pathNumebr);
//                else if (warehouse_id == "Full BOM查询")
//                    searchFullBOMTable(pathNumebr);
    }

    public static void fillFullBOMForm(int l1ID, int nextL1ID) throws SQLException {
        StringBuilder sqlBuilder = new StringBuilder(
                //"SELECT * FROM Agile_BOM WHERE id between " + l1ID + " and " + (nextL1ID - 1));
                "select  Level, Number, Description, New_Version  from Agile_EMC WHERE id between " + l1ID + " and " + (nextL1ID - 1));
        String sql = sqlBuilder.toString();
        List<Object[]> list = select(sql, 4, null);
        if (!list.isEmpty()) {
            for (Object[] object : list) {
                for (Object[] object1 : list) {
                    Vector temp = new Vector<String>();
                    for (int i = 0; i < object1.length; i++) {
                        temp.add(object1[i]);
                    }
                    FullBOMQueryVector.add(temp);
                }
            }
        }
    }
}
