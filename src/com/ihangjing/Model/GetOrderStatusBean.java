package com.ihangjing.Model;

//¶©µ¥×´Ì¬modelÀà
public class GetOrderStatusBean extends BaseBean {
	
	private int doingOrderNum = 0;
	private String orderId = "";
	private String orderNote = "";
	private int orderStatus = 1;
	private String resultStatus = "";

	@Override
	public String beanToString() {
		return null;
	}

	public int getDoingOrderNum() {
		return this.doingOrderNum;
	}

	public String getOrderId() {
		return this.orderId;
	}

	public String getOrderNote() {
		return this.orderNote;
	}

	public int getOrderStatus() {
		return this.orderStatus;
	}

	public String getResultStatus() {
		return this.resultStatus;
	}

	public void setDoingOrderNum(int paramInt) {
		this.doingOrderNum = paramInt;
	}

	public void setOrderId(String paramString) {
		this.orderId = paramString;
	}

	public void setOrderNote(String paramString) {
		this.orderNote = paramString;
	}

	public void setOrderStatus(int paramInt) {
		this.orderStatus = paramInt;
	}

	public void setResultStatus(String paramString) {
		this.resultStatus = paramString;
	}

	@Override
	public BaseBean stringToBean(String paramString) {
		return null;
	}
}
