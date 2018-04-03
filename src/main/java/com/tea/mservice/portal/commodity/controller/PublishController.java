package com.tea.mservice.portal.commodity.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.tea.mservice.portal.commodity.service.CommodityService;



/**
 * 发布模块
 *  
 *
 */
@Configuration
@Controller
public class PublishController {

	private static final Logger LOG = LoggerFactory.getLogger(PublishController.class);
	
	@Autowired
	private CommodityService commodityService;

	@GetMapping(value = "publish.html")
	public String goList(ModelMap map) {
		return getTemplatePath("list");
	}
	
	
	@GetMapping(value = "publish/imgs.html")
	public String goImgs(ModelMap map) {
		return getTemplatePath("imgs");
	}
	
	@GetMapping(value = "publish/upload.html")
	public String goUpload(ModelMap map) {
		return getTemplatePath("upload");
	}
	

	private String getTemplatePath(String fileName) {
		return "/publish/" + fileName;
	}
	
	/**
	 * 上传图片
	 * @param model
	 * @param request
	 * @param response
	 * @param type
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "publish/upload", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> upload(Model model, HttpServletRequest request, HttpServletResponse response,String type) throws IOException {

	
		Map<String, Object> messageMap = new HashMap<String, Object>();
		String fileName = "";
		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			Iterator<String> iterator = multipartRequest.getFileNames();
			while (iterator.hasNext()) {
				String key = iterator.next();
				MultipartFile multipartFile = multipartRequest.getFile(key);

				fileName = multipartFile.getOriginalFilename();


				return commodityService.upload(multipartFile,type);
	

			}

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			messageMap.put("res", 0);
			messageMap.put("msg", "图片:" + fileName + "处理异常,稍后重试！");
		}

		return messageMap;
	}
	
	
	/**
	 * 保存商品
	 */
	@RequestMapping(value = "publish", method = RequestMethod.POST)
	@ResponseBody
	public void saveCommodityController(@RequestBody String commodity){
		try {
			commodityService.saveCommodityController(commodity);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}

	
}
