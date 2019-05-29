package com.tcua.junit.soa;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sun.org.apache.xml.internal.serialize.Method;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import com.tcua.junit.soa.handler.RootHandler;

/**
 * Export SOA response to an XML file, which can be used by checker.
 * 
 * @author Svatos coufal
 * 
 */
public class SOAExporter extends SOAKit {

	/**
	 * Standard constructor initializes all default handlers
	 */
	public SOAExporter() {
		super();
	}

	public void dumpResponse(Object resp, File root,
			String fileName) {
	
	    DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
	    icFactory.setNamespaceAware(true);
	    DocumentBuilder icBuilder;
		try {
	        icBuilder = icFactory.newDocumentBuilder();
	        Document doc = icBuilder.newDocument();
			Element rootElement = doc.createElement(RootHandler
					.getHandlerTagName());
			getRootHandler().extend(rootElement, resp);
			
			doc.appendChild(rootElement);
	
			String path = resp.getClass().getCanonicalName().replace('.', '/')
					.replace('$', '/');
	
			File exportDir = new File(root, path);
			exportDir.mkdirs();
	
			java.io.Writer writer = new java.io.FileWriter(new File(exportDir,
					fileName));
	        OutputFormat format = new OutputFormat(Method.XML, StandardCharsets.UTF_8.toString(), true);
	        format.setLineWidth(80);
	        format.setPreserveEmptyAttributes(true);
			// format.setPreserveSpace(true);
			format.setIndent(2);
			format.setIndenting(true);
	        XMLSerializer xml = new XMLSerializer(writer, format);
	        xml.serialize(doc);
	        writer.close();
		} catch (IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

}
