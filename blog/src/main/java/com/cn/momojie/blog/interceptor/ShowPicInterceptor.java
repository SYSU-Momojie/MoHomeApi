package com.cn.momojie.blog.interceptor;

import com.cn.momojie.blog.SessionUtil;
import com.cn.momojie.blog.dao.GalleryDao;
import com.cn.momojie.blog.vo.GalleryGroupVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ShowPicInterceptor extends AuthInterceptor {

	@Autowired
	private GalleryDao galleryDao;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		Boolean auth = true;

		String galleryGroup = request.getParameter("type");
		if (!"blog".equals(galleryGroup)) {
			GalleryGroupVo vo = galleryDao.getGroupByCode(galleryGroup);
			if (vo == null || !SessionUtil.hasAuth(vo.getAuth())) {
				auth = false;
			}
		}

		if (auth) {
			return true;
		} else {
			return noAuthReturn(response);
		}
	}
}
