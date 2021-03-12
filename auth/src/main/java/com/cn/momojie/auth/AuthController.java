package com.cn.momojie.auth;

import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.cn.momojie.auth.param.WxLoginParam;
import com.cn.momojie.auth.service.UserService;
import com.cn.momojie.auth.session.SessionConfig;
import com.cn.momojie.auth.session.SessionOpService;
import com.cn.momojie.auth.session.UserInfo;
import com.cn.momojie.basic.vo.MessageResult;
import com.cn.momojie.utils.HttpUtils;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("auth")
@Slf4j
public class AuthController {

	private static final SimpleDateFormat format = new SimpleDateFormat("MMdd");

	private static final String WX_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";

	@Autowired
	private SessionOpService sessionOpService;

	@Autowired
	private UserService userService;

	@RequestMapping(path = "getUserInfo", method = RequestMethod.GET)
	@ResponseBody
	public MessageResult<UserInfo> getUserInfo() {
		MessageResult<UserInfo> result = new MessageResult<>();
		result.setData(SessionUtil.get());
		return result;
	}

	/**
	 * https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/login/auth.code2Session.html
	 * @param param
	 * @return
	 */
	@RequestMapping(path = "wxLogin", method = RequestMethod.POST)
	@ResponseBody
	public UserInfo wxLogin(@RequestBody WxLoginParam param) {
		String appId = System.getenv(param.getProgram().toUpperCase() + "_ID" );
		String appSecret = System.getenv(param.getProgram().toUpperCase() + "_SECRET" );

		String targetUrl = String.format(WX_LOGIN_URL, appId, appSecret, param.getCode());

		JSONObject resp = JSONObject.parseObject(HttpUtils.doGet(targetUrl, null));

		if (resp.containsKey("openid")) {
			UserInfo ui = userService.getOrCreateByWxOpenId(param.getProgram(), resp.getString("openid"));
			ui.setLogin(true);
			sessionOpService.write(SessionConfig.USER_KEY, ui);

			return ui;
		} else {
			log.error("微信登陆失败 {}", resp.getString("errmsg"));
		}

		return new UserInfo();
	}
}
