/**
 * @author manish goel
 *
 */
package com.jmuscles.props.util;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 
 */
public class Util {

	public static Timestamp currentTimeStamp() {
		return new Timestamp((new Date()).getTime());
	}

}
