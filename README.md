# JUnitSOA
Teamcenter JUnit SOA response tester


Generic code sniplet for dumping Teamcenter SOA responses to xml file.
The dump file can be adopted and used in JUnit validation module.

Code include:
use the com.tcua.junit.soa/shared folder as part source folde in your
project.

Basic usage:

```Java
// create exporter
SOAExporter soaExporter = new SOAExporter();
// Create handler wit properties export for ItemRevision
TCComponentHandler itemRevisionHandler = new TCComponentHandler(
		soaExporter);
itemRevisionHandler.setPropertyNames(new String[] {
		"object_name", "object_type", "item_revsion_id" });
soaExporter.setHandler(TCComponentItemRevision.class,
		itemRevisionHandler);
// export the response to filesystem
soaExporter.dumpResponse(resp, new File("C:\\temp\\"),
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

``'
