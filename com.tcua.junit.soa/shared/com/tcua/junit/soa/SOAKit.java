package com.tcua.junit.soa;

import static org.junit.Assert.assertEquals;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;

import com.tcua.junit.soa.handler.ArrayHandler;
import com.tcua.junit.soa.handler.ElementHandler;
import com.tcua.junit.soa.handler.ISOAClassHandler;
import com.tcua.junit.soa.handler.MapHandler;
import com.tcua.junit.soa.handler.RootHandler;
import com.tcua.junit.soa.handler.SOAEntryHandler;
import com.tcua.junit.soa.handler.StringValueHandler;
import com.tcua.junit.soa.handler.TCComponentHandler;
import com.tcua.junit.soa.handler.TCPropertyHandler;
import com.tcua.junit.soa.handler.ValueHandler;
import com.teamcenter.rac.kernel.ServiceData;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCProperty;

public class SOAKit {

	protected ISOAClassHandler primitiveHandler = null;
	protected ISOAClassHandler arrayHandler = null;
	protected ISOAClassHandler entryHandler;
	protected final Map<Class<?>, ISOAClassHandler> classHandlers;
	protected ISOAClassHandler rootHandler;
	protected ISOAClassHandler elementHandler;
	protected URL sourceURL;
	private Map<String, Class<?>> evaluators;

	public SOAKit() {
		// initialize handler for primitive objects
		primitiveHandler = new StringValueHandler(this);
		// initialize handler for array objects
		arrayHandler = new ArrayHandler(this);
		// initialize handler for SOA entry
		entryHandler = new SOAEntryHandler(this);
		// initialize root handler
		rootHandler = new RootHandler(this);
		// initialize element handler
		elementHandler = new ElementHandler(this);

		// class specific handlers
		classHandlers = new HashMap<Class<?>, ISOAClassHandler>();

		// runtime evaluators
		evaluators = new HashMap<String, Class<?>>();
		registerEvaluator("TCRuntime", com.tcua.junit.soa.TCRuntime.class);

		// handle basic classes as primitives
		classHandlers.put(String.class, primitiveHandler);
		classHandlers.put(Boolean.class, primitiveHandler);
		classHandlers.put(Integer.class, primitiveHandler);
		classHandlers.put(Long.class, primitiveHandler);
		classHandlers.put(Double.class, primitiveHandler);
		classHandlers.put(Byte.class, primitiveHandler);

		// handle map interface
		classHandlers.put(Map.class, new MapHandler(this));

		// Teamcenter object and property
		classHandlers.put(TCComponent.class, new TCComponentHandler(this));
		classHandlers.put(TCProperty.class, new TCPropertyHandler(this));

		// Standard response is ignored
		classHandlers.put(ServiceData.class, new SOAEntryHandler(this) {

			@Override
			public boolean extend(Element parent, Object obj) {
				// handle null only, no children
				handleNull(parent, obj);
				return true;
			}
		});

		// null handler
		classHandlers.put(null, new ValueHandler() {
			
			@Override
			public boolean valueChecked(ParsingStatus currentObj,
					Attributes attributes, Locator locator) {
				if (isValueNull(currentObj.object, attributes, locator))
					return true;
				assertEquals(null, currentObj.object);
				return false;
			}
			
			@Override
			public boolean hasEntry() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public String getTagName() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public boolean extend(Element entry, Object obj, String XMLName) {
				// TODO Auto-generated method stub
				return false;
			}
		});
}

	public ISOAClassHandler getHandler(Class<?> type) {

		if (type == null) {
			return classHandlers.get(type);
		} else if (type.isPrimitive()) {
			return primitiveHandler;
		} else if (type.isArray()) {
			return arrayHandler;
		}
	
		ISOAClassHandler handler = classHandlers.get(type);
	
		if (handler != null) {
			return handler;
		}
	
		// no direct mapping, go through derived
		for (Entry<Class<?>, ISOAClassHandler> exp : classHandlers.entrySet()) {
			if (exp.getKey() == null ) {
			} else if (exp.getKey().isAssignableFrom(type)) {
				return exp.getValue();
			} else if (exp.getKey().isInterface()) {
				for (Class<?> clazz : type.getInterfaces()) {
					if (clazz == exp.getKey()) {
						return exp.getValue();
					}
				}
			}
		}
	
		// all other objects are considered as entries
		return entryHandler;
	}

	public ISOAClassHandler setHandler(Class<?> masterType, ISOAClassHandler handler) {
		return classHandlers.put(masterType, handler);
	}

	public ISOAClassHandler getPrimitiveHandler() {
		return primitiveHandler;
	}

	public void setPrimitiveHandler(ISOAClassHandler primitiveHandler) {
		this.primitiveHandler = primitiveHandler;
	}

	public ISOAClassHandler getArrayHandler() {
		return arrayHandler;
	}

	public void setArrayHandler(ISOAClassHandler arrayHandler) {
		this.arrayHandler = arrayHandler;
	}

	public ISOAClassHandler getEntryHandler() {
		return entryHandler;
	}

	public void setEntryHandler(ISOAClassHandler entryHandler) {
		this.entryHandler = entryHandler;
	}

	protected ISOAClassHandler getRootHandler() {
		return rootHandler;
	}

	public URL getURL() {
		return sourceURL;
	}

	/**
	 * Get evaluator class for runtime evaluation. The class must be registered
	 * before.
	 * 
	 * @param string
	 * @return
	 */
	public Class<?> getEvaluator(String evaluatorKey) {
		return evaluators.get(evaluatorKey);
	}

	/**
	 * Register class evaluator
	 * 
	 * @param evaluatorKey
	 * @param clazz
	 */
	public void registerEvaluator(String evaluatorKey, Class<?> clazz) {
		evaluators.put(evaluatorKey, clazz);
	}

}