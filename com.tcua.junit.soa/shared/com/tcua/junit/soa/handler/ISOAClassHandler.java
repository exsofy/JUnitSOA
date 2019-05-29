package com.tcua.junit.soa.handler;

import org.w3c.dom.Element;
import org.xml.sax.Attributes;

import com.tcua.junit.soa.ParsingStatus;

public interface ISOAClassHandler {

	// XML tag name
	String getTagName();

	/**
	 * Extend parent with object content
	 * 
	 * @param parent
	 *            DOM parent element
	 * @param obj
	 *            exported object
	 * @return true if extension is completed
	 */
	boolean extend(Element parent, Object obj);

	/**
	 * Extend parent with object content. The extension name is overriden.
	 * 
	 * @param parent
	 *            DOM parent element
	 * @param obj
	 *            exported object
	 * @param XMLName
	 *            overridden name (attribute or tag)
	 * @return true if extension is completed
	 */
	boolean extend(Element entry, Object obj, String XMLName);

	/**
	 * The handler manages a entry sublevel.
	 * 
	 * @return true if entry expected
	 */
	boolean hasEntry();

	/**
	 * If value is defined, check it and return true. If the value is not
	 * correct assert fail.
	 * 
	 * @param currentObj
	 *            current object definition
	 * @param attributes
	 *            XML attributes
	 * @return true if value has been checked (not necessary correct)
	 */
	boolean valueChecked(ParsingStatus currentObj, Attributes attributes);

}
