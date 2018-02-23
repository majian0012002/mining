package com.miller.mining.config;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

@Configuration
@EnableAutoConfiguration
public class DruidConfig {
	
	private Logger logger = LoggerFactory.getLogger(DruidConfig.class);

	@Value("${spring.datasource.url}")
	private String dbUrl;
	@Value("${spring.datasource.username}")
	private String username;
	@Value("${spring.datasource.password}")
	private String password;
	@Value("${spring.datasource.driver-class-name}")
	private String driverClassName;
	@Value("${spring.datasource.initialSize}")
	private int initialisize;
	@Value("${spring.datasource.minIdle}")
	private int minIdle;
	@Value("${spring.datasource.maxActive}")
	private int maxActive;
	@Value("${spring.datasource.maxWait}")
	private int maxWait;
	@Value("${spring.datasource.timeBetweenEvictionRunsMillis}")
	private int timeBetweenEvinctionRunMillis;
	@Value("${spring.datasource.minEvictableIdleTimeMillis}")
	private int minEvintableIdleTimeMillis;
	@Value("${spring.datasource.validationQuery}")
	private String validationQuery;
	@Value("${spring.datasource.testWhileIdle}")
	private boolean testWhileIdle;
	@Value("${spring.datasource.testOnBorrow}")
	private boolean testOnBorrow;
	@Value("${spring.datasource.testOnReturn}")
	private boolean testOnReturn;
	@Value("${spring.datasource.poolPreparedStatements}")
	private boolean poolPreparedStatements;
	@Value("${spring.datasource.maxPoolPreparedStatementPerConnectionSize}")
	private int maxPoolPreparedStatementPerConnectionSize;
	@Value("${spring.datasource.filters}")
	private String filters;
	@Value("${spring.datasource.logSlowSql}")
	private String logSlowSql;
	
	@Bean
	public ServletRegistrationBean druidServlet() {
		ServletRegistrationBean reg = new ServletRegistrationBean();
		reg.setServlet(new StatViewServlet());
		reg.addUrlMappings("/druid/*");
		reg.addInitParameter("loginUsername", username);
		reg.addInitParameter("loginPassword", password);
		reg.addInitParameter("logSlowSql", logSlowSql);
		
		return reg;
	}
	
	@Bean
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean filter = new FilterRegistrationBean();
		filter.setFilter(new WebStatFilter());
		filter.addUrlPatterns("/*");
		filter.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
		filter.addInitParameter("profileEnable", "true");
		
		return filter;
	}
	
	public DataSource druidDataSource() {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUrl(dbUrl);
		dataSource.setDriverClassName(driverClassName);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		dataSource.setInitialSize(initialisize);
		dataSource.setMinIdle(minIdle);
		dataSource.setMaxActive(maxActive);
		dataSource.setMaxWait(maxWait);
		dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvinctionRunMillis);
		dataSource.setMinEvictableIdleTimeMillis(minEvintableIdleTimeMillis);
		dataSource.setValidationQuery(validationQuery);
		dataSource.setTestWhileIdle(testWhileIdle);
		dataSource.setTestOnBorrow(testOnBorrow);
		dataSource.setTestOnReturn(testOnReturn);
		try {
			dataSource.setFilters(filters);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("druid数据源无法初始化filter");
			e.printStackTrace();
		}
		
		return dataSource;
	}
}
