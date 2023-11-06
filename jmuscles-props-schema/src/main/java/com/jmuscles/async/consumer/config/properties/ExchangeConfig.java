/**
 * 
 */
package com.jmuscles.async.consumer.config.properties;

/**
 * @author manish goel
 *
 */
public class ExchangeConfig {

	private String name;
	private String type;
	private String parent;

	public ExchangeConfig() {
		// TODO Auto-generated constructor stub
	}

	public ExchangeConfig(String name, String type, String parent) {
		super();
		this.name = name;
		this.type = type;
		this.parent = parent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String exchangeType) {
		this.type = exchangeType;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parentExchange) {
		this.parent = parentExchange;
	}

}
