package cn.appsys.dao.datadictionary;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.Datadictionary;

/**
 * 数据字典表
 * 所属平台
 * App状态
 * 发布状态
 * @author Administrator
 */
public interface DatadictionaryMapper {

	public List<Datadictionary> getDatadictionaryByTypeCode(@Param("typeCode") String typeCode);
}
