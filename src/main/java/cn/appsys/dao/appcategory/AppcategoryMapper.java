package cn.appsys.dao.appcategory;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.Appcategory;

/**
 * APP分类接口
 * @author Administrator
 *
 */
public interface AppcategoryMapper {

	/**
	 * 根据parentId查询父节点
	 * @param parentId
	 * @return
	 */
	public List<Appcategory> getAppcategoryByParentIdList(@Param("parentId") Integer parentId);
	
	
}
