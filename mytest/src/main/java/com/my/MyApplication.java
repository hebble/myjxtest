package com.my;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@ComponentScan("com.my")
@Slf4j
public class MyApplication {
	
	public static void main(String[] args) {
		/** 创建SpringApplication对象 */
		SpringApplication springApplication = 
				new SpringApplication(MyApplication.class);
		/** 设置横幅关闭 */
//		springApplication.setBannerMode(Mode.OFF);
		/** 运行 */
		springApplication.run(args);
		System.out.println("---------------- MyApplication已启动! ---------------");
	}
}
