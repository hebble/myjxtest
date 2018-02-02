package com.my.mytest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Test {

	public static void main(String[] args) {
		try {
			int i = 1/0;
		} catch (Exception e) {
			log.error("异常信息:{}",e);
		}
	}

}
