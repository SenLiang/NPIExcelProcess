package org.lqz.module.view;

import org.lqz.module.entity.AgileBOM;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class ExcludeNANode extends JFrame {

    static JTree tree;
    static DefaultTreeModel newModel;
    static DefaultMutableTreeNode Node;
    public Vector<Integer> levelVector = new Vector<Integer>();
    public HashMap<Integer, AgileBOM> idAgileBOM = new HashMap<Integer, AgileBOM>();
    public int startID = 0;
    public int endID = 0;


    public ExcludeNANode(int startID, int endID, Vector<Integer> levelVector, HashMap<Integer, AgileBOM> idAgileBOM) {
        this.levelVector = levelVector;
        this.idAgileBOM = idAgileBOM;
        this.startID = startID;
        this.endID = endID;
    }

    public DefaultMutableTreeNode ExtraverseFolder(int level, int id, int end) {
        //System.out.println("level---"+level+"   id---"+id+"   end---"+end);
        AgileBOM getABbyID = idAgileBOM.get(id);
       // System.out.println("ABbyID.getVersion()---" + (getABbyID.getVersion() == null)+"---"+(getABbyID.getVersion().equals("N/A")));
        if (getABbyID.getVersion() == null|| getABbyID.getVersion().equals("N/A") )
            return null;
        else {
            DefaultMutableTreeNode fujiedian = new DefaultMutableTreeNode(getABbyID);
            HashMap<Integer, Level_End> vertorSame = new HashMap<Integer, Level_End>();
            int nextEND = end;
            for (int i = end; i > id; i--) {
                Level_End LEVELEND = null;
                if (levelVector.get(i - startID) == (level + 1)) {
                    LEVELEND = new Level_End(level + 1, nextEND);
                    vertorSame.put(i, LEVELEND);
                    nextEND = i - 1;
                }
            }
            if (vertorSame.isEmpty()) {//找不出与其相等的值
                return fujiedian;
                //不可能比当前level小
            } else {
                for (Map.Entry<Integer, Level_End> entry : vertorSame.entrySet()) {
                    int newID = entry.getKey();
                    Level_End newLEVELGAP = entry.getValue();
                    if (newLEVELGAP.getEnd() == newID) {//
                        AgileBOM NewgetABbyID = idAgileBOM.get(newID);
                        if (!(NewgetABbyID.getVersion() == null)&&!NewgetABbyID.getVersion().equals("N/A")  ) {
                            DefaultMutableTreeNode dn = new DefaultMutableTreeNode(NewgetABbyID);
                            fujiedian.add(dn);
                        }
                    } else //if (a[newID + 1] > a[newID])//a[newID+1]=a[newID]+1
                    {
                        DefaultMutableTreeNode NewTreeNode = ExtraverseFolder(newLEVELGAP.getLevel(), newID, newLEVELGAP.getEnd());
                        if (NewTreeNode!=null)
                           fujiedian.add(NewTreeNode);
                    }
                }
            }
            return fujiedian;
        }
    }

}