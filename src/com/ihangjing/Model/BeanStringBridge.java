package com.ihangjing.Model;

public abstract interface BeanStringBridge {
	public abstract String beanToString();

	public abstract BaseBean stringToBean(String paramString);
}