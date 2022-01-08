/**
 * 
 */
package com.jmuscles.datasource.jasypt;

import java.util.Properties;

/**
 * @author manish goel
 *
 */
@FunctionalInterface
public interface JasyptDecryptor {

	public Properties decrypt(Properties dsProps);

}
