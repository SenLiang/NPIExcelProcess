package org.lqz.module.view;
import org.lqz.framework.util.BaseTableModule;
import org.lqz.framework.util.Tools;
import org.lqz.module.entity.AgileBOM;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import static org.lqz.module.dao.Impl.BaseDaoImpl.select;
import static org.lqz.module.dao.Impl.BaseDaoImpl.selectArray;
import static org.lqz.module.dao.Impl.BaseDaoImpl.selectMaxID;
import static org.lqz.module.view.getConfigBOMJPanel.ModelMap;
import static org.lqz.module.view.getConfigBOMJPanel.ModelTextMap;
import static org.lqz.module.view.getConfigBOMJPanel.isNumeric;

public class GenerateConfigTableJPanel {
    // 定义全局组件
    static JPanel backgroundPanel, topPanel, toolPanel, searchPanel, showBOMPanel, btPanel;
    static JButton ExportButton;

    static JScrollPane jScrollPane;
    public static Vector<Vector> ModelQueryVector = new Vector<Vector>();
    static IndexJFrame jframe;
    public GenerateConfigTableJPanel() {
        backgroundPanel = new JPanel(new BorderLayout());
        backgroundPanel.setName("GenerateConfigTableJPanel");
        initTableShowPanel();
    }

    public void setJframe(IndexJFrame jframe) {
        this.jframe = jframe;
    }

    // 初始化数据表格面板
    public void initTableShowPanel() {
        String params[] = {"Quantity", "Level_1_Component", "Component_Description", "MAKE_v_BUY",};
        BaseTableModule baseTableModule = new BaseTableModule(params, ModelQueryVector);
        JTable table = new JTable(baseTableModule);
        Tools.setTableStyle(table);
        jScrollPane = new JScrollPane(table);
        Tools.setJspStyle(jScrollPane);
        showBOMPanel = new JPanel(new BorderLayout());
        showBOMPanel.setOpaque(false);
        showBOMPanel.add(jScrollPane);
        backgroundPanel.add(showBOMPanel, "Center");

        btPanel = new JPanel();
        btPanel.setPreferredSize(new Dimension(200, 30));
        ExportButton = new JButton("Export This Table");
        ExportButton.setPreferredSize(new Dimension(200, 30));// 设置按钮大小
        btPanel.add(ExportButton, BorderLayout.CENTER);
        backgroundPanel.add(btPanel, BorderLayout.SOUTH);
        //backgroundPanel.updateUI();
    }

    // 初始化数据表格面板
    public static void updateTableShowPanel() {
        String params[] = {"Quantity", "Level", "Level_1_Component", "Description", "EMC"};
        backgroundPanel.remove(showBOMPanel);
        backgroundPanel.remove(btPanel);
        ModelQueryVector.clear();
        for (String key : ModelMap.keySet()) {
            String modelname = key;
            String modelvalue = ModelMap.get(key);
            String multiplevalue = ModelTextMap.get(key).getText().trim();
            //System.out.println("Key: "+key+" ModelValue: "+ModelMap.get(key)+" MultipleValue: "+multiplevalue);
//            if ( !multiplevalue.equals("")&&!isNumeric(multiplevalue)) {//multiplevalue.equals("")
//                Object[] options = {"OK"};
//                int response = JOptionPane.showOptionDialog(backgroundPanel, "Input the correct multiple in " + modelname + " firstly!",
//                        "Warning", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
//                ModelQueryVector.clear();
//                break;
//            } else
            if (isNumeric(multiplevalue)&&!multiplevalue.trim().equals("0")&&!multiplevalue.trim().equals(""))  {
                int input_value = Integer.parseInt(multiplevalue);
                ModelQuery(modelname, modelvalue, input_value);
            }
        }
        BaseTableModule baseTableModule = new BaseTableModule(params, ModelQueryVector);
        JTable table = new JTable(baseTableModule);
        Tools.setTableStyle(table);
        jScrollPane = new JScrollPane(table);
        Tools.setJspStyle(jScrollPane);

        showBOMPanel = new JPanel(new BorderLayout());
        showBOMPanel.setOpaque(false);
        showBOMPanel.add(jScrollPane);

        backgroundPanel.add(showBOMPanel, "Center");
        btPanel = new JPanel();
        btPanel.setPreferredSize(new Dimension(200, 30));
        ExportButton = new JButton("Export This Table");
        ExportButton.setPreferredSize(new Dimension(200, 30));// 设置按钮大小

        ExportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                FileOperation fileOperation = new FileOperation(jframe);
                boolean isSaveSuccess = fileOperation.SaveAs("ConfigBOM");
                if (isSaveSuccess)
                    JOptionPane.showOptionDialog(backgroundPanel, "Export this table successfully!",
                            "Tips", JOptionPane.DEFAULT_OPTION,
                            JOptionPane.INFORMATION_MESSAGE, null, null, null);
            }
        });
        btPanel.add(ExportButton, BorderLayout.CENTER);
        backgroundPanel.add(btPanel, BorderLayout.SOUTH);
        backgroundPanel.updateUI();
    }


    public static void ModelQuery(String modelname, String select_value, int input_value) {
        StringBuilder sqlBuilder = new StringBuilder(
                "select Quantity, L1Comp, Agile_EMC.Description, New_Version from '" + modelname +
                        "', Agile_EMC WHERE Model='" + select_value + "' and L1Comp=Agile_EMC.Number  ");
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
                temp.add(object[0]);
                String Number = (String) object[1];
                Object[] PathNumberList = pathNumberBOMQuery(Number);
                temp.add(PathNumberList[1]);//level
                temp.add(object[1]);//number
                temp.add(object[2]);//desc
                temp.add(object[3]);//version
                ModelQueryVector.add(temp);
                String version = (String) object[3];
                if (!version.equals("BUY") && PathNumberList[0] != null && (int) PathNumberList[0] != maxID)//说明找得到，往下继续查找
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
                    ModelQueryVector.add(temp);
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
}
