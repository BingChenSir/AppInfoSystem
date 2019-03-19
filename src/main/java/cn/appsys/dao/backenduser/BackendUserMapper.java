package cn.appsys.dao.backenduser;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.Backenduser;

/**
 * 超级管理员接口
 * @author Administrator
 *
 */
public interface BackendUserMapper {

	public Backenduser getLoginUser(@Param("userCode")String userCode);
}
