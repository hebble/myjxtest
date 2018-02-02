package com.my.pojo;

import java.util.HashMap;
import java.util.Map;

/**
 *  用户账户类型
 */
public enum UserAccountTypeEnum {
	/**
	 * 现金
	 */
	TYPE_CASH("现金",1),
	/**
	 * 享贝
	 */
	TYPE_GOLD("享贝",2),
	/**
	 * 享点
	 */
	TYPE_POINT("享点",3),
	/**
	 * 信用分
	 */
	TYPE_HONOR("信用分",4),
	/**
	 * 种子
	 */
	TYPE_SEED("种子",5),
	/**
	 * 果实
	 */
	TYPE_FURIT("果实",6),
	
	;
	/**
	 * 描述
	 */
	private String desc;
	
	private final Integer value;

	private UserAccountTypeEnum(String desc,Integer value) {
		this.desc = desc;
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public static String getDesc (Integer value){
		Map<Integer, String> map = toMap();
		return map.get(value);
	}
	
	public static Map<Integer, String> toMap(){
		Map<Integer, String> map = new HashMap<>();
		UserAccountTypeEnum[] enums = UserAccountTypeEnum.values();
		for (UserAccountTypeEnum userAccountTypeEnum : enums) {
			map.put(userAccountTypeEnum.getValue(), userAccountTypeEnum.getDesc());
		}
		return map;
	}
}
