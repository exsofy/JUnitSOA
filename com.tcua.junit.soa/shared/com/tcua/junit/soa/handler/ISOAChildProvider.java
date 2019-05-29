package com.tcua.junit.soa.handler;

import org.xml.sax.Attributes;

import com.tcua.junit.soa.ParsingStatus;

public interface ISOAChildProvider {
	public Object getChild(ParsingStatus currentObj, Attributes attributes);
}
