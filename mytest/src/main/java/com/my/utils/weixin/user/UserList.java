package com.my.utils.weixin.user;

import java.util.List;

public class UserList{
	public int total;
	public int count;
	public String next_openid;
	public Data data;

	public int getTotal(){
		return this.total;
	}

	public void setTotal(int total){
		this.total = total;
	}

	public int getCount(){
		return this.count;
	}

	public void setCount(int count){
		this.count = count;
	}

	public String getNext_openid(){
		return this.next_openid;
	}

	public void setNext_openid(String next_openid){
		this.next_openid = next_openid;
	}

	public Data getData(){
		return this.data;
	}

	public void setData(Data data){
		this.data = data;
	}

	public class Data{
		public List<String> openid;

		public Data(){
		}
	}
}
