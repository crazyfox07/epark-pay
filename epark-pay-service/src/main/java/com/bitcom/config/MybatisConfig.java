package com.bitcom.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({"com.bitcom.base.mapper"})
public class MybatisConfig {
}



