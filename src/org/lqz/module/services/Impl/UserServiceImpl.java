package org.lqz.module.services.Impl;

import java.util.List;

import org.lqz.module.entity.User;

import static org.lqz.module.dao.Impl.BaseDaoImpl.select;
import static org.lqz.module.dao.Impl.BaseDaoImpl.update;

public class UserServiceImpl {
	
	//查询一条记录
	public User selectOne(Object[] paraArray) throws Exception {
		User user = new User();
		String sql = "select id,name,password,identity from user where name=? and password=?";
		List list = select(sql, 4, paraArray);
		if (!list.isEmpty()) {
			user.setId((String) ((Object[]) list.get(0))[0]);
			user.setName((String) ((Object[]) list.get(0))[1]);
			user.setPassword((String) ((Object[]) list.get(0))[2]);
			return user;
		}
		return null;
	}
	
	//通过Id修改用户
	public int updateUserById(Object[] paraArray) throws Exception {
		int result = 0;
		String sql = "update user set name = ?,password=? where id=?";
		result = update(sql, paraArray);
		return result;
	}

}
