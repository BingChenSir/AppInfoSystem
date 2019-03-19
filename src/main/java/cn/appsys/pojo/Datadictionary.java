package cn.appsys.pojo;

import java.util.Date;

/**
 * 数据字典表
 * 所属平台
 * App状态
 * 发布状态
 * @author Administrator
 *
 */
public class Datadictionary {
	private int id;            //主键ID                                                  
	private String typeCode;   //类型编码                                              
	private String typeName;   //类型名称                                              
	private int valueId;       //类型值ID                                               
	private String valueName;  //类型值Name                                             
	private int createdBy;     //创建者（来源于backend_user用户表的用户id）  
	private Date creationDate; //创建时间                                              
	private int modifyBy;      //更新者（来源于backend_user用户表的用户id）  
	private Date modifyDate;   //最新更新时间                      
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public int getValueId() {
		return valueId;
	}
	public void setValueId(int valueId) {
		this.valueId = valueId;
	}
	public String getValueName() {
		return valueName;
	}
	public void setValueName(String valueName) {
		this.valueName = valueName;
	}
	public int getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public int getModifyBy() {
		return modifyBy;
	}
	public void setModifyBy(int modifyBy) {
		this.modifyBy = modifyBy;
	}
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	
	@Override
	public String toString() {
		return "Data_dictionary [id=" + id + ", typeCode=" + typeCode
				+ ", typeName=" + typeName + ", valueId=" + valueId
				+ ", valueName=" + valueName + ", createdBy=" + createdBy
				+ ", creationDate=" + creationDate + ", modifyBy=" + modifyBy
				+ ", modifyDate=" + modifyDate + "]";
	}
}
