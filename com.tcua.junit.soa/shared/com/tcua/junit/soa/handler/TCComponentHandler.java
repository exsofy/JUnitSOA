package com.tcua.junit.soa.handler;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;

import com.tcua.junit.soa.ParsingStatus;
import com.tcua.junit.soa.SOAKit;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCProperty;

public class TCComponentHandler extends AbstractHandler implements
		ISOAChildProvider {

	String[] propertyNames = null;

	public TCComponentHandler(SOAKit soaKit) {
		super(soaKit);
	}

	@Override
	public String getTagName() {
		return getHandlerTagName();
	}

	public static String getHandlerTagName() {
		return "tccomponent";
	}

	/**
	 * Set exported property names.Per default no property names are exported.
	 * 
	 * @param strings
	 *            real property names
	 */
	public void setPropertyNames(String[] propertyNames) {
		this.propertyNames = propertyNames;
	}

	@Override
	public boolean extend(Element parent, Object obj) {
		if (super.extend(parent, obj))
			return true;

		if (obj instanceof TCComponent && propertyNames != null) {
			TCProperty[] tcProperties;
			try {
				tcProperties = ((TCComponent) obj)
						.getTCProperties(propertyNames);
				for (int iProp = 0; iProp < tcProperties.length; iProp++) {
					TCProperty property = tcProperties[iProp];
					Element entry = parent.getOwnerDocument().createElement(
							TCPropertyHandler.getHandlerTagName());
					if (property != null) {
						entry.setAttribute("name", property.getPropertyName());
						entry.setAttribute("value", property.getUIFValue());
					} else {
						entry.setAttribute("name", propertyNames[iProp]);
						entry.setAttribute("isNull", "true");
					}
					parent.appendChild(entry);
				}
			} catch (TCException e) {
				e.printStackTrace();
			}
		}

		return true;
	}

	@Override
	public boolean valueChecked(ParsingStatus currentObj,
			Attributes attributes, Locator locator) {
		if (super.isValueNull(currentObj.object, attributes, locator))
			return true;

		assertTrue("Is TCComponent " + getLocation(locator),
				currentObj.object instanceof TCComponent);

		return false;
	}

	@Override
	public Object getChild(ParsingStatus currentObj, Attributes attributes) {
		int iNameIndex = attributes.getIndex("name");
		if (iNameIndex >= 0) {
			String propertyName = attributes.getValue(iNameIndex);

			try {
				if (((TCComponent) currentObj.object)
						.isValidPropertyName(propertyName)) {
					return ((TCComponent) currentObj.object)
							.getTCProperty(propertyName);
				} else {
					return null;
				}
			} catch (TCException e) {
				e.printStackTrace();
				fail(e.getMessage());
			}
		}
		return null;
	}
}
