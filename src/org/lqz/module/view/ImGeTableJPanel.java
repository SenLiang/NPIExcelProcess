package org.lqz.module.view;

import org.lqz.framework.util.BaseTableModule;
import org.lqz.framework.util.Item;
import org.lqz.framework.util.MyFont;
import org.lqz.framework.util.Tools;
import org.lqz.module.entity.AgileBOM;
import org.lqz.module.services.Impl.AllTableServiceImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

import static org.lqz.framework.util.Tools.fitTableColumns;
import static org.lqz.module.dao.Impl.BaseDaoImpl.*;
import static org.lqz.module.dao.Impl.BaseDaoImpl.selectArray;

public class ImGeTableJPanel implements ActionListener {

    // 定义全局组件
    JPanel backgroundPanel, topPanel, toolPanel, exportPanel, tablePanel;
    BaseTableModule baseTableModule;
    JTable table;
    JScrollPane jScrollPane;
    JButton tool_import, tool_export;
    JTextArea ResultText;
    IndexJFrame jframe;

    public ImGeTableJPanel() {
        backgroundPanel = new JPanel(new BorderLayout());
        backgroundPanel.setName("ImGeTableJPanel");
        initTopPanel();
        initTablePanel();
    }
    public void setJframe(IndexJFrame jframe) {
        this.jframe = jframe;
    }

    // 初始化顶部面板
    public void initTopPanel() {
        topPanel = new JPanel(new BorderLayout());
        initToolPanel();
        backgroundPanel.add(topPanel, "North");
    }

    // 初始化工具面板
    public void initToolPanel() {
        toolPanel = new JPanel(new BorderLayout());
        tool_import = new JButton("ImportExcel");
        tool_import.setToolTipText("导入表格");
        tool_import.setForeground(Color.ORANGE);
        tool_import.addActionListener(this);

        tool_export = new JButton("ExportExcel");
        tool_export.setToolTipText("导出表格");
        tool_export.setForeground(Color.BLUE);
        tool_export.addActionListener(this);

        toolPanel.add(tool_import, BorderLayout.WEST);
        toolPanel.add(tool_export, BorderLayout.EAST);
        topPanel.add(toolPanel, "Center");
    }

    // 初始化数据表格面板
    public void initTablePanel() {
        String params[] = {"id", "Number", "Quantity"};
        Vector<Vector> vector = new Vector<Vector>();

        AllTableServiceImpl goodsService = new AllTableServiceImpl();
        if (validateTableNameExist("Query_Config")) {
            try {
                vector = goodsService.QueryConfigSelectList();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        baseTableModule = new BaseTableModule(params, vector);
        table = new JTable(baseTableModule);

        Tools.setTableStyle(table);
        jScrollPane = new JScrollPane(table);
        Tools.setJspStyle(jScrollPane);
        tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        tablePanel.add(jScrollPane);
        backgroundPanel.add(tablePanel, "Center");

        ResultText = new JTextArea();
        //ResultText.setPreferredSize(new Dimension(500, 30));
        ResultText.setFont(new Font("标楷体", Font.TRUETYPE_FONT | Font.ITALIC, 18));
        ResultText.setLineWrap(true);
        ResultText.setForeground(Color.BLUE);
        backgroundPanel.add(ResultText, "South");
    }


    // 更新数据表格
    public void refreshImportPanel() {
        backgroundPanel.remove(tablePanel);
        String params[] = {"id", "Number", "Quantity"};
        AllTableServiceImpl emcService = new AllTableServiceImpl();
        Vector<Vector> vector = new Vector<Vector>();
        try {
            vector = emcService.QueryConfigSelectList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        baseTableModule = new BaseTableModule(params, vector);

        table = new JTable(baseTableModule);
        Tools.setTableStyle(table);
        jScrollPane = new JScrollPane(table);
        Tools.setJspStyle(jScrollPane);

        tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);

        tablePanel.add(jScrollPane);
        backgroundPanel.add(tablePanel, "Center");
        backgroundPanel.validate();
    }

    // 更新数据表格
    public void refreshExportPanel() {
        backgroundPanel.remove(tablePanel);
        String params[] = {"Quantity", "Level", "Level_1_Component", "Description", "EMC"};
        ImGeQueryVector.clear();
        QueryChild();
        baseTableModule = new BaseTableModule(params, ImGeQueryVector);

        table = new JTable(baseTableModule);
        Tools.setTableStyle(table);
        jScrollPane = new JScrollPane(table);
        Tools.setJspStyle(jScrollPane);

        tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);

        tablePanel.add(jScrollPane);
        backgroundPanel.add(tablePanel, "Center");
        backgroundPanel.validate();
    }

    public void QueryChild() {
        StringBuilder sqlBuilder = new StringBuilder(
                "SELECT Quantity, Query_Config.Number, Agile_EMC.Description, New_Version FROM \n" +
                        "Query_Config, Agile_EMC WHERE Query_Config.Number=Agile_EMC.Number");
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
                temp.add(object[0]);//quantity  Integer.parseInt((String) object[0]);
                String Number = (String) object[1];
                Object[] PathNumberList = pathNumberBOMQuery(Number);

                temp.add(PathNumberList[1]);//level
                temp.add(object[1]);//number
                temp.add(object[2]);//desc
                temp.add(object[3]);//version
                ImGeQueryVector.add(temp);
                String version = (String) object[3];
                if (!version.equals("BUY") && PathNumberList[0] != null && (int) PathNumberList[0] != maxID)//说明找得到，往下继续查找
                    getChild(Integer.parseInt((String) object[0]), PathNumberList);
            }
        }
    }

    private static int NodeID = 0;
    private static int MarkLevel = 0;
    private static Vector<Integer> levelVector = new Vector<Integer>();
    private static HashMap<Integer, AgileBOM> idAgileBOM = new HashMap<Integer, AgileBOM>();
    public static Vector<Vector> ImGeQueryVector = new Vector<Vector>();

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
                    ImGeQueryVector.add(temp);
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

    private FileOperation fileOperation;

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == tool_import) {
            fileOperation = new FileOperation(this.jframe);
            if (fileOperation.open()) {
                boolean isImport = fileOperation.importToSQL("ImportConfig");
                if (isImport) {
                    Object[] options = {"OK"};
                    int response = JOptionPane.showOptionDialog(this.jframe, "Import this table successfully!",
                            "Tips", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                    //更新Table，让其显示
                    refreshImportPanel();
                }
            }
        } else if (e.getSource() == tool_export) {

            if (!validateTableNameExist("Query_Config")) {
                Object[] options = {"OK"};
                int response = JOptionPane.showOptionDialog(backgroundPanel, "Import the table firstly!",
                        "Warning", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            } else {
                //更新Table，让其显示
                refreshExportPanel();
                FileOperation fileOperation = new FileOperation(jframe);
                boolean isSaveSuccess = fileOperation.SaveAs("ImportConfig");
                if (isSaveSuccess)
                    JOptionPane.showOptionDialog(backgroundPanel, "Export this table successfully!",
                            "Tips", JOptionPane.DEFAULT_OPTION,
                            JOptionPane.INFORMATION_MESSAGE, null, null, null);
            }
        }
    }
}

