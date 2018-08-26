package org.lqz.module.services.Impl;

import org.lqz.module.dao.Impl.BaseDaoImpl;

import java.util.List;
import java.util.Vector;

import static org.lqz.module.dao.Impl.BaseDaoImpl.*;

public class ModelListServiceImpl{

	String modelName;
	public ModelListServiceImpl(String modelName) {
		this.modelName=modelName;
	}

	//遍历所有仓库
	public List selectAll() throws Exception {
		List list = select("select DISTINCT Model from '"+modelName+"'", 1, null);
		if (!list.isEmpty()) {
			return list;
		}
		return null;
	}
	
	//遍历所有仓库返回Vector
	public Vector<Vector> selectAllVexctor() throws Exception {
		Vector<Vector> rows = new Vector<Vector>();
		List<Object[]> list = select("select Model,L1Comp,CompDesc,MakeBuy,Quantity from '"+modelName+"'",
				5, null);
		if (!list.isEmpty()) {
			//int number = 1;
			for (Object[] object : list) {
				Vector temp = new Vector<String>();
				for (int i = 0; i < object.length; i++) {
//					if (i == 1) {
//						temp.add(number);
//					} else {
						temp.add(object[i]);
					//}
				}
				rows.add(temp);
				//number++;
			}
		}
		return rows;
	}

}
