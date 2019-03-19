package cn.appsys.service.appcategory;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.Appcategory;

public interface AppcategoryService {
	
	
	/**
	 * 根据parentId查询父节点
	 * @param parentId
	 * @return
	 */
	public List<Appcategory> getAppcategoryByParentIdList(@Param("parentId") Integer parentId);
	
}
