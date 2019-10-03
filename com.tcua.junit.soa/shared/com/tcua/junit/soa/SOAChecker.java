package com.tcua.junit.soa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.tcua.junit.soa.handler.ElementHandler;
import com.tcua.junit.soa.handler.ISOAChildProvider;
import com.tcua.junit.soa.handler.ISOAClassHandler;
import com.tcua.junit.soa.handler.RootHandler;

public class SOAChecker extends SOAKit {

	protected class CheckHandler extends DefaultHandler {

		// manages the current file location
		private Locator locator;

		// parsing stack, every XML tag push/pop a new parsing status object on
		// the stack
		final LinkedList<ParsingStatus> fifo = new LinkedList<ParsingStatus>();

		// parsing status for last parsed XML tag
		private ParsingStatus currentObj;

		public CheckHandler(Object rootObj) {
			// starts from root and entry
			currentObj = new ParsingStatus(null, rootObj, rootHandler);
		}

		// this will be called when XML-parser starts reading
		// XML-data; here we save reference to current position in XML:
		public void setDocumentLocator(Locator locator) {
			this.locator = locator;
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {

			if (!(currentObj.handler instanceof ISOAChildProvider)) {
				fail("Unexpected handler");
			}

			if (currentObj != null) {
				Object nextObj = ((ISOAChildProvider) currentObj.handler)
						.getChild(currentObj, attributes);
				
				ISOAClassHandler nextHandler = getHandler(nextObj == null ? null
						: nextObj.getClass());

				if (nextHandler != null) {
					
					if (!(RootHandler.getHandlerTagName().equals(qName)
							|| ElementHandler.getHandlerTagName().equals(qName) || nextHandler
								.getTagName() == null)) {
						// neither root, element nor null handler
						assertEquals(qName, nextHandler.getTagName());
					}

					fifo.push(currentObj);
					currentObj = new ParsingStatus(currentObj,nextObj, nextHandler);

					nextHandler.valueChecked(currentObj, attributes, locator);

				}
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			currentObj = fifo.pop();
		}
	}

	public SOAChecker() {
		super();
	}

	public void checkResponse(Object resp, File file)
			throws ParserConfigurationException, SAXException, IOException {

		sourceURL = file.toURI().toURL();
		FileInputStream is = new FileInputStream(file);

		checkResponse(resp, new InputSource(is));
	}

	public void checkResponse(Object resp, File fileRoot, String fileName)
			throws ParserConfigurationException, SAXException, IOException {

		String path = resp.getClass().getCanonicalName()
				.replace('.', File.separatorChar)
				.replace('$', File.separatorChar);

		File file = new File(fileRoot, path + File.separatorChar + fileName);
		sourceURL = file.toURI().toURL();
		
		FileInputStream is = new FileInputStream(file);

		checkResponse(resp, new InputSource(is));
	}

	public void checkResponse(Object resp, InputSource is)
			throws ParserConfigurationException, SAXException, IOException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();

		CheckHandler handler = this.new CheckHandler(resp);
		saxParser.parse(is, handler);
	}

	public void checkResponse(Object resp, URL resource)
			throws ParserConfigurationException, SAXException, IOException {
		sourceURL = resource;

		InputSource is = new InputSource(resource.openStream());

		checkResponse(resp, is);
	}

}
