package com.tcua.junit.soa.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;

import com.tcua.junit.soa.ParsingStatus;
import com.tcua.junit.soa.SOAKit;

public class StringValueHandler extends AbstractHandler implements
		ISOAClassHandler {

	public StringValueHandler(SOAKit soaKit) {
		super(soaKit);
	}

	@Override
	public boolean extend(Element parent, Object obj) {
		return extend(parent, obj, "value");
	}

	@Override
	public boolean extend(Element parent, Object obj, String XMLName) {
		// manage null general
		if (super.extend(parent, obj))
			return true;

		parent.setAttribute(XMLName, obj.toString());

		return true;
	}

	@Override
	public boolean valueChecked(ParsingStatus currentObj,
			Attributes attributes, Locator locator) {
		if (isValueNull(currentObj.object, attributes, locator))
			return true;

		int iAttr = attributes.getIndex("value");
		if (iAttr >= 0) {
			// value
			if (!attributes.getValue(iAttr)
					.equals(currentObj.object.toString())) {
				assertEquals( "Attribute value " + getLocation(locator),
						attributes.getValue(iAttr),
						currentObj.object.toString());
			}
		} else if ( ( iAttr = attributes.getIndex("regex") ) >= 0 ) {
			// regex
			assertTrue(
					"Attribute matches " + getLocation(locator),
					currentObj.object.toString()
					.matches(attributes.getValue(iAttr)));
		} else if ((iAttr = attributes.getIndex("get")) >= 0) {
			// evaluate
			try {
				String[] invoke = attributes.getValue(iAttr).split("[.(,)]");

				if (invoke.length < 2) {
					fail("get not configured " + attributes.getValue(iAttr));
				}

				Object evaluator = soaKit.getEvaluator(invoke[0]);

				if (evaluator == null) {
					assertNotNull("Evaluator " + invoke[0] + " not registered",
							evaluator);
				}

				Class[] paramTypes = new Class[invoke.length - 2];
				Arrays.fill(paramTypes, String.class);

				Method method = evaluator.getClass().getDeclaredMethod(
						invoke[1],
						paramTypes);

				if (method == null) {
					assertNotNull(
							"Method " + invoke[1] + " " + evaluator.getClass(),
							method);
				}

				String[] params = new String[invoke.length - 2];
				System.arraycopy(invoke, 2, params, 0, invoke.length - 2);
				String value = (String) method.invoke(evaluator, params);

				assertEquals("Attribute value " + getLocation(locator), value,
						currentObj.object.toString());
			} catch (NoSuchMethodException
					| SecurityException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
				fail(e.getMessage() + " in " + getLocation(locator));
			}
		}
		// neither null, value, regex or get

		// attribute is leaf
		return true;
	}

	private void assertTrue(String string, boolean matches) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getTagName() {
		return "attribute";
	}

	@Override
	public boolean hasEntry() {
		return false;
	}

}
