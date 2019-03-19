package cn.appsys.dao.appversion;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.Appversion;

/**
 * APP版本信息接口
 * @author Administrator
 *
 */
public interface AppversionMapper{
	
	/**
	 * 新增APP版本信息
	 * @param appver
	 * @return
	 */
	public int addAppversion(Appversion appver);		
	
	/**
	 * 修改APP版本信息
	 * @param appver
	 * @return
	 */
	public int updateAppversion(Appversion appver);
	
	/**
	 * 删除版本信息
	 * @param id
	 * @return
	 */
	public int deleteAppversionById(@Param("appId") Integer appId);
	
	/**
	 * 根据Appid查询版本信息
	 * @return
	 */
	public List<Appversion> getAppversionList(@Param("appId") Integer appId);
	
	/**
	 *根据id查询版本信息
	 * @param id
	 * @return
	 */
	public Appversion getAppversionById(@Param("id") Integer id);
	
	/**
	 * 删除apk文件
	 * @param id
	 * @return
	 */
	public int deleteApkFile(@Param("id") Integer id);
	
	/**
	 * 查询总记录条数
	 * @return
	 */
	public int getVersionCount(@Param("appId") Integer appId);
}
