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

	// Root directory for export
	private final File rootDir;
	/**
	 * Standard constructor initializes all default handlers and define user
	 * temporary directory as root export directory
	 */
	public SOAExporter() {
		this(new File(System.getProperty("java.io.tmpdir")));
	}

	/**
	 * Directory constructor initializes all default handlers and set root
	 * export directory
	 * 
	 * @param rootDir
	 */
	public SOAExporter(File rootDir) {
		if (!rootDir.exists() || !rootDir.canWrite() || !rootDir.isDirectory()) {
			throw new IllegalArgumentException("rootDir");
		}
		this.rootDir = rootDir;
	}

	// singleton for preconfigured export
	static SOAExporter lazyExportSingleton = null;

	/**
	 * Dump response to current user temporary directory
	 * 
	 * @param resp
	 *            response
	 * @param fileName
	 *            dump file name
	 */
	public static void dumpToTmp(Object resp, String fileName) {
		if (lazyExportSingleton == null) {
			lazyExportSingleton = new SOAExporter();
		}
		lazyExportSingleton.dumpResponse(resp, fileName);
	}

	/**
	 * Dump response to specific file. The directory must exist, Root export
	 * directory is not used.
	 * 
	 * @param resp
	 *            response
	 * @param filePath
	 *            absolute or relative file path
	 */
	public void dumpResponse(Object resp, File filePath) {
	
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
	
			java.io.Writer writer = new java.io.FileWriter(filePath);
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

	/**
	 * Dump response to package directory structure below defined directory. The
	 * package directory structure is created on demand, the root directory must
	 * exist
	 * 
	 * @param resp
	 *            response
	 * @param fileName
	 *            export file name
	 */
	public void dumpResponse(Object resp, String fileName) {

		String path = resp.getClass().getCanonicalName().replace('.', '/')
				.replace('$', '/');

		File exportDir = new File(rootDir, path);
		exportDir.mkdirs();

		dumpResponse(resp, new File(exportDir, fileName));
	}

}
