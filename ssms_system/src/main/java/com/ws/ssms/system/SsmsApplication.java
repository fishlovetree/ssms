package com.ws.ssms.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@MapperScan(value= {"com.ws.ssms.system.mapper"}) 
@EnableRedisHttpSession
public class SsmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SsmsApplication.class, args);
	}

}
