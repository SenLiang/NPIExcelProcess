package org.lqz.module.view;

import org.lqz.framework.util.BaseTableModule;
import org.lqz.framework.util.Item;
import org.lqz.framework.util.Tools;
import org.lqz.module.dao.Impl.BaseDaoImpl;
import org.lqz.module.dao.Impl.modellist_excel_import_export;
import org.lqz.module.entity.AgileBOM;
import org.lqz.module.services.Impl.AllTableServiceImpl;
import org.lqz.module.services.Impl.ModelListServiceImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.lqz.module.dao.Impl.BaseDaoImpl.*;
import static org.lqz.module.services.Impl.AllTableServiceImpl.AgileEMCTable;


class getConfigBOMJPanel implements ActionListener{
    // 定义全局组件
    static JPanel backgroundPanel, topPanel, toolPanel, searchPanel, showBOMPanel;
    static JScrollPane jScrollPane;
    static Vector<Vector> BOMQueryVector = new Vector<Vector>();
    //设置按钮尺寸
    final Dimension preferredButtonSize = new Dimension(60, 40);
    //设置按钮尺寸
    final Dimension preferredAllButtonSize = new Dimension(200, 40);
    //设置Model的label尺寸
    final Dimension preferredLabelSize = new Dimension(175, 40);
    //设置JComboBox的尺寸
    final Dimension preferredJComboBoxSize = new Dimension(190, 40);
    ArrayList<String>  list = new ArrayList<String>();
    public getConfigBOMJPanel() {
        backgroundPanel = new JPanel(new BorderLayout());
        backgroundPanel.setName("ConfigBOMJPanel");
        AllTableServiceImpl goodsService = new AllTableServiceImpl();
        try {
            list = goodsService.TableNameSelectList();
            if (validateTableListExist(list)) {
                initTopPanel();
                initDetailShowPanel();
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
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));
        ModelMap.clear();
        for (int i=0;i<list.size();i++) {
            //System.out.println("getConfigBOMJPanel---sheetName---" + list.get(i));
            JPanel InfinityJPanel = creatModelSelectJPanel(list.get(i));
            searchPanel.add(InfinityJPanel);
        }
        topPanel.add(searchPanel, BorderLayout.CENTER);
    }

    public static HashMap<String, String> ModelMap = new HashMap<String, String>();
    public static HashMap<String, JTextField> ModelTextMap = new HashMap<String, JTextField>();

    public JPanel creatModelSelectJPanel(String modelname) {
        JPanel ComputeJPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel label_Model = new JLabel(modelname, JLabel.LEFT);
        label_Model.setPreferredSize(preferredLabelSize);
        JLabel label_Model_Multiply = new JLabel(" Multiple  ");
        // 种类下拉框
        JComboBox select_JComboBox = new JComboBox();
        select_JComboBox.setPreferredSize(preferredJComboBoxSize);
        select_JComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String select_value = ((Item) select_JComboBox.getSelectedItem()).getValue();
                ModelMap.put(modelname, select_value);
                //System.out.println("你选择了------"+select_value);
            }
        });

        ModelListServiceImpl infinityService = new ModelListServiceImpl(modelname);
        List<Object[]> infinity_ModelList = null;
        try {
            infinity_ModelList = infinityService.selectAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //select_JComboBox.addItem(new Item("全部", "全部"));
        if (infinity_ModelList != null) {
            for (Object[] object : infinity_ModelList) {
                select_JComboBox.addItem(new Item((String) object[0], (String) object[0]));
            }
        }
        JTextField ModelMuitiText = new JTextField();
        ModelTextMap.put(modelname, ModelMuitiText);
        ModelMuitiText.setPreferredSize(new Dimension(120, 40));
        ModelMuitiText.setForeground(Color.PINK);
        ModelMuitiText.setFont(new Font("标楷体", Font.TRUETYPE_FONT | Font.ITALIC, 18));
        JButton ModelGButton = new JButton("Go");
        ModelGButton.setPreferredSize(preferredButtonSize);
        ModelGButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == ModelGButton) {
                    String MuitiStr = ModelMuitiText.getText();
                    if ( "".equals(MuitiStr) || !isNumeric(MuitiStr)) {
                        Object[] options = {"OK"};
                        int response = JOptionPane.showOptionDialog(backgroundPanel, "Input the correct multiple in " + modelname + " firstly!",
                                "Warning", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                    }else if (!validateTableNameExist("Agile_BOM") || !validateTableNameExist("EMC_MAKE_BUY")){
                        Object[] options = {"OK"};
                        int response = JOptionPane.showOptionDialog(backgroundPanel, "Input the table Agile BOM & EMC MAKE BUY correctly!",
                                "Warning", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                    } else {
                        if (!validateTableNameExist("Agile_EMC"))
                            AgileEMCTable();

                        String select_value = ((Item) select_JComboBox.getSelectedItem()).getValue();
                        int input_value = Integer.parseInt(MuitiStr);
                        //System.out.println("你点击了按钮------"+modelname+"  "+select_value+"  "+input_value);
                                backgroundPanel.remove(showBOMPanel);
                                searchModelTable(modelname, select_value, input_value);
                        }
                    }
                }
        });
        ComputeJPanel.add(label_Model);
        ComputeJPanel.add(select_JComboBox);
        ComputeJPanel.add(Box.createHorizontalStrut(20));
        ComputeJPanel.add(label_Model_Multiply);
        ComputeJPanel.add(ModelMuitiText);
        ComputeJPanel.add(ModelGButton);
        return ComputeJPanel;
    }


    /**
     * 利用正则表达式判断字符串是否是数字
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str.trim());
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    // 初始化数据表格面板
    public void initDetailShowPanel() {
        searchModelTable(null, "", 0);
    }

    public void searchModelTable(String modelname, String select_value, int input_value) {
        String params[] = {"Quantity", "Level", "Level_1_Component", "Description", "EMC",};
        if (!select_value.equals("")) {
            BOMQueryVector.clear();
            ModelQuery(modelname, select_value, input_value);
        }
        BaseTableModule baseTableModule = new BaseTableModule(params, BOMQueryVector);
        JTable table = new JTable(baseTableModule);
        Tools.setTableStyle(table);
        jScrollPane = new JScrollPane(table);
        Tools.setJspStyle(jScrollPane);

        showBOMPanel = new JPanel(new BorderLayout());
        showBOMPanel.setOpaque(false);
        showBOMPanel.add(jScrollPane);
        backgroundPanel.add(showBOMPanel, "Center");
        backgroundPanel.updateUI();
    }

    public void ModelQuery(String modelname, String select_value, int input_value) {
        StringBuilder sqlBuilder = new StringBuilder(
                "select   Quantity, L1Comp, Agile_EMC.Description, New_Version from '" + modelname +
                        "' ,Agile_EMC WHERE Model='" + select_value + "' and L1Comp=Agile_EMC.Number  ");
        String sql = sqlBuilder.toString();
        List<Object[]> list = null;
        try {
            list = select(sql, 4, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        QueryMaxID();
        if (!list.isEmpty()) {
            for (Object[] object : list) {
                Vector temp = new Vector<String>();
                temp.add((int) object[0] * input_value);
                String Number = (String) object[1];
                Object[] PathNumberList = pathNumberBOMQuery(Number);
                temp.add(PathNumberList[1]);//level
                temp.add(object[1]);//number
                temp.add(object[2]);//desc
                temp.add(object[3]);//version
                BOMQueryVector.add(temp);
                String version = (String) object[3];
                if (!version.equals("BUY") && PathNumberList[0] != null && (int) PathNumberList[0] != maxID)//说明找得到，往下继续查找
                    //FindChild((int) object[0] * input_value, PathNumberList);
                    getChild((int) object[0] * input_value, PathNumberList);
            }
        }
    }
    private static int NodeID = 0;
    private static int MarkLevel = 0;
    private static Vector<Integer> levelVector = new Vector<Integer>();
    private static HashMap<Integer, AgileBOM> idAgileBOM = new HashMap<Integer, AgileBOM>();

    public static void getChild(int Quantity, Object[] PathNumberList) {
        NodeID = (int) PathNumberList[0];
        MarkLevel = Integer.parseInt((String) PathNumberList[1]);
        QueryMaxID();
        int nextNodeID = getNextNode(PathNumberList, NodeID) - 1;
        //System.out.println("NodeID---" + NodeID + "  nextNodeID---" + nextNodeID + "  MarkLevel---" + MarkLevel + "  maxID---" + maxID);
        try {
            MarkID(NodeID, nextNodeID);//标注levelVector, idAgileBOM
        } catch (SQLException e) {
            e.printStackTrace();
        }
        traverseNode(Quantity, MarkLevel, NodeID, nextNodeID);
    }

    public static int getNextNode(Object[] PathNumberList, int NodeID) {
        //System.out.println("---" + PathNumberList[0] + "---" + PathNumberList[1] + "---");
        int level = Integer.parseInt((String) PathNumberList[1]);
        int id = (int) PathNumberList[0];
        if (id == maxID || level == 0) {//
            id = maxID;
        } else if (level <= MarkLevel && id != NodeID) {
            return id;
        } else {//   level=1,level=0
            int idQuery = id + 1;
            StringBuilder sqlBuilder = new StringBuilder(
                    "select id, Level from Agile_BOM WHERE id= '" + idQuery + "' ");
            String sql = sqlBuilder.toString();
            Object[] array = null;
            try {
                array = selectArray(sql, 2, null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            id = getNextNode(array, NodeID);
        }
        return id;
    }

    public static void MarkID(int NID, int nextNID) throws SQLException {
        StringBuilder sqlBuilder = new StringBuilder(
                "SELECT * FROM Agile_EMC WHERE id between " + NID + " and " + nextNID);
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

    public static void traverseNode(int Quantity, int level, int id, int end) {
        //System.out.println("level---" + level + "   id---" + id + "   end---" + end);
        HashMap<Integer, Level_End> vertorSame = new HashMap<Integer, Level_End>();
        int nextEND = end;
        for (int i = end; i > id; i--) {
            Level_End LEVELEND = null;
            if (levelVector.get(i - NodeID) == (level + 1)) {
                LEVELEND = new Level_End(level + 1, nextEND);
                vertorSame.put(i, LEVELEND);
                nextEND = i - 1;
            }
        }
//        for (Map.Entry<Integer, Level_End> entry : vertorSame.entrySet()) {
//            int newID = entry.getKey();
//            Level_End newLEVELEND = entry.getValue();
//            System.out.println("id-Key: " + entry.getKey() + "  level-Value:  " + newLEVELEND.getLevel()
//                    + "  end-Value:  " + newLEVELEND.getEnd());
//        }
        if (vertorSame.isEmpty()) {//找不出与其相等的值
            return;
        } else { //不可能比当前level小
            AgileBOM AB = null;
            Vector temp = new Vector<String>();
            for (Map.Entry<Integer, Level_End> entry : vertorSame.entrySet()) {
                int newID = entry.getKey();
                Level_End newLEVELGAP = entry.getValue();

                AB = idAgileBOM.get(newID);
                if (AB.getVersion() != null && !AB.getVersion().equals("N/A")) {
                    temp = new Vector<String>();
                    temp.add(Quantity);//AB.getId()
                    temp.add(AB.getLevel());//level
                    temp.add(AB.getNumber());//number
                    temp.add(AB.getDescription());//desc
                    temp.add(AB.getVersion());//version
                    BOMQueryVector.add(temp);
                }
                if (newLEVELGAP.getEnd() != newID && AB.getVersion() != null && !AB.getVersion().equals("BUY") && !AB.getVersion().equals("N/A"))
                    traverseNode(Quantity, newLEVELGAP.getLevel(), newID, newLEVELGAP.getEnd());
            }
        }
    }
    public static int maxID = 0;
    public static int QueryMaxID() {
        StringBuilder sqlBuilder = new StringBuilder(
                "SELECT max(id) FROM Agile_EMC");
        String sql = sqlBuilder.toString();
        try {
            maxID = selectMaxID(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maxID;
    }
    public static Object[] pathNumberBOMQuery(String number) {// 选中一个即可
        StringBuilder sqlBuilder = new StringBuilder(
                "select * from Agile_EMC WHERE Number= '" + number + "' ");
        String sql = sqlBuilder.toString();
        Object[] list = null;
        try {
            list = selectArray(sql, 6, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    // 下拉框改变事件
    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
