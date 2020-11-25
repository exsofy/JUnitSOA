package com.tcua.junit.soa;

import java.util.HashMap;
import java.util.Map;

import com.tcua.junit.soa.handler.ISOAClassHandler;

public class ParsingStatus {
	public Object object;
	public ISOAClassHandler handler;
	public Object handlerState;
	private final ParsingStatus parent;

	public ParsingStatus(ParsingStatus parent, Object object,
			ISOAClassHandler handler) {
		super();
		this.parent = parent;
		this.object = object;
		this.handler = handler;
		this.handlerState = null;
	}

	public ParsingStatus getParent() {
		return parent;
	}	
}