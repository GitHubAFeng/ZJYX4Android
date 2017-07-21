package com.ihangjing.Model;

//会员信息
public class UserDetail {

	public String userId;
	public String userName;
	public String password;
	public String email;
	public String trueName;
	public String tell;
	public String phone;
	public String QQ;
	public int point = 0;
	public float userMoney = 0.0f;
	public String nPoint;//当前积分
	public String hPoint;//历史积分
	public String pPoint;//公益积分
	public String myICO;//我的头像地址
	public UserDetail() {
	}

	public UserDetail(String _uid, String _userName,
			String _passWord, String _email, String _trueName,
			String _tell, String _phone, int _point,float _userMoney, String _qq, String _icon) {
		this.userId = _uid;
		this.userName = _userName;
		this.password = _passWord;
		this.email = _email;
		this.trueName = _trueName;
		this.tell = _tell;
		this.phone = _phone;
		this.point = _point;
		this.userMoney = _userMoney;
		QQ = _qq;
		myICO = _icon;
	}

}
