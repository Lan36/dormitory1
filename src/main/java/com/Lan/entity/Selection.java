package com.Lan.entity;

import org.hibernate.validator.constraints.Length;
import com.Lan.utils.Entity;
import java.util.Date;
import java.util.List;


public class Selection extends Entity{

	/**
	 * 
	 */
	private Integer id;
	/**
	 * 
	 */
	@Length(max = 100)
	private String name;
	/**
	 * 
	 */
	private Date startTime;
	/**
	 * 
	 */
	private Date endTime;
	/**
	 * 
	 */
	@Length(max = 100)
	private String remark;

	private List<Integer> clazzIds;

	private List<Org> clazzes;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<Integer> getClazzIds() {
		return clazzIds;
	}

	public void setClazzIds(List<Integer> clazzIds) {
		this.clazzIds = clazzIds;
	}

	public List<Org> getClazzes() {
		return clazzes;
	}

	public void setClazzes(List<Org> clazzes) {
		this.clazzes = clazzes;
	}
}