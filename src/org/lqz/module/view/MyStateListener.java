package org.lqz.module.view;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.lqz.module.view.GenerateConfigTableJPanel.updateTableShowPanel;

public class MyStateListener implements ChangeListener {
    private IndexJFrame frame;

    public MyStateListener(IndexJFrame indexJFrame) {
        this.frame = indexJFrame;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        JTabbedPane JTP = ((JTabbedPane) e.getSource());
        int index = ((JTabbedPane) e.getSource()).getSelectedIndex();
        String name = ((JTabbedPane) e.getSource()).getSelectedComponent().getName();
        if (name.equals("AgileBOMDBJPanel")) {
            JTP.setForegroundAt(index, Color.ORANGE);
            JTP.setForegroundAt(index + 1, Color.BLACK);
            JTP.setForegroundAt(index + 2, Color.BLACK);
        } else if (name.equals("EMCDBJPanel")) {
            JTP.setForegroundAt(index - 1, Color.BLACK);
            JTP.setForegroundAt(index, Color.ORANGE);
            JTP.setForegroundAt(index + 1, Color.BLACK);
        } else if (name.equals("AgileEMCJPanel")) {
            JTP.setForegroundAt(index - 2,Color.BLACK);
            JTP.setForegroundAt(index - 1, Color.BLACK);
            JTP.setForegroundAt(index, Color.ORANGE);
        } else if (name.equals("ShowIncludeNTreeJPanel")) {
            JTP.setForegroundAt(index,Color.RED);
            JTP.setForegroundAt(index + 1, Color.BLACK);
            JTP.setForegroundAt(index + 2, Color.BLACK);
        }else if (name.equals("ShowExcludeNTreeJPanel")) {
            JTP.setForegroundAt(index - 1, Color.BLACK);
            JTP.setForegroundAt(index,Color.RED);
            JTP.setForegroundAt(index + 1, Color.BLACK);
        }else if (name.equals("ShowFullTree")) {
            JTP.setForegroundAt(index - 1, Color.BLACK);
            JTP.setForegroundAt(index - 2, Color.BLACK);
            JTP.setForegroundAt(index,Color.RED);
        } else if (name.equals("ConfigBOMJPanel")) {
            JTP.setForegroundAt(index,Color.BLUE);
            JTP.setForegroundAt(index + 1, Color.BLACK);
            JTP.setForegroundAt(index + 2, Color.BLACK);
        }else if (name.equals("GenerateConfigTableJPanel")) {
            JTP.setForegroundAt(index - 1, Color.BLACK);
            JTP.setForegroundAt(index,Color.BLUE);
            JTP.setForegroundAt(index + 1, Color.BLACK);
            updateTableShowPanel();
        }else if (name.equals("ImGeTableJPanel")) {
            JTP.setForegroundAt(index - 1, Color.BLACK);
            JTP.setForegroundAt(index - 2, Color.BLACK);
            JTP.setForegroundAt(index,Color.BLUE);
        }
    }
}
