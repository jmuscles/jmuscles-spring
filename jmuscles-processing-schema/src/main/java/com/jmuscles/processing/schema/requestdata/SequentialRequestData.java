/**
 * 
 */
package com.jmuscles.processing.schema.requestdata;

import java.util.List;

/**
 * @author manish goel
 *
 */
public class SequentialRequestData implements RequestData {

	private static final long serialVersionUID = 4420931186072029629L;

	private List<RequestData> sequentialDataList;

	public List<RequestData> getSequentialDataList() {
		return sequentialDataList;
	}

	public void setSequentialDataList(List<RequestData> sequentialDataList) {
		this.sequentialDataList = sequentialDataList;
	}

}
