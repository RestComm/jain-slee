package org.restcomm.slee.container.build.as7.naming;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;

public class ChildContext extends NotImplementedContext {

	private final RootContext rootContext;
	private final String contextName;
	
	public ChildContext(RootContext rootContext, String contextName) {
		super();
		this.rootContext = rootContext;
		this.contextName = contextName;
	}

	@Override
	public String getNameInNamespace() throws NamingException {
		return this.rootContext.getNameInNamespace() + this.contextName;
	}

	@Override
	public Name composeName(Name name, Name prefix) throws NamingException {
		return this.rootContext.composeName(name, prefix);
	}

	@Override
	public String composeName(String name, String prefix) throws NamingException {
		return this.rootContext.composeName(name, prefix);
	}
	
	@Override
	public Object lookup(Name name) throws NamingException {
		return lookup(name.toString());
	}
	
	@Override
	public Object lookup(String name) throws NamingException {
		if(!name.startsWith(rootContext.getNamespace())) {
			name = contextName+"/"+name;
		}
		return rootContext.lookup(name);
	}
	
	@Override
	public void bind(Name name, Object obj) throws NamingException {
		bind(name.toString(), obj);
	}
	
	@Override
	public void bind(String name, Object obj) throws NamingException {
		if(!name.startsWith(rootContext.getNamespace())) {
			name = contextName+"/"+name;
		}
		rootContext.bind(name, obj);
	}
	
	@Override
	public void rebind(Name name, Object obj) throws NamingException {
		rebind(name.toString(), obj);
	}
	
	@Override
	public void rebind(String name, Object obj) throws NamingException {
		if(!name.startsWith(rootContext.getNamespace())) {
			name = contextName+"/"+name;
		}
		rootContext.rebind(name, obj);
	}
	
	@Override
	public Context createSubcontext(Name name) throws NamingException {
		return createSubcontext(name.toString());
	}
	
	@Override
	public Context createSubcontext(String name) throws NamingException {
		if(!name.startsWith(rootContext.getNamespace())) {
			name = contextName+"/"+name;
		}
		return rootContext.createSubcontext(name);
	}
	
	@Override
	public void close() throws NamingException {
		
	}
	
}
