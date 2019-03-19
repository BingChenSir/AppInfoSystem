package cn.appsys.pojo;

import java.util.Date;

/**
 * 开发者用户表
 * @author Administrator
 *
 */
public class Devuser {
	private int id;              //主键id                                                  
	private String devCode;      //开发者帐号                                           
	private String devName;      //开发者名称                                           
	private String devPassword;  //开发者密码                                           
	private String devEmail;     //开发者电子邮箱                                     
	private String devInfo;      //开发者简介                                           
	private int createdBy;       //创建者（来源于backend_user用户表的用户id）  
	private Date creationDate;   //创建时间                                              
	private int modifyBy;        //更新者（来源于backend_user用户表的用户id）  
	private Date modifyDate;     //最新更新时间                                        

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDevCode() {
		return devCode;
	}
	public void setDevCode(String devCode) {
		this.devCode = devCode;
	}
	public String getDevName() {
		return devName;
	}
	public void setDevName(String devName) {
		this.devName = devName;
	}
	public String getDevPassword() {
		return devPassword;
	}
	public void setDevPassword(String devPassword) {
		this.devPassword = devPassword;
	}
	public String getDevEmail() {
		return devEmail;
	}
	public void setDevEmail(String devEmail) {
		this.devEmail = devEmail;
	}
	public String getDevInfo() {
		return devInfo;
	}
	public void setDevInfo(String devInfo) {
		this.devInfo = devInfo;
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
		return "Dev_user [id=" + id + ", devCode=" + devCode + ", devName="
				+ devName + ", devPassword=" + devPassword + ", devEmail="
				+ devEmail + ", devInfo=" + devInfo + ", createdBy="
				+ createdBy + ", creationDate=" + creationDate + ", modifyBy="
				+ modifyBy + ", modifyDate=" + modifyDate + "]";
	}
}
