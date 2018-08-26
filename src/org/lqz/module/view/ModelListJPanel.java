package org.lqz.module.view;

import org.lqz.framework.util.BaseTableModule;
import org.lqz.framework.util.Tools;
import org.lqz.module.services.Impl.ModelListServiceImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

public class ModelListJPanel{

	// 定义全局组件
	JPanel backgroundPanel, topPanel, toolPanel, tablePanel;
	BaseTableModule baseTableModule;
	JTable table;
	JScrollPane jScrollPane;
    String ModelName;
	public ModelListJPanel( String ModelName) {
		backgroundPanel = new JPanel(new BorderLayout());
		this.ModelName=ModelName;
		backgroundPanel.setName(ModelName);
		initTablePanel();
	}
	// 初始化数据表格面板
	public void initTablePanel() {
		String params[] = {"Model", "Level_1_Component", "Component_Description", "MAKE_v_BUY", "Quantity"};
		ModelListServiceImpl backService = new ModelListServiceImpl(ModelName);
		Vector<Vector> vector = new Vector<Vector>();
		try {
			vector = backService.selectAllVexctor();
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
	}
}
