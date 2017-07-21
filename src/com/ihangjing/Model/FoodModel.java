package com.ihangjing.Model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ihangjing.common.OtherManager;

import android.R.integer;
//{"FoodID":"8521","Name":"飘香猪扒饭","Price":"15.0","Discount":"10.0","PackageFree":"0.0"}
public class FoodModel {
	private int id = 0;     // 编号
	private String name = "";   // 名称
	private float price = 0.0F; // 价格
	private float discount = 0.0F; // 折扣
	private float packagefree = 0.0F; // 打包费
	private String Disc = "";//简介描述
	private String notice = "";//提示
	private int stock = 0;    // 库存
	private boolean selected = false;// 是否被选中 列表中选中未选中状态的标识
	private int ordernum= 0;    //购买的数量 在订购时
	private int foodStock = -1;
	public List<FoodMakeModel> listMake;// 分类的额外项目
	public List<FoodAttrModel> listSpec;// 额外项目
	public List<FoodSizeModel> listSize;// 规格
	private String ImageNetPath;//图片网络路径
	private String ImageLocalPath;//图片本地路径
	private String publicPoint;//公益积分
	private int togoID;
	private String togoName;//商家名称
	//public boolean isDowload = ;//是否需要下载图片
	private int ActivitysID = 0;//活动编号
	private int ActivitysType = 0;//活动类型 0普通 1.团购 2.秒杀 3.限量 4.买撘赠
	private int canbuy;
	private int istuan;
	private int sale;
	private int styleid;
	private int allnum;
	private int joinnum;
	private int iscollect;
	
	
	public int getActivitysID() {
		return ActivitysID;
	}

	public void setActivitysID(int activitysID) {
		ActivitysID = activitysID;
	}

	public int getActivitysType() {
		return ActivitysType;
	}

	public void setActivitysType(int activitysType) {
		ActivitysType = activitysType;
	}

	public FoodModel() {

	}
	
	public String getTogoName() {
		return togoName;
	}

	public void setTogoName(String togoName) {
		this.togoName = togoName;
	}

	public String getPublicPoint() {
		return publicPoint;
	}

	public void setPublicPoint(String publicPoint) {
		this.publicPoint = publicPoint;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

	public String getDisc() {
		return Disc;
	}

	public void setDisc(String disc) {
		Disc = disc;
	}

	public String getImageNetPath() {
		return ImageNetPath;
	}

	public void setImageNetPath(String imageNetPath) {
		ImageNetPath = imageNetPath;
	}

	public String getImageLocalPath() {
		return ImageLocalPath;
	}

	public void setImageLocalPath(String imageLocalPath) {
		ImageLocalPath = imageLocalPath;
	}

	
	public int getTogoID() {
		return togoID;
	}

	public void setTogoID(int togoID) {
		this.togoID = togoID;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getPackagefree() {
		return packagefree;
	}

	public void setPackagefree(float packagefree) {
		this.packagefree = packagefree;
	}

	public int getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(int ordernum) {
		this.ordernum = ordernum;
	}

	public int getCanbuy() {
		return canbuy;
	}

	public void setCanbuy(int canbuy) {
		this.canbuy = canbuy;
	}

	public int getIstuan() {
		return istuan;
	}

	public void setIstuan(int istuan) {
		this.istuan = istuan;
	}

	public int getSale() {
		return sale;
	}

	public void setSale(int sale) {
		this.sale = sale;
	}

	public int getStyleid() {
		return styleid;
	}

	public void setStyleid(int styleid) {
		this.styleid = styleid;
	}

	public int getAllnum() {
		return allnum;
	}

	public void setAllnum(int allnum) {
		this.allnum = allnum;
	}

	public int getJoinnum() {
		return joinnum;
	}

	public void setJoinnum(int joinnum) {
		this.joinnum = joinnum;
	}

	public int getIscollect() {
		return iscollect;
	}

	public void setIscollect(int iscollect) {
		this.iscollect = iscollect;
	}

	// Json转换成model
	public FoodModel(JSONObject json) throws JSONException {
		try {
			JSONObject localJSONObject = json;
			setFoodId(localJSONObject.getInt("FoodID"));
			setFoodName(localJSONObject.getString("Name"));
			Disc = localJSONObject.getString("intro");
			setPackageFree(Float.parseFloat(localJSONObject.getString("PackageFree")));
			//notice = localJSONObject.getString("note");
			ImageNetPath = localJSONObject.getString("icon");
			ImageLocalPath = OtherManager.getFoodImageLocalPath(0, id);
			//publicPoint = localJSONObject.getString("publicgood");
			togoID = localJSONObject.getInt("togoid");
			setStock(localJSONObject.getInt("MaxPerDay"));
			setOrderNum(0);
			sale = localJSONObject.getInt("sale");
			//canbuy = localJSONObject.getInt("canbuy");//能否购买，0表示可以购买，1表示只可浏览(只对活动商品列表有效)
			//discount = (float)(localJSONObject.getDouble("discount") / 100.0);//商品折扣，只有从活动进入商品列表时此值有效，85折，返回85
			int len = 0;
			//istuan = localJSONObject.getInt("istuan");//赞的数量
			//sale = localJSONObject.getInt("sale");//已售数量
			//styleid = localJSONObject.getInt("styleid");//是否新品推荐：0表示不是，1表示是
			//allnum = localJSONObject.getInt("allnum");//是否热买，0表示不是，1表示是
			//joinnum = localJSONObject.getInt("joinnum");//是否促销：0表示没有，1表示是
			//iscollect = localJSONObject.getInt("iscollect");//0表示未收藏或者未登录，1表示已经收藏
			//JSONObject js;
			JSONArray array = localJSONObject.getJSONArray("Stylelist");
			listSize = new ArrayList<FoodSizeModel>();
			listMake = new ArrayList<FoodMakeModel>();
			listSpec = new ArrayList<FoodAttrModel>();
			//listMake = new ArrayList<FoodMakeModel>();
			len = array.length();
			FoodSizeModel mode;
			if(len > 0){
				
				for (int i = 0; i < len; i++) {
					JSONObject js = array.getJSONObject(i);
					
					mode = new FoodSizeModel(js);
					//mode.cId = this.id;
					listSize.add(mode);
				}
				setPrice(listSize.get(0).price);//设置价格为默认第一个类型的价格
			}else{
				price = (float)json.getDouble("price");
				mode = new FoodSizeModel();
				mode.cId = id;
				mode.name = name;
				mode.price = price;
				mode.oldPrice = price;
				listSize.add(mode);
			}
			
			/*array = localJSONObject.getJSONArray("sortattr");
			listMake = new ArrayList<FoodAttrModel>();//分类的额外项目
			
			len = array.length();
			FoodAttrModel makeMode;
			for (int i = 0; i < len; i++) {
				JSONObject js = array.getJSONObject(i);
				makeMode = new FoodAttrModel();
				makeMode.JsonToBean(js);
				listMake.add(makeMode);
			}
			
			array = localJSONObject.getJSONArray("foodattr");
			listSpec = new ArrayList<FoodAttrModel>();//额外项目 和上面的是独立的
			//listMake = new ArrayList<FoodMakeModel>();
			len = array.length();
			FoodAttrModel attrMode;
			for (int i = 0; i < len; i++) {
				JSONObject js = array.getJSONObject(i);
				attrMode = new FoodAttrModel(js);
				
				listSpec.add(attrMode);
			}*/
			
			
			//setDiscount(Float.parseFloat(localJSONObject.getString("Discount")));
			//setPackageFree(Float.parseFloat(localJSONObject.getString("PackageFree")));
			
		} catch (JSONException localJSONException) {
			localJSONException.printStackTrace();
		}
	}
	
	// Json转换成model
		public void BuyAgainJson(JSONObject json, int shopid) throws JSONException {
			try {
				JSONObject localJSONObject = json;
				setFoodId(localJSONObject.getInt("FoodID"));
				setFoodName(localJSONObject.getString("foodname"));
				//Disc = localJSONObject.getString("intro");
				setPackageFree(Float.parseFloat(localJSONObject.getString("package")));
				//notice = localJSONObject.getString("note");
				price = (float)localJSONObject.getDouble("FoodPrice");
				togoID = shopid;
				setStock(localJSONObject.getInt("MaxPerDay"));//库存
				sale = localJSONObject.getInt("Num");//数量，当数量为0表示菜品下架或者删除
				if(sale > stock){
					sale = stock;
					//stock = 0;
				}else{
					
				}
				listSize = new ArrayList<FoodSizeModel>();
				listMake = new ArrayList<FoodMakeModel>();
				listSpec = new ArrayList<FoodAttrModel>();
				//listMake = new ArrayList<FoodMakeModel>();
				
				FoodSizeModel mode = new FoodSizeModel();
					mode.cId = localJSONObject.getInt("isreview");
					mode.name = "";
					mode.price = price;
					mode.oldPrice = price;
					listSize.add(mode);
				
				//setOrderNum(0);
				//sale = localJSONObject.getInt("sale");
				//canbuy = localJSONObject.getInt("canbuy");//能否购买，0表示可以购买，1表示只可浏览(只对活动商品列表有效)
				//discount = (float)(localJSONObject.getDouble("discount") / 100.0);//商品折扣，只有从活动进入商品列表时此值有效，85折，返回85
				//int len = 0;
				//istuan = localJSONObject.getInt("istuan");//赞的数量
				//sale = localJSONObject.getInt("sale");//已售数量
				//styleid = localJSONObject.getInt("styleid");//是否新品推荐：0表示不是，1表示是
				//allnum = localJSONObject.getInt("allnum");//是否热买，0表示不是，1表示是
				//joinnum = localJSONObject.getInt("joinnum");//是否促销：0表示没有，1表示是
				//iscollect = localJSONObject.getInt("iscollect");//0表示未收藏或者未登录，1表示已经收藏
				//JSONObject js;
				//JSONArray array = localJSONObject.getJSONArray("Stylelist");
				
				
			} catch (JSONException localJSONException) {
				localJSONException.printStackTrace();
			}
		}
	
	public int getFoodStock() {
		return foodStock;
	}

	public void setFoodStock(int foodStock) {
		this.foodStock = foodStock;
	}

	// 生成json
	public String beanToString() {
		JSONObject localJSONObject1 = new JSONObject();
		try {
			localJSONObject1.put("FoodID", String.valueOf(getFoodId()));
			localJSONObject1.put("Name", getFoodName());
			localJSONObject1.put("Price", getPrice());
			localJSONObject1.put("num", getStock());
			localJSONObject1.put("ordernum", getOrderNum());
			localJSONObject1.put("Discount", getDiscount());
			localJSONObject1.put("PackageFree", getPackageFree());
			localJSONObject1.put("icon", ImageNetPath);
			localJSONObject1.put("intro", Disc);
			localJSONObject1.put("note", notice);
			localJSONObject1.put("locaPath", ImageLocalPath);
			//localJSONObject1.put("foodstylelist", value);
			JSONArray localJSONArray1 = new JSONArray();
			Iterator<FoodAttrModel> localIterator = this.listSpec.iterator();
			while (true) {
				if (!localIterator.hasNext()) {
					localJSONObject1.put("foodstylelist", localJSONArray1);
					break;
				}
				localJSONArray1.put(localIterator.next()
						.beanToString());
			}
			
			
			return localJSONObject1.toString();
		} catch (JSONException localJSONException) {
			return "";
		}
	}

	public int getFoodId() {
		return this.id;
	}

	public String getFoodName() {
		return this.name;
	}

	public float getPrice() {
		return this.price;
	}

	public float getDiscount() {
		return this.discount;
	}
	
	public float getPackageFree() {
		return this.packagefree;
	}
	
	

	public boolean isSelected() {
		return this.selected;
	}
	
	public int getOrderNum()
	{
		return this.ordernum;
	}

	public void setSelected(boolean paramBoolean) {
		this.selected = paramBoolean;
	}

	public void setFoodId(int paramString) {
		this.id = paramString;
	}

	public void setFoodName(String paramString) {
		this.name = paramString;
	}

	public void setPrice(float paramString) {
		this.price = paramString;
	}

	
	
	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public void setOrderNum(int paramString) {
		this.ordernum = paramString;
	}

	public void setDiscount(float paramString) {
		this.discount = paramString;
	}
	
	public void setPackageFree(float paramString) {
		this.packagefree = paramString;
	}
	
	// Json转换成model
	//sr = 0,网络获取 sr=1本地缓存
	public void JsonToBean(JSONObject localJSONObject, int sr) {
		
		try {
			
			setFoodId(localJSONObject.getInt("FoodID"));
			setFoodName(localJSONObject.getString("Name"));
			//setPrice(Float.parseFloat(localJSONObject.getString("Price")));
			Disc = localJSONObject.getString("intro");
			setStock(10000);
			setOrderNum(0);
			ImageLocalPath = OtherManager.getFoodImageLocalPath(0, id);
			notice = localJSONObject.getString("note");
			//setDiscount(Float.parseFloat(localJSONObject.getString("Discount")));
			setPackageFree(Float.parseFloat(localJSONObject.getString("PackageFree")));
			ImageNetPath = localJSONObject.getString("icon");
			/*if (sr == 1) {
				ImageLocalPath = localJSONObject.getString("locaPath");
				setPrice(Float.parseFloat(localJSONObject.getString("Price")));
			}else{*/
				//isDowload = true;
				int len = 0;
				//JSONObject js;
				JSONArray array = localJSONObject.getJSONArray("foodstylelist");
				listSpec = new ArrayList<FoodAttrModel>();
				//listMake = new ArrayList<FoodMakeModel>();
				len = array.length();
				for (int i = 0; i < len; i++) {
					JSONObject js = array.getJSONObject(i);
					FoodAttrModel mode = new FoodAttrModel();
					mode.cId = js.getInt("DataId");
					mode.name = js.getString("Title");
					mode.price = (float)js.getDouble("Price");
					listSpec.add(mode);
				}
				setPrice(listSpec.get(0).price);//设置价格为默认第一个类型的价格
				
				
			//}
			
			
		} catch (JSONException localJSONException) {
			localJSONException.printStackTrace();
		}
	}
}
