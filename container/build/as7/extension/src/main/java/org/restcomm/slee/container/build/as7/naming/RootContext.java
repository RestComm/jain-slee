package org.restcomm.slee.container.build.as7.naming;

import java.util.HashMap;
import java.util.Map;

import javax.naming.*;
import javax.naming.spi.NamingManager;

import org.jboss.as.naming.context.NamespaceContextSelector;
import org.jboss.as.naming.util.NameParser;

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

		Object obj = entries.get(name);
		if (obj == null) {
			throw new NameNotFoundException();
		}

		try {
			String className = null;

			if (!(obj instanceof Reference)) {
				if( obj != null )
					className = obj.getClass().getName();
			} else {
				Reference ref = (Reference) obj;
				className = ref.getClassName();
				Object content = ref.get(0).getContent();
				Object raw = NamingManager.getObjectInstance(obj, getNameParser(name).parse(name), this, null);
				if (raw instanceof LinkRef) {
					raw = resolveLink(raw);
				}
				return raw;
			}
		} catch (Exception e) {
			NamingException ex = new NamingException();
			ex.setRootCause(e);
			throw ex;
		}

		return obj;
	}

	private Object resolveLink(Object res) throws NamingException
	{
		Object linkResult = null;
		try
		{
			LinkRef link = (LinkRef) res;
			String ref = link.getLinkName();
			if (ref.startsWith("./"))
				linkResult = lookup(ref.substring(2));
			else
				linkResult = new InitialContext().lookup(ref);
		}
		catch (Exception e)
		{
			NamingException ex = new NamingException("Could not dereference object");
			ex.setRootCause(e);
			throw ex;
		}
		return linkResult;
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

	public NameParser getNameParser(Name name) throws NamingException {
		return NameParser.INSTANCE;
	}

	public NameParser getNameParser(String name) throws NamingException {
		return NameParser.INSTANCE;
	}

	@Override
	public String getNameInNamespace() throws NamingException {
		return getNamespace();
	}

	@Override
	public Name composeName(Name name, Name prefix) throws NamingException {
		Name result = (Name) (prefix.clone());
		result.addAll(name);
		return result;
	}

	@Override
	public String composeName(String name, String prefix) throws NamingException {
		Name result = composeName(NameParser.INSTANCE.parse(name),
				NameParser.INSTANCE.parse(prefix));
		return result.toString();
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
