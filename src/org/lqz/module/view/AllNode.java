package org.lqz.module.view;

import org.lqz.module.entity.AgileBOM;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class AllNode extends JFrame{
    public  Vector<Integer> levelVector=new Vector<Integer>();
    public  HashMap<Integer, AgileBOM> idAgileBOM = new HashMap<Integer, AgileBOM>();
    public  int startID = 0;
    public  int endID = 0;


    public AllNode(int startID,int endID,Vector<Integer> levelVector,HashMap<Integer, AgileBOM> idAgileBOM ) {
        this.levelVector=levelVector;
        this.idAgileBOM=idAgileBOM;
        this.startID=startID;
        this.endID=endID;
    }
    public DefaultMutableTreeNode traverseFolder(int level, int id, int end) {
        //System.out.println("level---"+level+"   id---"+id+"   end---"+end);
        DefaultMutableTreeNode fujiedian = new DefaultMutableTreeNode(idAgileBOM.get(id));
        HashMap<Integer, Level_End> vertorSame = new HashMap<Integer, Level_End>();
        int nextEND = end;
        for (int i = end; i > id; i--) {
            Level_End LEVELEND = null;
            if (levelVector.get(i-startID) == (level+1)) {
                LEVELEND = new Level_End(level+1, nextEND);
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
            return fujiedian;
            //不可能比当前level小
        } else {
            for (Map.Entry<Integer, Level_End> entry : vertorSame.entrySet()) {
                int newID = entry.getKey();
                Level_End newLEVELGAP = entry.getValue();
                if (newLEVELGAP.getEnd()==newID) {//
                    DefaultMutableTreeNode dn = new DefaultMutableTreeNode(idAgileBOM.get(newID));
                    fujiedian.add(dn);
                } else //if (a[newID + 1] > a[newID])//a[newID+1]=a[newID]+1
                    fujiedian.add(traverseFolder(newLEVELGAP.getLevel(), newID, newLEVELGAP.getEnd()));
            }
        }
        return fujiedian;
    }

}

class Level_End {
    private int level;
    private int end;

    public Level_End(int level, int end) {
        this.level = level;
        this.end = end;
    }
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getEnd() {
        return end;
    }

    public void setGap(int gap) {
        this.end = end;
    }




}