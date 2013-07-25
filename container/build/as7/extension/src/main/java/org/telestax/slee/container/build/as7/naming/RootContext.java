package org.telestax.slee.container.build.as7.naming;

import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NamingException;

import org.jboss.as.naming.context.NamespaceContextSelector;

public class RootContext extends NotImplementedContext {

	private final Map<String,Object> entries;
	private final String namespace; 
	private final NamespaceContextSelectorImpl namespaceContextSelector;
	
	public RootContext() {
		entries = new HashMap<String, Object>();
		this.namespace = "java:comp/";
		this.namespaceContextSelector = new NamespaceContextSelectorImpl();
	}
	
	@Override
	public Object lookup(Name name) throws NamingException {
		return lookup(name.toString());
	}

	@Override
	public Object lookup(String name) throws NamingException {
		if(name.startsWith(namespace)) {
			name = name.substring(namespace.length());
		}
		return entries.get(name);
	}

	@Override
	public void bind(Name name, Object obj) throws NamingException {
		bind(name.toString(),obj);
	}

	@Override
	public void bind(String name, Object obj) throws NamingException {
		if(name.startsWith(namespace)) {
			name = name.substring(namespace.length());
		}
		synchronized (entries) {
			// TODO care about the existence of parent context?
			if(entries.containsKey(name)) {
				throw new NameAlreadyBoundException();
			}
			entries.put(name, obj);
		}
	}

	@Override
	public void rebind(Name name, Object obj) throws NamingException {
		rebind(name.toString(), obj);		
	}

	@Override
	public void rebind(String name, Object obj) throws NamingException {
		if(name.startsWith(namespace)) {
			name = name.substring(namespace.length());
		}
		synchronized (entries) {
			// TODO care about the existence of parent context?
			entries.put(name, obj);
		}
		
	}

	@Override
	public Context createSubcontext(Name name) throws NamingException {
		return createSubcontext(name.toString());
	}

	@Override
	public Context createSubcontext(String name) throws NamingException {
		if(name.startsWith(namespace)) {
			name = name.substring(namespace.length());
		}
		synchronized (entries) {
			// TODO care about the existence of parent context?
			if(entries.containsKey(name)) {
				throw new NameAlreadyBoundException();
			}
			ChildContext childContext = new ChildContext(this, name);
			entries.put(name,childContext);
			return childContext;
		}
	}
	
	@Override
	public void close() throws NamingException {
		
	}
	
	public String getNamespace() {
		return namespace;
	}
	
	public NamespaceContextSelector getNamespaceContextSelector() {
		return namespaceContextSelector;		
	}
	
	private class NamespaceContextSelectorImpl extends NamespaceContextSelector {

		private final String identifier = "comp";
		
		@Override
		public Context getContext(String arg0) {
			if (identifier.equals(arg0)) {
				return RootContext.this;
			}
			// TODO add jboss, global and jboss/exported through namingstore injection on another selector, similar to InjectedEENamespaceContextSelector?
			return null;
		}
		
	}

}
