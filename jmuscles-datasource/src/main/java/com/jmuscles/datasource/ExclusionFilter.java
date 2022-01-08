/**
 * 
 */
package com.jmuscles.datasource;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.autoconfigure.AutoConfigurationImportFilter;
import org.springframework.boot.autoconfigure.AutoConfigurationMetadata;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

/**
 * @author manish goel
 */
public class ExclusionFilter implements AutoConfigurationImportFilter, EnvironmentAware {

	private Environment environment;

	private static final Set<String> SHOULD_SKIP = new HashSet<>(
			Arrays.asList("org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration"/*
																									 * ,
																									 * "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration"
																									 */));

	@Override
	public boolean[] match(String[] classNames, AutoConfigurationMetadata metadata) {
		boolean[] matches = new boolean[classNames.length];
		boolean isDefaultDatasourceAvailable = environment.containsProperty("spring.datasource.url");

		for (int i = 0; i < classNames.length; i++) {
			matches[i] = isDefaultDatasourceAvailable || !SHOULD_SKIP.contains(classNames[i]);
		}
		return matches;
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

}
