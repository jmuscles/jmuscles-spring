# jmuscles-spring-datasource-starter
Spring boot - Multiple Data Sources based on configurations

jmuscles-spring-datasource helps to create multiple data sources by property/yml file configurations.

* Segration of Database connections vs connection-pool properties
* Integration with Jasypt helps the credential encryption
* It uses Hikari datasource library
* Datasource properties can be updated at runtime With the help of actuator refresh
* Multiple datasources with several databases at same time

Installation and Getting Started
===

**Version Compatibility:**
| name | latest | JDK | Spring boot|
| --- | --- | --- | --- |
| jmuscles-datasource-j8-sb225 | 1.0 | 8 |2.2.5|

**Pom.xml changes:** <br />
Add below in your pom to get dependency

```
		<dependency>
  			<groupId>com.jmuscles</groupId>
  			<artifactId>jmuscles-datasource-j8-sb225</artifactId>
  			<version>1.0</version>
		</dependency>
```


**Property file configurations:**

Database connection properties:


```
jmuscles:
   db-properties:
     connections:
       <database-connection-key>:
         driverClassName: com.mysql.cj.jdbc.Driver
         jdbcUrl: jdbc:mysql://localhost:3306/test2
         username: root
         password: ENC(bZNZvziY9M2cpMi7ECvE+WkjgtgmcCNt) 
         jasypt-password: my-secret              
         jasypt-algorithm: PBEWithMD5AndDES    
      
```
Example:  https://github.com/jmuscles/jmuscles-spring-datasource-demo/tree/main/src/main/resources/application-dbconnections.yml

Connection pool and datasource configurations : 

```
jmuscles:
   db-properties:
     dataSources:
       <datasource-key>:
         type: hikari
         connectionPropsKey: database-connection-key
         connectionPoolProperties:
           poolName: 
           minimumIdle: 1
           maximumPoolSize: 5 
           maxLifetime: 0
           connectionTimeout: 180000     
```

Example : https://github.com/jmuscles/jmuscles-spring-datasource-demo/tree/main/src/main/resources/application-datasources.yml

Note: `connectionPropsKey` is the connection key in data source configuration. it's the link to connect connection and pool properties. `type` value can be either tomcat or hikari. if not defined then hikaricp will be used to create the datasource. 

**If `type` is `hikari` then all the connectionPoolProperties should exactly match as Hikari configuration properties and when `type` is `tomcat` connectionPoolProperties should match the tomcat jdbc pool properties.**

Hikari : https://github.com/brettwooldridge/HikariCP <br />
Tomcat : https://tomcat.apache.org/tomcat-7.0-doc/jdbc-pool.html

**Password Encryption:** <br />
For clear text password, please ignore or do not define the jasypt-password & jasypt-algorithm properties.

For encrypted password:
1. Default implementation:
	1. default implementation requires `jasypt-password` & `jasypt-algorithm` properties as mentioned above. Also, Encrypted password should be wrapped inside ENC(<encrypted_password>). For further detail on encryption, Please check out Jasypt (http://www.jasypt.org/) 
	2. How to encrypt password: `jasypt-password` is required and `jasypt-algorithm` is optional field, Also while encrypting password use `RandomIvGenerator`. `com.jmuscle.datasource.jasypt.JasyptUtil` provides a method to encrypt: JasyptUtil.encryptProperties("PBEWithMD5AndDES", "my-secret", "password")
	
2. Custom decryptor: implement `com.jmuscle.datasource.jasypt.JasyptDecryptor` interface and make the implemented class as Spring bean e.g. annotate class @Component please look at https://github.com/jmuscles/jmuscles-spring-datasource-demo/tree/main/src/main/java/com/jmuscles/datasource/demo/JasyptDecryptorImpl.java



**Java changes:** <br />
Autowire `DataSourceGenerator` in bean class and get the data source as : 

```
dataSourceGenerator.get("ds1")
```
ds1 is the datasource-key from property/yml file. 
Example: https://github.com/jmuscles/jmuscles-spring-datasource-demo/tree/main/src/main/java/com/jmuscles/datasource/demo/SQLQueryExecutor.java

**What If, desired version is not available**
please follow below steps to build it: 

* checkout jmuscles-spring-datasource-starter code
* change the version in pom.xml files to match with your jdk/spring boot version.
* maven build should spit out the desired jmuscles-spring-datasource-starter jar file 
