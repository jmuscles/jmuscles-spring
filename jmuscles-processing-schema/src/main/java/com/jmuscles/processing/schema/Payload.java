package com.jmuscles.processing.schema;

import java.io.Serializable;
import java.util.List;

import com.jmuscles.processing.schema.requestdata.RequestData;

/**
 * 
 * @author manish goel
 *
 */
public class Payload implements Serializable {

	private static final long serialVersionUID = 6270895554394469056L;

	private List<RequestData> requestDataList;

	public Payload() {
	}

	public Payload(List<RequestData> requestDataList) {
		super();
		this.requestDataList = requestDataList;
	}

	public List<RequestData> getRequestDataList() {
		return requestDataList;
	}

	public void setRequestDataList(List<RequestData> requestDataList) {
		this.requestDataList = requestDataList;
	}

}
