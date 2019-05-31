For JUnit test directly from eclipse create resource folder and put 
the structure and XML dump into it.
The following tester uses the current content and the JUnit test might 
be repeated until the file content is correct.

```Java
public class EclipseSOAChecker extends SOAChecker {

	private AbstractUIPlugin activator;

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


Export in one line
```Java 
( new SOAExporter() ).dumpResponse(columnResponse, new File( "C:\\temp")), "List_colums.xml" );
```

Export in system temp directory
```Java 
SOAExporter.dumpToTmp ( columnResponse, "List_colums.xml" );
```
