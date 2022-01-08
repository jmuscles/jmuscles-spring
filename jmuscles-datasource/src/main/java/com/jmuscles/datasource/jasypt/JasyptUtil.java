/**
 * 
 */
package com.jmuscles.datasource.jasypt;

import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;
import org.jasypt.properties.EncryptableProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * @author manish goel
 *
 */
public class JasyptUtil {

	private static Logger logger = LoggerFactory.getLogger(JasyptUtil.class);
	private static List<JasyptDecryptor> jasyptDecryptors;

	public static void setJasyptDecryptors(List<JasyptDecryptor> jasyptDecryptors) {
		JasyptUtil.jasyptDecryptors = jasyptDecryptors;
	}

	public static Properties decryptProperties(Properties dsProps) {
		if (jasyptDecryptors != null && !jasyptDecryptors.isEmpty() && jasyptDecryptors.get(0) != null) {
			return jasyptDecryptors.get(0).decrypt(dsProps);
		} else {
			return decryptPropertiesDefault(dsProps);
		}
	}

	public static Properties decryptPropertiesDefault(Properties props) {

		Properties newProps = null;
		String jasyptAlgorithm = (String) props.remove("jasypt-algorithm");
		String jasyptPassword = (String) props.remove("jasypt-password");

		if (StringUtils.hasText(jasyptPassword)) {
			StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
			if (StringUtils.hasText(jasyptAlgorithm)) {
				encryptor.setAlgorithm(jasyptAlgorithm);
			}
			encryptor.setIvGenerator(new RandomIvGenerator());
			encryptor.setPassword(jasyptPassword);
			EncryptableProperties encryptableProperties = new EncryptableProperties(encryptor);
			encryptableProperties.putAll(props);

			newProps = new Properties();

			Set<Object> keys = encryptableProperties.keySet();
			for (Object key : keys) {
				try {
					Object value = encryptableProperties.get(key);
					if (value == null) {
						value = props.get(key);
					}
					newProps.put(key, value);
				} catch (RuntimeException e) {
					logger.error("Error while decrypting property : " + key);
					throw e;
				}
			}
			logger.info("datasource props decrypted.");
		} else {
			newProps = props;
		}

		return newProps;
	}

	public static String encryptProperties(String jasyptAlgorithm, String jasyptPassword, String plainText) {

		if (!StringUtils.hasText(jasyptPassword)) {
			logger.info("jasyptPassword is not present, plainText can not be encrypted.");
			return plainText;
		}

		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		if (StringUtils.hasText(jasyptAlgorithm)) {
			encryptor.setAlgorithm(jasyptAlgorithm);
		}
		encryptor.setIvGenerator(new RandomIvGenerator());
		encryptor.setPassword(jasyptPassword);

		return encryptor.encrypt(plainText);
	}

}
