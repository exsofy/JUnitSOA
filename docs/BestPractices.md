
### Automatic export to system temp directory by any exporter e.g. after update

SOAChecker.setAutoDumpGeneral(true);


### Export in one line
```Java 
( new SOAExporter(new File("C:\\temp") ) ).dumpResponse(columnResponse, "List_colums.xml" );
```

### Export in system temp directory
```Java 
SOAExporter.dumpToTmp ( columnResponse, "List_colums.xml" );
```

### Within Eclipse project

For JUnit test directly from eclipse create resource folder and put 
the structure and XML dump into it.
The following tester uses the current content and the JUnit test might 
be repeated until the file content is correct.

```Java
public class EclipseSOAChecker extends SOAChecker {

	private AbstractUIPlugin activator;

	static {
		// automatic dump
		setAutoDumpGeneral(false);
	}

	public EclipseSOAChecker(AbstractUIPlugin activator) {
		super();
		this.activator = activator;
	}

	/**
	* Check response against file in eclipse repository.
	*/
	public void checkResponse(Object resp, String fileName) {
	
		String path = resp.getClass().getCanonicalName().replace('.', '/')
				.replace('$', '/');
		URL resource = activator.getBundle().getResource(
				"test/resources/" + path + "/" + fileName);
	
		try {
			checkResponse(resp, resource);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
```

### Export with defined properties of TCComponent
```Java
// create exporter
SOAExporter soaExporter = new SOAExporter( new File("C:\\temp") );
// Create handler wit properties export for ItemRevision
TCComponentHandler itemRevisionHandler = new TCComponentHandler(
		soaExporter);
itemRevisionHandler.setPropertyNames(new String[] {
		"object_name", "object_type", "item_revision_id" });
soaExporter.setHandler(TCComponentItemRevision.class,
		itemRevisionHandler);
// export the response to filesystem
soaExporter.dumpResponse(resp,
		"Product_Stage1.xml");

// create tester
SoaTest soaTest = new SoaTest();

try {
  // test the response, JUnit asserts are performed
  soaTest.checkResponse(resp, new File("C:\\temp"),
  "Product_Stage1.xml");
} catch (ParserConfigurationException | SAXException
 | IOException e) {
 // unexpected end
 e.printStackTrace();
}

```

