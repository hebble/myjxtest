package com.my.utils.weixin.user;

import java.util.Arrays;

public class UserInfo{
	public int subscribe;
	public String openid;
	public String nickname;
	public Integer sex = Integer.valueOf(0);
	public String city;
	public String country;
	public String province;
	public String language;
	public String headimgurl;
	public long subscribe_time;
	public String unionid;
	public String remark;
	public long groupid;
	private String[] privilege;
	private String[] tagid_list;
	private long errcode;
	private String errmsg;

	public String toString(){
		return "UserInfo [subscribe=" + this.subscribe + ", openid=" + this.openid + ", nickname=" + this.nickname + ", sex=" + this.sex + ", city=" + this.city + ", country=" + this.country + ", province=" + this.province + ", language=" + this.language + ", headimgurl=" + this.headimgurl + ", subscribe_time=" + this.subscribe_time + ", unionid=" + this.unionid + ", remark=" + this.remark + ", groupid=" + this.groupid + ", privilege=" + Arrays.toString(this.privilege) + ", tagid_list=" + Arrays.toString(this.tagid_list) + "]";
	}

	public String getErrmsg(){
		return this.errmsg;
	}

	public void setErrmsg(String errmsg){
		this.errmsg = errmsg;
	}

	public long getErrcode(){
		return this.errcode;
	}

	public void setErrcode(long errcode){
		this.errcode = errcode;
	}

	public String[] getTagid_list(){
		return this.tagid_list;
	}

	public void setTagid_list(String[] tagid_list){
		this.tagid_list = tagid_list;
	}

	public String[] getPrivilege(){
		return this.privilege;
	}

	public void setPrivilege(String[] privilege){
		this.privilege = privilege;
	}

	public int getSubscribe(){
		return this.subscribe;
	}

	public void setSubscribe(int subscribe){
		this.subscribe = subscribe;
	}

	public String getOpenid(){
		return this.openid;
	}

	public void setOpenid(String openid){
		this.openid = openid;
	}

	public String getNickname(){
		return this.nickname;
	}

	public void setNickname(String nickname){
		this.nickname = nickname;
	}

	public Integer getSex(){
		return this.sex;
	}

	public void setSex(Integer sex){
		this.sex = sex;
	}

	public String getCity(){
		return this.city;
	}

	public void setCity(String city){
		this.city = city;
	}

	public String getCountry(){
		return this.country;
	}

	public void setCountry(String country){
		this.country = country;
	}

	public String getProvince(){
		return this.province;
	}

	public void setProvince(String province){
		this.province = province;
	}

	public String getLanguage(){
		return this.language;
	}

	public void setLanguage(String language){
		this.language = language;
	}

	public String getHeadimgurl(){
		return this.headimgurl;
	}

	public void setHeadimgurl(String headimgurl){
		this.headimgurl = headimgurl;
	}

	public long getSubscribe_time(){
		return this.subscribe_time;
	}

	public void setSubscribe_time(long subscribe_time){
		this.subscribe_time = subscribe_time;
	}

	public String getUnionid(){
		return this.unionid;
	}

	public void setUnionid(String unionid){
		this.unionid = unionid;
	}

	public String getRemark(){
		return this.remark;
	}

	public void setRemark(String remark){
		this.remark = remark;
	}

	public long getGroupid(){
		return this.groupid;
	}

	public void setGroupid(long groupid){
		this.groupid = groupid;
	}
}
