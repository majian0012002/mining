package com.miller.mining.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

@Configuration
@EnableAutoConfiguration
public class FastJsonMessageConverterConfig {

	@Bean
	public HttpMessageConverters fastJsonHttpMessageConverters() {
		FastJsonHttpMessageConverter fastJsonConverter = new FastJsonHttpMessageConverter();
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		
		fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
		
		List<MediaType> fastMediaType = new ArrayList<MediaType>();
		fastMediaType.add(MediaType.APPLICATION_JSON_UTF8);
		
		fastJsonConverter.setSupportedMediaTypes(fastMediaType);
		fastJsonConverter.setFastJsonConfig(fastJsonConfig);
		
		HttpMessageConverter<?> converter = fastJsonConverter;
		
		return new HttpMessageConverters(converter);
	}
}
