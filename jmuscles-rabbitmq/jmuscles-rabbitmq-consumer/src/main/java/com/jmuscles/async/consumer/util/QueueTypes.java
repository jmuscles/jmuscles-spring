/**
 * 
 */
package com.jmuscles.async.consumer.util;

/**
 * @author manish goel
 *
 */
public class QueueTypes {

	public static final String PRIMARY = "primary";
	public static final String RETRY = "retry";
	public static final String WAIT = "wait";
	public static final String ABANDONED = "abandoned";

	public static String queueName(String primaryQueueName, String queueType) {
		String name = primaryQueueName;
		if (!PRIMARY.equals(queueType)) {
			name += ("_" + queueType);
		}
		return name;
	}

}
