package org.restcomm.slee.container.build.as7.naming;

import java.util.HashMap;
import java.util.Map;

import org.jboss.as.naming.context.NamespaceContextSelector;
import org.mobicents.slee.container.component.SleeComponent;
import org.mobicents.slee.container.jndi.JndiManagement;

public class JndiManagementImpl implements JndiManagement {

	private final Map<SleeComponent, RootContext> contexts;
	
	public JndiManagementImpl() {
		contexts = new HashMap<SleeComponent, RootContext>();
	}
	
	@Override
	public void componentInstall(SleeComponent component) {
		synchronized (contexts) {
			contexts.put(component, new RootContext());
		}
	}

	public RootContext getComponentRootContext(SleeComponent component) {
		return contexts.get(component);
	}
	
	@Override
	public void componentUninstall(SleeComponent component) {
		synchronized (contexts) {
			contexts.remove(component);
		}
	}

	@Override
	public void pushJndiContext(SleeComponent component) {
		RootContext rootContext = getComponentRootContext(component);
		if (rootContext != null) {
			NamespaceContextSelector.pushCurrentSelector(rootContext.getNamespaceContextSelector());
		} else {
			throw new IllegalArgumentException("root context not found for component "+component);
		}		
	}

	@Override
	public void popJndiContext() {
		NamespaceContextSelector.popCurrentSelector();		
	}

}
