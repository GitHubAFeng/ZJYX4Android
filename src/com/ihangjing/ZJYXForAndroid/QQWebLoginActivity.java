package com.ihangjing.ZJYXForAndroid;

import cn.sharesdk.framework.TitleLayout;
import cn.sharesdk.framework.authorize.AuthorizeAdapter;

public class QQWebLoginActivity extends AuthorizeAdapter {
	public void onCreate() {
		System.out.println("> ShareSDKUIShell created!");
		//��ȡ�������ؼ�
		        TitleLayout llTitle = getTitleLayout();
		        //get
		//�������������޸�
		        //int resID= this.getActivity().getSt;//����ֶζ�����strings.xml�ļ�����
		        llTitle.getTvTitle().setText(getActivity().getString(R.string.hello));
		        hideShareSDKLogo();
		} 
		 
		
	public void onDestroy() {
		System.out.println("> ShareSDKUIShell will be destroyed.");
	}
}
