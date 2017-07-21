package com.ihangjing.Model;

import org.json.JSONException;
import org.json.JSONObject;

/*
 * 程序节（Bean） 可重用软件组件。将它们组合起来使用就可创建出应用程序。 
 */
public class AddressBean extends BaseBean
{
  public String area = "";
  public String city = "";
  public double lat = 0.0D;
  public double lon = 0.0D;
  public String pro = "";
  public String road = "";

  @Override
public String beanToString()
  {
    JSONObject localJSONObject1 = new JSONObject();
    try
    {
      double d1 = this.lat;
      localJSONObject1.put("lat", d1);
      double d2 = this.lon;
      localJSONObject1.put("lon", d2);
      String str1 = this.pro;
      localJSONObject1.put("pro", str1);
      String str2 = this.city;
      localJSONObject1.put("city", str2);
      String str3 = this.area;
      localJSONObject1.put("area", str3);
      String str4 = this.road;
      localJSONObject1.put("road", str4);
      
      return localJSONObject1.toString();
    }
    catch (JSONException localJSONException)
    {
      while (true)
        localJSONException.printStackTrace();
    }
  }

  public void reset()
  {
    this.lat = 0.0D;
    this.lon = 0.0D;
    this.pro = "";
    this.city = "";
    this.area = "";
    this.road = "";
  }

  //保存地址
  @Override
public BaseBean stringToBean(String paramString)
  {
    try
    {
      JSONObject localJSONObject = new JSONObject(paramString);
      double d1 = localJSONObject.getDouble("lat");
      this.lat = d1;
      double d2 = localJSONObject.getDouble("lon");
      this.lon = d2;
      String str1 = localJSONObject.getString("pro");
      this.pro = str1;
      String str2 = localJSONObject.getString("city");
      this.city = str2;
      String str3 = localJSONObject.getString("area");
      this.area = str3;
      String str4 = localJSONObject.getString("road");
      this.road = str4;
      return this;
    }
    catch (JSONException localJSONException)
    {
      while (true)
        localJSONException.printStackTrace();
    }
  }

  //不带省份
  public String toShowString()
  {
    String str1 = String.valueOf(this.city);
    StringBuilder localStringBuilder1 = new StringBuilder(str1);
    String str2 = this.area;
    localStringBuilder1.append(str2);
    String str3 = this.road;
    localStringBuilder1.append(str3);
    
    return localStringBuilder1.toString();
  }

  //完整的地址
  @Override
public String toString()
  {
    String str1 = String.valueOf(this.pro);
    StringBuilder localStringBuilder1 = new StringBuilder(str1);
    String str2 = this.city;
    localStringBuilder1.append(str2);
    String str3 = this.area;
    localStringBuilder1.append(str3);
    String str4 = this.road;
    localStringBuilder1.append(str4);
    
    return localStringBuilder1.toString();
  }
}
