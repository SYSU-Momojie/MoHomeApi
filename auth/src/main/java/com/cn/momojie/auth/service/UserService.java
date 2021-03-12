package com.cn.momojie.auth.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.momojie.auth.constant.WeChatMiniProgram;
import com.cn.momojie.auth.dao.UserDao;
import com.cn.momojie.auth.dto.UserDto;
import com.cn.momojie.auth.param.UserParam;
import com.cn.momojie.auth.session.UserInfo;

@Service
public class UserService {

	@Autowired
	private UserDao userDao;

	public UserInfo getOrCreateByWxOpenId(String program, String openId) {
		UserParam param = new UserParam();
		if (WeChatMiniProgram.MO_TALK.equals(program)) {
			param.setMoTalkOpenId(openId);
		}
		UserDto user = userDao.getUser(param);
		if (user == null) {
			user = new UserDto();
			if (WeChatMiniProgram.MO_TALK.equals(program)) {
				user.setMoTalkOpenId(openId);
			}
			userDao.insert(unionid);
		}

		List<String> roles = userDao.getRoles(u);

		UserInfo ui = new UserInfo();
		ui.setWxId(u);
		ui.setRoles(roles);
		return ui;
	}
}
