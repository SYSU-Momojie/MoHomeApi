package com.cn.momojie.blog.controller;

import com.cn.momojie.blog.constant.MsgCode;
import com.cn.momojie.blog.dao.GalleryDao;
import com.cn.momojie.blog.vo.GalleryGroupVo;
import com.cn.momojie.blog.vo.GalleryPicVo;
import com.cn.momojie.blog.vo.MessageResult;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("gallery")
@Slf4j
public class GalleryController {

	private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");

	private static final Random RAN = new Random();

	@RequestMapping(value = "upload", method = RequestMethod.POST)
	@ResponseBody
	public MessageResult<GalleryPicVo> upload(HttpServletRequest request, MultipartFile file, @RequestParam("type") String type) {
		MessageResult<GalleryPicVo> result = new MessageResult<>();
		String oldName = file.getOriginalFilename();
		String realPath = getImageDir();
		mkdir(realPath);
		String newFileName = String.format("%s%03d", TIME_FORMAT.format(new Date()), RAN.nextInt(9)) + oldName.substring(oldName.lastIndexOf('.'));
		String retPath = null;
		if (StringUtils.isEmpty(type)) {
			retPath = newFileName;
		} else {
			retPath = type + "/" + newFileName;
		}
		File newFile = new File(realPath, retPath);
		newFile.getParentFile().mkdir();
		try {
			file.transferTo(newFile);
		} catch (Exception e) {
			log.error("保存图片到硬盘失败", e);
			result.setFail("保存图片到硬盘失败");
		}

		if (MsgCode.SUCCESS.equals(result.getCode())) {
			GalleryPicVo vo = new GalleryPicVo();
			vo.setGroupCode(type);
			vo.setFileName(newFileName);
			galleryDao.addGalleryPic(vo);
			result.setData(vo);
		}

		return result;
	}

	private void mkdir(String path) {
		File file = new File(path);
		file.mkdir();
	}

	@RequestMapping(value="/show", method = RequestMethod.GET)
	public void show(@RequestParam("type") String type, @RequestParam("name") String name, HttpServletRequest request, HttpServletResponse response) {
		String path = String.format("%s/%s", type, name);
		String realPath = getImageDir();
		String realFileName = String.format("%s/%s", realPath, path);
		response.setContentType("image/jpeg/jpg/png/gif/bmp/tiff/svg"); // 设置返回内容格式
		InputStream in = null;
		OutputStream os = null;
		try {
			File file = new File(realFileName);       //括号里参数为文件图片路径
			if (file.exists()) {   //如果文件存在
				in = new FileInputStream(realFileName);   //用该文件创建一个输入流
				os = response.getOutputStream();  //创建输出流
				byte[] b = new byte[1024];
				while (in.read(b) != -1) {
					os.write(b);
				}
				response.setStatus(200);
				os.flush();
			}
		} catch (Exception e) {
			log.error("获取图片失败 {}", path, e);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e) {
				log.error("关闭读取流失败 {}", path, e);
			}
			try {
				if (os != null) {
					os.close();
				}
			} catch (Exception e) {
				log.error("关闭输出流失败 {}", path, e);
			}
		}
	}

	@Autowired
	private GalleryDao galleryDao;

	@RequestMapping(path = "getGroups", method = RequestMethod.GET)
	@ResponseBody
	public MessageResult<List<GalleryGroupVo>> getGroups() {
		MessageResult<List<GalleryGroupVo>> result = new MessageResult<>();
		result.setData(galleryDao.getGroups());
		return result;
	}

	private String getImageDir() {
		return System.getenv("BLOG_IMAGE_DIR");
	}
}
