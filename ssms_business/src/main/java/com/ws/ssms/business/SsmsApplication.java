package com.ws.ssms.business;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@MapperScan(value= {"com.ws.ssms.business.mapper"}) 
@EnableRedisHttpSession
public class SsmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SsmsApplication.class, args);
	}

}
