package cn.appsys.controller.developer;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;

import cn.appsys.controller.backend.BackendController;
import cn.appsys.pojo.Appcategory;
import cn.appsys.pojo.Appinfo;
import cn.appsys.pojo.Appversion;
import cn.appsys.pojo.Datadictionary;
import cn.appsys.pojo.Devuser;
import cn.appsys.service.appcategory.AppcategoryService;
import cn.appsys.service.appinfo.AppinfoService;
import cn.appsys.service.appversion.AppversionService;
import cn.appsys.service.datadictionary.DatadictionaryService;
import cn.appsys.tools.PageSupport;

@Controller
@RequestMapping("Developer_AppInfo")
public class DeveloperController {

	@Resource
	private AppinfoService appinfoService;
	@Resource
	private AppcategoryService appCategoryService;
	@Resource
	private DatadictionaryService datadictionaryService;
	@Resource
	private AppversionService appversionService;
	
	private Logger logger = Logger.getLogger(BackendController.class);
	
	@RequestMapping("/list")
	public String appList(HttpServletRequest request,
						  HttpSession session,
						  @RequestParam(value="querySoftwareName",required=false) String _querySoftwareName,
						  @RequestParam(value="queryFlatformId",required=false) String _queryFlatformId,
						  @RequestParam(value="queryStatus",required=false) String _queryStatus,
						  @RequestParam(value="queryCategoryLevel1",required=false) String _queryCategoryLevel1,
						  @RequestParam(value="queryCategoryLevel2",required=false) String _queryCategoryLevel2,
						  @RequestParam(value="queryCategoryLevel3",required=false) String _queryCategoryLevel3,
						  @RequestParam(value="pageIndex",required=false) String pageIndex){
		
		logger.debug("querySoftwareName ===============================>>>   "+_querySoftwareName);
		logger.debug("queryFlatformId ===============================>>>   "+_queryFlatformId);
		logger.debug("queryStatus ===============================>>>   "+_queryStatus);
		logger.debug("queryCategoryLevel1 ===============================>>>   "+_queryCategoryLevel1);
		logger.debug("queryCategoryLevel2 ===============================>>>   "+_queryCategoryLevel2);
		logger.debug("queryCategoryLevel3 ===============================>>>   "+_queryCategoryLevel3);
		logger.debug("pageIndex ===============================>>>   "+pageIndex);
		
		List<Appinfo> appInfoList = null;
		List<Datadictionary> datadictionaryList = null;
		List<Datadictionary> statusList = null;
		List<Appcategory> categoryLevel1List = null;	//列出一级分类列表，注：二级和三级分类列表通过异步ajax获取
		List<Appcategory> categoryLevel2List = null;
		List<Appcategory> categoryLevel3List = null;
		Integer devId = ((Devuser)session.getAttribute("devUserSession")).getId();
		
		//当前页码
		Integer currentPageNo = 1;
		
		if(pageIndex != null){
			try {
				currentPageNo =Integer.parseInt(pageIndex);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		Integer queryStatus = null;
		if(_queryStatus != null && !_queryStatus.equals("")){
			queryStatus = Integer.parseInt(_queryStatus);
		}
		Integer queryCategoryLevel1 = null; 
		if(_queryCategoryLevel1 != null && !_queryCategoryLevel1.equals("")){
			queryCategoryLevel1 = Integer.parseInt(_queryCategoryLevel1);
		}
		Integer queryCategoryLevel2= null; 
		if(_queryCategoryLevel2 != null && !_queryCategoryLevel2.equals("")){
			queryCategoryLevel2 = Integer.parseInt(_queryCategoryLevel2);
		}
		Integer queryCategoryLevel3 = null; 
		if(_queryCategoryLevel3 != null && !_queryCategoryLevel3.equals("")){
			queryCategoryLevel3 = Integer.parseInt(_queryCategoryLevel3);
		}
		Integer queryFlatformId = null; 
		if(_queryFlatformId != null && !_queryFlatformId.equals("")){
			queryFlatformId = Integer.parseInt(_queryFlatformId);
		}
		
		//总数量(表)
		int totalCount = 0;
		try {
			totalCount = appinfoService.getAppInfoCount(_querySoftwareName,queryStatus,queryCategoryLevel1, 
					queryCategoryLevel2, queryCategoryLevel3,queryFlatformId,devId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//总页数
		PageSupport page = new PageSupport();
		page.setCurrentPageNo(currentPageNo);
		page.setTotalCount(totalCount);
		int totalPageCount = page.getTotalPageCount();
		//控制首页和尾页
		if(currentPageNo < 1){
			currentPageNo = 1;
		}else if(currentPageNo > totalPageCount){
			currentPageNo = totalPageCount;
		}
		try {
			appInfoList = appinfoService.getAppinfoList(_querySoftwareName,queryStatus, queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3, queryFlatformId,devId, currentPageNo, page.getPageSize());
			categoryLevel1List = appCategoryService.getAppcategoryByParentIdList(null);
			datadictionaryList = datadictionaryService.getDatadictionaryByTypeCode("APP_FLATFORM");
			statusList = datadictionaryService.getDatadictionaryByTypeCode("APP_STATUS");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		request.setAttribute("appInfoList",appInfoList);
		request.setAttribute("flatFormList",datadictionaryList);
		request.setAttribute("statusList",statusList);
		request.setAttribute("pages",page);
		request.setAttribute("queryStatus",queryStatus);
		request.setAttribute("categoryLevel1List",categoryLevel1List);
		request.setAttribute("queryFlatformId",queryFlatformId);
		request.setAttribute("queryCategoryLevel1",queryCategoryLevel1);
		request.setAttribute("queryCategoryLevel2",queryCategoryLevel2);
		request.setAttribute("queryCategoryLevel3",queryCategoryLevel3);
		request.setAttribute("querySoftwareName",_querySoftwareName);
		request.setAttribute("categoryLevel2List",categoryLevel2List);
		request.setAttribute("categoryLevel3List",categoryLevel3List);
		
		if(queryCategoryLevel1 != null && !queryCategoryLevel1.equals("")){
			categoryLevel2List = appCategoryService.getAppcategoryByParentIdList(queryCategoryLevel1);
			request.setAttribute("categoryLevel2List",categoryLevel2List);
		}
		if(queryCategoryLevel2 != null && !queryCategoryLevel2.equals("")){
			categoryLevel3List = appCategoryService.getAppcategoryByParentIdList(queryCategoryLevel2);
			request.setAttribute("categoryLevel3List",categoryLevel3List);
		}
		return "developer/appinfolist";
	}
	
	/**
	 * 根据parentId查询出相应的分类级别列表
	 * @param pid
	 * @return
	 */
	@RequestMapping(value="/categorylevellist.json",method=RequestMethod.GET)
	@ResponseBody
	public List<Appcategory> getAppCategoryList (@RequestParam String pid){
		logger.debug("getAppCategoryList pid ============ " + pid);
		if(pid.equals("")){ pid = null;}
		return appCategoryService.getAppcategoryByParentIdList(pid==null?null:Integer.parseInt(pid));
	}
	
	/**
	 * 进入新增APP基础信息页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/appinfoadd")
	public String Appinfoadd(HttpServletRequest request){
		logger.debug("Appinfoadd() ===============================");
		/*List<Datadictionary> datadictionaryList = datadictionaryService.getDatadictionaryByTypeCode("APP_FLATFORM");
		request.setAttribute("",datadictionaryList);*/
		return "developer/appinfoadd";
	}
	
	/**
	 * 新增App基础版本信息
	 * @param appInfo
	 * @param session
	 * @param request
	 * @param attach
	 * @return
	 */
	@RequestMapping(value="/appinfoaddsave",method=RequestMethod.POST)
	public String AppinfoaddSave(Appinfo appInfo,
								HttpSession session,
								HttpServletRequest request,
								@RequestParam(value="a_logoPicPath",required=false) MultipartFile attach){
		String logoPicPath = null;
		String logoLocPath =  null;
		//判断文件是否为空
		if(!attach.isEmpty()){
			String path = request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
			logger.info("uploadFile path =========== > "+path);
			String oldFileName = attach.getOriginalFilename();	//原文件名
			String prefix = FilenameUtils.getExtension(oldFileName);	//原文件后缀
			int filesize = 500000;
			if(attach.getSize() > filesize){		//上传大小不得超过50kb
				request.setAttribute("uploadFileError"," * 上传文件过大！");
				return "developer/appinfoaddsave";
			}else if(prefix.equalsIgnoreCase("jpg") 
					|| prefix.equalsIgnoreCase("png")
					|| prefix.equalsIgnoreCase("jpeg")){	//上传图片格式
				String fileName = appInfo.getAPKName() + ".jpg";	//上传LOGO图片命名：apk名称.apk
				File targetFile = new File(path,fileName);
				if(!targetFile.exists()){
					targetFile.mkdirs();
				}
				try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
					request.setAttribute("fileUploadError"," * 上传失败！");
					return "developer/appinfoadd";
				}
				logoPicPath = request.getContextPath()+"/statics/uploadfiles/"+fileName;
				logoLocPath = path+File.separator+fileName;
			
			}else{
				request.setAttribute("fileUploadError"," * 上传文件格式不正确！");
				return "developer/appinfoadd";
			}
		}
		Devuser devUser = (Devuser)session.getAttribute("devUserSession");
		appInfo.setCreatedBy(devUser.getId());
		appInfo.setCreationDate(new Date());
		appInfo.setLogoPicPath(logoPicPath);
		appInfo.setLogoLocPath(logoLocPath);
		appInfo.setDevId(devUser.getId());
		appInfo.setStatus(1);
		try {
			if(appinfoService.addAppinfo(appInfo) > 0){
				return "redirect:/Developer_AppInfo/list";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "developer/appinfoadd";
	}
	
	@RequestMapping(value="/appinfomodifysave",method=RequestMethod.POST)
	public String appInfoModifySave(Appinfo appInfo,
									HttpServletRequest request,
									HttpSession session,
									@RequestParam(value="attach",required=false) MultipartFile attach){
		logger.info("appInfoModifySave ===============================  >>>>>");
		String logoPicPath = null;
		String logoLocPath =  null;
		String APKName = appInfo.getAPKName();
		//判断文件是否为空
		if(!attach.isEmpty()){
			String path = request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
			logger.info("uploadFile path =========== > "+path);
			String oldFileName = attach.getOriginalFilename();	//原文件名
			String prefix = FilenameUtils.getExtension(oldFileName);	//原文件后缀
			int filesize = 500000;
			if(attach.getSize() > filesize){		//上传大小不得超过50kb
				request.setAttribute("uploadFileError"," * 上传文件过大！");
				return "developer/appinfoaddsave";
			}else if(prefix.equalsIgnoreCase("jpg") 
					|| prefix.equalsIgnoreCase("png")
					|| prefix.equalsIgnoreCase("jpeg")){	//上传图片格式
				String fileName = APKName + ".jpg";	//上传LOGO图片命名：apk名称.apk
				File targetFile = new File(path,fileName);
				if(!targetFile.exists()){
					targetFile.mkdirs();
				}
				try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
					request.setAttribute("fileUploadError"," * 上传失败！");
					return "developer/appinfoadd";
				}
				logoPicPath = request.getContextPath()+"/statics/uploadfiles/"+fileName;
				logoLocPath = path+File.separator+fileName;
			}else{
				request.setAttribute("fileUploadError"," * 上传文件格式不正确！");
				return "developer/appinfoadd";
			}
		}
		Devuser devUser = (Devuser) session.getAttribute("devUserSession");
		appInfo.setModifyBy(devUser.getId());		
		appInfo.setModifyDate(new Date());
		appInfo.setLogoPicPath(logoPicPath);
		appInfo.setLogoLocPath(logoLocPath);
		appInfo.setDevId(devUser.getId());
		if(appinfoService.updateAppinfo(appInfo) > 0){
			return "redirect:/Developer_AppInfo/list";
		}
		return "redirect:/Developer_AppInfo/appversionmodify";
	}
	
	/**
	 * 分类联动
	 * @param tcode
	 * @return
	 */
	@RequestMapping(value="datadictionarylist.json",method=RequestMethod.GET)
	@ResponseBody
	public List<Datadictionary> datadictionarylist(@RequestParam String tcode){
		return datadictionaryService.getDatadictionaryByTypeCode(tcode);
	}
	
	/**
	 * APKName重名验证
	 * @param APKName
	 * @return
	 */
	@RequestMapping(value="apkexist.json",method=RequestMethod.GET)
	@ResponseBody
	public Object apkNameIsExit(@RequestParam String APKName){
		HashMap<String, String> resultMap = new HashMap<String, String>();
		if(StringUtils.isNullOrEmpty(APKName)){
			resultMap.put("APKName", "empty");
		}else{
			String apkName= null;
			try {
				apkName = appinfoService.getAppInfoAPK(APKName);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(null != apkName){
				resultMap.put("APKName", "exist");
			}else{
				resultMap.put("APKName", "noexist");
			}
		}
		return JSONArray.toJSONString(resultMap);
	}
	
	@RequestMapping(value="/appversionadd",method=RequestMethod.GET)
	public String appversionadd(@RequestParam Integer id,
								@RequestParam(value="error",required=false) String fileUploadError,
								Appversion appVersion,
								HttpServletRequest request){
		if(null != fileUploadError && fileUploadError.equals("error1")){
			fileUploadError = " * APK信息不完整！";
		}else if(null != fileUploadError && fileUploadError.equals("error2")){
			fileUploadError	= " * 上传失败！";
		}else if(null != fileUploadError && fileUploadError.equals("error3")){
			fileUploadError = " * 上传文件格式不正确！";
		}
		appVersion.setAppId(id);
		List<Appversion> appVersionList = appversionService.getAppversionList(id);
		appVersion.setAppName((appinfoService.getAppInfo(id).getSoftwareName()));
		request.setAttribute("appVersion",appVersion);
		request.setAttribute("appVersionList",appVersionList);
		request.setAttribute("fileUploadError",fileUploadError);
		return "/developer/appversionadd";
	}

	@RequestMapping("/appversionmodify")
	public String appversionmodify(@RequestParam Integer vid,
									@RequestParam Integer aid,
									HttpServletRequest request,
									@RequestParam(value="error",required=false) String fileUploadError,
									HttpSession session){
		if(null != fileUploadError && fileUploadError.equals("error1")){
			fileUploadError = " * APK信息不完整！";
		}else if(null != fileUploadError && fileUploadError.equals("error2")){
			fileUploadError	= " * 上传失败！";
		}else if(null != fileUploadError && fileUploadError.equals("error3")){
			fileUploadError = " * 上传文件格式不正确！";
		}
		Appversion appversion = appversionService.getAppversionById(vid);
		List<Appversion> appVersionList = appversionService.getAppversionList(aid);
		request.setAttribute("appVersion",appversion);
		request.setAttribute("appVersionList",appVersionList);
		request.setAttribute("fileUploadError",fileUploadError);
		return "/developer/appversionmodify";
	}
	
	
	@RequestMapping(value="/addversionsave",method=RequestMethod.POST)
	public String addversionSave(Appversion appversion,
								HttpServletRequest request,
								HttpSession session,
								@RequestParam(value="a_downloadLink",required=false) MultipartFile attach){
		String downloadLink = null;
		String apkLocPath = null;
		String apkFileName = null;
		//判断文件是否为空
		if(!attach.isEmpty()){
			String path = request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
			logger.info("uploadFile path =========== > "+path);
			String oldFileName = attach.getOriginalFilename();	//原文件名
			String prefix = FilenameUtils.getExtension(oldFileName);	//原文件后缀
			if(prefix.equalsIgnoreCase("apk")){		//apk文件命名：apk名称+版本号+.apk
				String apkName = null;				
				try {
					apkName = appinfoService.getAppInfo(appversion.getAppId()).getAPKName();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(apkName == null || "".equals(apkName)){
					return "redirect:Developer_AppInfo/appversionadd&id="+appversion.getAppId()
							 +"&error=error1";
				}
				apkFileName = apkName + "-" + appversion.getVersionNo() + ".apk";
				File targetFile = new File(path,apkFileName);
				if(targetFile.exists()){
					targetFile.mkdirs();
				}
				try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
					return "redirect:/Developer_AppInfo/appversionadd&id="+appversion.getAppId()
					 +"&error=error2";
				} 
				downloadLink = request.getContextPath() + "/statics/uploadfiles/" + apkFileName;
				apkLocPath = path + File.separator + apkFileName;
			}else{
				return  "redirect:/Developer_AppInfo/appversionadd&id="+appversion.getAppId()
						 +"&error=error3";
			}
		}
		Devuser devUSer = (Devuser)session.getAttribute("devUserSession");
		appversion.setCreatedBy(devUSer.getId());
		appversion.setCreationDate(new Date());
		appversion.setDownloadLink(downloadLink);
		appversion.setApkLocPath(apkLocPath);
		appversion.setApkFileName(apkFileName);
		if(appversionService.addAppversion(appversion)){
			return "redirect:/Developer_AppInfo/list";
		}
		return "/appversionadd?id="+appversion.getAppId();
	}
	
	@RequestMapping(value="/appversionmodifysave",method=RequestMethod.POST)
	public String appversionmodifysave(Appversion appversion,
										HttpServletRequest request,
										HttpSession session,
										@RequestParam(value="attach",required=false) MultipartFile attach){
		String downloadLink = null;
		String apkLocPath = null;
		String apkFileName = null;
		//判断文件是否为空
		if(!attach.isEmpty()){
			String path = request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
			logger.info("uploadFile path =========== > "+path);
			String oldFileName = attach.getOriginalFilename();	//原文件名
			String prefix = FilenameUtils.getExtension(oldFileName);	//原文件后缀
			if(prefix.equalsIgnoreCase("apk")){		//apk文件命名：apk名称+版本号+.apk
				String apkName = null;				
				try {
					apkName = appinfoService.getAppInfo(appversion.getAppId()).getAPKName();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(apkName == null || "".equals(apkName)){
					return "redirect:Developer_AppInfo/appversionadd&id="+appversion.getAppId()
							 +"&error=error1";
				}
				apkFileName = apkName + "-" + appversion.getVersionNo() + ".apk";
				File targetFile = new File(path,apkFileName);
				if(targetFile.exists()){
					targetFile.mkdirs();
				}
				try {
					attach.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
					return "redirect:/Developer_AppInfo/appversionadd&id="+appversion.getAppId()
					 +"&error=error2";
				} 
				downloadLink = request.getContextPath() + "/statics/uploadfiles/" + apkFileName;
				apkLocPath = path + File.separator + apkFileName;
			}else{
				return  "redirect:/Developer_AppInfo/appversionadd&id="+appversion.getAppId()
						 +"&error=error3";
			}
		}
		Devuser devUSer = (Devuser)session.getAttribute("devUserSession");
		appversion.setCreatedBy(devUSer.getId());
		appversion.setCreationDate(new Date());
		appversion.setDownloadLink(downloadLink);
		appversion.setApkLocPath(apkLocPath);
		appversion.setApkFileName(apkFileName);
		if(appversionService.updateAppversion(appversion) > 0){
			return "redirect:/Developer_AppInfo/list";
		}
		return "developer/appversionmodify";
	}
	

	@RequestMapping(value="/delapp.json")
	@ResponseBody
	public Object delapp(@RequestParam String id){
		logger.info("delapp ============================>>>> "+id);
		HashMap<String,String> resultMap = new HashMap<String, String>();
		if(StringUtils.isNullOrEmpty(id)){
			resultMap.put("delResult","notexist");
		}
		try {
			if(appinfoService.deleteAppInfo(Integer.parseInt(id))){
				resultMap.put("delResult","true");
			}else{
				resultMap.put("delResult","flase");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSONArray.toJSONString(resultMap);
	}
	
	@RequestMapping(value="/appview/{appinfoid}",method=RequestMethod.GET)
	public String appview(@PathVariable Integer appinfoid,
						  HttpServletRequest request){
		logger.info("appview ====================================>>>>  "+appinfoid);
		Appinfo appinfo = appinfoService.getAppInfo(appinfoid);
		List<Appversion> appVersionList = appversionService.getAppversionList(appinfoid);
		request.setAttribute("appInfo",appinfo);
		request.setAttribute("appVersionList",appVersionList);
		return "/developer/appinfoview";
	}

	
	@RequestMapping("/appinfomodify")
	public String appinfomodify(@RequestParam Integer id,
								HttpServletRequest request){
		Appinfo appInfo = appinfoService.getAppInfo(id);
		request.setAttribute("appInfo",appInfo);
		return "/developer/appinfomodify";
	}
	
	@RequestMapping(value="/{appid}/sale",method=RequestMethod.PUT)
	@ResponseBody
	public Object sale(@PathVariable String appid,
						HttpSession session){
		logger.info("sale =========================>>>>>");
		HashMap<String,String> resultMap = new HashMap<String, String>();
		Integer appIdInteger = 0;
		try {
			appIdInteger = Integer.parseInt(appid);
		} catch (NumberFormatException e) {
			appIdInteger = 0;
		}
		resultMap.put("errorCode","0");
		resultMap.put("appId",appid);
		if(appIdInteger > 0){
			try {
				Devuser devUser = (Devuser)session.getAttribute("devUserSession");
				if(appinfoService.updateStatus(devUser.getId(),appIdInteger) > 0){
					resultMap.put("resultMsg", "success");
				}else{
					resultMap.put("resultMsg", "failed");
				}
			} catch (Exception e) {
				resultMap.put("errorCode", "exception000001");
			}
		}else{
			resultMap.put("errorCode", "param000001");
		}
		return resultMap;
	}
	
	@RequestMapping("/delfile.json")
	@ResponseBody
	public Object delFile(@RequestParam(value="flag",required=false) String flag,
						  @RequestParam(value="id",required=false) String id){
		logger.debug("delApp appId===================== "+id);
		HashMap<String, String> resultMap = new HashMap<String, String>();
		String fileLocPath = null;
		if(flag == null || flag.equals("") || id == null || id.equals("")){
			resultMap.put("result","failed");
		}else if(flag.equals("logo")){	//删除logo图片(app_info表)
			fileLocPath = (appinfoService.getAppInfo(Integer.parseInt(id)).getLogoLocPath());
			System.out.println(fileLocPath);
			File file = new File(fileLocPath);
			if(file.exists()){
				if(file.delete()){		//删除服务器储存的物理文件
					if(appinfoService.deleteAppLogo(Integer.parseInt(id)) > 0){
						resultMap.put("delResult","success");
					}
				}
			}
		}else if(flag.equals("apk")){	//删除apk文件(app_version表)
			fileLocPath = (appversionService.getAppversionById(Integer.parseInt(id))).getApkLocPath();
			File file = new File(fileLocPath);
			if(file.exists()){	
				if(file.delete()){	//删除服务器储存的文理文件
					if(appversionService.deleteApkFile(Integer.parseInt(id)) > 0){
						resultMap.put("delResult","success");
					}
				}
			}
		}
		return JSONArray.toJSONString(resultMap);
	}
	
}
