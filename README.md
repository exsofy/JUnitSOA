# JUnitSOA
Teamcenter JUnit SOA response tester


Generic code sniplet for dumping Teamcenter SOA responses to xml file.
The dump file can be adopted to generic template and used in JUnit tests.

### Basic usage:

```Java
// create tester
SoaTest soaTest = new SoaTest();

// export if necessary
// soaTest.setAutoDump(true);

try {
  // test the response, JUnit asserts are performed
  soaTest.checkResponse(resp, new File("C:\\SOATest"),
  "Product_Stage1.xml");
} catch (ParserConfigurationException | SAXException
 | IOException e) {
 // unexpected end
 e.printStackTrace();
}

```

### Standalone exporter:

```Java
// create exporter
SOAExporter soaExporter = new SOAExporter( new File("C:\\SOATest") );

// export the response to filesystem
soaExporter.dumpResponse(resp,
		"Product_Stage1.xml");
```

[Best practices](docs/BestPractices.md)


### Code include:
use the [com.tcua.junit.soa/shared](com.tcua.junit.soa/shared) folder as additional source folder
in your java project.
e.g. in svn
```
svn:externals 
-r10 https://github.com/exsofy/JUnitSOA/trunk/com.tcua.junit.soa/shared JUnitSOA
```

### Code clone:
Copy [com.tcua.junit.soa/shared/com](com.tcua.junit.soa/shared/com) into your project.


## Localization

For tests with multiple locales the attributes and elements can be evaluated 
locale specific:

```xml
<!--attribute name="displayName" value="Category"/ -->
<localized name="displayName">
	<attribute locale="en" name="displayName" value="Category"/>
	<attribute locale="de" name="displayName" value="Kategorie"/>
</localized>
...
<!--element value="empty" -->
<localized>
	<element locale="en" value="empty"/>
	<element locale="de" value="leer"/>
</localized>
```
