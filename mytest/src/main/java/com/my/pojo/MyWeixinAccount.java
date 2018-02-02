package com.my.pojo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import lombok.Data;

@ConfigurationProperties(prefix="wx")
@Component
@Data
public class MyWeixinAccount {
	private String appid;
	private String appsecret;
}
