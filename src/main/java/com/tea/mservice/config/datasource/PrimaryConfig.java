package com.tea.mservice.config.datasource;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactoryPrimary", transactionManagerRef = "transactionManagerPrimary", basePackages = {
		"com.tea.mservice.*" })
public class PrimaryConfig {
	
	@Value("${spring.datasource.primary.hibernate.show_sql}")
	private Boolean primaryShowSql;
	@Value("${spring.datasource.primary.org.hibernate.dialect}")
	private String primaryHibernateDialet;
	
	@Autowired
	private JpaProperties jpaProperties;

	@Autowired
	@Qualifier("primaryDataSource")
	private DataSource primaryDataSource;

	@Bean(name = "entityManagerPrimary")
	@Primary
	public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
		return entityManagerFactoryPrimary(builder).getObject().createEntityManager();
	}

	@PersistenceContext(unitName = "primary")
	@Bean(name = "entityManagerFactoryPrimary")
	@Qualifier("entityManagerFactoryPrimary")
	@Primary
	public LocalContainerEntityManagerFactoryBean entityManagerFactoryPrimary(EntityManagerFactoryBuilder builder) {
		return builder.dataSource(primaryDataSource).properties(getVendorProperties(primaryDataSource))
				.packages("com.tea.mservice.*").persistenceUnit("primary").build();
	}

	private Map<String, String> getVendorProperties(DataSource dataSource) {
		Map<String, String> jpaMap = jpaProperties.getProperties();
		jpaMap.put("hibernate.show_sql", primaryShowSql.toString());
		jpaMap.put("hibernate.dialect", primaryHibernateDialet);
		jpaMap.put("hibernate.hbm2ddl.auto", "validate");
		return jpaProperties.getHibernateProperties(dataSource);
	}

	@Bean(name = "transactionManagerPrimary")
	@Primary
	PlatformTransactionManager transactionManagerPrimary(EntityManagerFactoryBuilder builder) {
		return new JpaTransactionManager(entityManagerFactoryPrimary(builder).getObject());
	}
	
	@Bean(name = "jdbcTemplate")
	public JdbcTemplate getJdbcTemplate() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(primaryDataSource);
		return jdbcTemplate;
	}

}