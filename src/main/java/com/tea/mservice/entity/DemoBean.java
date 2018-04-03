package com.tea.mservice.entity;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;

public class DemoBean {

	private Integer num;
	private String name;
	private Date date;
	private BigDecimal price;

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getType() throws IllegalArgumentException, IllegalAccessException {
		DemoBean instance = new DemoBean();
		Field[] fields = instance.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			System.out.print("成员变量" + i + "类型 : " + fields[i].getType().getName());
			System.out.print("\t成员变量" + i + "变量名: " + fields[i].getName() + "\t");
			System.out.println("成员变量" + i + "值: " + fields[i].get(instance));
		}
		return null;
	}

	public static void main(String[] args) {
		DemoBean bean = new DemoBean();
		try {
			bean.getType();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
