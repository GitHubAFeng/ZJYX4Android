package com.ihangjing.Model;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

public class MyLocationModel {
	double lat;
	double lon;
	String addressDetail;
	String cityName;
	String cityID;
	String areaName = "所有区域";
	String areaID = "0";
	String Street;//街名+街号
	double cityLat;//城市
	double cityLon;
	
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getAreaID() {
		return areaID;
	}
	public void setAreaID(String areaID) {
		this.areaID = areaID;
	}
	public MyLocationModel(){
		
	}
	public MyLocationModel(MyLocationModel model){
		lat = model.getLat();
		lon = model.getLon();
		addressDetail = model.getAddressDetail();
		cityName = model.getCityName();
		Street = model.getStreet();
		cityLat = model.getCityLat();
		cityLon = model.getCityLon();
	}
	public void setMyLocation(MyLocationModel model){
		lat = model.getLat();
		lon = model.getLon();
		addressDetail = model.getAddressDetail();
		cityName = model.getCityName();
		Street = model.getStreet();
		cityLat = model.getCityLat();
		cityLon = model.getCityLon();
	}
	public double getCityLat() {
		return cityLat;
	}

	public void setCityLat(double cityLat) {
		this.cityLat = cityLat;
	}

	public double getCityLon() {
		return cityLon;
	}

	public void setCityLon(double cityLon) {
		this.cityLon = cityLon;
	}

	public String getStreet() {
		return Street;
	}

	public void setStreet(String street) {
		Street = street;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCityID() {
		return cityID;
	}

	public void setCityID(String cityID) {
		this.cityID = cityID;
	}

	public MyLocationModel(BDLocation location){
		
		lat = location.getLatitude();
		lon = location.getLongitude();
		addressDetail = location.getAddrStr();
		cityName = location.getCity();
		Street = location.getStreet() + location.getStreetNumber();
		cityLat = lat;
		cityLon = lon;
	}
	public MyLocationModel(ReverseGeoCodeResult result){
		lat = result.getLocation().latitude;
		lon = result.getLocation().longitude;
		addressDetail = result.getAddress();
		Street = addressDetail;
		cityLat = lat;
		cityLon = lon;
		
	}
	
	public MyLocationModel(PoiInfo poi){
		lat = poi.location.latitude;
		lon = poi.location.longitude;
		addressDetail = poi.address;
		cityName = poi.city;
		Street = addressDetail;
		cityLat = lat;
		cityLon = lon;
		
		
	}
	
	public void setResult(ReverseGeoCodeResult result){
		lat = result.getLocation().latitude;
		lon = result.getLocation().longitude;
		addressDetail = result.getAddress();
		Street = addressDetail;
		cityLat = lat;
		cityLon = lon;
		
	}
	
	public void setLocation(BDLocation location){
		
		lat = location.getLatitude();
		lon = location.getLongitude();
		addressDetail = location.getAddrStr();
		Street = location.getStreet() + location.getStreetNumber();
		cityName = location.getCity();
		cityLat = lat;
		cityLon = lon;
	}
	
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public String getAddressDetail() {
		return addressDetail;
	}
	public void setAddressDetail(String addressDetail) {
		this.addressDetail = addressDetail;
	}
	
	
}
