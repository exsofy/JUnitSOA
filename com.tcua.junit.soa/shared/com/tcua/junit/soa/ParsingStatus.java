package com.tcua.junit.soa;

import com.tcua.junit.soa.handler.ISOAClassHandler;

public class ParsingStatus {
	public Object object;
	public ISOAClassHandler handler;
	public Object handlerState;

	public ParsingStatus(Object object, ISOAClassHandler handler) {
		super();
		this.object = object;
		this.handler = handler;
		this.handlerState = null;
	}

}