# JUnitSOA
Teamcenter JUnit SOA response tester


Generic code sniplet for dumping Teamcenter SOA responses to xml file.
The dump file can be adopted and used in JUnit validation module.

Code include:
use the [com.tcua.junit.soa/shared](com.tcua.junit.soa/shared) folder as additional source folder
in your java project.
e.g. in svn
svn:externals 
-r2 https://github.com/exsofy/JUnitSOA/trunk/com.tcua.junit.soa/shared JUnitSOA

Code clone:
Copy [com.tcua.junit.soa/shared/com](com.tcua.junit.soa/shared/com) into your project.

[Best practices](docs/BestPractices.md)

Principal usage:

```Java
// create exporter
SOAExporter soaExporter = new SOAExporter( new File("C:\\temp\\") );
// Create handler wit properties export for ItemRevision
TCComponentHandler itemRevisionHandler = new TCComponentHandler(
		soaExporter);
itemRevisionHandler.setPropertyNames(new String[] {
		"object_name", "object_type", "item_revsion_id" });
soaExporter.setHandler(TCComponentItemRevision.class,
		itemRevisionHandler);
// export the response to filesystem
soaExporter.dumpResponse(resp,
		"Product_Stage1.xml");

// create tester
SoaTest soaTest = new SoaTest();

try {
  // test the response, JUnit asserts are performed
  soaTest.checkResponse(resp, new File("C:\\temp\\"),
  "Product_Stage1.xml");
} catch (ParserConfigurationException | SAXException
 | IOException e) {
 // unexpected end
 e.printStackTrace();
}

```
