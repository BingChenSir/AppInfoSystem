package cn.appsys.controller.backend;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.appsys.pojo.Appcategory;
import cn.appsys.pojo.Appinfo;
import cn.appsys.pojo.Appversion;
import cn.appsys.pojo.Datadictionary;
import cn.appsys.service.appcategory.AppcategoryService;
import cn.appsys.service.appinfo.AppinfoService;
import cn.appsys.service.appversion.AppversionService;
import cn.appsys.service.datadictionary.DatadictionaryService;
import cn.appsys.tools.PageSupport;

@Controller
@RequestMapping("Backend_AppInfo")
public class BackendController {

	@Resource
	private AppinfoService appinfoService;
	@Resource
	private AppcategoryService appCategoryService;
	@Resource
	private DatadictionaryService datadictionaryService;
	@Resource
	private AppversionService appversionService;
	
	private Logger logger = Logger.getLogger(BackendController.class);
	
	@RequestMapping("list")
	public String applist(HttpServletRequest request,
						  @RequestParam(value="querySoftwareName",required=false) String _querySoftwareName,
						  @RequestParam(value="queryFlatformId",required=false) String _queryFlatformId,
						  @RequestParam(value="queryCategoryLevel1",required=false) String _queryCategoryLevel1,
						  @RequestParam(value="queryCategoryLevel2",required=false) String _queryCategoryLevel2,
						  @RequestParam(value="queryCategoryLevel3",required=false) String _queryCategoryLevel3,
						  @RequestParam(value="pageIndex",required=false) String pageIndex){
		
		logger.debug("querySoftwareName ===============================>>>   "+_querySoftwareName);
		logger.debug("queryFlatformId ===============================>>>   "+_queryFlatformId);
		logger.debug("queryCategoryLevel1 ===============================>>>   "+_queryCategoryLevel1);
		logger.debug("queryCategoryLevel2 ===============================>>>   "+_queryCategoryLevel2);
		logger.debug("queryCategoryLevel3 ===============================>>>   "+_queryCategoryLevel3);
		logger.debug("pageIndex ===============================>>>   "+pageIndex);
		
		List<Appinfo> appInfoList = null;
		List<Datadictionary> datadictionaryList = null;
		List<Appcategory> categoryLevel1List = null;	//列出一级分类列表，注：二级和三级分类列表通过异步ajax获取
		List<Appcategory> categoryLevel2List = null;
		List<Appcategory> categoryLevel3List = null;
		
		//当前页码
		Integer currentPageNo = 1;
		
		if(pageIndex != null){
			try {
				currentPageNo =Integer.parseInt(pageIndex);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
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
			totalCount = appinfoService.getAppInfoCount(_querySoftwareName,1,queryCategoryLevel1, 
					queryCategoryLevel2, queryCategoryLevel3,queryFlatformId,null);
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
			appInfoList = appinfoService.getAppinfoList(_querySoftwareName,1, queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3, queryFlatformId,null, currentPageNo, page.getPageSize());
			categoryLevel1List = appCategoryService.getAppcategoryByParentIdList(null);
			datadictionaryList = datadictionaryService.getDatadictionaryByTypeCode("APP_FLATFORM");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		request.setAttribute("appInfoList",appInfoList);
		request.setAttribute("flatFormList",datadictionaryList);
		request.setAttribute("pages",page);
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
		return "backend/applist";
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
	
	@RequestMapping("/check")
	public String check(HttpServletRequest request,
						@RequestParam String aid,
						@RequestParam String vid){
		Appinfo appinfo = appinfoService.getAppInfo(Integer.parseInt(aid));
		Appversion appversion = appversionService.getAppversionById(Integer.parseInt(vid));
		request.setAttribute("appInfo",appinfo);
		request.setAttribute("appVersion",appversion);
		return "/backend/appcheck";
	}
	
	@RequestMapping("/checksave")
	public String checkSave(Appinfo appInfo){
		if(appinfoService.updateStatus(appInfo.getStatus(),appInfo.getId()) > 0){
			return "redirect:/Backend_AppInfo/list";
		}
		return "/backend/appcheck";
	}
	
}
