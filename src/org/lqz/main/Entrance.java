package org.lqz.main;

import javax.swing.UIManager;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import org.lqz.module.dao.Impl.BaseDaoImpl;
import org.lqz.module.entity.User;
import org.lqz.module.services.Impl.UserServiceImpl;
import org.lqz.module.view.IndexJFrame;

/**
 * 程序一开始执行的类
 * 
 * @author Administrator
 * 
 */
public class Entrance {

	public static void main(String[] args) {
		try {
			// 设置窗口边框样式
			BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.translucencyAppleLike;
			org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
			// 隐藏设置按钮
			UIManager.put("RootPane.setupButtonVisible", false);
		} catch (Exception e) {
			
		}
		new BaseDaoImpl();
		new IndexJFrame();
	}
}
