package com.lifetree.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@Data
@ConfigurationProperties(prefix = "user.master")
@EnableConfigurationProperties
public class AppConfig {

	private Map<String,String> messages;
	
}
