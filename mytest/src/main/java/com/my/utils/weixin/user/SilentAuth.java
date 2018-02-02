package com.my.utils.weixin.user;
public class SilentAuth {
	private String openid;
	private String context;
	private String data;

	public String toString() {
		return "[openid=" + this.openid + ", context=" + this.context + ", data=" + this.data + "]";
	}

	public SilentAuth() {
	}

	public SilentAuth(String openid, String context, String data) {
		this.openid = openid;
		this.context = context;
		this.data = data;
	}

	public String getData() {
		return this.data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getOpenid() {
		return this.openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getContext() {
		return this.context;
	}

	public void setContext(String context) {
		this.context = context;
	}
}
