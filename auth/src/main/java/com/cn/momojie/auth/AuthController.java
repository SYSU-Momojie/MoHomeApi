package com.cn.momojie.auth;

import com.cn.momojie.auth.dao.UserDao;
import com.cn.momojie.auth.param.LoginParam;
import com.cn.momojie.auth.session.SessionConfig;
import com.cn.momojie.auth.session.SessionOpService;
import com.cn.momojie.auth.session.UserInfo;
import com.cn.momojie.basic.vo.MessageResult;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.List;

@Controller
@RequestMapping("auth")
@Slf4j
public class AuthController {

	private static final SimpleDateFormat format = new SimpleDateFormat("MMdd");

	@Autowired
	private SessionOpService sessionOpService;

	@Autowired
	private UserDao userDao;

	@RequestMapping(path = "login", method = RequestMethod.POST)
	@ResponseBody
	public MessageResult<UserInfo> login(@RequestBody LoginParam param) {
		MessageResult<UserInfo> result = new MessageResult<>();
		UserInfo info = null;
		if (SessionUtil.isLogin()) {
			info = SessionUtil.get();
		} else if (true) {
			info = new UserInfo();
			info.setLogin(true);
			info.setName(param.getName());
			List<String> roles = userDao.getRoles(param.getName());
			List<String> auths = userDao.getAuths(param.getName());
			info.setRoles(roles);
			info.setAuths(auths);
			sessionOpService.write(SessionConfig.UNIQUE_KEY, info.getName());
			sessionOpService.write(SessionConfig.ROLE, info.getRoles());
			sessionOpService.write(SessionConfig.AUTH, info.getAuths());
		} else {
			result.setFail("登陆密码错误");
		}

		result.setData(info);

		return result;
	}

	@RequestMapping(path = "logout", method = RequestMethod.POST)
	@ResponseBody
	public MessageResult<UserInfo> logout() {
		sessionOpService.invalidate();
		MessageResult<UserInfo> result = new MessageResult<>();
		UserInfo info = new UserInfo();
		result.setData(info);
		return result;
	}

	@RequestMapping(path = "getUserInfo", method = RequestMethod.GET)
	@ResponseBody
	public MessageResult<UserInfo> getUserInfo() {
		MessageResult<UserInfo> result = new MessageResult<>();
		result.setData(SessionUtil.get());
		return result;
	}

}
