package cn.appsys.dao.devuser;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.Devuser;

/**
 * 开发者用户接口
 * @author Administrator
 *
 */
public interface DevUserMapper {

	/**
	 * 通过userCode获取User
	 * @param userCode
	 * @return
	 * @throws Exception
	 */
	public Devuser getLoginUser(@Param("devCode")String devCode);
}
