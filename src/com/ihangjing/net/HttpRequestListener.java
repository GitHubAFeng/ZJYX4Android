package com.ihangjing.net;

public abstract interface HttpRequestListener {
	public static final int EVENT_BASE = 256;
	public static final int EVENT_GET_DATA_EEEOR = 259;
	public static final int EVENT_GET_DATA_SUCCESS = 260;
	public static final int EVENT_NETWORD_EEEOR = 258;
	public static final int EVENT_NOT_NETWORD = 257;

	public abstract void action(int paramInt, Object paramObject, int tag);
}