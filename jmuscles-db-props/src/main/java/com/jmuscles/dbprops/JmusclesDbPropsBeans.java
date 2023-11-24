package com.jmuscles.dbprops;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

import com.jmuscles.datasource.DataSourceEssentialBeans;
import com.jmuscles.datasource.DataSourceProvider;
import com.jmuscles.dbprops.jpa.entity.repository.AppGroupRepository;
import com.jmuscles.dbprops.jpa.entity.repository.AppProvisionRepository;
import com.jmuscles.dbprops.jpa.entity.repository.AppRepository;
import com.jmuscles.dbprops.jpa.entity.repository.PropReadRepository;
import com.jmuscles.dbprops.jpa.entity.repository.PropVersionCrudRepository;
import com.jmuscles.dbprops.jpa.entity.repository.PropWriteRepository;
import com.jmuscles.dbprops.jpa.entity.repository.RepositorySetup;
import com.jmuscles.dbprops.jpa.entity.repository.TenantCrudRepository;
import com.jmuscles.dbprops.service.ReadPropsFromDBService;
import com.jmuscles.props.config.AppPropsDBConfig;

/**
 * @author manish goel
 *
 */
@Import(DataSourceEssentialBeans.class)
@DependsOn("dataSourceGenerator")
public class JmusclesDbPropsBeans implements BeanFactoryAware, EnvironmentAware {

	private static final Logger logger = LoggerFactory.getLogger(JmusclesDbPropsBeans.class);

	private BeanFactory beanFactory;
	private Environment environment;

	public JmusclesDbPropsBeans() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	public BeanFactory getBeanFactory() {
		return this.beanFactory;
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	@Bean("appPropsDBConfig")
	@ConfigurationProperties(prefix = "props-db-setup", ignoreInvalidFields = true, ignoreUnknownFields = true)
	public AppPropsDBConfig appPropsDBConfig() {
		return new AppPropsDBConfig();
	}

	@Bean("repositorySetup")
	public RepositorySetup repositorySetup(@Qualifier("dataSourceProvider") DataSourceProvider dataSourceProvider,
			@Qualifier("appPropsDBConfig") AppPropsDBConfig appPropsDBConfig) {
		return new RepositorySetup(environment.getProperty("spring.application.name"), dataSourceProvider,
				appPropsDBConfig);
	}

	@Bean("tenantCrudRepository")
	public TenantCrudRepository tenantCrudRepository(@Qualifier("repositorySetup") RepositorySetup repositorySetup) {
		return new TenantCrudRepository(environment.getProperty("spring.application.name"), repositorySetup);
	}

	@Bean("propVersionCrudRepository")
	public PropVersionCrudRepository propVersionCrudRepository(
			@Qualifier("repositorySetup") RepositorySetup repositorySetup) {
		return new PropVersionCrudRepository(environment.getProperty("spring.application.name"), repositorySetup);
	}

	@Bean("propReadRepository")
	public PropReadRepository propReadRepository(@Qualifier("repositorySetup") RepositorySetup repositorySetup,
			@Qualifier("propVersionCrudRepository") PropVersionCrudRepository propVersionCrudRepository) {
		return new PropReadRepository(repositorySetup, propVersionCrudRepository);
	}

	@Bean("propWriteRepository")
	public PropWriteRepository propWriteRepository(@Qualifier("repositorySetup") RepositorySetup repositorySetup,
			@Qualifier("propVersionCrudRepository") PropVersionCrudRepository propVersionCrudRepository,
			@Qualifier("propReadRepository") PropReadRepository propReadRepository) {
		return new PropWriteRepository(environment.getProperty("spring.application.name"), repositorySetup,
				propVersionCrudRepository, propReadRepository);
	}

	@Bean("appRepository")
	public AppRepository appRepository(@Qualifier("repositorySetup") RepositorySetup repositorySetup) {
		return new AppRepository(environment.getProperty("spring.application.name"), repositorySetup);
	}

	@Bean("appGroupRepository")
	public AppGroupRepository appGroupRepository(@Qualifier("repositorySetup") RepositorySetup repositorySetup) {
		return new AppGroupRepository(environment.getProperty("spring.application.name"), repositorySetup);
	}

	@Bean("appProvisionRepository")
	public AppProvisionRepository appProvisionRepository(
			@Qualifier("repositorySetup") RepositorySetup repositorySetup) {
		return new AppProvisionRepository(environment.getProperty("spring.application.name"), repositorySetup);
	}

	@Bean("readPropsFromDBService")
	public ReadPropsFromDBService readPropsFromDBService(
			@Qualifier("appPropsDBConfig") AppPropsDBConfig appPropsDBConfig,
			@Qualifier("appProvisionRepository") AppProvisionRepository appProvisionRepository,
			@Qualifier("propReadRepository") PropReadRepository propReadRepository) {
		return new ReadPropsFromDBService(appProvisionRepository, propReadRepository);
	}

	@Bean("refreshBeanDbProps")
	public RefreshBeanDbProps refreshBeanDbProps() {
		return new RefreshBeanDbProps(this);
	}

}
