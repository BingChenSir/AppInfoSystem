package cn.appsys.service.appinfo;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.Appinfo;

/**
 * APP基础信息接口
 * @author Administrator
 *
 */
public interface AppinfoService {
	
	/**
	 * 新增APP基础信息
	 * @param appinfo
	 * @return
	 */
	public int addAppinfo(Appinfo appinfo);
	
	/**
	 * 修改APP基础信息
	 * @param appinfo
	 * @return
	 */
	public int updateAppinfo(Appinfo appinfo);
	 
	/**
	 * 根据条件分页查询APP基础信息
	 * @param softwareName
	 * @param status
	 * @param categoryLevel1
	 * @param categoryLevel2
	 * @param categoryLevel3
	 * @param flatformId
	 * @param currentPageNo
	 * @param pageSize
	 * @return
	 */
	public List<Appinfo> getAppinfoList(@Param("softwareName") String softwareName,
										@Param("status") Integer status,
										@Param("categoryLevel1") Integer categoryLevel1,
										@Param("categoryLevel2") Integer categoryLevel2,
										@Param("categoryLevel3") Integer categoryLevel3,
										@Param("flatformId") Integer flatformId,
										@Param(value="devId")Integer devId,
										@Param("from") Integer currentPageNo,
										@Param("pageSize") Integer pageSize);
	
	/**
	 * 根据条件查询总条数
	 * @param softwareName
	 * @param status
	 * @param categoryLevel1
	 * @param categoryLevel2
	 * @param categoryLevel3
	 * @param flatformId
	 * @return
	 */
	public int getAppInfoCount(@Param("softwareName") String softwareName,     
								@Param("status") Integer status,                
								@Param("categoryLevel1") Integer categoryLevel1,
								@Param("categoryLevel2") Integer categoryLevel2,
								@Param("categoryLevel3") Integer categoryLevel3,
								@Param("flatformId") Integer flatformId,        
								@Param(value="devId")Integer devId);            
	/**
	 * 重名验证:APK名称（唯一）
	 * @param APKName
	 * @return
	 */
	public String getAppInfoAPK(@Param("APKName") String APKName); 
	
	/**
	 * 根据appId，更新最新versionId
	 * @param versionId
	 * @param appId
	 * @return
	 * @throws Exception
	 */
	public int updateVersionId(@Param(value="versionId")Integer versionId,@Param(value="id")Integer appId)throws Exception;
	
	/**
	 * 
	 * @param id
	 * @param APKName
	 * @return
	 */
	public Appinfo getAppInfo(@Param("id") Integer id);
	
	/**
	 * 更新app状态
	 * @param status
	 * @param id
	 * @return
	 */
	public int updateStatus(@Param("status") Integer status,@Param("id") Integer id);
	
	/**
	 * 根据id删除App基础信息
	 * @param id
	 * @return
	 */
	public boolean deleteAppInfo(@Param("id") Integer id) throws Exception;
	
	/**
	 * 删除Logo图片
	 * @param id
	 * @return
	 */
	public int deleteAppLogo(@Param("id") Integer id);
}