package com.ihangjing.ZJYXForAndroid;

import cn.sharesdk.framework.TitleLayout;
import cn.sharesdk.framework.authorize.AuthorizeAdapter;

public class QQWebLoginActivity extends AuthorizeAdapter {
	public void onCreate() {
		System.out.println("> ShareSDKUIShell created!");
		//获取标题栏控件
		        TitleLayout llTitle = getTitleLayout();
		        //get
		//标题栏的文字修改
		        //int resID= this.getActivity().getSt;//这个字段定义在strings.xml文件里面
		        llTitle.getTvTitle().setText(getActivity().getString(R.string.hello));
		        hideShareSDKLogo();
		} 
		 
		
	public void onDestroy() {
		System.out.println("> ShareSDKUIShell will be destroyed.");
	}
}
